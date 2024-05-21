package net.minecraft.client.renderer;

import net.minecraft.util.BlockRenderLayer;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.util.EnumWorldBlockLayer;

public class RegionRenderCacheBuilder
{
    private final BufferBuilder[] worldRenderers = new BufferBuilder[BlockRenderLayer.values().length];
    private final WorldRenderer[] worldRenderer = new WorldRenderer[EnumWorldBlockLayer.values().length];


    public RegionRenderCacheBuilder()
    {
        this.worldRenderers[BlockRenderLayer.SOLID.ordinal()] = new BufferBuilder(2097152);
        this.worldRenderers[BlockRenderLayer.CUTOUT.ordinal()] = new BufferBuilder(131072);
        this.worldRenderers[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BufferBuilder(131072);
        this.worldRenderers[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BufferBuilder(262144);

        this.worldRenderer[EnumWorldBlockLayer.SOLID.ordinal()] = new WorldRenderer(2097152);
		this.worldRenderer[EnumWorldBlockLayer.CUTOUT.ordinal()] = new WorldRenderer(131072);
		this.worldRenderer[EnumWorldBlockLayer.CUTOUT_MIPPED.ordinal()] = new WorldRenderer(131072);
		this.worldRenderer[EnumWorldBlockLayer.TRANSLUCENT.ordinal()] = new WorldRenderer(262144);
    }

    public BufferBuilder getWorldRendererByLayer(BlockRenderLayer layer)
    {
        return this.worldRenderers[layer.ordinal()];
    }

    public BufferBuilder getWorldRendererByLayerId(int id)
    {
        return this.worldRenderers[id];
    }

    public WorldRenderer getWorldRendererByLayer2(BlockRenderLayer layer) {
		return this.worldRenderer[layer.ordinal()];
	}

	public WorldRenderer getWorldRendererByLayerId2(int id) {
		return this.worldRenderer[id];
	}
}
