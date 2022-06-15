package de.othr.sw.pumpal.web;

import de.othr.sw.pumpal.entity.Friendship;
import de.othr.sw.pumpal.entity.User;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private FriendshipService friendshipService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String viewProfile(@AuthenticationPrincipal User user,
                               Model model) {
        model.addAttribute("user", user);
        List<Workout> workouts = workoutService.getAllWorkoutsOfUser(user);
        model.addAttribute("workouts", workouts);
        return "profile";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editProfile(@AuthenticationPrincipal User user,
                              Model model) {
        model.addAttribute("user", user);
        return "profile-edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String viewOtherProfile(@PathVariable("id") String id,
                                   Model model) {
        User user = userService.getUserByEmail(id);
        User user_auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getEmail().equals(user_auth.getEmail())) {
            return "redirect:";
        }

        // Check Friendship status for different HTML Elements, prepare attributes
        boolean friends, friendRequested, friendRequesting;
        friends = friendRequested = friendRequesting = false;


        String friendshipStatus = friendshipService.getStatusOfFriendship(user, user_auth);
        switch (friendshipStatus) {
            case "friends": friends = true; break;
            case "friendRequested": friendRequested = true; break;
            case "friendRequesting": friendRequesting = true; break;
            default: // none -> no friendship yet
        }

        model.addAttribute("friends", friends);
        model.addAttribute("friendRequested", friendRequested);
        model.addAttribute("friendRequesting", friendRequesting);

        model.addAttribute("user", user);
        List<Workout> workouts = workoutService.getAllWorkoutsOfUser(user);
        model.addAttribute("workouts", workouts);
        return "profile";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    String doEditFriendship(@PathVariable("id") String id,
                            @AuthenticationPrincipal User user_auth,
                            @RequestParam(required = false, value = "friends",defaultValue = "false") boolean friends,
                            @RequestParam(required = false, value = "friendRequested",defaultValue = "false") boolean friendRequested,
                            @RequestParam(required = false, value = "friendRequesting",defaultValue = "false") boolean friendRequesting,
                            @RequestParam(required = false, value = "deny",defaultValue = "false") boolean deny,
                            @RequestParam(required = false, value = "accept",defaultValue = "false") boolean accept,
                            Model model) {

        User user = userService.getUserByEmail(id);
        Friendship friendship;

        //if there is no connection yet
        if (friends==friendRequested==friendRequesting==deny==accept==false) {
            friendship = friendshipService.sendFriendRequest(user_auth, user);
        //if request is denied, deleted or removed
        } else if ((deny == true ) || (friends == true ) || (friendRequested == true )) {
            friendship = friendshipService.getFriendshipOfUsers(user_auth, user);
            friendshipService.deleteFriendship(friendship);
            //if request is accepted
        } else if (accept == true ) {
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
        List<User> workouts = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("workouts", workouts);
        return "users";
    }

}