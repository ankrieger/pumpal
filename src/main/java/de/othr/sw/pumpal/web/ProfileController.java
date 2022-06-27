package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.service.FriendshipService;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.rest.TestService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private TestService testService;

    @Autowired
    Logger logger;

//    TODO: outsource get friendship/workout/size of those into separate functions for better readability

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewProfile(@AuthenticationPrincipal User user,
                               Model model) {
        model.addAttribute("user", user);

        //ansonsten wäre es möglich @OneToMany Liste an Workouts im User mit FetchType.EAGER
        //zu annotieren und über den getter zu holen + entsprechende stream Methode zu filtern;
//
//        List<Workout> workoutsPublic = user.getWorkouts().stream()
//                .filter(workout -> workout.getVisibility().equals(Visibility.PUBLIC))
//                .collect(Collectors.toList());

        //TODO: Variable isAuthor einführen?

        if(user.getAccountType().equals(AccountType.USER)) {            //public workouts
            List<Workout> workoutsPublic = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PUBLIC, user);
            List<Workout> workoutsPrivate = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PRIVATE, user);
            List<Workout> workoutsSaved = workoutService.getSavedWorkoutsOfUser(user);
            List<User> friends = friendshipService.getAllFriendsOfUser(user);
            List<User> friendsIn = friendshipService.getAllIncomingFriendRequestsOfUser(user);
            List<User> friendsOut = friendshipService.getAllOutgoingFriendRequestsOfUser(user);


            //REST Test:
            try {
                List<Friend> friendsRest = testService.getFriendsOfUserRest(user.getID());
                model.addAttribute("friendsDto", friendsRest);
            } catch (HttpClientErrorException e) {
                logger.error(e.getMessage());
                return "redirect:/index?error";
            }


            model.addAttribute("workouts", workoutsPublic);
            model.addAttribute("privWorkouts", workoutsPrivate);
            model.addAttribute("savedWorkouts", workoutsSaved);
            model.addAttribute("friends", friends);
            model.addAttribute("friendsIn", friendsIn);
            model.addAttribute("friendsOut", friendsOut);
        }

        return "profile";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editProfile(@AuthenticationPrincipal User user,
                              Model model) {

        model.addAttribute("user", user);
        return "profile-edit";
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public String doEditProfile(@AuthenticationPrincipal User user,
                                @Valid User newUser,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "profile-edit";
        }

        userService.updateUser(user, newUser);

        return "redirect:/profile";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewOtherProfile(@PathVariable("id") String id,
                                   Model model) {

        User user = userService.getUserByEmail(id);
        User user_auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wenn auf Profil mit eigener ID
        if (user.getEmail().equals(user_auth.getEmail())) {
            return "redirect:/profile";
        }

        String friendshipStatus;
        if(user.getAccountType().name().equals("USER")) {
            friendshipStatus = friendshipService.getStatusOfFriendship(user, user_auth);
        } else friendshipStatus = "admin";

        List<User> friends = friendshipService.getAllFriendsOfUser(user);
        List<Workout> workoutsPublic = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PUBLIC,user);

        //zusätzlich private Workouts darstellen, falls Freundschaft besteht oder Admin Profil besucht
        List<Workout> privateWorkouts = new ArrayList<>();
        List<Workout> savedWorkouts = new ArrayList<>();

        if (friendshipStatus.matches("friends|admin")) {
            privateWorkouts = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PRIVATE, user);
            savedWorkouts = workoutService.getSavedWorkoutsOfUser(user);
        }

        //REST Test:
        //generell echt schlechte idee die ganze profil seite down zu nehmen, wenn
        //rest ss down ist; ist zu spät aufgefallen um größere änderungen vorzunehmen
        //dient hier also eher der exemplarischen veranschauung
        try {
            List<Friend> friendsRest = testService.getFriendsOfUserRest(user.getID());
            model.addAttribute("friendsDto", friendsRest);
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
            return "redirect:/index?error";
        }

        //absichtlich Anzahl aller Workouts statt nur privater; möglicherweise Anreiz zur Anfrage,
        //wenn die Zahl deutlich höher ausfällt als einsehbare Workouts

        model.addAttribute("user", user);
        model.addAttribute("friendShipStatus", friendshipStatus);
        model.addAttribute("friends", friends);
        model.addAttribute("workouts", workoutsPublic);
        model.addAttribute("privWorkouts", privateWorkouts);
        model.addAttribute("savedWorkouts", savedWorkouts);

        return "profile";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String doEditFriendship(@PathVariable("id") String id,
                            @AuthenticationPrincipal User user_auth,
                            @RequestParam(value = "friendShipStatus") String friendShipStatus,
                            Model model) {


        User user = userService.getUserByEmail(id);
        Friendship friendship;

        if (friendShipStatus.equals("requesting")) {
            friendship = friendshipService.sendFriendRequest(user_auth, user);
        } else if (friendShipStatus.matches("removed|withdrawn|denied")) {
            friendship = friendshipService.getFriendshipOfUsers(user_auth, user);
            friendshipService.deleteFriendship(friendship);
        } else if (friendShipStatus.equals("accepted")) {
            friendship = friendshipService.getFriendshipOfUsers(user_auth, user);
            friendshipService.acceptFriendRequest(friendship);
        }

        return viewOtherProfile(id, model);
    }


    @RequestMapping(value = "/{id}/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("id") String id) {
        User user = userService.getUserByEmail(id);
        userService.deleteUser(user);
        return "redirect:/index";
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAllUsers(Model model,
                              @ModelAttribute("keyword") String keyword) {
        return getFirstPage(model, keyword,1);
    }


    @RequestMapping(value = "/all/page/{pageNumber}", method = RequestMethod.GET)
    public String getFirstPage(Model model,
                               @RequestParam(required = false, value = "keyword") String keyword,
                               @PathVariable("pageNumber") int currentPage) {
        Page<User> page = userService.findFilteredUser(keyword, currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        List<User> users = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "users";
    }


}
