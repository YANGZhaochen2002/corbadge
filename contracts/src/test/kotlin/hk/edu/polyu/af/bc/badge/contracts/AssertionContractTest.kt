package hk.edu.polyu.af.bc.badge.contracts

import com.r3.corda.lib.tokens.contracts.NonFungibleTokenContract
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class AssertionContractTest {
    @Test
    fun `can read contract class name`() {
        assertNotNull(NonFungibleTokenContract.contractId)
    }
}