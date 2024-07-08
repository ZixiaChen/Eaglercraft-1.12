package net.minecraft.client.renderer.texture;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.TextureUtil;

public class LayeredColorMaskTexture extends AbstractTexture
{
    /** Access to the Logger, for all your logging needs. */
    private static final Logger LOGGER = LogManager.getLogger();

    /** The location of the texture. */
    private final ResourceLocation textureLocation;
    private final List<String> listTextures;
    private final List<EnumDyeColor> listDyeColors;

    public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_)
    {
        this.textureLocation = textureLocationIn;
        this.listTextures = p_i46101_2_;
        this.listDyeColors = p_i46101_3_;
    }

    public void loadTexture(IResourceManager parIResourceManager) throws IOException {
		this.deleteGlTexture();

		ImageData bufferedimage;
		try {
			ImageData bufferedimage1 = TextureUtil
					.readBufferedImage(parIResourceManager.getResource(this.textureLocation).getInputStream());

			bufferedimage = new ImageData(bufferedimage1.width, bufferedimage1.height, false);
			bufferedimage.drawLayer(bufferedimage1, 0, 0, bufferedimage1.width, bufferedimage1.height, 0, 0,
					bufferedimage1.width, bufferedimage1.height);

			for (int j = 0; j < 17 && j < this.listTextures.size() && j < this.listDyeColors.size(); ++j) {
				String s = (String) this.listTextures.get(j);
				MapColor mapcolor = MapColor.COLORS[j];
				if (s != null) {
					InputStream inputstream = parIResourceManager.getResource(new ResourceLocation(s)).getInputStream();
					ImageData bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
					if (bufferedimage2.width == bufferedimage.width && bufferedimage2.height == bufferedimage.height) {
						for (int k = 0; k < bufferedimage2.height; ++k) {
							for (int l = 0; l < bufferedimage2.width; ++l) {
								int i1 = bufferedimage2.pixels[k * bufferedimage2.width + l];
								if ((i1 & -16777216) != 0) {
									int j1 = (i1 & 16711680) << 8 & -16777216;
									int k1 = bufferedimage1.pixels[k * bufferedimage1.width + l];
									int l1 = MathHelper.multiplyColor(k1, ImageData.swapRB(mapcolor.colorValue))
											& 16777215;
									bufferedimage2.pixels[k * bufferedimage2.width + l] = j1 | l1;
								}
							}
						}

						bufferedimage.drawLayer(bufferedimage2, 0, 0, bufferedimage2.width, bufferedimage2.height, 0, 0,
								bufferedimage2.width, bufferedimage2.height);
					}
				}
			}
		} catch (IOException ioexception) {
			LOGGER.error("Couldn\'t load layered image", ioexception);
			return;
		}

		TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
	}
}
