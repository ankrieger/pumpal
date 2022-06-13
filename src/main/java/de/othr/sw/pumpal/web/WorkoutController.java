package de.othr.sw.pumpal.web;


import de.othr.sw.pumpal.entity.Exercise;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/workout")
public class WorkoutController {


    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createNewWorkout(Model model) {
        model.addAttribute("workout", new Workout()); //TODO: Vorbelegung probieren mit visibility!
       // model.addAttribute("exercises", new ArrayList<Exercise>());
        return "create-workout";
    }

//      Trying to dynamically add exercises
//    @RequestMapping(value = "/create", params = {"addExercise"}, method = RequestMethod.GET)
//    public String addExercise(@ModelAttribute("workout") Workout workout, BindingResult result) {
//        workout.getExercises().add(new Exercise());
//        return "create-workout";
//    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public  String doCreateWorkout(@AuthenticationPrincipal User user,
                                   @Valid @ModelAttribute("workout") Workout workout,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam(required = false, value = "success",defaultValue = "false") boolean success,
                                   @RequestParam(required = false, value = "error",defaultValue = "false") boolean error,
                                   Model model) {
        System.out.println(workout);
        if (result.hasErrors()) {
            return "redirect:/workout/create?error";
        }
        System.out.println(workout);
        workoutService.saveWorkout(workout, user);
        redirectAttributes.addFlashAttribute("newworkout", workout);
        return "redirect:/workout/create?success";
    }


    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public String workoutDetails(@PathVariable("id") Long id, Model model) {
        Workout workout = workoutService.getWorkoutById(id);
        model.addAttribute("workout", workoutService.getWorkoutById(id)); //Vorbelegung probieren mit visibility!
        return "workout-details";
    }
}
