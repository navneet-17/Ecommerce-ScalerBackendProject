package dev.navneet.productservice;

import dev.navneet.productservice.inheritanceDemo.tableperclass.Mentor;
import dev.navneet.productservice.inheritanceDemo.tableperclass.MentorRepository;
import dev.navneet.productservice.inheritanceDemo.tableperclass.User;
import dev.navneet.productservice.inheritanceDemo.tableperclass.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
	public class ProductserviceApplication implements CommandLineRunner {
		private final MentorRepository mentorRepository;
		private final UserRepository userRepository;

		public ProductserviceApplication(@Qualifier("tpc_mr") MentorRepository mentorRepository,
										 @Qualifier("tpc_ur")UserRepository userRepository) {
			this.mentorRepository = mentorRepository;
			this.userRepository = userRepository;
		}

		public static void main(String[] args) {
			SpringApplication.run(ProductserviceApplication.class, args);
		}

		@Override
		public void run(String... args) throws Exception {
			Mentor mentor = new Mentor();
			mentor.setName("Naman");
			mentor.setEmail("Naman@scaler.com");
			mentor.setAverageRating(4.65);
			mentorRepository.save(mentor);

			User user = new User();
			user.setName("Navneet");
			user.setEmail("Navneet@mymail.com");
			userRepository.save(user);

			List<User> users = userRepository.findAll();
			for (User user1: users) {
				System.out.println(user1);
			}
		}
}
