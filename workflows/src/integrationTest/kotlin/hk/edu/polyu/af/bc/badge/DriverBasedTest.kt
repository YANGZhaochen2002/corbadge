package hk.edu.polyu.af.bc.badge

import com.github.manosbatsis.corda.testacles.nodedriver.NodeHandles
import com.github.manosbatsis.corda.testacles.nodedriver.config.NodeDriverNodesConfig
import com.github.manosbatsis.corda.testacles.nodedriver.jupiter.NodeDriverExtensionConfig
import com.github.manosbatsis.corda.testacles.nodedriver.jupiter.NodeDriverNetworkExtension
import net.corda.testing.driver.NodeHandle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertTrue

@ExtendWith(NodeDriverNetworkExtension::class)
class DriverBasedTest {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(DriverBasedTest::class.java)
        
        @NodeDriverExtensionConfig
        @JvmStatic
        val nodeDriverConfig: NodeDriverNodesConfig = customNodeDriverConfig
    }
    
    @Test
    fun `can retrieve node identity`(nodeHandles: NodeHandles) {
        val nodeA: NodeHandle = nodeHandles.getNode("partyA")
        assertTrue(nodeA.nodeInfo.legalIdentities.isNotEmpty())
        
        logger.info("Retrieved nodeA identity: ${nodeA.nodeInfo.legalIdentities[0]}")
    }
}