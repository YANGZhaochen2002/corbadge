package hk.edu.polyu.af.bc.badge.states

import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import net.corda.testing.core.TestIdentity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test

internal class AssertionTest {
    private val me: Party = TestIdentity.fresh("AFBlockchain").party
    private val badgeClass = BadgeClass("A Badge", "Cool", me, UniqueIdentifier())

    @Test
    fun `can create an assertion`() {
        val assertion = Assertion(badgeClass, me, UniqueIdentifier())

        assertThat(assertion.holder, equalTo(me))
        assertThat(assertion.issuer, equalTo(me))
        assertThat(assertion.issuedTokenType.tokenType.tokenClass, equalTo(BadgeClass::class.java))
    }
}