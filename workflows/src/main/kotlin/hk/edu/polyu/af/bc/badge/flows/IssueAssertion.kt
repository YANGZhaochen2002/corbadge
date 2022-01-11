package hk.edu.polyu.af.bc.badge.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.NonFungibleTokenContract
import com.r3.corda.lib.tokens.contracts.states.AbstractToken
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.contracts.types.TokenType
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.workflows.flows.issue.addIssueTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import hk.edu.polyu.af.bc.badge.states.Assertion
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.contracts.Command
import net.corda.core.contracts.TransactionState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.*
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.unwrap
import org.intellij.lang.annotations.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
@InitiatingFlow
@StartableByRPC
class IssueAssertion (
    private var badgeClassPointer: TokenPointer<BadgeClass>,
    private var recipient: AbstractParty
) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        var revoked:Boolean=false
        val issuerofBadgeclass:Party=badgeClassPointer.pointer.resolve(serviceHub).state.data.getIssuer
        val issuer: Party = ourIdentity
        if(issuerofBadgeclass.equals(issuer)){
            revoked=true
        }

        val assertion= Assertion(badgeClassPointer, issuer, recipient, Date(),revoked,UniqueIdentifier())
        val tokens = listOf(assertion)

        return subFlow(IssueTokens(tokens))
    }
}
