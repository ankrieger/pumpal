package de.othr.sw.pumpal.web;


import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.service.CommentService;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/workout")
public class WorkoutController {


    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private CommentService commentService;


    // erstmaliges Laden
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String viewWorkouts(Model model,
                               @ModelAttribute("keyword") String keyword,
                               @ModelAttribute("selectedlevel") String level) {
        return getOnePage(model, keyword, level,1);
    }


    @RequestMapping(value = "/all/page/{pageNumber}", method = RequestMethod.GET)
    public String getOnePage(Model model,
                             @RequestParam(required = false, value = "keyword") String keyword,
                             @RequestParam(required = false, value = "selectedlevel") String level,
                             @PathVariable("pageNumber") int currentPage) {

        List<Level> levels = new ArrayList<>();
        if (level.isBlank() || level.equals("any")) {
            levels.add(Level.EASY);
            levels.add(Level.MEDIUM);
            levels.add(Level.HARD);
        } else levels.add(Level.valueOf(level));

        List<Visibility> visibilities = new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getAccountType().name().equals("ADMIN")) {
            visibilities.add(Visibility.PRIVATE);
        }
        //user sehen hier nur öffentliche workouts; besser wäre es, die privaten der Freunde mitreinzunehmen
        visibilities.add(Visibility.PUBLIC);

//        Page<Workout> page = workoutService.findWorkoutPage(visibilities, currentPage);
        Page<Workout> page = workoutService.findFilteredWorkoutPage(keyword, levels, visibilities, currentPage);

        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Workout> workouts = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("workouts", workouts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedlevel", level);
        return "workouts";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createNewWorkout(Model model) {
        Workout workout = new Workout();

        Exercise exercise = new Exercise();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(exercise);

        workout.setExercises(exercises);
        workout.setVisibility(Visibility.PUBLIC);
        model.addAttribute("workout", workout); //TODO: Vorbelegung probieren mit visibility!

        return "create-workout";
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public  String doCreateWorkout(@AuthenticationPrincipal User user,
                                   @Valid @ModelAttribute("workout") Workout workout,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(required = false, value = "success",defaultValue = "false") boolean success,
                                   @RequestParam(required = false, value = "error",defaultValue = "false") boolean error,
                                   Model model) {
        if (result.hasErrors()) {
            return "create-workout";
        }
        workoutService.createWorkout(workout, user);
        redirectAttributes.addFlashAttribute("newworkout", workout);
        return "redirect:/workout/create?success";
    }


    //TODO: zugriff über adressleiste möglich! check ob user berechtigung hat das workout zu sehen!
    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String workoutDetails(@AuthenticationPrincipal User user,
                                 @PathVariable("id") Long id,
                                 Model model) {
        Workout workout = workoutService.getWorkoutById(id);


        List<Comment> comments = commentService.getAllCommentsOfWorkout(workout);
        List<User> savingUsers = userService.getAllUsersSavingWorkout(workout);
        //maybe just one status variable?
        boolean isAuthor = workout.getAuthor().equals(user);
        boolean isSaved;

        List<Workout> savedWorkouts = workoutService.getSavedWorkoutsOfUser(user);

        if(savedWorkouts.contains(workout)) {
            isSaved = true;
        } else isSaved = false;

        model.addAttribute("workout", workout);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("comments", comments);
        model.addAttribute("savingUsers", savingUsers);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("isSaved", isSaved);
        model.addAttribute("user", user);
        return "workout-details";
    }


    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public String updateSavedStatus(@AuthenticationPrincipal User user,
                                    @PathVariable("id") Long id,
                                    @RequestParam(value = "saved") boolean saved,
                                    Model model) {
        Workout workout = workoutService.getWorkoutById(id);
        //funktionalität
        if (!saved) {
            //add user to savedBy and add workout to savedWorkouts
            userService.saveWorkoutForUser(workout, user);
        } else {
            //remove the relation
            userService.removeWorkoutFromSavedWorkoutsFromUser(workout, user);
        }
        return "redirect:/workout/"+id+"/details";
    }

    @RequestMapping(value = "/{id}/deleteWorkout", method = RequestMethod.POST)
    public String deleteWorkout(@PathVariable("id") Long id,
                                 Model model) {
        Workout workout = workoutService.getWorkoutById(id);
        workoutService.deleteWorkout(workout);
        return "redirect:/index";
    }

    @RequestMapping(value = "/{id}/addComment", method = RequestMethod.POST)
    public String addComment(@AuthenticationPrincipal User user,
                             @PathVariable("id") Long id,
                             Comment comment) {
        if(!comment.getDescription().isBlank()) {
            commentService.addComment(comment,user, workoutService.getWorkoutById(id));
        }
        return "redirect:/workout/"+id+"/details";
    }


    @RequestMapping(value = "/{workoutId}/deleteComment/{commentId}", method = RequestMethod.POST)
    public String deleteComment(@PathVariable("workoutId") Long workoutId,
                                @PathVariable("commentId") Long commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/workout/"+workoutId+"/details";
    }
}
