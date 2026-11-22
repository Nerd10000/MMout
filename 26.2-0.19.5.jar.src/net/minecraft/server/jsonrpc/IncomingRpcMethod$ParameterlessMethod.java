/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*    */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*    */ 
/*    */ public final class ParameterlessMethod<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> {
/*    */   private final MethodInfo<Params, Result> info;
/*    */   
/*    */   private final IncomingRpcMethod.Attributes attributes;
/*    */   
/*    */   private final IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier;
/*    */   
/*    */   public ParameterlessMethod(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier) {
/* 45 */     this.info = info;
/* 45 */     this.attributes = attributes;
/* 45 */     this.supplier = supplier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public MethodInfo<Params, Result> info() {
/* 45 */     return this.info;
/*    */   }
/*    */   
/*    */   public IncomingRpcMethod.Attributes attributes() {
/* 45 */     return this.attributes;
/*    */   }
/*    */   
/*    */   public IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier() {
/* 45 */     return this.supplier;
/*    */   }
/*    */   
/*    */   public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/* 53 */     if (paramsJson != null && (!paramsJson.isJsonArray() || !paramsJson.getAsJsonArray().isEmpty()))
/* 54 */       throw new InvalidParameterJsonRpcException("Expected no params, or an empty array"); 
/* 56 */     if (this.info.params().isPresent())
/* 57 */       throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description"); 
/* 59 */     Result result = this.supplier.apply(minecraftApi, clientInfo);
/* 60 */     if (this.info.result().isEmpty())
/* 61 */       throw new IllegalStateException("No result codec defined"); 
/* 63 */     return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, result).getOrThrow(InvalidParameterJsonRpcException::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$ParameterlessMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */