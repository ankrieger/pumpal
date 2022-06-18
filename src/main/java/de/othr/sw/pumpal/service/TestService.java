package de.othr.sw.pumpal.service;

import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.entity.dto.WorkoutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private RestTemplate restTemplate;


    //exemplarische Darstellung, wie ich REST Methoden nutzen würde (anhand eigener Beispiele)

    public List<Friend> getFriendsOfUserRest(String id) {

        //pfad müsste natürlich entsprechend angepasst werden an tatsächlichen pfad

        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/restapi/users/{id}/friends", List.class, id);
        List<Friend> friends = response.getBody();

        return friends;
}

    public WorkoutDto getWorkoutById(Long id) {
        WorkoutDto workoutDto = restTemplate.getForObject("http://localhost:8080/restapi/workouts/{id}", WorkoutDto.class, id);
        return workoutDto;
    }
}
