package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class PreYggdrasilConverter
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
    public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
    public static final File OLD_OPS_FILE = new File("ops.txt");
    public static final File OLD_WHITELIST_FILE = new File("white-list.txt");

    private static void lookupNames(MinecraftServer server, Collection<String> names, Object callback)
    {

    }

    public static String convertMobOwnerIfNeeded(final MinecraftServer server, String username)
    {
        if (!StringUtils.isNullOrEmpty(username) && username.length() <= 16)
        {
            GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(username);

            if (gameprofile != null && gameprofile.getId() != null)
            {
                return gameprofile.getId().toString();
            }
            else if (!server.isSinglePlayer() && server.isServerInOnlineMode())
            {
                final List<GameProfile> list = Lists.<GameProfile>newArrayList();
                return username;
            }
        }
        else
        {
            return username;
        }
        return username;
    }
}
