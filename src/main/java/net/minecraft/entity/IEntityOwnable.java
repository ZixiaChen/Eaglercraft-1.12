package net.minecraft.entity;

import javax.annotation.Nullable;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

public interface IEntityOwnable
{
    @Nullable
    EaglercraftUUID getOwnerId();

    @Nullable
    Entity getOwner();
}
