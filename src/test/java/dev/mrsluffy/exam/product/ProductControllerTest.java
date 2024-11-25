package dev.mrsluffy.exam.product;

/**
 * product
 *
 * @author John Andrew Camu <werdna.jac@gmail.com>
 * @version 1.0
 * @since 10/12/2024
 **/

import dev.mrsluffy.exam.helper.common.response.PagedResponse;
import dev.mrsluffy.exam.product.controller.ProductController;
import dev.mrsluffy.exam.product.data.entities.Product;
import dev.mrsluffy.exam.product.data.entities.user.User;
import dev.mrsluffy.exam.product.model.ProductModel;
import dev.mrsluffy.exam.product.repository.UserRepository;
import dev.mrsluffy.exam.product.service.JwtService;
import dev.mrsluffy.exam.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    private String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3ZXJkbmEuamFjQGdtYWlsLmNvbSIsImlhdCI6MTcyODY3MzY1OCwiZXhwIjoxNzI5ODU5NjE0fQ.Nk9NoFZyNyyk6F3-c67Oiy7f-aW8Nh_8DZWgIe_XAvM";

    @Mock
    private ProductService productService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testCreateProduct_Success() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Test Product");

        // Mock JWT service behavior for a valid token
        when(jwtService.extractUserName(eq(token), anyBoolean())).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new User()));

        doNothing().when(productService).addProduct(any(ProductModel.class));

        ResponseEntity<?> response = productController.CreateProduct(token, productModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Added Test Product Successfully"));
    }

    @Test
    public void testGetProducts_Success() {
        // Mock JWT service behavior for a valid token
        when(jwtService.extractUserName(eq(token), anyBoolean())).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new User()));

        // Mocking a successful response
        PagedResponse<Product> pagedResponse = new PagedResponse<>(Collections.singletonList(new Product()), 1);
        when(productService.getProducts(anyString(), any(Pageable.class))).thenReturn(pagedResponse);

        ResponseEntity<?> response = productController.GetProducts(token, "Test", 1, 10, "name", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagedResponse, response.getBody());
    }

    @Test
    public void testUpdateProduct_Success() {
        ProductModel productModel = new ProductModel();
        productModel.setName("Updated Product");

        // Mock JWT service behavior for a valid token
        when(jwtService.extractUserName(eq(token), anyBoolean())).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new User()));

        doNothing().when(productService).updateProduct(anyInt(), any(ProductModel.class));

        ResponseEntity<?> response = productController.UpdateProduct(token, 1, productModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Update 1 Successfully"));
    }

    @Test
    public void testDeleteProduct_Success() {
        // Mock JWT service behavior for a valid token
        when(jwtService.extractUserName(eq(token), anyBoolean())).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new User()));

        doNothing().when(productService).deleteProduct(anyInt());

        ResponseEntity<?> response = productController.DeleteProduct(token, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Deleted 1 Successfully"));
    }

}