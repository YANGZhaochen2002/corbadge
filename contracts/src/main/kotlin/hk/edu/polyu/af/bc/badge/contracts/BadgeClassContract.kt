package hk.edu.polyu.af.bc.badge.contracts

import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class BadgeClassContract: EvolvableTokenContract(), Contract {
    companion object {
        val ID: String = BadgeClassContract::class.java.canonicalName
    }

    override fun additionalCreateChecks(tx: LedgerTransaction) {
        // TODO
    }

    override fun additionalUpdateChecks(tx: LedgerTransaction) {
        // TODO
    }

    // build a Create command for BadgeClassContract
    interface Commands : CommandData {
        class Create : Commands
    }
}