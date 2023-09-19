package dev.navneet.productservice;

import dev.navneet.productservice.inheritanceDemo.singletable.Mentor;
import dev.navneet.productservice.inheritanceDemo.singletable.MentorRepository;
import dev.navneet.productservice.inheritanceDemo.singletable.User;
import dev.navneet.productservice.inheritanceDemo.singletable.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
	public class ProductserviceApplication implements CommandLineRunner {
		private MentorRepository mentorRepository;
		private UserRepository userRepository;

		public ProductserviceApplication(MentorRepository mentorRepository,
										 UserRepository userRepository) {
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
		}
}
