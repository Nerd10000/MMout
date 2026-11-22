/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ 
/*     */ final class DelayedMessage extends Record {
/*     */   private final ChannelHandlerContext ctx;
/*     */   
/*     */   private final Object msg;
/*     */   
/*     */   private DelayedMessage(ChannelHandlerContext ctx, Object msg) {
/* 253 */     this.ctx = ctx;
/* 253 */     this.msg = msg;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #253	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #253	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #253	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/network/ServerConnectionListener$LatencySimulator$DelayedMessage;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ChannelHandlerContext ctx() {
/* 253 */     return this.ctx;
/*     */   }
/*     */   
/*     */   public Object msg() {
/* 253 */     return this.msg;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerConnectionListener$LatencySimulator$DelayedMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */