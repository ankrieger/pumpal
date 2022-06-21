package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Visibility;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.service.FriendshipService;
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

//    TODO: outsource get friendship/workout/size of those into separate functions for better readability

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewProfile(@AuthenticationPrincipal User user,
                               Model model) {
        model.addAttribute("user", user);

        //ansonsten wäre es möglich @OneToMany Liste an Workouts im User mit FetchType.EAGER
        //zu annotieren und über den getter zu holen + entsprechende stream Methode zu filtern;
        // persönlich fand ich die Query eleganter
        // + nur eine FetchType.EGAER Annotation möglich -> für savedWorkouts entschieden da
        //wsl mehr Workouts gepsiehcert als selber erstellt werden + keine Filteroperationen
//
//        List<Workout> workoutsPublic = user.getWorkouts().stream()
//                .filter(workout -> workout.getVisibility().equals(Visibility.PUBLIC))
//                .collect(Collectors.toList());
//
//        List<Workout> workoutsPrivate= user.getWorkouts().stream()
//                .filter(workout -> workout.getVisibility().equals(Visibility.PRIVATE))
//                .collect(Collectors.toList());

        //TODO: Variable isAuthor einführen

        //public workouts
        List<Workout> workoutsPublic = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PUBLIC, user);

        //private workouts
        List<Workout> workoutsPrivate = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PRIVATE, user);

        //saved workouts
        List<Workout> workoutsSaved = workoutService.getSavedWorkoutsOfUser(user);

        //active friendships
        List<User> friends = friendshipService.getAllFriendsOfUser(user);
        // Incoming friend requests
        //possibility to do .getFriendRequests and then all the Users?
        List<User> friendsIn = friendshipService.getAllIncomingFriendRequestsOfUser(user);
        //Outgoing friend requests
        List<User> friendsOut = friendshipService.getAllOutgoingFriendRequestsOfUser(user);


        model.addAttribute("workouts", workoutsPublic);
        model.addAttribute("privWorkouts", workoutsPrivate);
        model.addAttribute("savedWorkouts", workoutsSaved);
        model.addAttribute("friends", friends);
        model.addAttribute("friendsIn", friendsIn);
        model.addAttribute("friendsOut", friendsOut);


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
            return "redirect:edit?error"; //TODO:error alert in profile-edit page
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

        //extra enum wäre möglicherweise besser; aber passt an der Stelle schon
        String friendshipStatus = friendshipService.getStatusOfFriendship(user, user_auth);
        List<User> friends = friendshipService.getAllFriendsOfUser(user);

//        List<Workout> workoutsPublic = user.getWorkouts().stream()
//                .filter(workout -> workout.getVisibility().equals(Visibility.PUBLIC))
//                .collect(Collectors.toList());

        List<Workout> workoutsPublic = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PUBLIC,user);

        //zusätzlich private Workouts darstellen, falls Freundschaft besteht
        List<Workout> privateWorkouts = new ArrayList<>();
        List<Workout> savedWorkouts = new ArrayList<>();
        if (friendshipStatus.equals("friends")) {
            privateWorkouts = workoutService.getAllWorkoutsOfUserByVisibility(Visibility.PRIVATE, user);
            savedWorkouts = workoutService.getSavedWorkoutsOfUser(user);
//            privateWorkouts = user.getWorkouts().stream()
//                    .filter(workout -> workout.getVisibility().equals(Visibility.PRIVATE))
//                    .collect(Collectors.toList());
        }

        //absichtlichAnzahl aller Workouts statt nur privater; möglicherweise Anreiz zur Anfrage,
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
    String doEditFriendship(@PathVariable("id") String id,
                            @AuthenticationPrincipal User user_auth,
                            @RequestParam(required = false, value = "friendShipStatus") String friendShipStatus,
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


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        return getFirstPage(model, 1);
    }


    @RequestMapping(value = "/all/page/{pageNumber}", method = RequestMethod.GET)
    public String getFirstPage(Model model,
                      @PathVariable("pageNumber") int currentPage) {
        Page<User> page = userService.findUserPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        List<User> users = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("users", users);
        return "users";
    }

}
