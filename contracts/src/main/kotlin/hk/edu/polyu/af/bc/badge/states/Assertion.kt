package hk.edu.polyu.af.bc.badge.states

import com.r3.corda.lib.tokens.contracts.NonFungibleTokenContract
import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty

@BelongsToContract(NonFungibleTokenContract::class) // use generic NFT-Contract
class Assertion(
        badgeClass: BadgeClass,
        recipient: AbstractParty,
        override val linearId: UniqueIdentifier
): NonFungibleToken(
        IssuedTokenType(badgeClass.maintainers.first(), badgeClass.toPointer<BadgeClass>()),
        recipient,
        linearId
)