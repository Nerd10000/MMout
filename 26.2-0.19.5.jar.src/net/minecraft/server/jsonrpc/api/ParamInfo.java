/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ParamInfo<Param> extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final Schema<Param> schema;
/*    */   
/*    */   private final boolean required;
/*    */   
/*    */   public ParamInfo(String name, Schema<Param> schema, boolean required) {
/*  7 */     this.name = name;
/*  7 */     this.schema = schema;
/*  7 */     this.required = required;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>;
/*    */   }
/*    */   
/*    */   public String name() {
/*  7 */     return this.name;
/*    */   }
/*    */   
/*    */   public Schema<Param> schema() {
/*  7 */     return this.schema;
/*    */   }
/*    */   
/*    */   public boolean required() {
/*  7 */     return this.required;
/*    */   }
/*    */   
/*    */   public static <Param> MapCodec<ParamInfo<Param>> typedCodec() {
/*  9 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(ParamInfo::name), (App)Schema.<T>typedCodec().fieldOf("schema").forGetter(ParamInfo::schema), (App)Codec.BOOL.fieldOf("required").forGetter(ParamInfo::required)).apply((Applicative)i, ParamInfo::new));
/*    */   }
/*    */   
/*    */   public ParamInfo(String name, Schema<Param> schema) {
/* 17 */     this(name, schema, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\ParamInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */