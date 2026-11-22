/*   */ package net.minecraft.server.jsonrpc.api;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import java.util.function.BiFunction;
/*   */ 
/*   */ public final class ResultInfo<Result> extends Record {
/*   */   private final String name;
/*   */   
/*   */   private final Schema<Result> schema;
/*   */   
/*   */   public ResultInfo(String name, Schema<Result> schema) {
/* 6 */     this.name = name;
/* 6 */     this.schema = schema;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>;
/*   */   }
/*   */   
/*   */   public String name() {
/* 6 */     return this.name;
/*   */   }
/*   */   
/*   */   public Schema<Result> schema() {
/* 6 */     return this.schema;
/*   */   }
/*   */   
/*   */   public static <Result> Codec<ResultInfo<Result>> typedCodec() {
/* 8 */     return RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(ResultInfo::name), (App)Schema.<T>typedCodec().fieldOf("schema").forGetter(ResultInfo::schema)).apply((Applicative)i, ResultInfo::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\api\ResultInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */