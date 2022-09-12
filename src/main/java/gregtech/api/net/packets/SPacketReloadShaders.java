package gregtech.api.net.packets;

import gregtech.api.gui.resources.ShaderTexture;
import gregtech.api.net.IClientExecutor;
import gregtech.api.net.IPacket;
import gregtech.client.shader.Shaders;
import lombok.NoArgsConstructor;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;

@NoArgsConstructor
@IPacket.Packet
public class SPacketReloadShaders implements IPacket, IClientExecutor {

    @Override
    public void encode(PacketBuffer buf) {
    }

    @Override
    public void decode(PacketBuffer buf) {
    }

    @Override
    public void executeClient(NetHandlerPlayClient handler) {
        if (Shaders.allowedShader()) {
            Shaders.initShaders();
            ShaderTexture.clear();
        }
    }
}
