---
title: Transceiver
nav_order: 5
has_children: false
parent: Computercraft
---

{% include computercraft_doc.html %}

# Transceiver
{: .no_toc }

1. Table of Contents
{:toc}

[//]: # (TODO: improve this description)
[Transceiver]({{ site.baseurl }}/blocks/technological_blocks/#transceiver) allows to receive an IDentification Code (IDC) from a [Garage Door Opener (GDO)]({{ site.baseurl }}/items/functional/#garage-door-opener-gdo) through an active stargate on a configured frequency.
Transceiver can be configured to listen on a specific frequency and for a specific IDC.
When the transceiver receives a signal on the configured frequency, it will raise a computercraft event [transceiver_transmission_received]({{ site.baseurl }}/computercraft/events/#transceiver_transmission_received).
Although the transceiver is able to validate only a single IDC, the computer can perform the validation itself by checking the received IDC from the event.
The transceiver can listen only on a single frequency.
More transceivers can be used to listen on different frequencies simultaneously.

It is a standalone peripheral (do not use the stargate interface). 
It can be placed next to the computer or connected using the wired modem.

