---
title: Stargate Network
nav_order: 70
has_children: true
---

1. Table of Contents
{:toc}

# Stargate Network
Stargate Network is a complex system that allows Stargates to communicate and establish wormhole connections between each other. Each Stargate has up to three specific addresses that allow other Stargates to dial itself. Stargate Network also is responsible for choosing the optimal Stargate whenever a Solar System is dialed.

## Addresses
There are 3 kinds of addresses the Stargates may have. Each of them serve different purposes, commonly distinguished by their length:
* `7-Symbol address`, also called "Solar System address", is an address used to dial a particular Solar System. This means the Stargate Network will pick the most suitable Stargate to route the incoming connection within the Solar System. Keep in mind that 7-Symbol addresses work only inside of the same galaxy, with only exception being if the given Solar System is a part of two Galaxies simultaneously.
* `8-Symbol addresses`, also called "Extragalactic Solar System address", is also an address used to dial Solar Systems, however 8-Symbol addresses allow for intergalactic connections, unlike 7-Symbol addresses.
* `9-chevron Addresses` is not an address allocated to a certain area of space like Solar Systems, but instead a unique ID of each existing Stargate. This type of address can be used to dial a specific Stargate, no matter how far or where it is located.
*Note: 7 and 8-Symbol addresses can and will change depending on the Stargate's location (Depending on which Solar System it is in), but a 9-Symbol address of a Stargate always remains the same.*

## Preferred Stargate
When a Stargate dials a Solar System, the Stargate Network will scan through the list of all available Stargates within given Solar System, and choose one to recieve the connection. It does so according to the three priorities it has, ordered in decrease of their importance. Higher priority will override lower priority:
1. Presence of a DHD - Due to high importance of any traveler having the ability to return, this priority is the top one.
2. Stargate Generation - Stargate Network will choose higher generation Stargates over lower ones, as higher generation most likely means they are more reliable and easy to use.
3. Total connection count - The Network will prioritise Stargates that have a higher number of total connections, because that means the given Stargate must be more reliable as it is used much more than others.

## Getting an Address
At the time of writing this part of the wiki, there are currently no ways to obtain specific 7 and 8-Symbol addresses in survival without using commands, but be aware that they are planned and will eventually be added.
To get an address of a Dimension, there exists two commands:
* `/sgjourney stargateNetwork address` is used to get a 7-Symbol address of a Dimension in the current Galaxy.
* `/sgjourney stargateNetwork extragalacticAddress` is used to get a 8-Symbol address of a Dimension.
In survival, you can find a **Naturally Generated** Cartouche on planets like Abydos and Chulak. They contain a 7-Symbol address of a random Solar System.
> Cartouches have defined Cartouche Tables similar to Chests with Loot Tables. Made Cartouches that don't have any predefined table will will default to the 7-Symbol addresss of the Dimensions they are placed in.
* You can give yourself a Cartouche with a specific Cartouche Table already set with usage of commands, for example: `/give @p sjgourney:sandstone_cartouche{BlockEntityTag:{AddressTable:"sgjourney:cartouche_buried_gate"}}`
> You can take a look at the [Cartouche Tables](https://github.com/Povstalec/StargateJourney/tree/main/src/main/resources/data/sgjourney/sgjourney/address_table) that are currently in the mod.

# Connections
Estabilishing a wormhole connection between two Stargates requires energy (Currently disabled by default, can be enabled in the mod's config), and each type of connection has a different energy cost. There are 3 kinds of connections you can estabilish when using a Stargate:
* `System-wide connection` is a connection between two Stargates which are either in the same Dimension, or in two Dimensions which are both part of the same Solar System.
> The only way to estabilish a System-wide connection is through usage of a 9-chevron Address, as the Stargate Network will not allow a Stargate to dial the same Solar System it's located in.
> Default energy cost of the connection is 50 000 FE.

* `Interstellar connection` is a connection between two Stargates located in different Solar Systems but within the same Galaxy.
> An Interstellar connection can be estabilished with either 7-Symbol, 8-Symbol or a 9-Symbol address.
> Default energy cost of the connection is 100 000 FE.

* `Intergalctic connection` is a connection between two Stargates located in different Galaxies, or with a Stargate located outside of any Galaxy.
> A connection can be estabilished with usage only of 8-Symbol or 9-Symbol addresses.
> Default energy cost of the connection is 100 000 000 000 FE.

# Stellar Update
Stellar updates are essentially updates to the Stargate Network which ensure the Stargate Network works properly. During a Stellar update, all Stargates are disconnected and the Stargate Network is regenerated from scratch, including any changes to the world. For instance, if you were to add a new Dimension to your world, the Dimension would not be registered by the Stargate Network until the next Stellar Update.
* A Stellar Update can be activated in multiple ways:
1. Stargate Network update (If the world has been loaded with a version of Stargate Journey that features a newer version of Stargate Network, it will automatically commence Stellar Update)
2. Running the `/sgjourney stargateNetwork forceStellarUpdate` command.
3. Outside of the game by deleting the `sgjourney-stargate_network` file from the world's `data` folder.

# Versions
Stargate Network has gone through multiple versions, growing in complexity and featuring different mechanics.

## Version 0
* **First used in:** `Stargate Journey 0.4.0`
* **Last used in:** `Stargate Journey 0.5.1`
> * Version 0 used a Primary Stargate system, which would designate the oldest Stargate in a given Solar System as the Primary Stargate, which would receive all incoming connections.

### Version 1
* **First used in:** `Stargate Journey 0.5.2`
* **Last used in:** `Stargate Journey 0.5.4`
* Changes:
> * TBD

### Version 2
* **First used in:** `Stargate Journey 0.6.0`
* **Last used in:** `Stargate Journey 0.6.0`
* Changes:
> * Removed the Primary Stargate system in favor of Preferred Stargate System.

### Version 3
* **First used in:** `Stargate Journey 0.6.1`
* **Last used in:** `Stargate Journey 0.6.2`
* Changes:
> * Nether Address in Milky Way is changed from -8-7-6-5-4-3- to -27-23-4-34-12-28-
> * End Address in Milky Way is changed from -1-2-3-4-5-6- to -13-24-2-19-3-30-
> * End Address in Pegasus is changed from -1-2-3-4-5-6- to -19-30-6-13-3-24-

### Version 4
* **First used in:** `Stargate Journey 0.6.3`
* **Last used in:** `Stargate Journey 0.6.5`
* * Changes:
> * TBD

### Version 5
* **First used in:** `Stargate Journey 0.6.6`
* Changes:
> * Dialed Stargates now lock Chevrons one by one until a wormhole is formed

### Version 6
* TBD

### Version 7
* **First used in:** `Stargate Journey 0.6.10`
* **Last used in:** Still used as of the newest version (Stargate Journey 0.6.10)
* Changes:
> * Changed Extragalactic Addresses of Abydos and Chulak
