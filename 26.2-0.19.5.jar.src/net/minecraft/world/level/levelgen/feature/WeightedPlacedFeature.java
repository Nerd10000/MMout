/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ @Deprecated
/*    */ public final class WeightedPlacedFeature extends Record {
/*    */   private final Holder<PlacedFeature> feature;
/*    */   
/*    */   private final float chance;
/*    */   
/*    */   public static final Codec<WeightedPlacedFeature> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/WeightedPlacedFeature;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<PlacedFeature> feature() {
/* 13 */     return this.feature;
/*    */   }
/*    */   
/*    */   public float chance() {
/* 13 */     return this.chance;
/*    */   }
/*    */   
/*    */   public WeightedPlacedFeature(Holder<PlacedFeature> feature, float chance) {
/* 14 */     this.feature = feature;
/* 14 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(())).apply((Applicative)i, WeightedPlacedFeature::new));
/*    */   }
/*    */   
/*    */   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
/* 21 */     return ((PlacedFeature)this.feature.value()).place(level, chunkGenerator, random, origin);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\WeightedPlacedFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */