package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import javax.annotation.Nullable;

public class ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    @Nullable
    public ImageData parseUserSkin(ImageData bufferedimage) {
		if (bufferedimage == null) {
			return null;
		} else {
			this.imageWidth = 64;
			this.imageHeight = 64;
			ImageData bufferedimage1 = new ImageData(this.imageWidth, this.imageHeight, true);
			bufferedimage1.copyPixelsFrom(bufferedimage, 0, 0, bufferedimage.width, bufferedimage.height, 0, 0,
					bufferedimage.width, bufferedimage.height);
			if (bufferedimage.height == 32) {
				bufferedimage1.drawLayer(bufferedimage, 24, 48, 20, 52, 4, 16, 8, 20);
				bufferedimage1.drawLayer(bufferedimage, 28, 48, 24, 52, 8, 16, 12, 20);
				bufferedimage1.drawLayer(bufferedimage, 20, 52, 16, 64, 8, 20, 12, 32);
				bufferedimage1.drawLayer(bufferedimage, 24, 52, 20, 64, 4, 20, 8, 32);
				bufferedimage1.drawLayer(bufferedimage, 28, 52, 24, 64, 0, 20, 4, 32);
				bufferedimage1.drawLayer(bufferedimage, 32, 52, 28, 64, 12, 20, 16, 32);
				bufferedimage1.drawLayer(bufferedimage, 40, 48, 36, 52, 44, 16, 48, 20);
				bufferedimage1.drawLayer(bufferedimage, 44, 48, 40, 52, 48, 16, 52, 20);
				bufferedimage1.drawLayer(bufferedimage, 36, 52, 32, 64, 48, 20, 52, 32);
				bufferedimage1.drawLayer(bufferedimage, 40, 52, 36, 64, 44, 20, 48, 32);
				bufferedimage1.drawLayer(bufferedimage, 44, 52, 40, 64, 40, 20, 44, 32);
				bufferedimage1.drawLayer(bufferedimage, 48, 52, 44, 64, 52, 20, 56, 32);
			}

			this.imageData = bufferedimage1.pixels;
			this.setAreaOpaque(0, 0, 32, 16);
			this.setAreaTransparent(32, 0, 64, 32);
			this.setAreaOpaque(0, 16, 64, 32);
			this.setAreaTransparent(0, 32, 16, 48);
			this.setAreaTransparent(16, 32, 40, 48);
			this.setAreaTransparent(40, 32, 56, 48);
			this.setAreaTransparent(0, 48, 16, 64);
			this.setAreaOpaque(16, 48, 48, 64);
			this.setAreaTransparent(48, 48, 64, 64);
			return bufferedimage1;
		}
	}

    public void skinAvailable()
    {
    }

    private void setAreaTransparent(int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                int k = this.imageData[i + j * this.imageWidth];

                if ((k >> 24 & 255) < 128)
                {
                    return;
                }
            }
        }

        for (int l = x; l < width; ++l)
        {
            for (int i1 = y; i1 < height; ++i1)
            {
                this.imageData[l + i1 * this.imageWidth] &= 16777215;
            }
        }
    }

    /**
     * Makes the given area of the image opaque
     */
    private void setAreaOpaque(int x, int y, int width, int height)
    {
        for (int i = x; i < width; ++i)
        {
            for (int j = y; j < height; ++j)
            {
                this.imageData[i + j * this.imageWidth] |= -16777216;
            }
        }
    }
}
