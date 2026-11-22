/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*    */ 
/*    */ public final class ParameterlessMethod<Result> extends Record implements OutgoingRpcMethod<Void, Result> {
/*    */   private final MethodInfo<Void, Result> info;
/*    */   
/*    */   private final OutgoingRpcMethod.Attributes attributes;
/*    */   
/*    */   public ParameterlessMethod(MethodInfo<Void, Result> info, OutgoingRpcMethod.Attributes attributes) {
/* 61 */     this.info = info;
/* 61 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*    */   }
/*    */   
/*    */   public MethodInfo<Void, Result> info() {
/* 61 */     return this.info;
/*    */   }
/*    */   
/*    */   public OutgoingRpcMethod.Attributes attributes() {
/* 61 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public Result decodeResult(JsonElement result) {
/* 67 */     if (this.info.result().isEmpty())
/* 68 */       throw new IllegalStateException("Method defined as having no result"); 
/* 70 */     return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, result).getOrThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$ParameterlessMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */