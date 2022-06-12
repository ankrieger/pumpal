package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.AccountType;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StartController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @RequestMapping(value = {"/", "/index"})
    public String start(Model model) {
        List<Visibility> visibilities = new ArrayList<>();
        //check visibility if user is logged in -> TODO wenn Security implementiert
        visibilities.add(Visibility.PUBLIC);
        model.addAttribute("workouts", workoutService.getNewestWorkouts(visibilities));

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin(@ModelAttribute("user") User user) {
        //TODO: check if user exists: either return login?success or login?error
        System.out.println(user);
        return "redirect:index?success";
    }



    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("user", new User()); //Vorbelegung probieren mit accountType!
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST) // th:action="@{/register}"
    public String doRegister(@Valid User user, BindingResult result) {
        System.out.println(user);
        if (result.hasErrors()) {
            return "registration";
        }
        userService.registerUser(user);
        return "index";
    }

    @RequestMapping(value = "/create-workout", method = RequestMethod.GET)
    public String createNewWorkout(Model model) {
        model.addAttribute("workout", new Workout()); //Vorbelegung probieren mit visibility!
        return "create-workout";
    }

    @RequestMapping(value = "/create-workout", method = RequestMethod.POST)
    public  String doCreateWorkout(@Valid Workout workout, BindingResult result) {
        System.out.println(workout);
        if (result.hasErrors()) {
            return "create-workout";
        }
        System.out.println(workout);
        workoutService.saveWorkout(workout, userService.getUserByEmail("testo@othr.de"));
        return "redirect:index?success";
    }
}
