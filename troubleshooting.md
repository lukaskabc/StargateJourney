---
title: Troubleshooting
nav_order: 20
has_children: false
---

# Troubleshooting
{: .no_toc }

If you can't find a solution to your problem on this page or if one of the solutions below doesn’t work, 
you can visit the [Discord server]({{ site.discord_invite_link }}) and ask there.

1. Table of Contents
{:toc}

## Stargate is not dialing / not disconnecting / causing any trouble
_With access to commands:_  
Use the `/sgjourney stargateNetwork forceStellarUpdate`  
As of 0.6.6 this command can no longer be used in Survival

_Without access to commands, but with access to world files:_  
Go to your `saves/<world name here>/data` folder
Delete `sgjourney-stargate_network.dat`

{: .warning }
> **UNDER NO CIRCUMSTANCES EVER DELETE** `sgjourney-block_enties.dat`.
> It holds info with **locations** of all the block entities, 
> like **Stargates** and **Transport Rings**,
> and deleting it means Stargate Network won't be able to find the Stargates anymore.

After all that, log in to your world again to let the Stargate Network regenerate, and everything should be fine.

___


## I added the mod to an existing world, but Stargate isn't generating.
The Stargate structure can only generate in **new chunks**. 
By default, Stargate structures generate in an area **centered around** the `X = 0` `Z = 0` coordinates, 
and the maximum allowed **offset** from that **center** position is `64` chunks.
Suppose you've already explored the world **before** adding Stargate Journey. 
In that case, you must change the **area offset** that the Stargate structure can generate.
To do this:

Go to the **Common Config** (`config/sgjourney_common.toml`), and under the **Stargate Network Config**, change 
`stargate_generation_center_x_chunk_offset` or `stargate_generation_center_z_chunk_offset` to some number of chunks further away, which you haven't generated yet.

___

<!-- TODO: datapack update tutorial also with KubeJS variant -->