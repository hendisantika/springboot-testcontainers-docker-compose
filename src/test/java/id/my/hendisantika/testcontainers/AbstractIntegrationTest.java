package id.my.hendisantika.testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * Project : testcontainers
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 01/11/24
 * Time: 09.22
 * To change this template use File | Settings | File Templates.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringbootTestcontainersDockerComposeApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);

    private static final String DB_SERVICE_NAME = "db-service-test";
    private static final int DB_PORT = 5432;

    static DockerComposeContainer<?> container;

    static {
        File dockerComposeFile = new File("src/test/compose.yml");

        if (dockerComposeFile.exists()) {
            logger.info("Docker-compose file found in {}", dockerComposeFile.getAbsolutePath());
            logger.info("Starting docker-compose container. This should happen only once before all tests.");

            container = new DockerComposeContainer<>(dockerComposeFile)
                    .withExposedService(DB_SERVICE_NAME, DB_PORT)
                    .waitingFor(
                            "liquibase-test",
                            Wait.forLogMessage(
                                            ".*Liquibase command 'update' was executed successfully.*\\n",
                                            1)
                                    .withStartupTimeout(java.time.Duration.ofMinutes(2)))
                    .withRemoveImages(DockerComposeContainer.RemoveImages.LOCAL);

            logger.info("Starting docker-compose container.");
            container.start();

            logger.info("Docker-compose container started.");

        } else {
            logger.warn("Docker-compose file not found");
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if (container != null) {
            String host = container.getServiceHost(DB_SERVICE_NAME, DB_PORT);
            Integer port = container.getServicePort(DB_SERVICE_NAME, DB_PORT);

            String jdbcUrl = String.format(
                    "jdbc:postgresql://%s:%d/postgres?currentSchema=testcontainer",
                    host, port
            );

            logger.info("Configuring database URL: {}", jdbcUrl);

            registry.add("spring.datasource.url", () -> jdbcUrl);
            registry.add("spring.datasource.username", () -> "postgres");
            registry.add("spring.datasource.password", () -> "postgres");
        }
    }

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
