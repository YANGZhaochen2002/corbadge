package hk.edu.polyu.af.bc.badge

import com.github.manosbatsis.corda.testacles.containers.config.NodeImageNameConfig
import com.github.manosbatsis.corda.testacles.containers.config.database.CordformDatabaseSettingsFactory
import com.github.manosbatsis.corda.testacles.containers.cordform.CordformNetworkContainer
import com.github.manosbatsis.corda.testacles.containers.cordform.config.CordaNetworkConfig
import com.github.manosbatsis.corda.testacles.containers.cordform.config.CordformNetworkConfig
import com.github.manosbatsis.corda.testacles.jupiter.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.testcontainers.containers.Network
import java.io.File

@ExtendWith(CordformNetworkExtension::class)
class DockerBasedTest {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(DockerBasedTest::class.java)

        // Note: Ignored if a [CordaNetworkConfig]-annotated
        // field is present.
        @NodesImageName
        @JvmStatic
        val nodesImageName = NodeImageNameConfig.CORDA_OS_ZULU_4_8

        // Optional, defaults to new network
        // Note: Ignored if a [CordaNetworkConfig]-annotated
        // field is present.
        @NodesNetwork
        @JvmStatic
        val nodesNetwork: Network = Network.newNetwork()

        // Optional, defaults to auto-lookup (build/nodes, ../build/nodes)
        // Note: Ignored if a [CordaNetworkConfig]-annotated
        // field is present.
        @NodesDir
        @JvmStatic
        val nodesDir = File(System.getProperty("user.dir"))
                .parentFile.resolve("build/nodes")

        // Optional, provides the Corda network config to the extension.
        // When using this all other extension config annotations
        // will be ignored (@NodesImageName, @NodesNetwork and @NodesDir)
        @CordaNetwork
        @JvmStatic
        val networkConfig: CordaNetworkConfig = CordformNetworkConfig(
                nodesDir = nodesDir,
                cloneNodesDir = true,
                imageName = nodesImageName,
                network = nodesNetwork,
                imageCordaArgs = "--logging-level DEBUG",
                // Create a Postgres DB for each node (default is H2)
                databaseSettings = CordformDatabaseSettingsFactory.POSTGRES,
                clearEnv = true)
    }

    @Test
    fun `can get identity`(nodesContainer: CordformNetworkContainer) {
        val rpcOps = nodesContainer.getNode("PartyA").getRpc()
        logger.info("NodeA identity: ${rpcOps.nodeInfo().legalIdentities[0]}")
    }
}