/*     */ package net.minecraft.advancements.predicates;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class FloatDegrees extends Record implements MinMaxBounds<Float> {
/*     */   private final MinMaxBounds.Bounds<Float> bounds;
/*     */   
/*     */   public FloatDegrees(MinMaxBounds.Bounds<Float> bounds) {
/* 139 */     this.bounds = bounds;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MinMaxBounds$FloatDegrees;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MinMaxBounds.Bounds<Float> bounds() {
/* 139 */     return this.bounds;
/*     */   }
/*     */   
/* 140 */   public static final FloatDegrees ANY = new FloatDegrees(MinMaxBounds.Bounds.any());
/*     */   
/* 142 */   public static final Codec<FloatDegrees> CODEC = MinMaxBounds.Bounds.<Number>createCodec((Codec<Number>)Codec.FLOAT)
/* 143 */     .xmap(FloatDegrees::new, FloatDegrees::bounds);
/*     */   
/* 145 */   public static final StreamCodec<ByteBuf, FloatDegrees> STREAM_CODEC = MinMaxBounds.Bounds.<ByteBuf, Number>createStreamCodec(ByteBufCodecs.FLOAT)
/* 146 */     .map(FloatDegrees::new, FloatDegrees::bounds);
/*     */   
/*     */   public static FloatDegrees fromReader(StringReader reader) throws CommandSyntaxException {
/* 149 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS);
/* 149 */     MinMaxBounds.Bounds<Float> bounds = MinMaxBounds.Bounds.fromReader(reader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat);
/* 150 */     return new FloatDegrees(bounds);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MinMaxBounds$FloatDegrees.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */