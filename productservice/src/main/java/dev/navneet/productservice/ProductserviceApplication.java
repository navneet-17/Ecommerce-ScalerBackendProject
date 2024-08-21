package dev.navneet.productservice;


import dev.navneet.productservice.inheritanceDemo.tableperclass.MentorRepository;
import dev.navneet.productservice.inheritanceDemo.tableperclass.UserRepository;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.models.Price;
import dev.navneet.productservice.repositories.CategoryRepository;
import dev.navneet.productservice.repositories.PriceRepository;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class ProductserviceApplication{// implements CommandLineRunner {
//
//
//
//	private final ProductRepository productRepository;
//	private final CategoryRepository categoryRepository;
//	private final PriceRepository priceRepository;
//
//	public ProductserviceApplication(ProductRepository productRepository,
//     CategoryRepository categoryRepository,
////	 @Qualifier("tpc_mr") MentorRepository mentorRepository,
////       @Qualifier("tpc_ur") UserRepository userRepository,
//									 PriceRepository priceRepository) {
//
//		this.productRepository = productRepository;
//		this.categoryRepository = categoryRepository;
////		this.mentorRepository = mentorRepository;
////		this.userRepository = userRepository;
//
//
//		this.priceRepository = priceRepository;
//	}

		public static void main(String[] args) {
			SpringApplication.run(ProductserviceApplication.class, args);

		}
//@Transactional
//		@Override
//		public void run(String... args) throws Exception {
////			Mentor mentor = new Mentor();
////			mentor.setName("Naman");
////			mentor.setEmail("Naman@scaler.com");
////			mentor.setAverageRating(4.65);
////			mentorRepository.save(mentor);
////
////			User user = new User();
////			user.setName("Navneet");
////			user.setEmail("Navneet@mymail.com");
////			userRepository.save(user);
////
////			List<User> users = userRepository.findAll();
////			for (User user1: users) {
////				System.out.println(user1);
////			}
//			Category category = new Category();
//			category.setName("Apple Devices");
////			Category savedCategory = categoryRepository.save(category);
//
//			Price price = new Price("INR", 100000);
////			priceRepository.save(price);
//
//			Product product = new Product();
//			product.setTitle("iPhone 15 Pro");
//			product.setDescription("The best iPhone Ever");
//			product.setCategory(category);
//			product.setPrice(price);
//			productRepository.save(product);
//
//
////			Optional<Category> optionalCategory1 = categoryRepository.findById(UUID.fromString("d818e0c7-8e39-45da-8ad9-20a607784389"));
////			if(!optionalCategory1.isEmpty()) {
////				Category category1 = optionalCategory1.get();
////				System.out.println("Category name is: " + category1.getName());
////				System.out.println("Printing all products in the category");
////				Thread.sleep(1000);
////				// Code fails due to lazy loading of products -- we will discuss this in the next lecture
//////				for (Product product1: category1.getProducts()) {
//////					System.out.println(product1.getTitle());
//////					System.out.println(product1.getDescription());
//////				}
////			}
////			else {
////				System.out.println("Category not found");
////			}
//
////			List<Product> products = productRepository.findAllByPrice_Currency("INR");
////			System.out.println("Printing all products with currency INR");
////			for (Product product1: products) {
////				System.out.println(product1.getTitle());
////				System.out.print(" "+product1.getDescription());
////				System.out.print(" "+product1.getPrice().getCurrency());
////				System.out.print(" "+product1.getPrice().getPrice());
////			}
//
//		//** Count the number of products in the database for a particular currency:
//			Long count = productRepository.countByPrice_Currency("INR");
//			System.out.println("Number of products with currency INR: " + count);
//
//
//		//** Get all the products by title
////			List<Product> products1 = productRepository.findAllProductsByTitle("iPhone 15 Pro");
////			System.out.println("Printing all products with title iPhone 15 Pro");
////			for (Product product1: products1) {
////				System.out.println(product1.getTitle());
////				System.out.print(" "+product1.getDescription());
////				System.out.print(" "+product1.getPrice().getCurrency());
////				System.out.print(" "+product1.getPrice().getPrice());
////			}
//
//			// Demonstrate Lazy Loading
//			Optional<Category> optionalCategory1 = categoryRepository.findById
//					(UUID.fromString("016dfcd4-054a-478f-bedd-777599e7eb51"));
//			if(!optionalCategory1.isEmpty()) {
//				Category category1 = optionalCategory1.get();
//				System.out.println("Category name is: " + category1.getName());
//				System.out.println("Printing all products in the category");
//				Thread.sleep(1000);
//
//				//Trying to fetch the products from the category
//				List<Product> products = category1.getProducts();
//				Thread.sleep(1000);
//				for (Product product1: products) {
//					System.out.println(" Title: " + product1.getTitle());
//					System.out.println(" Description: "+product1.getDescription());
//					System.out.println(" Currency: "+product1.getPrice().getCurrency());
//					System.out.println(" Price: "+product1.getPrice().getPrice());
//				}
//			}
//			else {
//				System.out.println("Category not found");
//			}

//	}
}





