package hk.edu.polyu.af.bc.badge.flows

import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.flows.StartableByService
import net.corda.core.identity.AbstractParty
import net.corda.core.transactions.SignedTransaction

/**
 * Create a [BadgeClass].
 *
 * This flow is initiated by the issuer. Optionally, provide a list of observers, who will also see the state (refer to
 * the observers in [CreateEvolvableTokens]).
 *
 * @property name name of this type of badges
 * @property description a short description of achievement denoted by this type of badges
 * @property observers observer parties (optional)
 */
@StartableByRPC
@StartableByService
class CreateBadgeClass(
        private val name: String,
        private val description: String,
        private val observers: List<AbstractParty> = listOf()
): FlowLogic<SignedTransaction>() {
    override fun call(): SignedTransaction {
        TODO("Not yet implemented")
    }
}