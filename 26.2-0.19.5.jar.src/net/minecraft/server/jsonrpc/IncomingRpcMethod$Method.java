/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ 
/*     */ public final class Method<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> {
/*     */   private final MethodInfo<Params, Result> info;
/*     */   
/*     */   private final IncomingRpcMethod.Attributes attributes;
/*     */   
/*     */   private final IncomingRpcMethod.RpcMethodFunction<Params, Result> function;
/*     */   
/*     */   public Method(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/*  67 */     this.info = info;
/*  67 */     this.attributes = attributes;
/*  67 */     this.function = function;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #67	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #67	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #67	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */   }
/*     */   
/*     */   public MethodInfo<Params, Result> info() {
/*  67 */     return this.info;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod.Attributes attributes() {
/*  67 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod.RpcMethodFunction<Params, Result> function() {
/*  67 */     return this.function;
/*     */   }
/*     */   
/*     */   public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*     */     JsonElement paramsJsonElement;
/*  75 */     if (paramsJson == null || (!paramsJson.isJsonArray() && !paramsJson.isJsonObject()))
/*  76 */       throw new InvalidParameterJsonRpcException("Expected params as array or named"); 
/*  79 */     if (this.info.params().isEmpty())
/*  80 */       throw new IllegalArgumentException("Method defined as having parameters without describing them"); 
/*  84 */     if (paramsJson.isJsonObject()) {
/*  85 */       String parameterName = ((ParamInfo)this.info.params().get()).name();
/*  86 */       JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
/*  87 */       if (jsonElement == null)
/*  88 */         throw new InvalidParameterJsonRpcException(String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", new Object[] { parameterName })); 
/*  90 */       paramsJsonElement = jsonElement;
/*     */     } else {
/*  92 */       JsonArray jsonArray = paramsJson.getAsJsonArray();
/*  93 */       if (jsonArray.isEmpty() || jsonArray.size() > 1)
/*  94 */         throw new InvalidParameterJsonRpcException("Expected exactly one element in the params array"); 
/*  96 */       paramsJsonElement = jsonArray.get(0);
/*     */     } 
/*  98 */     Params params = (Params)((ParamInfo)this.info.params().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, paramsJsonElement).getOrThrow(InvalidParameterJsonRpcException::new);
/*  99 */     Result result = this.function.apply(minecraftApi, params, clientInfo);
/* 100 */     if (this.info.result().isEmpty())
/* 101 */       throw new IllegalStateException("No result codec defined"); 
/* 103 */     return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException::new);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$Method.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */