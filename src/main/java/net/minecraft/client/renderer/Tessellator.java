package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;

public class Tessellator
{
    private final BufferBuilder worldRenderer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    public static final int GL_TRIANGLES = RealOpenGLEnums.GL_TRIANGLES;
	public static final int GL_TRIANGLE_STRIP = RealOpenGLEnums.GL_TRIANGLE_STRIP;
	public static final int GL_TRIANGLE_FAN = RealOpenGLEnums.GL_TRIANGLE_FAN;
	public static final int GL_QUADS = RealOpenGLEnums.GL_QUADS;
	public static final int GL_LINES = RealOpenGLEnums.GL_LINES;
	public static final int GL_LINE_STRIP = RealOpenGLEnums.GL_LINE_STRIP;
	public static final int GL_LINE_LOOP = RealOpenGLEnums.GL_LINE_LOOP;

    /** The static instance of the Tessellator. */
    private static final Tessellator INSTANCE = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return INSTANCE;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new BufferBuilder(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }

    public BufferBuilder getBuffer()
    {
        return this.worldRenderer;
    }
}
