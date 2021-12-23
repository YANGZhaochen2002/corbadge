package hk.edu.polyu.af.bc.badge.badgeclass

import com.github.manosbatsis.corda.testacles.mocknetwork.NodeHandles
import com.github.manosbatsis.corda.testacles.mocknetwork.config.MockNetworkConfig
import com.github.manosbatsis.corda.testacles.mocknetwork.jupiter.MockNetworkExtension
import com.github.manosbatsis.corda.testacles.mocknetwork.jupiter.MockNetworkExtensionConfig
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import hk.edu.polyu.af.bc.badge.assertHaveState
import hk.edu.polyu.af.bc.badge.flows.CreateBadgeClass
import hk.edu.polyu.af.bc.badge.mockNetworkConfig
import hk.edu.polyu.af.bc.badge.output
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.driver.NodeHandle
import net.corda.testing.node.MockNodeParameters
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

@ExtendWith(MockNetworkExtension::class)
@Tag("mockNetwork")
class CreateBadgeClassTest {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(CreateBadgeClassTest::class.java)

        @MockNetworkExtensionConfig
        @JvmStatic
        val mockNetworkConfig = mockNetworkConfig()
    }

    @Test
    fun `can create a BadgeClass`(nodeHandles: NodeHandles) {
        val instA = nodeHandles["InstituteA"]!!

        val tx = instA.startFlow(CreateBadgeClass("Test Badge", "Just for testing")).getOrThrow()
        nodeHandles.network.runNetwork()

        // assert flow output
        val badgeClass = tx.output(BadgeClass::class.java)
        assertEquals("Test Badge", badgeClass.name)
        // assert vault status
        instA.assertHaveState(badgeClass)
    }
}