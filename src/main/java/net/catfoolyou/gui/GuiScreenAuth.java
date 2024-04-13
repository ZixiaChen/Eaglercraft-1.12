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
import net.minecraft.client.gui.*;

/*
 * For once I have actually written some code myself
 * Not even sure how tf I'm gonna be making the skin system, not even sure this is gonna work
 * 
 * Copywrite (c) Catfoolyou 2024
 */

public class GuiScreenAuth extends GuiScreen
{

	private GuiTextField Username;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 75, this.height / 4 + 120 + 12, I18n.format("Done")));
		this.Username = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
        this.Username.setFocused(true);
        this.Username.setText(super.mc.getSession().getUsername());
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
        this.drawString(this.fontRenderer, I18n.format("Username"), this.width / 2 - 100, 53, 10526880);
        this.Username.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
