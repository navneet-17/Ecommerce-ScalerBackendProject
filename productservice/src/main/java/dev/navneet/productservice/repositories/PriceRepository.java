package dev.navneet.productservice.repositories;

import dev.navneet.productservice.models.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Long> {
}
