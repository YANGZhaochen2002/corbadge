package hk.edu.polyu.af.bc.badge

import com.github.manosbatsis.corda.testacles.mocknetwork.config.MockNetworkConfig
import com.r3.corda.lib.tokens.workflows.flows.rpc.IssueTokens
import hk.edu.polyu.af.bc.badge.flows.CreateBadgeClass
import net.corda.core.contracts.ContractState
import net.corda.core.identity.CordaX500Name
import net.corda.core.transactions.SignedTransaction
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.node.MockNodeParameters
import net.corda.testing.node.StartedMockNode
import java.lang.AssertionError

/**
 * Common [MockNetworkConfig] configuration
 */
fun mockNetworkConfig() = MockNetworkConfig(
        listOf(MockNodeParameters(legalName = CordaX500Name.parse("O=InstituteA, L=Athens, C=GR")),
                MockNodeParameters(legalName = CordaX500Name.parse("O=InstituteB, L=Athens, C=GR")),
                MockNodeParameters(legalName = CordaX500Name.parse("O=LearnerA, L=Athens, C=GR")),
                MockNodeParameters(legalName = CordaX500Name.parse("O=LearnerB, L=Athens, C=GR")),
                MockNodeParameters(legalName = CordaX500Name.parse("O=ObserverA, L=Athens, C=GR")),
                MockNodeParameters(legalName = CordaX500Name.parse("O=ObserverB, L=Athens, C=GR"))),
        cordappProjectPackage = CreateBadgeClass::class.java.`package`.name,
        cordappPackages = listOf<String>(IssueTokens::class.java.`package`.name),
        threadPerNode = true,
        networkParameters = testNetworkParameters(minimumPlatformVersion = 4))

/**
 * Get the first output of type `clazz` from the tx.
 */
inline fun <reified T: ContractState> SignedTransaction.output(clazz: Class<T>): T {
    return coreTransaction.filterOutputs<T> { true }[0]
}

/**
 * Assert the node's vault contain the given state.
 */
fun <T: ContractState> StartedMockNode.assertHaveState(state: T) {
    val flag = services.vaultService.queryBy(state.javaClass).states.map { it.state.data }.contains(state)
    //TODO: change to standard assertion error message
    if (!flag) throw AssertionError("Node $this does not contain $state")
}