/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ final class BiomeConditionSource extends Record implements SurfaceRules.ConditionSource {
/*     */   private final HolderSet<Biome> biomes;
/*     */   
/*     */   private static final MapCodec<BiomeConditionSource> CODEC;
/*     */   
/*     */   private BiomeConditionSource(HolderSet<Biome> biomes) {
/* 652 */     this.biomes = biomes;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #652	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #652	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BiomeConditionSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public HolderSet<Biome> biomes() {
/* 652 */     return this.biomes;
/*     */   }
/*     */   
/*     */   static {
/* 653 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biome_is").forGetter(BiomeConditionSource::biomes)).apply((Applicative)i, BiomeConditionSource::new));
/*     */   }
/*     */   
/*     */   public MapCodec<BiomeConditionSource> codec() {
/* 659 */     return CODEC;
/*     */   }
/*     */   
/*     */   public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 666 */     if (ruleContext.possibleBiomes != null) {
/* 667 */       if (canNeverMatch(ruleContext.possibleBiomes))
/* 668 */         return () -> false; 
/* 669 */       if (willAlwaysMatch(ruleContext.possibleBiomes))
/* 670 */         return () -> true; 
/*     */     } 
/*     */     class BiomeCondition extends SurfaceRules.LazyYCondition {
/*     */       private BiomeCondition() {
/* 676 */         super(param2Context);
/*     */       }
/*     */       
/*     */       protected boolean compute() {
/* 681 */         return SurfaceRules.BiomeConditionSource.this.biomes.contains(this.context.getBiome());
/*     */       }
/*     */     };
/* 685 */     return new BiomeCondition();
/*     */   }
/*     */   
/*     */   private boolean canNeverMatch(Set<Holder<Biome>> possibleBiomes) {
/* 689 */     for (Holder<Biome> biome : this.biomes) {
/* 690 */       if (possibleBiomes.contains(biome))
/* 691 */         return false; 
/*     */     } 
/* 694 */     return true;
/*     */   }
/*     */   
/*     */   private boolean willAlwaysMatch(Set<Holder<Biome>> possibleBiomes) {
/* 698 */     for (Holder<Biome> possibleBiome : possibleBiomes) {
/* 699 */       if (!this.biomes.contains(possibleBiome))
/* 700 */         return false; 
/*     */     } 
/* 703 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 708 */     return "BiomeConditionSource[biomes=" + String.valueOf(this.biomes) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$BiomeConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */