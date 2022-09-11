package gregtech.common.command;

import gregtech.api.net.NetworkHandler;
import gregtech.api.net.packets.SPacketReloadShaders;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import org.jetbrains.annotations.NotNull;

public class CommandShaders extends CommandBase {

    @Override
    @NotNull
    public String getName() {
        return "reloadshaders";
    }

    @Override
    @NotNull
    public String getUsage(@NotNull ICommandSender iCommandSender) {
        return "Reload GTCEu Shaders";
    }

    @Override
    public void execute(@NotNull MinecraftServer minecraftServer, @NotNull ICommandSender iCommandSender, @NotNull String[] strings) {
        if (iCommandSender instanceof EntityPlayerMP) {
            NetworkHandler.channel.sendTo(new SPacketReloadShaders().toFMLPacket(), (EntityPlayerMP) iCommandSender);
            iCommandSender.sendMessage(new TextComponentString("Reloaded Shaders"));
        } else {
            iCommandSender.sendMessage(new TextComponentString("Command cannot be run on the server"));
        }
    }
}
