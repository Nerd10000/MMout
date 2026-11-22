/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ 
/*     */ public class OutgoingRpcMethodBuilder<Params, Result> {
/*  96 */   public static final OutgoingRpcMethod.Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod.Attributes(true, false);
/*     */   
/*     */   private final OutgoingRpcMethod.Factory<Params, Result> method;
/*     */   
/*  98 */   private String description = "";
/*     */   
/*     */   private ParamInfo<Params> paramInfo;
/*     */   
/*     */   private ResultInfo<Result> resultInfo;
/*     */   
/*     */   private boolean allowPreServerInit = false;
/*     */   
/*     */   public OutgoingRpcMethodBuilder(OutgoingRpcMethod.Factory<Params, Result> method) {
/* 104 */     this.method = method;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> description(String description) {
/* 108 */     this.description = description;
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 113 */     this.resultInfo = new ResultInfo(resultName, resultSchema);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 118 */     this.paramInfo = new ParamInfo(paramName, paramSchema);
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> allowPreServerInit() {
/* 123 */     this.allowPreServerInit = true;
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   private OutgoingRpcMethod<Params, Result> build() {
/*     */     OutgoingRpcMethod.Attributes attributes;
/* 128 */     MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/* 134 */     if (this.allowPreServerInit) {
/* 135 */       attributes = new OutgoingRpcMethod.Attributes(DEFAULT_ATTRIBUTES.discoverable(), true);
/*     */     } else {
/* 137 */       attributes = DEFAULT_ATTRIBUTES;
/*     */     } 
/* 139 */     return this.method.create(methodInfo, attributes);
/*     */   }
/*     */   
/*     */   public Holder.Reference<OutgoingRpcMethod<Params, Result>> register(String key) {
/* 143 */     return register(Identifier.withDefaultNamespace("notification/" + key));
/*     */   }
/*     */   
/*     */   private Holder.Reference<OutgoingRpcMethod<Params, Result>> register(Identifier id) {
/* 147 */     return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, id, build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$OutgoingRpcMethodBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */