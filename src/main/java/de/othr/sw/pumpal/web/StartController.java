package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.entity.dto.WorkoutDto;
import de.othr.sw.pumpal.service.rest.TestService;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StartController {

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    Logger logger;

    @RequestMapping(value = {"/", "/index"})
    public String start(Model model) {
        List<Visibility> visibilities = new ArrayList<>();
        //TODO: check if admin or regular user -> if admin: add Visibility.PRIVATE as well!
        visibilities.add(Visibility.PUBLIC);
        //wenn noch zeit: TODO: add display of friends' private workouts?
        List<Workout> workouts = workoutService.getNewestWorkouts(visibilities);
        model.addAttribute("workouts", workouts);


//        // Testing REST Functionality
//        WorkoutDto workoutDto = testService.getWorkoutById((long)111);
//        model.addAttribute("workoutDTO", workoutDto);
//
//        List<Friend> friendsRest = testService.getFriendsOfUserRest("gio75@gmx.de");
//        model.addAttribute("friendsDto", friendsRest);

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin() {
        return "redirect:index?success";
    }



    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("user", new User()); //TODO:Vorbelegung probieren mit accountType!
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST) // th:action="@{/register}"
    public String doRegister(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:registration?error";
        }
        //TODO: Test ob email bereits belegt
        userService.registerUser(user);
        return "redirect:index";
    }


}
