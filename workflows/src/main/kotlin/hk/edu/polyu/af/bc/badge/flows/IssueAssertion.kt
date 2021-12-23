package hk.edu.polyu.af.bc.badge.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import hk.edu.polyu.af.bc.badge.states.Assertion
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.flows.StartableByService
import net.corda.core.identity.AbstractParty
import net.corda.core.transactions.SignedTransaction

/**
 * Issue an [Assertion].
 *
 * The calling node is the **issuer** of this [Assertion], which must be (enforced by contract) the **issuer** of the
 * underlying [BadgeClass]. The consistency should also be checked by this flow.
 *
 * @property badgeClassPointer a pointer the the [BadgeClass]. This can be obtained by calling `toPointer` of a [BadgeClass]
 * which may in turn be obtained from a vault query; or can be constructed directly if the linearId is known.
 * @property recipient recipient of this [Assertion]
 */
@StartableByService
@StartableByRPC
class IssueAssertion(
        private val badgeClassPointer: TokenPointer<BadgeClass>,
        private val recipient: AbstractParty
): FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        TODO("Not yet implemented")
    }
}