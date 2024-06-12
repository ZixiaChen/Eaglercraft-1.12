package net.minecraft.client.renderer.debug;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private EntityPlayer player;
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;

    public DebugRendererCollisionBox(Minecraft minecraftIn)
    {
        this.minecraft = minecraftIn;
    }

    public void render(float partialTicks, long finishTimeNano)
    {
        this.player = this.minecraft.player;
        this.renderPosX = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)partialTicks;
        this.renderPosY = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)partialTicks;
        this.renderPosZ = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)partialTicks;
        World world = this.minecraft.player.world;
        List<AxisAlignedBB> list = world.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().grow(6.0D));
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        EaglercraftGPU.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        for (AxisAlignedBB axisalignedbb : list)
        {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.grow(0.002D).offset(-this.renderPosX, -this.renderPosY, -this.renderPosZ), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
