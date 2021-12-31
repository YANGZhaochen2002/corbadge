package hk.edu.polyu.af.bc.badge.badgeclass

import com.github.manosbatsis.corda.testacles.mocknetwork.jupiter.MockNetworkExtensionConfig
import hk.edu.polyu.af.bc.badge.assertHaveState
import hk.edu.polyu.af.bc.badge.flows.CreateBadgeClass
import hk.edu.polyu.af.bc.badge.mockNetworkConfig
import hk.edu.polyu.af.bc.badge.output
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateBadgeClassTest {
    private lateinit var network: MockNetwork
    private lateinit var a: StartedMockNode
    private lateinit var b: StartedMockNode
    private lateinit var c: StartedMockNode

    @BeforeAll
    fun setup() {
        network = MockNetwork(
                MockNetworkParameters(cordappsForAllNodes = listOf(
                        TestCordapp.findCordapp("hk.edu.polyu.af.bc.badge.contracts"),
                        TestCordapp.findCordapp("hk.edu.polyu.af.bc.badge.flows"),
                        TestCordapp.findCordapp("com.r3.corda.lib.tokens.workflows.flows"),
                        TestCordapp.findCordapp("com.r3.corda.lib.tokens.contracts")
                ), networkParameters = testNetworkParameters(minimumPlatformVersion = 4)))

        a = network.createPartyNode(CordaX500Name.parse("O=InstituteA, L=Athens, C=GR"))
        b = network.createPartyNode(CordaX500Name.parse("O=LearnerA, L=Athens, C=GR"))
        c = network.createPartyNode(CordaX500Name.parse("O=ObserverA, L=Athens, C=GR"))

        network.runNetwork()
    }

    @AfterAll
    fun tearDown() {
        network.stopNodes()
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(CreateBadgeClassTest::class.java)
    }

    @Test
    fun `can create a BadgeClass`() {
        val tx = a.startFlow(CreateBadgeClass("Test Badge", "Just for testing")).getOrThrow()
        network.runNetwork()

        // assert flow output
        val badgeClass = tx.output(BadgeClass::class.java)
        assertEquals("Test Badge", badgeClass.name)

        // assert vault status
        a.assertHaveState(badgeClass)

    }
}