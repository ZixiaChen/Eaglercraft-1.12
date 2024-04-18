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
import net.lax1dude.eaglercraft.v1_8.profile.*;
import net.minecraft.client.gui.inventory.GuiInventory;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.*;


/*
 * For once I have actually written some code myself
 * This file contains code written by Lax1dude
 * 
 * Copyright (c) Catfoolyou 2022-2024. All Rights Reserved.
 */

public class GuiScreenAuth extends GuiScreen
{

	private GuiTextField Username;
	private GuiTextField Skin;

	/** The old x position of the mouse pointer */
	private float oldMouseX;

	/** The old y position of the mouse pointer */
	private float oldMouseY;

	/** The default skin for the Steve model. */
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");

	/** The default skin for the Alex model. */
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

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

		this.Skin = new GuiTextField(1, this.fontRenderer, width / 2 - 20 + 1, height / 6 + 24 + 1, 138, 20);
		this.Skin.setFocused(true);
		this.Skin.setText(super.mc.getSession().getUsername());
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

		int whichDefaultSkin = 0;
		boolean isCustomSkin = false;

		int skinX = width / 2 - 120;
		int skinY = height / 6 + 8;
		int skinWidth = 80;
		int skinHeight = 130;
		
		this.drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, 0xFFA0A0A0);
		this.drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, 0xFF000015);

		this.oldMouseX = (float)mouseX;
		this.oldMouseY = (float)mouseY;

		int xx = width / 2 - 80;
		int yy = height / 6 + 130;
	
		skinX = width / 2 - 90;
		skinY = height / 4;

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableNormalize();

		SkinPreviewRenderer.initialize();
	
		if(whichDefaultSkin == 0){
			mc.getTextureManager().bindTexture(TEXTURE_STEVE);
			SkinPreviewRenderer.renderBiped(xx, yy, (int)mouseX, (int)mouseY, SkinModel.STEVE);
		}
		else{
			mc.getTextureManager().bindTexture(TEXTURE_ALEX);
			SkinPreviewRenderer.renderBiped(xx, yy, (int)mouseX, (int)mouseY, SkinModel.ALEX);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}