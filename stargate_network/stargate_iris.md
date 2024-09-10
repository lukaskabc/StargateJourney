---
title: Stargate Iris
nav_order: 10
has_children: false
parent: Stargate Network
iris_types: [Naquadah Alloy, Steel, Copper, Iron, Golden, Diamond, Netherite, Bronze]
---

1. Table of Contents
{:toc}

# Stargate Iris

> The iris is a metal covering on the Earth Stargate which is used to screen incoming traffic.  
> When the iris is closed, it forms a barrier less than three μm from the wormhole's event horizon, 
> thereby preventing most forms of matter from properly reintegrating.
> Source: [stargate.fandom.com](https://stargate.fandom.com/wiki/Iris)

Stargate is a gateway to other worlds, hiding many things to discover but also many dangers.

Surely, you don't want any uninvited visitors; 
there is a very *democratic* way of preventing such occurrences -- build a wall and let every visitor smash into it, 
and by the wall, I mean the iris.

[//]: # (Idk man, its 1AM, what do you want from me?)

<details markdown="block">
<summary id="iris-types">Iris types</summary>

[//]: # (This loop will print each iris type defined in the page header as the name and image)
{% for type in page.iris_types %}

{{ type }} iris  
`{{ type | downcase | replace: " ", "_" }}_iris`  

![{{ type }} iris]({{ site.baseurl }}/assets/img/blocks/technological/iris/{{ type | downcase | replace: " ", "_" }}_iris.png)
{: .max-width-512 }

___

{% endfor %}

</details>

## Crafting

To craft an iris, you will need a Stargate shielding ring first
which you can craft with four iron ingots and four redstone dusts.

![Stargate shielding ring recipe]({{ site.baseurl }}/assets/img/blocks/technological/iris/stargate_shielding_ring_recipe.png)

Then you can use the ring in the middle of crafting table and surround it with a material for the [iris type](#iris-types).

![Stargate naquadah alloy iris]({{ site.baseurl }}/assets/img/blocks/technological/iris/stargate_iris_recipe.png)

## Setting up

Once you have an iris, 
you can install it into any stargate with **right-click** 
(except for [Tollan Stargate]({{ site.baseurl }}/blocks/technological_blocks/#tollan) 
which is too thin for an iris installation).

{: .warning }
Make sure that all your stuff controlling the gate is in **the same chunk as the gate itself**.  
Otherwise, when the remote chunk is loaded by an incoming connection to the gate, 
anything **outside** the gate's chunk will not be loaded and so **won't work**.
Also, **do not use pistons** in the redstone circuits, 
when chunk is loaded by the gate, pistons and similar mechanics are not working.


### Manual control

{: .future }
In some future release, an option to control the iris with computers will be added

Currently, it's only possible to control the iris with redstone, using the **basic interface**.  
Additionally, you can use Garage Door Opener (GDO) to open iris remotely.

{: .warning }
If you don't know redstone well, pay attention to each block and its direction, comparators and repeaters don't work both ways.

First, you need to set up the **basic interface** in the shielding mode (not that other interfaces can't be used yet).  
[Stargate Network / Stargate Interface / Shielding mode]({{ site.baseurl }}/stargate_network/interface/#shielding-mode)

![Basic interface in shielding mode]({{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/basic_interface_shielding.png)
{: .max-width-512 }

**Basic setup with a lever control**  
For controlling the iris with a single lever, we need to constantly power the interface with a signal of strength 7 or lower.  
That is required to close the iris whenever the lever is turned off.
Then simply powering the interface with a signal of strength 8 or more will open the iris.

There are, of course, plenty of ways how to do it, here are two examples.  
The simple way using a lever and redstone torch.
The torch emits a signal of strength **15**, the left path is **9** blocks long.
Redstone loses a level for each block, leaving the interface with signal strength **7** 
(which is the largest strength level that closes the iris). 
The direct way from lever has a length of **4** blocks, providing signal strength **12** when lever is switched on
(any signal above **7** will work).

{: .note }
Note that the length of redstone is critical here.

![Basic lever iris control]({{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/basic_lever.png)
{: .max-width-512 }

**More minimalistic way**  
The redstone torch emits signal strength **15**.
The right path loses two levels,
providing the comparator with signal strength **13** from the side.
The comparator is set to the subtraction mode outputting signal strength **2**.
The lever provides signal strength 15 using the repeater, which is more than two from the comparator, 
and so iris is opened once the lever is switched on.

![Minimalistic lever iris control]({{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/minimalistic_lever.png)
{: .max-width-512 }

### Automatic iris closing

What if you wanted the iris to automatically open for outgoing connections, 
so you can use the gate, 
and closed for incoming connections reflecting any uninvited visitors, 
unless opened with a lever?  
It is indeed possible with a very simple setup.

Using an interface in the wormhole mode will provide appropriate signal strength for the interface in the iris mode
to automatically open/close the iris based on the connection direction.

During an incoming connection, the iris can be easily opened with a redstone signal of strength **8** or higher.

![Iris auto close]({{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/auto_close.png)

You can also instruct the iris to automatically open when there is no connection.

![Iris auto open-close]({{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/auto_open_close.png)

### Remote iris control

So you have secured the gate with an auto-closing iris for incoming connection, but how you can open the iris
when you are on the other side of the connection?  
Garage Door Opener (GDO) is a hand-held device that allows to send a signal through the Stargate connection
to the other side. 
For example, automatically opening the iris.

[//]: # (![Iris with GDO]&#40;{{ site.baseurl }}/assets/img/mechanics/stargate_network/iris/auto_open_close.png&#41;)