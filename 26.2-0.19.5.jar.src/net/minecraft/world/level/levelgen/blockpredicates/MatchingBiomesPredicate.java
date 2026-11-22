/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class MatchingBiomesPredicate implements BlockPredicate {
/*    */   public static final MapCodec<MatchingBiomesPredicate> CODEC;
/*    */   
/*    */   private final HolderSet<Biome> biomes;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(())).apply((Applicative)i, MatchingBiomesPredicate::new));
/*    */   }
/*    */   
/*    */   public MatchingBiomesPredicate(HolderSet<Biome> biomes) {
/* 20 */     this.biomes = biomes;
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
/* 25 */     return this.biomes.contains(worldGenLevel.getBiome(blockPos));
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 30 */     return BlockPredicateType.MATCHING_BIOMES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBiomesPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */