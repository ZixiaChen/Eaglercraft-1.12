package net.catfoolyou.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;

/*
 * For once I have actually written some code myself
 * This file contains code written by Lax1dude
 * 
 * Copywrite (c) Catfoolyou 2024
 */

public class GuiScreenAuth extends GuiScreen
{

	private GuiTextField Username;
	private GuiTextField Skin;

	/** The old x position of the mouse pointer */
    private float oldMouseX;

    /** The old y position of the mouse pointer */
    private float oldMouseY;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
		Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
		this.buttonList.add(new GuiButton(1, width / 2 - 21, height / 6 + 110, 71, 20, I18n.format("Add Skin")));
		this.buttonList.add(new GuiButton(2, width / 2 - 21 + 71, height / 6 + 110, 72, 20, I18n.format("Clear List")));
		this.Username = new GuiTextField(0, this.fontRenderer, width / 2 - 20 + 1, height / 6 + 24 + 1, 138, 20);
        this.Username.setFocused(true);
        this.Username.setText(super.mc.getSession().getUsername());
    }

	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
			super.mc.getSession().overrideUsername(this.Username.getText());
            super.mc.displayGuiScreen(new GuiMainMenu());
        }
    }


    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.Username.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15)
        {
            this.Username.setFocused(!this.Username.isFocused());
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed(this.buttonList.get(0));
        }

        (this.buttonList.get(0)).enabled = !this.Username.getText().isEmpty();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.format("Edit Profile"), this.width / 2, 17, 16777215);
        this.drawString(this.fontRenderer, I18n.format("Username"), width / 2 - 20, height / 6 + 8, 10526880);
		this.drawString(this.fontRenderer, I18n.format("Player Skin"), width / 2 - 20, height / 6 + 66, 10526880);
        this.Username.drawTextBox();

		int skinX = width / 2 - 120;
		int skinY = height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		this.drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xFFA0A0A0);
		this.drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xFF000015);

		this.oldMouseX = (float)mouseX;
        this.oldMouseY = (float)mouseY;

		int i = 0;
        int j = 0;

		//drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, new EntityPlayer()); //Make a new entityplayer with the desired skin.

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
