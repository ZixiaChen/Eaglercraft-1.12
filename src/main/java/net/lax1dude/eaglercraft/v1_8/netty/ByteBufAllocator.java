package net.lax1dude.eaglercraft.v1_8.netty;

public interface ByteBufAllocator {

   ByteBuf buffer();

   ByteBuf buffer(int var1);

   ByteBuf buffer(int var1, int var2);

   ByteBuf ioBuffer();

   ByteBuf ioBuffer(int var1);

   ByteBuf ioBuffer(int var1, int var2);

   ByteBuf heapBuffer();

   ByteBuf heapBuffer(int var1);

   ByteBuf heapBuffer(int var1, int var2);

   ByteBuf directBuffer();

   ByteBuf directBuffer(int var1);

   ByteBuf directBuffer(int var1, int var2);

   boolean isDirectBufferPooled();

   int calculateNewCapacity(int var1, int var2);
}
