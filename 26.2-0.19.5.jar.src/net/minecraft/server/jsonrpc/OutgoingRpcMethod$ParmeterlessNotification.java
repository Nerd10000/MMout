/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ 
/*    */ public final class ParmeterlessNotification extends Record implements OutgoingRpcMethod<Void, Void> {
/*    */   private final MethodInfo<Void, Void> info;
/*    */   
/*    */   private final OutgoingRpcMethod.Attributes attributes;
/*    */   
/*    */   public ParmeterlessNotification(MethodInfo<Void, Void> info, OutgoingRpcMethod.Attributes attributes) {
/* 42 */     this.info = info;
/* 42 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MethodInfo<Void, Void> info() {
/* 42 */     return this.info;
/*    */   }
/*    */   
/*    */   public OutgoingRpcMethod.Attributes attributes() {
/* 42 */     return this.attributes;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$ParmeterlessNotification.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */