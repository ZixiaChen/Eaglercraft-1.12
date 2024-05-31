package net.minecraftforge.common.model;

import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.minecraft.util.EnumFacing;

public interface ITransformation
{
    Matrix4f getMatrix();

    EnumFacing rotate(EnumFacing var1);

    int rotate(EnumFacing var1, int var2);
}
