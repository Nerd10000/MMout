/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ 
/*     */ public class IncomingRpcMethodBuilder<Params, Result> {
/* 108 */   private String description = "";
/*     */   
/*     */   private ParamInfo<Params> paramInfo;
/*     */   
/*     */   private ResultInfo<Result> resultInfo;
/*     */   
/*     */   private boolean discoverable = true;
/*     */   
/*     */   private boolean runOnMainThread = true;
/*     */   
/*     */   private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction;
/*     */   
/*     */   private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction;
/*     */   
/*     */   private boolean allowPreServerInit = false;
/*     */   
/*     */   public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> function) {
/* 118 */     this.parameterlessFunction = function;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/* 122 */     this.parameterFunction = function;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> supplier) {
/* 126 */     this.parameterlessFunction = ((apiService, paramClientInfo) -> supplier.apply(apiService));
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> description(String description) {
/* 130 */     this.description = description;
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 135 */     this.resultInfo = new ResultInfo(resultName, resultSchema.info());
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 140 */     this.paramInfo = new ParamInfo(paramName, paramSchema.info());
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 145 */     this.discoverable = false;
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 150 */     this.runOnMainThread = false;
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> allowPreServerInit() {
/* 155 */     this.allowPreServerInit = true;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod<Params, Result> build() {
/* 160 */     if (this.resultInfo == null)
/* 161 */       throw new IllegalStateException("No response defined"); 
/* 164 */     IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable, this.allowPreServerInit);
/* 165 */     MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/* 171 */     if (this.parameterlessFunction != null)
/* 172 */       return new IncomingRpcMethod.ParameterlessMethod<>(methodInfo, attributes, this.parameterlessFunction); 
/* 177 */     if (this.parameterFunction != null) {
/* 178 */       if (this.paramInfo == null)
/* 179 */         throw new IllegalStateException("No param schema defined"); 
/* 181 */       return new IncomingRpcMethod.Method<>(methodInfo, attributes, this.parameterFunction);
/*     */     } 
/* 187 */     throw new IllegalStateException("No method defined");
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, String key) {
/* 192 */     return register(methodRegistry, Identifier.withDefaultNamespace(key));
/*     */   }
/*     */   
/*     */   private IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> methodRegistry, Identifier id) {
/* 196 */     return (IncomingRpcMethod<?, ?>)Registry.register(methodRegistry, id, build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$IncomingRpcMethodBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */