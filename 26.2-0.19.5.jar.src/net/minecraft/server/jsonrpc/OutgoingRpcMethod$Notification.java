/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*    */ 
/*    */ public final class Notification<Params> extends Record implements OutgoingRpcMethod<Params, Void> {
/*    */   private final MethodInfo<Params, Void> info;
/*    */   
/*    */   private final OutgoingRpcMethod.Attributes attributes;
/*    */   
/*    */   public Notification(MethodInfo<Params, Void> info, OutgoingRpcMethod.Attributes attributes) {
/* 48 */     this.info = info;
/* 48 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*    */   }
/*    */   
/*    */   public MethodInfo<Params, Void> info() {
/* 48 */     return this.info;
/*    */   }
/*    */   
/*    */   public OutgoingRpcMethod.Attributes attributes() {
/* 48 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public JsonElement encodeParams(Params params) {
/* 54 */     if (this.info.params().isEmpty())
/* 55 */       throw new IllegalStateException("Method defined as having no parameters"); 
/* 57 */     return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, params).getOrThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Notification.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */