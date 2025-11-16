package net.povstalec.sgjourney.common.packets;

import io.netty.buffer.ByteBuf;
import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.client.ClientAccess;

public record ClientboundDialerOpenScreenPacket(UUID playerId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ClientboundDialerOpenScreenPacket> TYPE =
            new CustomPacketPayload.Type<>(StargateJourney.sgjourneyLocation("s2c_dialer_open_screen"));
    
    public static final StreamCodec<ByteBuf, ClientboundDialerOpenScreenPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ClientboundDialerOpenScreenPacket::playerId,
            ClientboundDialerOpenScreenPacket::new
    );
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
    
    public static void handle(ClientboundDialerOpenScreenPacket packet, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
        	ClientAccess.updateDialer(packet.playerId);
        });
    }
}


