/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MethodInfo<Params, Result> extends Record {
/*    */   private final String description;
/*    */   
/*    */   private final Optional<ParamInfo<Params>> params;
/*    */   
/*    */   private final Optional<ResultInfo<Result>> result;
/*    */   
/*    */   public MethodInfo(String description, Optional<ParamInfo<Params>> params, Optional<ResultInfo<Result>> result) {
/* 12 */     this.description = description;
/* 12 */     this.params = params;
/* 12 */     this.result = result;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/MethodInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo<TParams;TResult;>;
/*    */   }
/*    */   
/*    */   public String description() {
/* 12 */     return this.description;
/*    */   }
/*    */   
/*    */   public Optional<ParamInfo<Params>> params() {
/* 12 */     return this.params;
/*    */   }
/*    */   
/*    */   public Optional<ResultInfo<Result>> result() {
/* 12 */     return this.result;
/*    */   }
/*    */   
/*    */   private static <Params> Optional<ParamInfo<Params>> toOptional(List<ParamInfo<Params>> list) {
/* 15 */     return list.isEmpty() ? Optional.<ParamInfo<Params>>empty() : Optional.<ParamInfo<Params>>of(list.getFirst());
/*    */   }
/*    */   
/*    */   private static <Params> List<ParamInfo<Params>> toList(Optional<ParamInfo<Params>> opt) {
/* 20 */     if (opt.isPresent())
/* 21 */       return List.of(opt.get()); 
/* 23 */     return List.of();
/*    */   }
/*    */   
/*    */   private static <Params> Codec<Optional<ParamInfo<Params>>> paramsTypedCodec() {
/* 27 */     return ParamInfo.<Param>typedCodec().codec().listOf().xmap(MethodInfo::toOptional, MethodInfo::toList);
/*    */   }
/*    */   
/*    */   private static <Params, Result> MapCodec<MethodInfo<Params, Result>> typedCodec() {
/* 34 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("description").forGetter(MethodInfo::description), (App)paramsTypedCodec().fieldOf("params").forGetter(MethodInfo::params), (App)ResultInfo.<Result>typedCodec().optionalFieldOf("result").forGetter(MethodInfo::result)).apply((Applicative)i, MethodInfo::new));
/*    */   }
/*    */   
/*    */   public MethodInfo(String description, ParamInfo<Params> paramInfo, ResultInfo<Result> resultInfo) {
/* 42 */     this(description, 
/*    */         
/* 44 */         Optional.ofNullable(paramInfo), 
/* 45 */         Optional.ofNullable(resultInfo));
/*    */   }
/*    */   
/*    */   public Named<Params, Result> named(Identifier name) {
/* 50 */     return new Named<>(name, this);
/*    */   }
/*    */   
/*    */   public static final class Named<Params, Result> extends Record {
/*    */     private final Identifier name;
/*    */     
/*    */     private final MethodInfo<Params, Result> contents;
/*    */     
/*    */     public Named(Identifier name, MethodInfo<Params, Result> contents) {
/* 53 */       this.name = name;
/* 53 */       this.contents = contents;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/MethodInfo$Named<TParams;TResult;>;
/*    */     }
/*    */     
/*    */     public Identifier name() {
/* 53 */       return this.name;
/*    */     }
/*    */     
/*    */     public MethodInfo<Params, Result> contents() {
/* 53 */       return this.contents;
/*    */     }
/*    */     
/*    */     public static <Params, Result> Codec<Named<Params, Result>> typedCodec() {
/* 58 */       return RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("name").forGetter(Named::name), (App)MethodInfo.<Params, Result>typedCodec().forGetter(Named::contents)).apply((Applicative)i, Named::new));
/*    */     }
/*    */     
/* 65 */     public static final Codec<Named<?, ?>> CODEC = typedCodec();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\MethodInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */