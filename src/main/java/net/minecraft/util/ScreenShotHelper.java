package net.minecraft.util;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformApplication;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ScreenShotHelper
{
    public static ITextComponent saveScreenshot() {
		return new TextComponentString("Saved Screenshot As: " + PlatformApplication.saveScreenshot());
	}
}
