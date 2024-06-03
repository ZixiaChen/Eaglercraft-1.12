package net.minecraft.src;

import java.util.Comparator;
import net.lax1dude.eaglercraft.v1_8.Display;

public class DisplayModeComparator implements Comparator
{
    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        Display displaymode = (Display)p_compare_1_;
        Display displaymode1 = (Display)p_compare_2_;

        if (displaymode.getWidth() != displaymode1.getWidth())
        {
            return displaymode.getWidth() - displaymode1.getWidth();
        }
        else if (displaymode.getHeight() != displaymode1.getHeight())
        {
            return displaymode.getHeight() - displaymode1.getHeight();
        }

        return 0;
    }
}
