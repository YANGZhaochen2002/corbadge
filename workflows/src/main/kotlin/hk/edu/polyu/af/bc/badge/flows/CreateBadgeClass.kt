package hk.edu.polyu.af.bc.badge.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import hk.edu.polyu.af.bc.badge.contracts.BadgeClassContract
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.*
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.node.StatesToRecord
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.unwrap
import java.security.PublicKey
import java.util.*

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
    /**
     * The progress tracker checkpoints each stage of the flow and outputs the specified messages when each
     * checkpoint is reached in the code. See the 'progressTracker.currentStep' expressions within the call() function.
     */
    companion object {
        object GENERATING_TRANSACTION : ProgressTracker.Step("Generating transaction based on new BadgeClass.")

        object VERIFYING_TRANSACTION : ProgressTracker.Step("Verifying contract constraints.")

        object SIGNING_TRANSACTION : ProgressTracker.Step("Signing transaction with our private key.")

        object FINALISING_TRANSACTION : ProgressTracker.Step("Obtaining notary signature and recording transaction.")

        object REPORT_MANULLY: ProgressTracker.Step("Start the reportManully")

        fun tracker() = ProgressTracker(
                GENERATING_TRANSACTION,
                VERIFYING_TRANSACTION,
                SIGNING_TRANSACTION,
                FINALISING_TRANSACTION
        )
    }
    override val progressTracker = tracker()
    @Suspendable
    override fun call(): SignedTransaction {
        //get the corda default notary
        val notary = serviceHub.networkMapCache.notaryIdentities[0]

        //step 1 create new transation and BadgeClass
        progressTracker.currentStep = GENERATING_TRANSACTION;
        val badgeClass = BadgeClass(name, description,ourIdentity, UniqueIdentifier());
        val txCommand = Command(BadgeClassContract.Commands.Create(),ourIdentity.owningKey);
        val txBuilder = TransactionBuilder(notary)
                .addOutputState(badgeClass, BadgeClassContract.ID)
                .addCommand(txCommand);

        //step 2 verify the transaction is valid
        progressTracker.currentStep = VERIFYING_TRANSACTION;
        txBuilder.verify(serviceHub);

        //step 3 sign the transaction
        val signedTransaction = serviceHub.signInitialTransaction(txBuilder);


        //step 4 Notarise and record the transaction in vaults.
        progressTracker.currentStep = FINALISING_TRANSACTION;
        subFlow(FinalityFlow(signedTransaction, emptyList()));

        //step 5 recorde the state in the obesrvers vaults
        for (party in observers) {
            subFlow(ReportManually(signedTransaction,party as Party))
        }
        return signedTransaction;
    }
}

@InitiatingFlow
class ReportManually(val signedTransaction: SignedTransaction, val regulator: Party) : FlowLogic<Unit>() {
    companion object {

        object SEND_TRANSACTION: ProgressTracker.Step("Send the transaction to observes")

        fun tracker() = ProgressTracker(
                SEND_TRANSACTION
        )
    }
    override val progressTracker = tracker()

    @Suspendable
    override fun call() {
        val session = initiateFlow(regulator)
        progressTracker.currentStep = SEND_TRANSACTION;
        session.send(signedTransaction)
    }
}

@InitiatedBy(ReportManually::class)
class ReportManuallyResponder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        val signedTransaction = counterpartySession.receive<SignedTransaction>().unwrap { it }
        // The national regulator records all of the transaction's states using
        // `recordTransactions` with the `ALL_VISIBLE` flag.
        serviceHub.recordTransactions(StatesToRecord.ALL_VISIBLE, listOf(signedTransaction))
    }
}