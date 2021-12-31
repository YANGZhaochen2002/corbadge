package hk.edu.polyu.af.bc.badge

import com.github.manosbatsis.corda.testacles.nodedriver.NodeParamsHelper
import com.github.manosbatsis.corda.testacles.nodedriver.config.NodeDriverNodesConfig
import com.github.manosbatsis.corda.testacles.nodedriver.config.SimpleNodeDriverNodesConfig
import com.r3.corda.lib.tokens.workflows.flows.rpc.CreateEvolvableTokens
import hk.edu.polyu.af.bc.badge.flows.CreateBadgeClass
import net.corda.client.rpc.CordaRPCConnection
import net.corda.testing.core.ALICE_NAME
import net.corda.testing.core.BOB_NAME
import net.corda.client.rpc.CordaRPCClient

import net.corda.core.utilities.NetworkHostAndPort




val nodeParamsHelper = NodeParamsHelper()

val customNodeDriverConfig: NodeDriverNodesConfig =
        SimpleNodeDriverNodesConfig (
                cordappProjectPackage = CreateBadgeClass::class.java.`package`.name,
                cordappPackages = listOf<String>(CreateEvolvableTokens::class.java.`package`.name),
                nodes = mapOf("partyA" to nodeParamsHelper.toNodeParams(ALICE_NAME), "partyB" to nodeParamsHelper.toNodeParams(BOB_NAME)),
                minimumPlatformVersion = 4,
                debug = false
        )