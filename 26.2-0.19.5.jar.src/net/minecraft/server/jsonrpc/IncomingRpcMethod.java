/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
/*     */ 
/*     */ public interface IncomingRpcMethod<Params, Result> {
/*     */   MethodInfo<Params, Result> info();
/*     */   
/*     */   Attributes attributes();
/*     */   
/*     */   JsonElement apply(MinecraftApi paramMinecraftApi, JsonElement paramJsonElement, ClientInfo paramClientInfo);
/*     */   
/*     */   public static final class Attributes extends Record {
/*     */     private final boolean runOnMainThread;
/*     */     
/*     */     private final boolean discoverable;
/*     */     
/*     */     private final boolean allowPreServerInit;
/*     */     
/*     */     public Attributes(boolean runOnMainThread, boolean discoverable, boolean allowPreServerInit) {
/*  28 */       this.runOnMainThread = runOnMainThread;
/*  28 */       this.discoverable = discoverable;
/*  28 */       this.allowPreServerInit = allowPreServerInit;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean runOnMainThread() {
/*  28 */       return this.runOnMainThread;
/*     */     }
/*     */     
/*     */     public boolean discoverable() {
/*  28 */       return this.discoverable;
/*     */     }
/*     */     
/*     */     public boolean allowPreServerInit() {
/*  28 */       return this.allowPreServerInit;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ParameterlessMethod<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> {
/*     */     private final MethodInfo<Params, Result> info;
/*     */     
/*     */     private final IncomingRpcMethod.Attributes attributes;
/*     */     
/*     */     private final IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier;
/*     */     
/*     */     public ParameterlessMethod(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier) {
/*  45 */       this.info = info;
/*  45 */       this.attributes = attributes;
/*  45 */       this.supplier = supplier;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public MethodInfo<Params, Result> info() {
/*  45 */       return this.info;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod.Attributes attributes() {
/*  45 */       return this.attributes;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier() {
/*  45 */       return this.supplier;
/*     */     }
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*  53 */       if (paramsJson != null && (!paramsJson.isJsonArray() || !paramsJson.getAsJsonArray().isEmpty()))
/*  54 */         throw new InvalidParameterJsonRpcException("Expected no params, or an empty array"); 
/*  56 */       if (this.info.params().isPresent())
/*  57 */         throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description"); 
/*  59 */       Result result = this.supplier.apply(minecraftApi, clientInfo);
/*  60 */       if (this.info.result().isEmpty())
/*  61 */         throw new IllegalStateException("No result codec defined"); 
/*  63 */       return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, result).getOrThrow(InvalidParameterJsonRpcException::new);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Method<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> {
/*     */     private final MethodInfo<Params, Result> info;
/*     */     
/*     */     private final IncomingRpcMethod.Attributes attributes;
/*     */     
/*     */     private final IncomingRpcMethod.RpcMethodFunction<Params, Result> function;
/*     */     
/*     */     public Method(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/*  67 */       this.info = info;
/*  67 */       this.attributes = attributes;
/*  67 */       this.function = function;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #67	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public MethodInfo<Params, Result> info() {
/*  67 */       return this.info;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod.Attributes attributes() {
/*  67 */       return this.attributes;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod.RpcMethodFunction<Params, Result> function() {
/*  67 */       return this.function;
/*     */     }
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*     */       JsonElement paramsJsonElement;
/*  75 */       if (paramsJson == null || (!paramsJson.isJsonArray() && !paramsJson.isJsonObject()))
/*  76 */         throw new InvalidParameterJsonRpcException("Expected params as array or named"); 
/*  79 */       if (this.info.params().isEmpty())
/*  80 */         throw new IllegalArgumentException("Method defined as having parameters without describing them"); 
/*  84 */       if (paramsJson.isJsonObject()) {
/*  85 */         String parameterName = ((ParamInfo)this.info.params().get()).name();
/*  86 */         JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
/*  87 */         if (jsonElement == null)
/*  88 */           throw new InvalidParameterJsonRpcException(String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", new Object[] { parameterName })); 
/*  90 */         paramsJsonElement = jsonElement;
/*     */       } else {
/*  92 */         JsonArray jsonArray = paramsJson.getAsJsonArray();
/*  93 */         if (jsonArray.isEmpty() || jsonArray.size() > 1)
/*  94 */           throw new InvalidParameterJsonRpcException("Expected exactly one element in the params array"); 
/*  96 */         paramsJsonElement = jsonArray.get(0);
/*     */       } 
/*  98 */       Params params = (Params)((ParamInfo)this.info.params().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, paramsJsonElement).getOrThrow(InvalidParameterJsonRpcException::new);
/*  99 */       Result result = this.function.apply(minecraftApi, params, clientInfo);
/* 100 */       if (this.info.result().isEmpty())
/* 101 */         throw new IllegalStateException("No result codec defined"); 
/* 103 */       return (JsonElement)((ResultInfo)this.info.result().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException::new);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RpcMethodFunction<Params, Result> {
/*     */     Result apply(MinecraftApi param1MinecraftApi, Params param1Params, ClientInfo param1ClientInfo);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ParameterlessRpcMethodFunction<Result> {
/*     */     Result apply(MinecraftApi param1MinecraftApi, ClientInfo param1ClientInfo);
/*     */   }
/*     */   
/*     */   public static class IncomingRpcMethodBuilder<Params, Result> {
/* 108 */     private String description = "";
/*     */     
/*     */     private ParamInfo<Params> paramInfo;
/*     */     
/*     */     private ResultInfo<Result> resultInfo;
/*     */     
/*     */     private boolean discoverable = true;
/*     */     
/*     */     private boolean runOnMainThread = true;
/*     */     
/*     */     private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction;
/*     */     
/*     */     private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction;
/*     */     
/*     */     private boolean allowPreServerInit = false;
/*     */     
/*     */     public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> function) {
/* 118 */       this.parameterlessFunction = function;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/* 122 */       this.parameterFunction = function;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> supplier) {
/* 126 */       this.parameterlessFunction = ((apiService, param1ClientInfo) -> supplier.apply(apiService));
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> description(String description) {
/* 130 */       this.description = description;
/* 131 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 135 */       this.resultInfo = new ResultInfo(resultName, resultSchema.info());
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 140 */       this.paramInfo = new ParamInfo(paramName, paramSchema.info());
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 145 */       this.discoverable = false;
/* 146 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 150 */       this.runOnMainThread = false;
/* 151 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> allowPreServerInit() {
/* 155 */       this.allowPreServerInit = true;
/* 156 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod<Params, Result> build() {
/* 160 */       if (this.resultInfo == null)
/* 161 */         throw new IllegalStateException("No response defined"); 
/* 164 */       IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable, this.allowPreServerInit);
/* 165 */       MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/* 171 */       if (this.parameterlessFunction != null)
/* 172 */         return new IncomingRpcMethod.ParameterlessMethod<>(methodInfo, attributes, this.parameterlessFunction); 
/* 177 */       if (this.parameterFunction != null) {
/* 178 */         if (this.paramInfo == null)
/* 179 */           throw new IllegalStateException("No param schema defined"); 
/* 181 */         return new IncomingRpcMethod.Method<>(methodInfo, attributes, this.parameterFunction);
/*     */       } 
/* 187 */       throw new IllegalStateException("No method defined");
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, String key) {
/* 192 */       return register(methodRegistry, Identifier.withDefaultNamespace(key));
/*     */     }
/*     */     
/*     */     private IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, Identifier id) {
/* 196 */       return (IncomingRpcMethod<?, ?>)Registry.register(methodRegistry, id, build());
/*     */     }
/*     */   }
/*     */   
/*     */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(ParameterlessRpcMethodFunction<Result> function) {
/* 201 */     return new IncomingRpcMethodBuilder<>(function);
/*     */   }
/*     */   
/*     */   static <Params, Result> IncomingRpcMethodBuilder<Params, Result> method(RpcMethodFunction<Params, Result> function) {
/* 205 */     return new IncomingRpcMethodBuilder<>(function);
/*     */   }
/*     */   
/*     */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(Function<MinecraftApi, Result> supplier) {
/* 209 */     return new IncomingRpcMethodBuilder<>(supplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */