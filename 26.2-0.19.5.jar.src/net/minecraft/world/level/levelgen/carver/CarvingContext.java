/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.NoiseChunk;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class CarvingContext extends WorldGenerationContext {
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   private final NoiseChunk noiseChunk;
/*    */   
/*    */   private final RandomState randomState;
/*    */   
/*    */   private final SurfaceRules.RuleSource surfaceRule;
/*    */   
/*    */   public CarvingContext(NoiseBasedChunkGenerator generator, RegistryAccess registryAccess, LevelHeightAccessor heightAccessor, NoiseChunk noiseChunk, RandomState randomState, SurfaceRules.RuleSource surfaceRule) {
/* 26 */     super((ChunkGenerator)generator, heightAccessor);
/* 27 */     this.registryAccess = registryAccess;
/* 28 */     this.noiseChunk = noiseChunk;
/* 29 */     this.randomState = randomState;
/* 30 */     this.surfaceRule = surfaceRule;
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public Optional<BlockState> topMaterial(Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, BlockPos pos, boolean underFluid) {
/* 36 */     return this.randomState.surfaceSystem().topMaterial(this.surfaceRule, this, biomeGetter, chunk, this.noiseChunk, pos, underFluid);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public RegistryAccess registryAccess() {
/* 42 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public RandomState randomState() {
/* 46 */     return this.randomState;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\carver\CarvingContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */