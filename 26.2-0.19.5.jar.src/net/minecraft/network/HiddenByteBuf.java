/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.util.ReferenceCounted;
/*    */ 
/*    */ public final class HiddenByteBuf extends Record implements ReferenceCounted {
/*    */   private final ByteBuf contents;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/HiddenByteBuf;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HiddenByteBuf;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/HiddenByteBuf;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/HiddenByteBuf;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/HiddenByteBuf;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/HiddenByteBuf;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ByteBuf contents() {
/* 14 */     return this.contents;
/*    */   }
/*    */   
/*    */   public HiddenByteBuf(ByteBuf contents) {
/* 18 */     this.contents = ByteBufUtil.ensureAccessible(contents);
/*    */   }
/*    */   
/*    */   public static Object pack(Object msg) {
/* 22 */     if (msg instanceof ByteBuf) {
/* 22 */       ByteBuf buf = (ByteBuf)msg;
/* 23 */       return new HiddenByteBuf(buf);
/*    */     } 
/* 25 */     return msg;
/*    */   }
/*    */   
/*    */   public static Object unpack(Object msg) {
/* 29 */     if (msg instanceof HiddenByteBuf) {
/* 29 */       HiddenByteBuf buf = (HiddenByteBuf)msg;
/* 30 */       return ByteBufUtil.ensureAccessible(buf.contents);
/*    */     } 
/* 32 */     return msg;
/*    */   }
/*    */   
/*    */   public int refCnt() {
/* 37 */     return this.contents.refCnt();
/*    */   }
/*    */   
/*    */   public HiddenByteBuf retain() {
/* 42 */     this.contents.retain();
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public HiddenByteBuf retain(int increment) {
/* 48 */     this.contents.retain(increment);
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public HiddenByteBuf touch() {
/* 54 */     this.contents.touch();
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public HiddenByteBuf touch(Object hint) {
/* 60 */     this.contents.touch(hint);
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release() {
/* 66 */     return this.contents.release();
/*    */   }
/*    */   
/*    */   public boolean release(int decrement) {
/* 71 */     return this.contents.release(decrement);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\HiddenByteBuf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */