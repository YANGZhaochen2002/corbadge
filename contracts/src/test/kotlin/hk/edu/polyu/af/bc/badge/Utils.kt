package hk.edu.polyu.af.bc.badge

import com.r3.corda.lib.tokens.contracts.NonFungibleTokenContract
import hk.edu.polyu.af.bc.badge.contracts.BadgeClassContract
import hk.edu.polyu.af.bc.badge.states.Assertion
import hk.edu.polyu.af.bc.badge.states.BadgeClass
import net.corda.core.identity.CordaX500Name
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices

/**
 * Identities
 */
val identityA = TestIdentity(CordaX500Name("PartyA", "London", "GB"))
val identityB = TestIdentity(CordaX500Name("PartyB", "London", "GB"))
val identityC = TestIdentity(CordaX500Name("PartyC", "London", "GB"))

/**
 * Package level mock service
 */
val ledgerService = MockServices(
        listOf(Assertion::class.java.`package`.name,
                BadgeClass::class.java.`package`.name,
                BadgeClassContract::class.java.`package`.name,
                NonFungibleTokenContract::class.java.`package`.name),
        identityA,
        testNetworkParameters(minimumPlatformVersion = 4),
        identityB, identityC
)