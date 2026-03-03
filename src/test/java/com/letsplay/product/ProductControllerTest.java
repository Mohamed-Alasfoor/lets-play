package com.letsplay.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testCreateProduct() throws Exception {
        String productJson = "{\"name\":\"Test Product\",\"description\":\"Test Description\",\"price\":99.99,\"userId\":\"user123\"}";

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId("1");
            return p;
        });

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk());

        verify(productRepository).save(org.mockito.ArgumentMatchers.argThat(product ->
            "Test Product".equals(product.getName()) &&
            "Test Description".equals(product.getDescription()) &&
            Double.valueOf(99.99).equals(product.getPrice()) &&
            "user123".equals(product.getUserId())
        ));
    }
}
