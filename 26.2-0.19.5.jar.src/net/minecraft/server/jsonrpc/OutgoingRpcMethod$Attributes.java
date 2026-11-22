/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ public final class Attributes extends Record {
/*    */   private final boolean discoverable;
/*    */   
/*    */   private final boolean allowPreServerInit;
/*    */   
/*    */   public Attributes(boolean discoverable, boolean allowPreServerInit) {
/* 31 */     this.discoverable = discoverable;
/* 31 */     this.allowPreServerInit = allowPreServerInit;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean discoverable() {
/* 31 */     return this.discoverable;
/*    */   }
/*    */   
/*    */   public boolean allowPreServerInit() {
/* 31 */     return this.allowPreServerInit;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Attributes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */