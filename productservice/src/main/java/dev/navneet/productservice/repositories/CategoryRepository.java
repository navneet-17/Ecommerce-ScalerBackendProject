package dev.navneet.productservice.repositories;

import dev.navneet.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CategoryRepository
        extends JpaRepository<Category, UUID> {
}
