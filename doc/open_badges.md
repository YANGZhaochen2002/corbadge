# Open Badges

A summary of Open Badges standard in Corda's vocabulary.

## Parties

There are three types of participants regarding open badges operations: *issuer*, *recipient*, *host* and *displayer*, *badge consumer*. <!-- Can we generalize host and displayer to be observer?-->

## Domain Object Types

### BadgeClass

### Assertion

### Backpack

Storage service. This function is built-in on the blockchain platform? Storage first-of-all is provided by the *Badge Issuer*. If *Badge Recipient* hosts a node, then this node also offers storage. Else if *Badge Recipient* is an account on some node, the hosting node of that account acts as the host node of the issued badge. Any observers of the badge also provide storage service.