package net.minecraft.util;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.UUIDTypeAdapter;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;

//import com.mojang.util.UUIDTypeAdapter;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

public class Session
{
    private String username;
    private final String playerID;
    private final String token;
    private final Session.Type sessionType;
    private static EaglercraftUUID offlineUUID;

    private GameProfile profile;

    public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn)
    {
        this.username = usernameIn;
        this.playerID = playerIDIn;
        this.token = tokenIn;
        this.sessionType = Session.Type.setSessionType(sessionTypeIn);
    }
    
    public Session() {
		this.playerID = "";
        this.token = "";
        this.sessionType = null;
        reset();
	}

    public String getSessionID()
    {
        return "token:" + this.token + ":" + this.playerID;
    }

    public String getPlayerID()
    {
        return this.playerID;
    }

    public String getUsername()
    {
        return this.username;
    }

	public void overrideUsername(String name)
    {
        this.username = name;
    }

    public String getToken()
    {
        return this.token;
    }

    public void reset() {
		update(EaglerProfile.getName(), offlineUUID);
	}

    public GameProfile getProfile()
    {
        try
        {
            EaglercraftUUID uuid = UUIDTypeAdapter.fromString(this.getPlayerID());
            profile = new GameProfile(uuid, this.getUsername());
            return profile;
        }
        catch (IllegalArgumentException var2)
        {
            profile = new GameProfile(new EaglercraftUUID(playerID), this.getUsername());
            return profile;
        }
    }

    public void update(String serverUsername, EaglercraftUUID uuid) {
		profile = new GameProfile(uuid, serverUsername);
	}

    public static enum Type
    {
        LEGACY("legacy"),
        MOJANG("mojang");

        private static final Map<String, Session.Type> SESSION_TYPES = Maps.<String, Session.Type>newHashMap();
        private final String sessionType;

        private Type(String sessionTypeIn)
        {
            this.sessionType = sessionTypeIn;
        }

        @Nullable
        public static Session.Type setSessionType(String sessionTypeIn)
        {
            return SESSION_TYPES.get(sessionTypeIn.toLowerCase(Locale.ROOT));
        }

        static {
            for (Session.Type session$type : values())
            {
                SESSION_TYPES.put(session$type.sessionType, session$type);
            }
        }
    }

    static {
		byte[] bytes = new byte[16];
		(new EaglercraftRandom()).nextBytes(bytes);
		offlineUUID = new EaglercraftUUID(bytes);
	}
}
