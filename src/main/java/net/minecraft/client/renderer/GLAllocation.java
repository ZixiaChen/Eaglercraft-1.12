package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

public class GLAllocation
{

    /**
     * Generates the specified number of display lists and returns the first index.
     */
    public static int generateDisplayLists() {
		return EaglercraftGPU.glGenLists();
	}

    public static int generateDisplayLists(int range) {
		return EaglercraftGPU.glGenLists();
	}

	public static void deleteDisplayLists(int list) {
		EaglercraftGPU.glDeleteLists(list);
	}

    public static void deleteDisplayLists(int list, int range) {
		EaglercraftGPU.glDeleteLists(list);
	}

	/**+
	 * Creates and returns a direct byte buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static ByteBuffer createDirectByteBuffer(int capacity) {
		return EagRuntime.allocateByteBuffer(capacity);
	}

	/**+
	 * Creates and returns a direct int buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static IntBuffer createDirectIntBuffer(int capacity) {
		return EagRuntime.allocateIntBuffer(capacity);
	}

	/**+
	 * Creates and returns a direct float buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static FloatBuffer createDirectFloatBuffer(int capacity) {
		return EagRuntime.allocateFloatBuffer(capacity);
	}
}
