package net.minecraft.client.renderer.texture;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import net.lax1dude.eaglercraft.v1_8.minecraft.TextureUtil;

public class DynamicTexture extends AbstractTexture
{
    private final int[] dynamicTextureData;

    /** width of this icon in pixels */
    private final int width;

    /** height of this icon in pixels */
    private final int height;
    private boolean shadersInitialized;

    public DynamicTexture(ImageData bufferedImage)
    {
        this(bufferedImage.width, bufferedImage.height);
        bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, this.dynamicTextureData, 0, bufferedImage.width);
        this.updateDynamicTexture();
    }

    public DynamicTexture(int textureWidth, int textureHeight)
    {
        this.shadersInitialized = false;
        this.width = textureWidth;
        this.height = textureHeight;
        this.dynamicTextureData = new int[textureWidth * textureHeight * 3];

        TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
    }

    public void updateDynamicTexture()
    {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }
}
