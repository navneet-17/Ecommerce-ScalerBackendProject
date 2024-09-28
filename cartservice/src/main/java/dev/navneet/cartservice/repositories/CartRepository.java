package dev.navneet.cartservice.repositories;

import dev.navneet.cartservice.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
}