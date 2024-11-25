package dev.mrsluffy.exam.product.repository;

import dev.mrsluffy.exam.product.data.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL)")
    Page<Product> findByNameContainingIgnoreCaseAndNotDeleted(String name, Pageable pageable);

}
