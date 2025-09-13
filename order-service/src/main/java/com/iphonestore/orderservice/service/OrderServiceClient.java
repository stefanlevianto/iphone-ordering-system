package main.java.com.iphonestore.orderservice.service;

import com.iphonestore.orderservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://localhost:8081}")
    private String productServiceUrl;

    @Autowired
    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<ProductDto> getProductById(Long productId) {
        try {
            String url = productServiceUrl + "/api/products/" + productId;
            ResponseEntity<ProductDto> response = restTemplate.getForEntity(url, ProductDto.class);
            return response.getStatusCode().is2xxSuccessful() ? Optional.ofNullable(response.getBody())
                    : Optional.empty();
        } catch (RestClientException e) {
            System.err.println("Error fetching product " + productId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ProductDto> getAvailableProductsByIds(List<Long> productIds) {
        try {
            String url = productServiceUrl + "/api/products/check-availability";
            HttpEntity<List<Long>> request = new HttpEntity<>(productIds);

            ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<ProductDto>>() {
                    });

            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null ? response.getBody()
                    : List.of();
        } catch (RestClientException e) {
            System.err.println("Error checking product availability: " + e.getMessage());
            return List.of();
        }
    }

    public boolean updateProductStock(Long productId, Integer newQuantity) {
        try {
            String url = productServiceUrl + "/api/products/" + productId + "/stock?quantity=" + newQuantity;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    null,
                    String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            System.err.println("Error updating stock for product " + productId + ": " + e.getMessage());
            return false;
        }
    }
}