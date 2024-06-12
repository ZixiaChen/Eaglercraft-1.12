package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;

public class RenderList extends ChunkRenderContainer
{
    public void renderChunkLayer(BlockRenderLayer layer)
    {
        if (this.initialized)
        {
            if (this.renderChunks.size() == 0)
            {
                return;
            }

            for (RenderChunk renderchunk : this.renderChunks)
            {
                ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderchunk);
                EaglercraftGPU.glCallList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
                GlStateManager.popMatrix();
            }

            /*if (Config.isMultiTexture())
            {
                GlStateManager.bindCurrentTexture();
            }*/

            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}
