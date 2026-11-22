/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ 
/*     */ public interface OutgoingRpcMethod<Params, Result> {
/*     */   public static final String NOTIFICATION_PREFIX = "notification/";
/*     */   
/*     */   MethodInfo<Params, Result> info();
/*     */   
/*     */   Attributes attributes();
/*     */   
/*     */   default JsonElement encodeParams(Params params) {
/*  24 */     return null;
/*     */   }
/*     */   
/*     */   default Result decodeResult(JsonElement result) {
/*  28 */     return null;
/*     */   }
/*     */   
/*     */   public static final class Attributes extends Record {
/*     */     private final boolean discoverable;
/*     */     
/*     */     private final boolean allowPreServerInit;
/*     */     
/*     */     public Attributes(boolean discoverable, boolean allowPreServerInit) {
/*  31 */       this.discoverable = discoverable;
/*  31 */       this.allowPreServerInit = allowPreServerInit;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Attributes;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean discoverable() {
/*  31 */       return this.discoverable;
/*     */     }
/*     */     
/*     */     public boolean allowPreServerInit() {
/*  31 */       return this.allowPreServerInit;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ParmeterlessNotification extends Record implements OutgoingRpcMethod<Void, Void> {
/*     */     private final MethodInfo<Void, Void> info;
/*     */     
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*     */     public ParmeterlessNotification(MethodInfo<Void, Void> info, OutgoingRpcMethod.Attributes attributes) {
/*  42 */       this.info = info;
/*  42 */       this.attributes = attributes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParmeterlessNotification;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MethodInfo<Void, Void> info() {
/*  42 */       return this.info;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethod.Attributes attributes() {
/*  42 */       return this.attributes;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Notification<Params> extends Record implements OutgoingRpcMethod<Params, Void> {
/*     */     private final MethodInfo<Params, Void> info;
/*     */     
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*     */     public Notification(MethodInfo<Params, Void> info, OutgoingRpcMethod.Attributes attributes) {
/*  48 */       this.info = info;
/*  48 */       this.attributes = attributes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Notification<TParams;>;
/*     */     }
/*     */     
/*     */     public MethodInfo<Params, Void> info() {
/*  48 */       return this.info;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethod.Attributes attributes() {
/*  48 */       return this.attributes;
/*     */     }
/*     */     
/*     */     public JsonElement encodeParams(Params params) {
/*  54 */       if (this.info.params().isEmpty())
/*  55 */         throw new IllegalStateException("Method defined as having no parameters"); 
/*  57 */       return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, params).getOrThrow();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ParameterlessMethod<Result> extends Record implements OutgoingRpcMethod<Void, Result> {
/*     */     private final MethodInfo<Void, Result> info;
/*     */     
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*     */     public ParameterlessMethod(MethodInfo<Void, Result> info, OutgoingRpcMethod.Attributes attributes) {
/*  61 */       this.info = info;
/*  61 */       this.attributes = attributes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$ParameterlessMethod<TResult;>;
/*     */     }
/*     */     
/*     */     public MethodInfo<Void, Result> info() {
/*  61 */       return this.info;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethod.Attributes attributes() {
/*  61 */       return this.attributes;
/*     */     }
/*     */     
/*     */     public Result decodeResult(JsonElement result) {
/*  67 */       if (this.info.result().isEmpty())
/*  68 */         throw new IllegalStateException("Method defined as having no result"); 
/*  70 */       return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, result).getOrThrow();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Method<Params, Result> extends Record implements OutgoingRpcMethod<Params, Result> {
/*     */     private final MethodInfo<Params, Result> info;
/*     */     
/*     */     private final OutgoingRpcMethod.Attributes attributes;
/*     */     
/*     */     public Method(MethodInfo<Params, Result> info, OutgoingRpcMethod.Attributes attributes) {
/*  74 */       this.info = info;
/*  74 */       this.attributes = attributes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #74	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/OutgoingRpcMethod$Method<TParams;TResult;>;
/*     */     }
/*     */     
/*     */     public MethodInfo<Params, Result> info() {
/*  74 */       return this.info;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethod.Attributes attributes() {
/*  74 */       return this.attributes;
/*     */     }
/*     */     
/*     */     public JsonElement encodeParams(Params params) {
/*  80 */       if (this.info.params().isEmpty())
/*  81 */         throw new IllegalStateException("Method defined as having no parameters"); 
/*  83 */       return (JsonElement)((ParamInfo)this.info.params().get()).schema().codec().encodeStart((DynamicOps)JsonOps.INSTANCE, params).getOrThrow();
/*     */     }
/*     */     
/*     */     public Result decodeResult(JsonElement result) {
/*  88 */       if (this.info.result().isEmpty())
/*  89 */         throw new IllegalStateException("Method defined as having no result"); 
/*  91 */       return (Result)((ResultInfo)this.info.result().get()).schema().codec().parse((DynamicOps)JsonOps.INSTANCE, result).getOrThrow();
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Factory<Params, Result> {
/*     */     OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> param1MethodInfo, OutgoingRpcMethod.Attributes param1Attributes);
/*     */   }
/*     */   
/*     */   public static class OutgoingRpcMethodBuilder<Params, Result> {
/*  96 */     public static final OutgoingRpcMethod.Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod.Attributes(true, false);
/*     */     
/*     */     private final OutgoingRpcMethod.Factory<Params, Result> method;
/*     */     
/*  98 */     private String description = "";
/*     */     
/*     */     private ParamInfo<Params> paramInfo;
/*     */     
/*     */     private ResultInfo<Result> resultInfo;
/*     */     
/*     */     private boolean allowPreServerInit = false;
/*     */     
/*     */     public OutgoingRpcMethodBuilder(OutgoingRpcMethod.Factory<Params, Result> method) {
/* 104 */       this.method = method;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> description(String description) {
/* 108 */       this.description = description;
/* 109 */       return this;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> response(String resultName, Schema<Result> resultSchema) {
/* 113 */       this.resultInfo = new ResultInfo(resultName, resultSchema);
/* 114 */       return this;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> param(String paramName, Schema<Params> paramSchema) {
/* 118 */       this.paramInfo = new ParamInfo(paramName, paramSchema);
/* 119 */       return this;
/*     */     }
/*     */     
/*     */     public OutgoingRpcMethodBuilder<Params, Result> allowPreServerInit() {
/* 123 */       this.allowPreServerInit = true;
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     private OutgoingRpcMethod<Params, Result> build() {
/*     */       OutgoingRpcMethod.Attributes attributes;
/* 128 */       MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/* 134 */       if (this.allowPreServerInit) {
/* 135 */         attributes = new OutgoingRpcMethod.Attributes(DEFAULT_ATTRIBUTES.discoverable(), true);
/*     */       } else {
/* 137 */         attributes = DEFAULT_ATTRIBUTES;
/*     */       } 
/* 139 */       return this.method.create(methodInfo, attributes);
/*     */     }
/*     */     
/*     */     public Holder.Reference<OutgoingRpcMethod<Params, Result>> register(String key) {
/* 143 */       return register(Identifier.withDefaultNamespace("notification/" + key));
/*     */     }
/*     */     
/*     */     private Holder.Reference<OutgoingRpcMethod<Params, Result>> register(Identifier id) {
/* 147 */       return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, id, build());
/*     */     }
/*     */   }
/*     */   
/*     */   static OutgoingRpcMethodBuilder<Void, Void> notification() {
/* 152 */     return new OutgoingRpcMethodBuilder<>(ParmeterlessNotification::new);
/*     */   }
/*     */   
/*     */   static <Params> OutgoingRpcMethodBuilder<Params, Void> notificationWithParams() {
/* 156 */     return new OutgoingRpcMethodBuilder<>(Notification::new);
/*     */   }
/*     */   
/*     */   static <Result> OutgoingRpcMethodBuilder<Void, Result> request() {
/* 160 */     return new OutgoingRpcMethodBuilder<>(ParameterlessMethod::new);
/*     */   }
/*     */   
/*     */   static <Params, Result> OutgoingRpcMethodBuilder<Params, Result> requestWithParams() {
/* 164 */     return new OutgoingRpcMethodBuilder<>(Method::new);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */