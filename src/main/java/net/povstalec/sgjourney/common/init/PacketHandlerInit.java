package net.povstalec.sgjourney.common.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.povstalec.sgjourney.common.packets.*;

public final class PacketHandlerInit
{
	//============================================================================================
	//****************************************Registering*****************************************
	//============================================================================================
	
	@SubscribeEvent
	public static void registerPackets(final RegisterPayloadHandlersEvent event)
	{
		// Sets the current network version
		final PayloadRegistrar registrar = event.registrar("1");
		
		//============================================================================================
		//****************************************Client-bound****************************************
		//============================================================================================
		
		// Screen opening
		registrar.playToClient(
				ClientboundDialerOpenScreenPacket.TYPE,
				ClientboundDialerOpenScreenPacket.STREAM_CODEC,
				(packet, context) -> ClientboundDialerOpenScreenPacket.handle(packet, context));
		
		registrar.playToClient(
				ClientboundGDOOpenScreenPacket.TYPE,
				ClientboundGDOOpenScreenPacket.STREAM_CODEC,
				(packet, context) -> ClientboundGDOOpenScreenPacket.handle(packet, context));
		
		registrar.playToClient(
				ClientboundArcheologistNotebookOpenScreenPacket.TYPE,
				ClientboundArcheologistNotebookOpenScreenPacket.STREAM_CODEC,
				(packet, context) -> ClientboundArcheologistNotebookOpenScreenPacket.handle(packet, context));
		
		// Tech
		registrar.playToClient(
				ClientboundRingPanelUpdatePacket.TYPE,
				ClientboundRingPanelUpdatePacket.STREAM_CODEC,
				(packet, context) -> ClientboundRingPanelUpdatePacket.handle(packet, context));
		
		//Stargate Info
		registrar.playToClient(
				ClientboundStargateParticleSpawnPacket.TYPE,
				ClientboundStargateParticleSpawnPacket.STREAM_CODEC,
				(packet, context) -> ClientboundStargateParticleSpawnPacket.handle(packet, context));
		
		
		// Sounds
		registrar.playToClient(
				ClientBoundSoundPackets.OpenWormhole.TYPE,
				ClientBoundSoundPackets.OpenWormhole.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.OpenWormhole.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.IdleWormhole.TYPE,
				ClientBoundSoundPackets.IdleWormhole.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.IdleWormhole.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.CloseWormhole.TYPE,
				ClientBoundSoundPackets.CloseWormhole.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.CloseWormhole.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.IrisThud.TYPE,
				ClientBoundSoundPackets.IrisThud.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.IrisThud.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.Chevron.TYPE,
				ClientBoundSoundPackets.Chevron.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.Chevron.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.Fail.TYPE,
				ClientBoundSoundPackets.Fail.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.Fail.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.StargateRotation.TYPE,
				ClientBoundSoundPackets.StargateRotation.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.StargateRotation.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.UniverseStart.TYPE,
				ClientBoundSoundPackets.UniverseStart.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.UniverseStart.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.RotationStartup.TYPE,
				ClientBoundSoundPackets.RotationStartup.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.RotationStartup.handle(packet, context));
		
		registrar.playToClient(
				ClientBoundSoundPackets.RotationStop.TYPE,
				ClientBoundSoundPackets.RotationStop.STREAM_CODEC,
				(packet, context) -> ClientBoundSoundPackets.RotationStop.handle(packet, context));
		
		//============================================================================================
		//****************************************Server-bound****************************************
		//============================================================================================
		
		registrar.playToServer(
				ServerboundDHDUpdatePacket.TYPE,
				ServerboundDHDUpdatePacket.STREAM_CODEC,
				(packet, context) -> ServerboundDHDUpdatePacket.handle(packet, context));
		
		registrar.playToServer(
				ServerboundRingPanelUpdatePacket.TYPE,
				ServerboundRingPanelUpdatePacket.STREAM_CODEC,
				(packet, context) -> ServerboundRingPanelUpdatePacket.handle(packet, context));
		
		registrar.playToServer(
				ServerboundInterfaceUpdatePacket.TYPE,
				ServerboundInterfaceUpdatePacket.STREAM_CODEC,
				(packet, context) -> ServerboundInterfaceUpdatePacket.handle(packet, context));
		
		registrar.playToServer(
				ServerboundGDOUpdatePacket.TYPE,
				ServerboundGDOUpdatePacket.STREAM_CODEC,
				(packet, context) -> ServerboundGDOUpdatePacket.handle(packet, context));
		
		registrar.playToServer(
				ServerboundTransceiverUpdatePacket.TYPE,
				ServerboundTransceiverUpdatePacket.STREAM_CODEC,
				(packet, context) -> ServerboundTransceiverUpdatePacket.handle(packet, context));
	}
}
