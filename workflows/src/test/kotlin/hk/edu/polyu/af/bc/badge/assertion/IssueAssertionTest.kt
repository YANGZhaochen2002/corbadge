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
import net.corda.core.utilities.getOrThrow
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import kotlin.test.assertTrue

@ExtendWith(MockNetworkExtension::class)
@Tag("mockNetwork")
class IssueAssertionTest {
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(CreateBadgeClassTest::class.java)

        @MockNetworkExtensionConfig
        @JvmStatic
        val mockNetworkConfig = mockNetworkConfig()
    }

    @Test
    fun `can issue assertion`(nodeHandles: NodeHandles) {
        val instA = nodeHandles["InstituteA"]!!
        val learnerA = nodeHandles["LearnerA"]!!

        // create a BadgeClass
        val badgeClassTx = instA.startFlow(CreateBadgeClass("test", "test")).getOrThrow()
        val badgeClass = badgeClassTx.output(BadgeClass::class.java)

        // issue an Assertion
        val assertionTx = instA.startFlow(IssueAssertion(badgeClass.toPointer(),
                learnerA.info.legalIdentities[0])).getOrThrow()
        val assertion = assertionTx.output(Assertion::class.java)

        assertTrue(assertion.issuedTokenType.isPointer())
        instA.assertHaveState(assertion)
        learnerA.assertHaveState(assertion)
    }
}