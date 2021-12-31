package hk.edu.polyu.af.bc.badge.assertion

import com.github.manosbatsis.corda.testacles.mocknetwork.NodeHandles
import com.github.manosbatsis.corda.testacles.mocknetwork.jupiter.MockNetworkExtension
import com.github.manosbatsis.corda.testacles.mocknetwork.jupiter.MockNetworkExtensionConfig
import hk.edu.polyu.af.bc.badge.assertHaveState
import hk.edu.polyu.af.bc.badge.badgeclass.CreateBadgeClassTest
import hk.edu.polyu.af.bc.badge.flows.CreateBadgeClass
import hk.edu.polyu.af.bc.badge.flows.IssueAssertion
import hk.edu.polyu.af.bc.badge.mockNetworkConfig
import hk.edu.polyu.af.bc.badge.output
import hk.edu.polyu.af.bc.badge.states.Assertion
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.identity.CordaX500Name
import net.corda.core.utilities.getOrThrow
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import kotlin.math.log
import kotlin.test.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IssueAssertionTest {
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
    fun `can issue assertion`() {
        // create a BadgeClass
        logger.info("Creating BadgeClass");
        val badgeClassTx = a.startFlow(CreateBadgeClass("test", "test")).getOrThrow()
        network.runNetwork()
        val badgeClass = badgeClassTx.output(BadgeClass::class.java)
        logger.info("BadgeClass Created");

        // issue an Assertion
        logger.info("Issuing assertion")
        val future = a.startFlow(IssueAssertion(badgeClass.toPointer(),
                b.info.legalIdentities[0]))
        network.runNetwork()
        val assertionTx = future.getOrThrow()
        val assertion = assertionTx.output(Assertion::class.java)
        logger.info("Assertion issued: {}", assertion.toString())

        a.assertHaveState(assertion)
        b.assertHaveState(assertion)
    }
}