/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class OreVeinifier {
/*     */   private static final float VEININESS_THRESHOLD = 0.4F;
/*     */   
/*     */   private static final int EDGE_ROUNDOFF_BEGIN = 20;
/*     */   
/*     */   private static final double MAX_EDGE_ROUNDOFF = 0.2D;
/*     */   
/*     */   private static final float VEIN_SOLIDNESS = 0.7F;
/*     */   
/*     */   private static final float MIN_RICHNESS = 0.1F;
/*     */   
/*     */   private static final float MAX_RICHNESS = 0.3F;
/*     */   
/*     */   private static final float MAX_RICHNESS_THRESHOLD = 0.6F;
/*     */   
/*     */   private static final float CHANCE_OF_RAW_ORE_BLOCK = 0.02F;
/*     */   
/*     */   private static final float SKIP_ORE_IF_GAP_NOISE_IS_BELOW = -0.3F;
/*     */   
/*     */   static NoiseChunk.BlockStateFiller create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, PositionalRandomFactory oreVeinsPositionalRandomFactory) {
/*  44 */     BlockState defaultState = SharedConstants.DEBUG_ORE_VEINS ? Blocks.AIR.defaultBlockState() : null;
/*  46 */     return context -> {
/*     */         double oreVeininessNoiseValue = veinToggle.compute(context);
/*     */         int posY = context.blockY();
/*     */         VeinType veinType = (oreVeininessNoiseValue > 0.0D) ? VeinType.COPPER : VeinType.IRON;
/*     */         double veininessRidged = Math.abs(oreVeininessNoiseValue);
/*     */         int distanceFromTop = veinType.maxY - posY;
/*     */         int distanceFromBottom = posY - veinType.minY;
/*     */         if (distanceFromBottom < 0 || distanceFromTop < 0)
/*     */           return defaultState; 
/*     */         int distanceFromEdge = Math.min(distanceFromTop, distanceFromBottom);
/*     */         double edgeRoundoff = Mth.clampedMap(distanceFromEdge, 0.0D, 20.0D, -0.2D, 0.0D);
/*     */         if (veininessRidged + edgeRoundoff < 0.4000000059604645D)
/*     */           return defaultState; 
/*     */         RandomSource positionalRandom = oreVeinsPositionalRandomFactory.at(context.blockX(), posY, context.blockZ());
/*     */         if (positionalRandom.nextFloat() > 0.7F)
/*     */           return defaultState; 
/*     */         if (veinRidged.compute(context) >= 0.0D)
/*     */           return defaultState; 
/*     */         double richness = Mth.clampedMap(veininessRidged, 0.4000000059604645D, 0.6000000238418579D, 0.10000000149011612D, 0.30000001192092896D);
/*  79 */         return (positionalRandom.nextFloat() < richness && veinGap.compute(context) > -0.30000001192092896D) ? ((positionalRandom.nextFloat() < 0.02F) ? veinType.rawOreBlock : veinType.ore) : (SharedConstants.DEBUG_ORE_VEINS ? Blocks.OAK_BUTTON.defaultBlockState() : veinType.filler);
/*     */       };
/*     */   }
/*     */   
/*     */   protected enum VeinType {
/*  87 */     COPPER((String)Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50),
/*  88 */     IRON((String)Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);
/*     */     
/*     */     private final BlockState ore;
/*     */     
/*     */     private final BlockState rawOreBlock;
/*     */     
/*     */     private final BlockState filler;
/*     */     
/*     */     protected final int minY;
/*     */     
/*     */     protected final int maxY;
/*     */     
/*     */     VeinType(BlockState ore, BlockState rawOreBlock, BlockState filler, int minY, int maxY) {
/*  98 */       this.ore = ore;
/*  99 */       this.rawOreBlock = rawOreBlock;
/* 100 */       this.filler = filler;
/* 101 */       this.minY = minY;
/* 102 */       this.maxY = maxY;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\OreVeinifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */