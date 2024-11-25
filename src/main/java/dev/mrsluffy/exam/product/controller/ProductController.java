package dev.mrsluffy.exam.product.controller;

import dev.mrsluffy.exam.helper.common.response.PagedResponse;
import dev.mrsluffy.exam.helper.common.response.UnauthorizedException;
import dev.mrsluffy.exam.product.data.entities.Product;
import dev.mrsluffy.exam.product.data.entities.enums.ProductType;
import dev.mrsluffy.exam.product.model.ProductModel;
import dev.mrsluffy.exam.product.repository.UserRepository;
import dev.mrsluffy.exam.product.service.JwtService;
import dev.mrsluffy.exam.product.service.ProductService;
import dev.mrsluffy.exam.util.Utilities;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/11/2024
 **/

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/CreateProduct")
    public ResponseEntity<?> CreateProduct(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ProductModel productModel) {
        try {
            validateAuthHeader(authHeader);
            productService.addProduct(productModel);
            return ResponseEntity.ok(Utilities.message("msg", "Added " + productModel.getName() + " Successfully"));
        } catch (HttpClientErrorException.BadRequest err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utilities.error(e.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Utilities.errorTraceId(exception.getMessage()));
        }
    }

    @GetMapping("/GetProducts")
    public ResponseEntity<?> GetProducts(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {

            validateAuthHeader(authHeader);

            // Validate page number
            if (page <= 0) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Page number must be greater than 0");
            }

            int pageIndex = page > 0 ? page - 1 : 0;
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageIndex, size, sort);

            PagedResponse<Product> pagedResponse = productService.getProducts(name, pageable);
            return ResponseEntity.ok(pagedResponse);
        } catch (HttpClientErrorException.BadRequest err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utilities.error(e.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Utilities.errorTraceId(exception.getMessage()));
        }
    }

    @PutMapping("/UpdateProduct")
    public ResponseEntity<?> UpdateProduct(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Integer productId,
            @RequestBody ProductModel productModel) {
        try {
            validateAuthHeader(authHeader);
            productService.updateProduct(productId, productModel);
            return ResponseEntity.ok(Utilities.message("msg", "Update " + productId + " Successfully"));
        } catch (HttpClientErrorException.BadRequest err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utilities.error(e.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Utilities.errorTraceId(exception.getMessage()));
        }
    }

    @DeleteMapping("/DeleteProduct")
    public ResponseEntity<?> DeleteProduct(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Integer productId) {
        try {
            validateAuthHeader(authHeader);
            productService.deleteProduct(productId);
            return ResponseEntity.ok(Utilities.message("msg", "Deleted " + productId + " Successfully"));
        } catch (HttpClientErrorException.BadRequest err) {
            return ResponseEntity.badRequest().body(Utilities.error(err.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Utilities.error(e.getMessage()));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Utilities.errorTraceId(exception.getMessage()));
        }
    }

    @GetMapping("GetProductTypes")
    public ResponseEntity<?> getProductTypes() {
        List<String> productTypes = Arrays.stream(ProductType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productTypes);
    }

    private void validateAuthHeader(String authHeader) {
        if (authHeader == null) {
            throw new UnauthorizedException("Authorization header is missing or malformed.");
        }

        String email = jwtService.extractUserName(authHeader, true);
        if (email == null || !userExists(email)) {
            throw new UnauthorizedException("Email not found or invalid token.");
        }
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
