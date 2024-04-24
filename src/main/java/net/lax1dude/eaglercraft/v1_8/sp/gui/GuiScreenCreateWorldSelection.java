package net.lax1dude.eaglercraft.v1_8.sp.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.catfoolyou.pakutils.FileCopyUtils;

import net.catfoolyou.gui.GuiPakErrorScreen;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class GuiScreenCreateWorldSelection extends GuiScreen {

	private GuiScreen mainmenu;
	private GuiButton worldCreate = null;
	private GuiButton worldImport = null;
	private GuiButton worldVanilla = null;
	private boolean isImportingEPK = false;
	private boolean isImportingMCA = false;
	
	public GuiScreenCreateWorldSelection(GuiScreen mainmenu) {
		this.mainmenu = mainmenu;
	}
	
	public void initGui() {
		this.buttonList.add(worldCreate = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 40, I18n.format("Create New World")));
		this.buttonList.add(worldImport = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 65, I18n.format("Load EPK File")));
		this.buttonList.add(worldVanilla = new GuiButton(3, this.width / 2 - 100, this.height / 4 + 90, I18n.format("Import Vanilla World")));
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 130, I18n.format("gui.cancel")));
	}
	
	public void updateScreen() {
		if(false && (isImportingEPK || isImportingMCA)) {
			//FileChooserResult fr = EagRuntime.getFileChooserResult();
			this.mc.displayGuiScreen(mainmenu);
			isImportingEPK = isImportingMCA = false;
		}
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRenderer, I18n.format("What do you wanna do?"), this.width / 2, this.height / 4, 16777215);
		
		int toolTipColor = 0xDDDDAA;
		if(worldCreate.isMouseOver()) {
			this.drawCenteredString(this.fontRenderer, I18n.format("Make a new world for Eaglercraft"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}else if(worldImport.isMouseOver()) {
			this.drawCenteredString(this.fontRenderer, I18n.format("Load an Eaglercraft .epk file"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}else if(worldVanilla.isMouseOver()) {
			this.drawCenteredString(this.fontRenderer, I18n.format("Load a vanilla Minecraft 1.12 world"), this.width / 2, this.height / 4 + 20, toolTipColor);
		}
		
		super.drawScreen(par1, par2, par3);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(mainmenu);
		}else if(par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiCreateWorld(mainmenu));
		}else if(par1GuiButton.id == 2) {
			//isImportingEPK = FileCopyUtils.copyEPK();
			this.mc.displayGuiScreen(new GuiPakErrorScreen());
		}else if(par1GuiButton.id == 3) {
			isImportingMCA = FileCopyUtils.copyZip();
			this.mc.displayGuiScreen(mainmenu);
		}
	}

}
