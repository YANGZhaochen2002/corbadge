package hk.edu.polyu.af.bc.badge;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

import java.io.File;

@Testcontainers
public class ComposeTest {
    private static final int REDIS_PORT = 6379;

    public static Logger logger = LoggerFactory.getLogger(ComposeTest.class);

    @Container
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/integrationTest/resources/docker-compose.yml"))
                    .withExposedService("redis_1", REDIS_PORT);

    @Test
    public void test() {
        String redisUrl = "redis://" +
                environment.getServiceHost("redis_1", REDIS_PORT)
                + ":" +
                environment.getServicePort("redis_1", REDIS_PORT);

        Jedis jedis = new Jedis(redisUrl);
        jedis.set("events/city/rome", "32,15,223,828");
        String cachedResponse = jedis.get("events/city/rome");

        assert cachedResponse.equals("32,15,223,828");
    }
}
