package hk.edu.polyu.af.bc.badge;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.utilities.NetworkHostAndPort;
import net.corda.nodeapi.internal.ArtemisMessagingComponent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.DockerHealthcheckWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

import java.io.File;

@Testcontainers
public class ComposeTest {
    public static Logger logger = LoggerFactory.getLogger(ComposeTest.class);

    @Container
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/integrationTest/resources/docker-compose.yml"))
            .withExposedService("notary", 10003);

    @Test
    public void test() {
        logger.info("Waiting...") ;
        environment.waitingFor("notary", new DockerHealthcheckWaitStrategy());
        logger.info("Notary started");

        String address = environment.getServiceHost("notary", 10003) + ":" + 10003;

        logger.info("Address: " + address);
        CordaRPCClient client = new CordaRPCClient(NetworkHostAndPort.parse(address));
        CordaRPCConnection connection = client.start("user1", "test");
        CordaRPCOps proxy = connection.getProxy();

        logger.info(proxy.nodeInfo().toString());
    }
}
