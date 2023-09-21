package dev.navneet.productservice;


//import dev.navneet.productservice.inheritanceDemo.tableperclass.MentorRepository;
//import dev.navneet.productservice.inheritanceDemo.tableperclass.UserRepository;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.CategoryRepository;
import dev.navneet.productservice.repositories.ProductRepository;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class ProductserviceApplication implements CommandLineRunner {


	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductserviceApplication(ProductRepository productRepository,
     CategoryRepository categoryRepository
	/* @Qualifier("tpc_mr") MentorRepository mentorRepository,
       @Qualifier("tpc_ur") UserRepository userRepository*/ ) {

		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		/*this.mentorRepository = mentorRepository;
		this.userRepository = userRepository;
		 */

	}

		public static void main(String[] args) {
			SpringApplication.run(ProductserviceApplication.class, args);
		}

		@Override
		public void run(String... args) throws Exception {
//			Mentor mentor = new Mentor();
//			mentor.setName("Naman");
//			mentor.setEmail("Naman@scaler.com");
//			mentor.setAverageRating(4.65);
//			mentorRepository.save(mentor);
//
//			User user = new User();
//			user.setName("Navneet");
//			user.setEmail("Navneet@mymail.com");
//			userRepository.save(user);
//
//			List<User> users = userRepository.findAll();
//			for (User user1: users) {
//				System.out.println(user1);
//			}
			Category category = new Category();
			category.setName("Apple Devices");
			Category savedCategory = categoryRepository.save(category);

			Product product = new Product();
			product.setTitle("iPhone 15 Pro");
			product.setDescription("The best iPhone Ever");
			product.setCategory(savedCategory);

			productRepository.save(product);

			Optional<Category> optionalCategory1 = categoryRepository.findById(UUID.fromString("d818e0c7-8e39-45da-8ad9-20a607784389"));
			if(!optionalCategory1.isEmpty()) {
				Category category1 = optionalCategory1.get();
				System.out.println("Category name is: " + category1.getName());
				System.out.println("Printing all products in the category");
				Thread.sleep(1000);
				// Code fails due to lazy loading of products -- we will discuss this in the next lecture
//				for (Product product1: category1.getProducts()) {
//					System.out.println(product1.getTitle());
//					System.out.println(product1.getDescription());
//				}
			}
			else {
				System.out.println("Category not found");
			}
		}
}
