package net.catfoolyou.gui;

import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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

	private int whichDefaultSkin = 0;
	private boolean newSkinWaitSteveOrAlex = false; // The eagler equivalent

	private float oldMouseX;
	private float oldMouseY;

	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

	private boolean dropDownOpen = false;
	private String[] dropDownOptions;
	private int slotsVisible = 0;
	private int selectedSlot = 0;
	private int scrollPos = -1;
	private int skinsHeight = 0;
	private boolean dragging = false;
	private int mousex = 0;
	private int mousey = 0;

	private static final ResourceLocation eaglerGui = new ResourceLocation("textures/gui/eagler_gui.png");

	private void updateOptions() {
		String[] n = new String[DefaultSkins.defaultSkinsMap.length];
		int numDefault = DefaultSkins.defaultSkinsMap.length;
		for(int i = 0; i < numDefault; ++i) {
			n[i] = DefaultSkins.defaultSkinsMap[i].name;
		}
		
		dropDownOptions = n;
	}

	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
		this.Username = new GuiTextField(0, this.fontRenderer, width / 2 - 20 + 1, height / 6 + 24 + 1, 138, 20);
		this.Username.setFocused(true);
		this.Username.setText(super.mc.getSession().getUsername());

		whichDefaultSkin = DefaultPlayerSkin.getSkin();
		selectedSlot = whichDefaultSkin;
		updateOptions();

		buttonList.add(new GuiButton(1, width / 2 - 21, height / 6 + 110, 71, 20, I18n.format("Add Skin")));
		buttonList.add(new GuiButton(2, width / 2 - 21 + 71, height / 6 + 110, 72, 20, I18n.format("Clear List")));
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

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if(dropDownOpen) {
			int var1 = Mouse.getEventDWheel();
			if(var1 < 0) {
				scrollPos += 3;
			}
			if(var1 > 0) {
				scrollPos -= 3;
				if(scrollPos < 0) {
					scrollPos = 0;
				}
			}
		}
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

		boolean isCustomSkin = false;

		whichDefaultSkin = selectedSlot;
		DefaultPlayerSkin.setDefaultSkin(whichDefaultSkin);

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

		if(dropDownOpen ||  newSkinWaitSteveOrAlex) {
			super.drawScreen(0, 0, partialTicks);
		}else {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	
		skinX = width / 2 - 20;
		skinY = height / 6 + 82;
		skinWidth = 140;
		skinHeight = 22;
		
		drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
		drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 21, skinY + skinHeight - 1, -16777216);
		drawRect(skinX + skinWidth - 20, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
		
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		mc.getTextureManager().bindTexture(eaglerGui);
		drawTexturedModalRect(skinX + skinWidth - 18, skinY + 3, 0, 0, 16, 16);

		//Begin experimantal dropdown code

		drawString(this.fontRenderer, dropDownOptions[selectedSlot], skinX + 5, skinY + 7, 14737632);

		skinX = width / 2 - 20;
		skinY = height / 6 + 103;
		skinWidth = 140;
		skinHeight = (height - skinY - 10);
		slotsVisible = (skinHeight / 10);
		if(slotsVisible > dropDownOptions.length) slotsVisible = dropDownOptions.length;
		skinHeight = slotsVisible * 10 + 7;
		skinsHeight = skinHeight;
		if(scrollPos == -1) {
			scrollPos = selectedSlot - 2;
		}
		if(scrollPos > (dropDownOptions.length - slotsVisible)) {
			scrollPos = (dropDownOptions.length - slotsVisible);
		}
		if(scrollPos < 0) {
			scrollPos = 0;
		}
		if(dropDownOpen) {
			drawRect(skinX, skinY, skinX + skinWidth, skinY + skinHeight, -6250336);
			drawRect(skinX + 1, skinY + 1, skinX + skinWidth - 1, skinY + skinHeight - 1, -16777216);
			for(int i = 0; i < slotsVisible; i++) {
				if(i + scrollPos < dropDownOptions.length) {
					if(selectedSlot == i + scrollPos) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x77ffffff);
					}else if(mouseX >= skinX && mouseX < (skinX + skinWidth - 10) && mouseY >= (skinY + i*10 + 5) && mouseY < (skinY + i*10 + 15)) {
						drawRect(skinX + 1, skinY + i*10 + 4, skinX + skinWidth - 1, skinY + i*10 + 14, 0x55ffffff);
					}
					drawString(this.fontRenderer, dropDownOptions[i + scrollPos], skinX + 5, skinY + 5 + i*10, 14737632);
				}
			}
			int scrollerSize = skinHeight * slotsVisible / dropDownOptions.length;
			int scrollerPos = skinHeight * scrollPos / dropDownOptions.length;
			drawRect(skinX + skinWidth - 4, skinY + scrollerPos + 1, skinX + skinWidth - 1, skinY + scrollerPos + scrollerSize, 0xff888888);
		}

		//End experimental dropdown code

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

		//super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public void updateScreen() {
		Username.updateCursorCounter();
		if(dropDownOpen) {
			if(Mouse.isButtonDown(0)) {
				int skinX = width / 2 - 20;
				int skinY = height / 6 + 103;
				int skinWidth = 140;
				if(mousex >= (skinX + skinWidth - 10) && mousex < (skinX + skinWidth) && mousey >= skinY && mousey < (skinY + skinsHeight)) {
					dragging = true;
				}
				if(dragging) {
					int scrollerSize = skinsHeight * slotsVisible / dropDownOptions.length;
					scrollPos = (mousey - skinY - (scrollerSize / 2)) * dropDownOptions.length / skinsHeight;
				}
			}else {
				dragging = false;
			}
		}else {
			dragging = false;
		}
	}

	protected void mouseClicked(int mx, int my, int button) throws IOException {
		super.mouseClicked(mx, my, button);
		Username.mouseClicked(mx, my, button);
		if (button == 0) {
			int skinX = width / 2 + 140 - 40;
			int skinY = height / 6 + 82;
		
			if(mx >= skinX && mx < (skinX + 20) && my >= skinY && my < (skinY + 22)) {
				dropDownOpen = !dropDownOpen;
				return;
			}
			
			skinX = width / 2 - 20;
			skinY = height / 6 + 82;
			int skinWidth = 140;
			int skinHeight = skinsHeight;
			
			if(!(mx >= skinX && mx < (skinX + skinWidth) && my >= skinY && my < (skinY + skinHeight + 22))) {
				dropDownOpen = false;
				dragging = false;
				return;
			}
			
			skinY += 21;
			
			if(dropDownOpen && !dragging) {
				for(int i = 0; i < slotsVisible; i++) {
					if(i + scrollPos < dropDownOptions.length) {
						if(selectedSlot != i + scrollPos) {
							if(mx >= skinX && mx < (skinX + skinWidth - 10) && my >= (skinY + i * 10 + 5) && my < (skinY + i * 10 + 15) && selectedSlot != i + scrollPos) {
								selectedSlot = i + scrollPos;
								dropDownOpen = false;
								dragging = false;
							}
						}
					}
				}
			}
		}
	}

}