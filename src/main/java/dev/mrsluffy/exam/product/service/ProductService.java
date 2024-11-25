package dev.mrsluffy.exam.product.service;

import dev.mrsluffy.exam.helper.common.response.PagedResponse;
import dev.mrsluffy.exam.product.data.entities.Product;
import dev.mrsluffy.exam.product.model.ProductModel;
import dev.mrsluffy.exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductModel productModel) {

        if (productModel.getName().isEmpty() || productModel.getName().isBlank()) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product name cannot be empty");

        var product = Product.builder().name(productModel.getName())
                .description(productModel.getDescription())
                .price(productModel.getPrice())
                .quantity(productModel.getQuantity())
                .currency(productModel.getCurrency())
                .type(productModel.getType())
                .requirements(productModel.getRequirements())
                .build();

        productRepository.save(product);
    }

    public PagedResponse<Product> getProducts(String name, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseAndNotDeleted(name, pageable);
        return new PagedResponse<>(productPage.getContent(), productPage.getTotalElements());
    }

    @Transactional
    public void updateProduct(Integer productId, ProductModel productModel) {

        if (productId == null) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"ProductId cannot be empty");

        var getProductById = productRepository.findById(productId);

        Product product = getProductById.orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found with ID: " + productId));

        product.setName(productModel.getName());
        product.setDescription(productModel.getDescription());
        product.setPrice(productModel.getPrice());
        product.setQuantity(productModel.getQuantity());
        product.setCurrency(productModel.getCurrency());
        product.setType(productModel.getType());
        product.setRequirements(productModel.getRequirements());

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer productId) {

        if (productId == null) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ProductId cannot be empty");

        var getProductById = productRepository.findById(productId);
        Product product = getProductById.orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found with ID: " + productId));
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}


