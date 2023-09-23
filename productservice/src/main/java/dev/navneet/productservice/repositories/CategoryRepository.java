package dev.navneet.productservice.repositories;

import dev.navneet.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CategoryRepository
        extends JpaRepository<Category, UUID> {

    Optional<Category> findById(UUID uuid);
    @Override
    List<Category> findAllById(Iterable<UUID> uuids);
}

















