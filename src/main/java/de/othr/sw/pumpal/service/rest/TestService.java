package de.othr.sw.pumpal.service.rest;

import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.entity.dto.WorkoutDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("customRestLogger")
    Logger logger;


    //exemplarische Darstellung, wie ich REST Methoden nutzen würde (anhand eigener Beispiele)
    //Nutzung der FriendsOfUser Methode im Profile Controller

    public List<Friend> getFriendsOfUserRest(String id) {

        //pfad müsste natürlich entsprechend angepasst werden an tatsächlichen pfad


//        "http://localhost:8080/restapi/users/{id}/friends"                     -> OTH server
//        "http://im-codd.oth-regensburg.de:8804/restapi/users/{id}/friends"     -> Home
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:8080/restapi/users/{id}/friends", List.class, id);
        List<Friend> friends = response.getBody();

        logger.info("Friends of user " + id + " have been received via REST API");
        return friends;
}

    public WorkoutDto getWorkoutById(Long id) {
        WorkoutDto workoutDto = restTemplate.getForObject("http://localhost:8080/restapi/users/{id}/friends", WorkoutDto.class, id);

        logger.info("WorkoutDto with ID " + id + " has been received via REST API");
        return workoutDto;
    }
}
