package de.othr.sw.pumpal.web;


import de.othr.sw.pumpal.entity.Exercise;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//    @Autowired
//    private ExerciseService exerciseService;

//    @Autowired
//    private FriendshipService friendshipService;


    // erstmaliges Laden
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewWorkouts(Model model) {
        return getOnePage(model, 1);
    }

    @RequestMapping(value = "/page/{pageNumber}", method = RequestMethod.GET)
    public String getOnePage(Model model,
                             @PathVariable("pageNumber") int currentPage) {
        Collection<Visibility> visibilities = new ArrayList<>();
        visibilities.add(Visibility.PUBLIC);

        Page<Workout> page = workoutService.findWorkoutPage(visibilities, currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Workout> workouts = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("workouts", workouts);
        return "workouts";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createNewWorkout(Model model) {
        Workout workout = new Workout();
        Exercise exercise = new Exercise("Example exercise description", 3, 10, 15, "Example note");

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
        System.out.println("Workout:" + workout);
        System.out.println("Workout exercises:" + workout.getExercises());
        if (result.hasErrors()) {
            return "redirect:/workout/create?error";
        }
        System.out.println(workout);
        workoutService.createWorkout(workout, user);
        redirectAttributes.addFlashAttribute("newworkout", workout);
        return "redirect:/workout/create?success";
    }


    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String workoutDetails(@AuthenticationPrincipal User user,
                                 @PathVariable("id") Long id,
                                 Model model) {
        Workout workout = workoutService.getWorkoutById(id);
        List<User> savingUsers = userService.getAllUsersSavingWorkout(workout);
        //maybe just one status variable?
        boolean isAuthor = workout.getAuthor().equals(user);
        boolean isSaved;

       List<Workout> savedWorkouts = workoutService.getSavedWorkoutsOfUser(user);

        if(savedWorkouts.contains(workout)) {
            isSaved = true;
        } else isSaved = false;

        System.out.println("isSaved = " + isSaved);
        System.out.println("Size of saved workout list: " + savedWorkouts.size());

        model.addAttribute("workout", workout); //Vorbelegung probieren mit visibility!
        model.addAttribute("savingUsers", savingUsers);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("isSaved", isSaved);
        return "workout-details";
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.POST)
    public String updateSavedStatus(@AuthenticationPrincipal User user,
                                    @PathVariable("id") Long id,
                                    @RequestParam(required = false, value = "saved") boolean saved,
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

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String deleteWorkout(@PathVariable("id") Long id,
                                 Model model) {
        Workout workout = workoutService.getWorkoutById(id);
        workoutService.deleteWorkout(workout);
        return "redirect:/index";
    }
}
