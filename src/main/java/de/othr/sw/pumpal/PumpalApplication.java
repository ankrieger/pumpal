package de.othr.sw.pumpal;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.entity.dto.Friend;
import de.othr.sw.pumpal.service.UserService;
import de.othr.sw.pumpal.service.WorkoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootApplication
public class PumpalApplication implements ApplicationRunner {

	@Autowired
	private UserService userService;
	@Autowired
	private WorkoutService workoutService;

	public static void main(String[] args) {
//		Logger logger = LoggerFactory.getLogger(PumpalApplication.class);
//		logger.info("Hello World");
		SpringApplication.run(PumpalApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {


//		try {
//			userService.getUserByEmail("admin@gmx.de");
//		} catch (Exception ex) {
//			User user = new User();
//			user.setEmail("admin@gmx.de");
//			user.setName("Mumm");
//			user.setFirstName("Armin");
//			user.setPassword("secret");
//			user.setAccountType(AccountType.ADMIN);
//
//			Address address = new Address();
//			address.setCity("Muenchen");
//			address.setZip("54321");
//			address.setStreet("Adminstraße");
//			address.setStreetNumber("22");
//
//			user.setAddress(address);
//
//			userService.registerUser(user);
//		}

//		try {
//			workoutService.getWorkoutById((long)1);
//		} catch (Exception ex) {
////			Workout workout = new Workout();
////			workout.setTitle("Easy Beginner Workout");
////			workout.setDescription("Very Beginner friendly workout for anyone with little time");
////			//workout.setDate(Timestamp.valueOf(LocalDateTime.now()));
////			workout.setVisibility(Visibility.PUBLIC);
////			workout.setLevel(Level.EASY);
////			workout.setDurationInMin(30);
////			workoutService.saveWorkout(workout, userService.getUserByEmail("testo@othr.de"));
//		}


	}
}
