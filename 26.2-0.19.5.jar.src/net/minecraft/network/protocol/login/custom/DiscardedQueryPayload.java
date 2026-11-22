/*   */ package net.minecraft.network.protocol.login.custom;
/*   */ 
/*   */ import net.minecraft.network.FriendlyByteBuf;
/*   */ import net.minecraft.resources.Identifier;
/*   */ 
/*   */ public final class DiscardedQueryPayload extends Record implements CustomQueryPayload {
/*   */   private final Identifier id;
/*   */   
/*   */   public DiscardedQueryPayload(Identifier id) {
/* 6 */     this.id = id;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Identifier id() {
/* 6 */     return this.id;
/*   */   }
/*   */   
/*   */   public void write(FriendlyByteBuf output) {}
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\custom\DiscardedQueryPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */