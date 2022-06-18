package de.othr.sw.pumpal.web.rest;

import de.othr.sw.pumpal.entity.User;
import de.othr.sw.pumpal.entity.Workout;
import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.entity.dto.WorkoutDto;
import de.othr.sw.pumpal.service.FriendshipService;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import de.othr.sw.pumpal.service.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestServiceRestController {

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    UserService userService;

    @Autowired
    WorkoutService workoutService;

    @Autowired
    private ModelMapper modelMapper;


    @RequestMapping(value = "restapi/users/{email}/friends", method = RequestMethod.GET)
    public List<Friend> getFriendsOfUser(@PathVariable("email") String email) {
        //TODO: catch try blabla REST
        User user = userService.getUserByEmail(email);
        List<User> friends = friendshipService.getAllFriendsOfUser(user);

        return friends.stream()
                .map(friend -> modelMapper.map(friend, Friend.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "restapi/workouts/{id}", method = RequestMethod.GET)
    public WorkoutDto getWorkoutById(@PathVariable("id") Long workoutId) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        return new WorkoutDto(workout.getID(), workout.getTitle(), workout.getDescription()) ;
    }

}