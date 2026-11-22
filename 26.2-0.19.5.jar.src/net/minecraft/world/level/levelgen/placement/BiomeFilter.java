/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class BiomeFilter extends PlacementFilter {
/* 12 */   private static final BiomeFilter INSTANCE = new BiomeFilter();
/*    */   
/* 14 */   public static final MapCodec<BiomeFilter> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public static BiomeFilter biome() {
/* 20 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 25 */     PlacedFeature feature = context.topFeature().<Throwable>orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
/* 26 */     Holder<Biome> biome = context.getLevel().getBiome(origin);
/* 27 */     return context.generator().getBiomeGenerationSettings(biome).hasFeature(feature);
/*    */   }
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 32 */     return PlacementModifierType.BIOME_FILTER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\BiomeFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */