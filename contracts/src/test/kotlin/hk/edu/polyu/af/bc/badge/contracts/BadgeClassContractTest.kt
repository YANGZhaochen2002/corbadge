package hk.edu.polyu.af.bc.badge.contracts

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

internal class BadgeClassContractTest {
    @Test
    fun `can read contract class name`() {
        assertNotNull(BadgeClassContract.ID)
    }
}