package de.othr.sw.pumpal;

import de.othr.sw.pumpal.entity.*;
import de.othr.sw.pumpal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PumpalApplication implements ApplicationRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PumpalApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		try {
			userService.getUserByEmail("admin@pumpal.de");
		} catch (Exception ex) {
			User user = new User();
			user.setEmail("admin@pumpal.de");
			user.setName("Admin");
			user.setFirstName("Pumpal");
			user.setPassword("secret");
			user.setAccountType(AccountType.ADMIN);
			user.setDescription("Admin of pumpal application");

			Address address = new Address();
			address.setCity("Adminson");
			address.setZip("54321");
			address.setStreet("Adminstraße");
			address.setStreetNumber("22");

			user.setAddress(address);

			userService.registerUser(user);
		}
	}
}
