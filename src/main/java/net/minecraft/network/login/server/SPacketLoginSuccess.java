package net.minecraft.network.login.server;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.*;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class SPacketLoginSuccess implements Packet<INetHandlerLoginClient>
{
    private GameProfile profile;

    public SPacketLoginSuccess()
    {
    }

    public SPacketLoginSuccess(GameProfile profileIn)
    {
        this.profile = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        String s = buf.readString(36);
        String s1 = buf.readString(16);
        EaglercraftUUID uuid = new EaglercraftUUID(s);
        this.profile = new GameProfile(uuid, s1);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        EaglercraftUUID uuid = this.profile.getId();
        buf.writeString(uuid == null ? "" : uuid.toString());
        buf.writeString(this.profile.getName());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleLoginSuccess(this);
    }

    public GameProfile getProfile()
    {
        return this.profile;
    }
}
