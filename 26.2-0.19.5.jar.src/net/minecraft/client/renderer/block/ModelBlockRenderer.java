/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.QuadInstance;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.color.block.BlockTintSource;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ModelBlockRenderer {
/*  27 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final BlockModelLighter lighter;
/*     */   
/*     */   private final boolean ambientOcclusion;
/*     */   
/*     */   private final boolean cull;
/*     */   
/*     */   private final BlockColors blockColors;
/*     */   
/*  36 */   private final RandomSource random = RandomSource.createThreadLocalInstance(0L);
/*     */   
/*  37 */   private final List<BlockStateModelPart> parts = (List<BlockStateModelPart>)new ObjectArrayList();
/*     */   
/*  39 */   private final BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();
/*     */   
/*  41 */   private final QuadInstance quadInstance = new QuadInstance();
/*     */   
/*  44 */   private int tintCacheIndex = -1;
/*     */   
/*     */   private int tintCacheValue;
/*     */   
/*     */   private boolean tintSourcesInitialized;
/*     */   
/*  49 */   private final List<BlockTintSource> tintSources = (List<BlockTintSource>)new ObjectArrayList();
/*     */   
/*  50 */   private final IntList computedTintValues = (IntList)new IntArrayList();
/*     */   
/*     */   public ModelBlockRenderer(boolean ambientOcclusion, boolean cull, BlockColors blockColors) {
/*  53 */     this.lighter = new BlockModelLighter();
/*  54 */     this.ambientOcclusion = ambientOcclusion;
/*  55 */     this.cull = cull;
/*  56 */     this.blockColors = blockColors;
/*     */   }
/*     */   
/*     */   public static boolean forceOpaque(boolean cutoutLeaves, BlockState blockState) {
/*  60 */     return (!cutoutLeaves && blockState.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock);
/*     */   }
/*     */   
/*     */   public void tesselateBlock(BlockQuadOutput output, float x, float y, float z, BlockAndTintGetter level, BlockPos pos, BlockState blockState, BlockStateModel model, long seed) {
/*  64 */     this.random.setSeed(seed);
/*  65 */     model.collectParts(this.random, this.parts);
/*  66 */     if (this.parts.isEmpty())
/*     */       return; 
/*     */     try {
/*  70 */       Vec3 offset = blockState.getOffset(pos);
/*  72 */       if (this.ambientOcclusion && blockState.getLightEmission() == 0 && ((BlockStateModelPart)this.parts.getFirst()).useAmbientOcclusion()) {
/*  73 */         tesselateAmbientOcclusion(output, x + (float)offset.x, y + (float)offset.y, z + (float)offset.z, this.parts, level, blockState, pos);
/*     */       } else {
/*  75 */         tesselateFlat(output, x + (float)offset.x, y + (float)offset.y, z + (float)offset.z, this.parts, level, blockState, pos);
/*     */       } 
/*     */     } finally {
/*  78 */       this.parts.clear();
/*  79 */       resetTintCache();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void configureTintCache(BlockState blockState) {
/*  85 */     List<BlockTintSource> tintSources = this.blockColors.getTintSources(blockState);
/*  86 */     int tintSourceCount = tintSources.size();
/*  87 */     if (tintSourceCount > 0) {
/*  89 */       this.tintSources.addAll(tintSources);
/*  90 */       for (int i = 0; i < tintSourceCount; i++)
/*  91 */         this.computedTintValues.add(-1); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetTintCache() {
/*  97 */     this.tintCacheIndex = -1;
/*  98 */     if (this.tintSourcesInitialized) {
/*  99 */       this.tintSources.clear();
/* 100 */       this.computedTintValues.clear();
/* 101 */       this.tintSourcesInitialized = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tesselateAmbientOcclusion(BlockQuadOutput output, float x, float y, float z, List<BlockStateModelPart> parts, BlockAndTintGetter level, BlockState state, BlockPos pos) {
/* 106 */     int cacheValid = 0;
/* 107 */     int shouldRenderFaceCache = 0;
/* 109 */     for (BlockStateModelPart part : parts) {
/* 110 */       for (Direction direction : DIRECTIONS) {
/* 111 */         int cacheMask = 1 << direction.ordinal();
/* 112 */         boolean validCacheForDirection = ((cacheValid & cacheMask) != 0);
/* 113 */         boolean shouldRenderFace = ((shouldRenderFaceCache & cacheMask) != 0);
/* 115 */         if (!validCacheForDirection || shouldRenderFace) {
/* 120 */           List<BakedQuad> culledQuads = part.getQuads(direction);
/* 121 */           if (!culledQuads.isEmpty()) {
/* 125 */             if (!validCacheForDirection) {
/* 126 */               shouldRenderFace = shouldRenderFace(level, state, direction, (BlockPos)this.scratchPos.setWithOffset((Vec3i)pos, direction));
/* 127 */               cacheValid |= cacheMask;
/* 128 */               if (shouldRenderFace)
/* 129 */                 shouldRenderFaceCache |= cacheMask; 
/*     */             } 
/* 133 */             if (shouldRenderFace)
/* 134 */               for (BakedQuad quad : culledQuads) {
/* 135 */                 this.lighter.prepareQuadAmbientOcclusion(level, state, pos, quad, this.quadInstance);
/* 136 */                 putQuadWithTint(output, x, y, z, level, state, pos, quad);
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       } 
/* 141 */       List<BakedQuad> unculledQuads = part.getQuads(null);
/* 142 */       for (BakedQuad quad : unculledQuads) {
/* 143 */         this.lighter.prepareQuadAmbientOcclusion(level, state, pos, quad, this.quadInstance);
/* 144 */         putQuadWithTint(output, x, y, z, level, state, pos, quad);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tesselateFlat(BlockQuadOutput output, float x, float y, float z, List<BlockStateModelPart> parts, BlockAndTintGetter level, BlockState state, BlockPos pos) {
/* 150 */     int cacheValid = 0;
/* 151 */     int shouldRenderFaceCache = 0;
/* 153 */     for (BlockStateModelPart part : parts) {
/* 154 */       for (Direction direction : DIRECTIONS) {
/* 155 */         int cacheMask = 1 << direction.ordinal();
/* 156 */         boolean validCacheForDirection = ((cacheValid & cacheMask) != 0);
/* 157 */         boolean shouldRenderFace = ((shouldRenderFaceCache & cacheMask) != 0);
/* 159 */         if (!validCacheForDirection || shouldRenderFace) {
/* 164 */           List<BakedQuad> culledQuads = part.getQuads(direction);
/* 165 */           if (!culledQuads.isEmpty()) {
/* 169 */             BlockPos.MutableBlockPos mutableBlockPos = this.scratchPos.setWithOffset((Vec3i)pos, direction);
/* 170 */             if (!validCacheForDirection) {
/* 171 */               shouldRenderFace = shouldRenderFace(level, state, direction, (BlockPos)mutableBlockPos);
/* 172 */               cacheValid |= cacheMask;
/* 173 */               if (shouldRenderFace)
/* 174 */                 shouldRenderFaceCache |= cacheMask; 
/*     */             } 
/* 178 */             if (shouldRenderFace) {
/* 179 */               int lightCoords = this.lighter.getLightCoords(state, level, (BlockPos)mutableBlockPos);
/* 180 */               for (BakedQuad quad : culledQuads) {
/* 181 */                 this.lighter.prepareQuadFlat(level, state, pos, lightCoords, quad, this.quadInstance);
/* 182 */                 putQuadWithTint(output, x, y, z, level, state, pos, quad);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 187 */       List<BakedQuad> unculledQuads = part.getQuads(null);
/* 188 */       for (BakedQuad quad : unculledQuads) {
/* 189 */         this.lighter.prepareQuadFlat(level, state, pos, -1, quad, this.quadInstance);
/* 190 */         putQuadWithTint(output, x, y, z, level, state, pos, quad);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldRenderFace(BlockAndTintGetter level, BlockState state, Direction direction, BlockPos neighborPos) {
/* 196 */     if (!this.cull)
/* 197 */       return true; 
/* 199 */     BlockState neighborState = level.getBlockState(neighborPos);
/* 200 */     return Block.shouldRenderFace(state, neighborState, direction);
/*     */   }
/*     */   
/*     */   private void putQuadWithTint(BlockQuadOutput output, float x, float y, float z, BlockAndTintGetter level, BlockState state, BlockPos pos, BakedQuad quad) {
/* 204 */     int tintIndex = quad.materialInfo().tintIndex();
/* 205 */     if (tintIndex != -1)
/* 206 */       this.quadInstance.multiplyColor(getTintColor(level, state, pos, tintIndex)); 
/* 208 */     output.put(x, y, z, quad, this.quadInstance);
/*     */   }
/*     */   
/*     */   private int getTintColor(BlockAndTintGetter level, BlockState state, BlockPos pos, int tintIndex) {
/* 212 */     if (this.tintCacheIndex == tintIndex)
/* 213 */       return this.tintCacheValue; 
/* 215 */     int tintColor = computeTintColor(level, state, pos, tintIndex);
/* 216 */     this.tintCacheIndex = tintIndex;
/* 217 */     this.tintCacheValue = tintColor;
/* 218 */     return tintColor;
/*     */   }
/*     */   
/*     */   private int computeTintColor(BlockAndTintGetter level, BlockState state, BlockPos pos, int tintIndex) {
/* 222 */     if (!this.tintSourcesInitialized) {
/* 223 */       configureTintCache(state);
/* 224 */       this.tintSourcesInitialized = true;
/*     */     } 
/* 227 */     if (tintIndex >= this.tintSources.size())
/* 228 */       return -1; 
/* 232 */     BlockTintSource tintSource = this.tintSources.set(tintIndex, null);
/* 233 */     if (tintSource != null) {
/* 234 */       int computedTintValue = tintSource.colorInWorld(state, level, pos);
/* 235 */       this.computedTintValues.set(tintIndex, computedTintValue);
/* 236 */       return computedTintValue;
/*     */     } 
/* 238 */     return this.computedTintValues.getInt(tintIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\ModelBlockRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */