package dev.navneet.ordermanagementservice.services;

import dev.navneet.ordermanagementservice.dtos.UserDto;
import dev.navneet.ordermanagementservice.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClient {
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserClient.class);

    @Value("${user.service.url}") // fetch user service url from application.properties
    private String userServiceUrl;

    public UserClient(RestTemplate restTemplate) { // inject RestTemplate
        this.restTemplate = restTemplate;
    }

    public UserDto getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/" + userId; // Construct User Service URL
            return restTemplate.getForObject(url, UserDto.class); // Fetch the UserDto directly
        } catch (RestClientException e) {
            logger.error("Error fetching user details for userId {}: {}", userId, e.getMessage(), e);
            throw new UserNotFoundException ("Failed to fetch user details for user id" +userId); // Handle failure
        }
    }
}
