package net.minecraft.client.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NarratorChatListener implements IChatListener
{
    /**
     * Called whenever this listener receives a chat message, if this listener is registered to the given type in {@link
     * net.minecraft.client.gui.GuiIngame#chatListeners chatListeners}
     *  
     * @param chatTypeIn The type of chat message
     * @param message The chat message.
     */
    public void say(ChatType chatTypeIn, ITextComponent message)
    {
        
    }

    public void announceMode(int p_193641_1_)
    {
        
    }

    public boolean isActive()
    {
        return false;
    }

    public void clear()
    {
        
    }
}
