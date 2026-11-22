/*     */ package net.minecraft.world.level.levelgen.blending;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final int minSection;
/*     */   
/*     */   private final int maxSection;
/*     */   
/*     */   private final Optional<double[]> heights;
/*     */   
/*     */   public Packed(int minSection, int maxSection, Optional<double[]> heights) {
/* 457 */     this.minSection = minSection;
/* 457 */     this.maxSection = maxSection;
/* 457 */     this.heights = heights;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #457	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #457	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #457	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int minSection() {
/* 457 */     return this.minSection;
/*     */   }
/*     */   
/*     */   public int maxSection() {
/* 457 */     return this.maxSection;
/*     */   }
/*     */   
/*     */   public Optional<double[]> heights() {
/* 457 */     return this.heights;
/*     */   }
/*     */   
/* 460 */   private static final Codec<double[]> DOUBLE_ARRAY_CODEC = Codec.DOUBLE.listOf().xmap(Doubles::toArray, Doubles::asList);
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   static {
/* 465 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("min_section").forGetter(Packed::minSection), (App)Codec.INT.fieldOf("max_section").forGetter(Packed::maxSection), (App)DOUBLE_ARRAY_CODEC.lenientOptionalFieldOf("heights").forGetter(Packed::heights)).apply((Applicative)i, Packed::new)).validate(Packed::validateArraySize);
/*     */   }
/*     */   
/*     */   private static DataResult<Packed> validateArraySize(Packed blendingData) {
/* 468 */     if (blendingData.heights.isPresent() && ((double[])blendingData.heights.get()).length != BlendingData.CELL_COLUMN_COUNT)
/* 469 */       return DataResult.error(() -> "heights has to be of length " + BlendingData.CELL_COLUMN_COUNT); 
/* 472 */     return DataResult.success(blendingData);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blending\BlendingData$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */