package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;

public class DefaultPlayerSkin
{
    /** The default skin for the Steve model. */
    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");

    /** The default skin for the Alex model. */
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

	private static ResourceLocation Skin = TEXTURE_STEVE;

    /**
     * Returns the default skind for versions prior to 1.8, which is always the Steve texture.
     */
    public static ResourceLocation getDefaultSkinLegacy()
    {
        return Skin;
    }

    /**
     * Retrieves the default skin for this player. Depending on the model used this will be Alex or Steve.
     */
    public static ResourceLocation getDefaultSkin(UUID playerUUID)
    {
        return Skin;
    }

    public static int getSkin()
    {
        if(Skin == TEXTURE_STEVE){
		return 0;
	}
	if(Skin == TEXTURE_ALEX){
		return 1;
	}
	else{
		return 0;
	}
    }

	public static ResourceLocation setDefaultSkin(int skin)
    {
		if(skin == 0){
			Skin = TEXTURE_STEVE;
		}
		if(skin == 1){
			Skin = TEXTURE_ALEX;
		}
        return Skin;
    }

    /**
     * Retrieves the type of skin that a player is using. The Alex model is slim while the Steve model is default.
     */
    public static String getSkinType(UUID playerUUID)
    {
        return "default";
    }

    /**
     * Checks if a players skin model is slim or the default. The Alex model is slime while the Steve model is default.
     */
    private static boolean isSlimSkin(UUID playerUUID)
    {
		if(Skin == TEXTURE_STEVE){
			return false;
		}
		else{
			return true;		
		}
        
    }
}