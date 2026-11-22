/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public class CheckerboardColumnBiomeSource extends BiomeSource {
/*    */   public static final MapCodec<CheckerboardColumnBiomeSource> CODEC;
/*    */   
/*    */   private final HolderSet<Biome> allowedBiomes;
/*    */   
/*    */   private final int bitShift;
/*    */   
/*    */   private final int size;
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Biome.LIST_CODEC.fieldOf("biomes").forGetter(()), (App)Codec.intRange(0, 62).optionalFieldOf("scale", Integer.valueOf(2)).forGetter(())).apply((Applicative)i, CheckerboardColumnBiomeSource::new));
/*    */   }
/*    */   
/*    */   public CheckerboardColumnBiomeSource(HolderSet<Biome> allowedBiomes, int size) {
/* 22 */     this.allowedBiomes = allowedBiomes;
/* 23 */     this.bitShift = size + 2;
/* 24 */     this.size = size;
/*    */   }
/*    */   
/*    */   protected Stream<Holder<Biome>> collectPossibleBiomes() {
/* 29 */     return this.allowedBiomes.stream();
/*    */   }
/*    */   
/*    */   protected MapCodec<? extends BiomeSource> codec() {
/* 34 */     return (MapCodec)CODEC;
/*    */   }
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/* 39 */     return this.allowedBiomes.get(Math.floorMod((quartX >> this.bitShift) + (quartZ >> this.bitShift), this.allowedBiomes.size()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\CheckerboardColumnBiomeSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */