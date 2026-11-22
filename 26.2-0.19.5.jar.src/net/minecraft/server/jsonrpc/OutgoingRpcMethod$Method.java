/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*    */ 
/*    */ public final class Method<Params, Result> extends Record implements OutgoingRpcMethod<Params, Result> {
/*    */   private final MethodInfo<Params, Result> info;
/*    */   
/*    */   private final OutgoingRpcMethod.Attributes attributes;
/*    */   
/*    */   public Method(MethodInfo<Params, Result> info, OutgoingRpcMethod.Attributes attributes) {
/* 74 */     this.info = info;
/* 74 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public MethodInfo<Params, Result> info() {
/* 74 */     return this.info;
/*    */   }
/*    */   
/*    */   public OutgoingRpcMethod.Attributes attributes() {
/* 74 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public JsonElement encodeParams(Params params) {
/* 80 */     if (this.info.params().isEmpty())
/* 81 */       throw new IllegalStateException("Method defined as having no parameters"); 
/* 83 */     return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, params).getOrThrow();
/*    */   }
/*    */   
/*    */   public Result decodeResult(JsonElement result) {
/* 88 */     if (this.info.result().isEmpty())
/* 89 */       throw new IllegalStateException("Method defined as having no result"); 
/* 91 */     return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, result).getOrThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Method.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */