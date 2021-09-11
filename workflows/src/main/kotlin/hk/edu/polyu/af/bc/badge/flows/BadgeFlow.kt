package hk.edu.polyu.af.bc.badge.flows

import net.corda.core.flows.FlowLogic

/**
 * Toy flow returning module information
 */
class BadgeFlow: FlowLogic<String>() {
    override fun call(): String {
        return "The Badge Module"
    }
}