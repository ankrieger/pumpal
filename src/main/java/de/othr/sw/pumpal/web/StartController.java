package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.AccountType;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.entity.dto.WorkoutDto;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import de.othr.sw.pumpal.service.rest.TestService;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

//    @Autowired
//    private TestService testService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    Logger logger;

    @RequestMapping(value = {"/", "/index"})
    public String start(Model model) {
        List<Visibility> visibilities = new ArrayList<>();
        visibilities.add(Visibility.PUBLIC);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass() != String.class) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user.getAccountType().equals(AccountType.ADMIN)) {
                visibilities.add(Visibility.PRIVATE);
            }
        }
        List<Workout> workouts = workoutService.getNewestWorkouts(visibilities);
        model.addAttribute("workouts", workouts);
//        // Testing REST Functionality
//        WorkoutDto workoutDto = testService.getWorkoutById((long)111);
//        model.addAttribute("workoutDTO", workoutDto);
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
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String doRegister(@Valid @ModelAttribute User user,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            User exUser = userService.getUserByEmail(user.getEmail());
            result.rejectValue("email", exUser.getEmail(), "This email is already taken. Plase choose another one.");
            if (result.hasErrors()) return "registration";
        } catch (UserNotFoundException e) {
            logger.error(e.getMessage());
        }
        userService.registerUser(user);
        return "redirect:/";
    }


}
