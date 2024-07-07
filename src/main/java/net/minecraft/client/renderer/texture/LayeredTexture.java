package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.TextureUtil;

public class LayeredTexture extends AbstractTexture
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public LayeredTexture(String... textureNames)
    {
        this.layeredTextureNames = Lists.newArrayList(textureNames);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        this.deleteGlTexture();
        ImageData bufferedimage = null;

        for (String s : this.layeredTextureNames)
        {
            IResource iresource = null;

            try
            {
                if (s != null)
                {
                    iresource = resourceManager.getResource(new ResourceLocation(s));
                    ImageData bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());

                    if (bufferedimage == null)
                    {
                        bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, true);
                    }

                    bufferedimage.drawLayer(bufferedimage1, 0, 0, 0, 0, 0, 0, 0, 0);
                }

                continue;
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't load layered image", (Throwable)ioexception);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)iresource);
            }

            return;
        }

        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}
