package org.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringBootAppTests {

    @Container
    static GenericContainer<?> devContainer =
            new GenericContainer<>(DockerImageName.parse("devapp:latest"))
                    .withExposedPorts(8080)
                    .waitingFor(Wait.forHttp("/profile").forStatusCode(200));
    @Container
    static GenericContainer<?> prodContainer =
            new GenericContainer<>(DockerImageName.parse("prodapp:latest"))
                    .withExposedPorts(8081)
                    .waitingFor(Wait.forHttp("/profile").forStatusCode(200));
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devContainer.start();
        prodContainer.start();
    }

    @Test
    void devContainerShouldReturnExpectedResponse() {
        Integer mappedPort = devContainer.getFirstMappedPort();
        String url = "http://localhost:" + mappedPort + "/profile";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is dev", response.getBody());
    }

    @Test
    void prodContainerShouldReturnExpectedResponse() {
        Integer mappedPort = prodContainer.getFirstMappedPort();
        String url = "http://localhost:" + mappedPort + "/profile";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is production", response.getBody());
    }
}