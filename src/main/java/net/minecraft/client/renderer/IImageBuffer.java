package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

public interface IImageBuffer
{
    ImageData parseUserSkin(ImageData image);

    void skinAvailable();
}
