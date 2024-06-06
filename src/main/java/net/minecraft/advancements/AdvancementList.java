package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class AdvancementList
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ResourceLocation, Advancement> advancements = Maps.<ResourceLocation, Advancement>newHashMap();
    private final Set<Advancement> roots = Sets.<Advancement>newLinkedHashSet();
    private final Set<Advancement> nonRoots = Sets.<Advancement>newLinkedHashSet();
    private AdvancementList.Listener listener;

    private void remove(Advancement advancementIn)
    {
        for (Advancement advancement : advancementIn.getChildren())
        {
            this.remove(advancement);
        }

        LOGGER.info("Forgot about advancement " + advancementIn.getId());
        this.advancements.remove(advancementIn.getId());

        if (advancementIn.getParent() == null)
        {
            this.roots.remove(advancementIn);

            if (this.listener != null)
            {
                this.listener.rootAdvancementRemoved(advancementIn);
            }
        }
        else
        {
            this.nonRoots.remove(advancementIn);

            if (this.listener != null)
            {
                this.listener.nonRootAdvancementRemoved(advancementIn);
            }
        }
    }

    public void removeAll(Set<ResourceLocation> ids)
    {
        for (ResourceLocation resourcelocation : ids)
        {
            Advancement advancement = this.advancements.get(resourcelocation);

            if (advancement == null)
            {
                LOGGER.warn("Told to remove advancement " + resourcelocation + " but I don't know what that is");
            }
            else
            {
                this.remove(advancement);
            }
        }
    }

    public void loadAdvancements(Map<ResourceLocation, Advancement.Builder> advancementsIn)
    {
        
    }

    public void clear()
    {
        this.advancements.clear();
        this.roots.clear();
        this.nonRoots.clear();

        if (this.listener != null)
        {
            this.listener.advancementsCleared();
        }
    }

    public Iterable<Advancement> getRoots()
    {
        return this.roots;
    }

    public Iterable<Advancement> getAdvancements()
    {
        return this.advancements.values();
    }

    @Nullable
    public Advancement getAdvancement(ResourceLocation id)
    {
        return this.advancements.get(id);
    }

    public void setListener(@Nullable AdvancementList.Listener listenerIn)
    {
        this.listener = listenerIn;

        if (listenerIn != null)
        {
            for (Advancement advancement : this.roots)
            {
                listenerIn.rootAdvancementAdded(advancement);
            }

            for (Advancement advancement1 : this.nonRoots)
            {
                listenerIn.nonRootAdvancementAdded(advancement1);
            }
        }
    }

    public interface Listener
    {
        void rootAdvancementAdded(Advancement advancementIn);

        void rootAdvancementRemoved(Advancement advancementIn);

        void nonRootAdvancementAdded(Advancement advancementIn);

        void nonRootAdvancementRemoved(Advancement advancementIn);

        void advancementsCleared();
    }
}
