---
title: Stargate Iris
nav_order: 50
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

## Crafting an iris

To craft an iris, you will need a Stargate shielding ring first
which you can craft with four iron ingots and four redstone dusts.

![Stargate shielding ring recipe]({{ site.baseurl }}/assets/img/blocks/technological/iris/stargate_shielding_ring_recipe.png)

Then you can use the ring in the middle of crafting table and surround it with a material for the [iris type](#iris-types).

![Stargate naquadah alloy iris]({{ site.baseurl }}/assets/img/blocks/technological/iris/stargate_iris_recipe.png)

