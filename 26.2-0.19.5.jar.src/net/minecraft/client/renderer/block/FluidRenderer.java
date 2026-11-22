/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CardinalLighting;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FluidRenderer {
/*     */   private static final float MAX_FLUID_HEIGHT = 0.8888889F;
/*     */   
/*     */   private final FluidStateModelSet fluidModels;
/*     */   
/*     */   public FluidRenderer(FluidStateModelSet fluidModels) {
/*  31 */     this.fluidModels = fluidModels;
/*     */   }
/*     */   
/*     */   private static boolean isNeighborSameFluid(FluidState fluidState, FluidState neighborFluidState) {
/*  35 */     return neighborFluidState.getType().isSame(fluidState.getType());
/*     */   }
/*     */   
/*     */   private static boolean isFaceOccludedByState(Direction direction, float height, BlockState state) {
/*  39 */     VoxelShape occluder = state.getFaceOcclusionShape(direction.getOpposite());
/*  40 */     if (occluder == Shapes.empty())
/*  41 */       return false; 
/*  42 */     if (occluder == Shapes.block()) {
/*  43 */       boolean fullBlock = (height == 1.0F);
/*  44 */       return (direction != Direction.UP || fullBlock);
/*     */     } 
/*  46 */     VoxelShape shape = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, height, 1.0D);
/*  47 */     return Shapes.blockOccludes(shape, occluder, direction);
/*     */   }
/*     */   
/*     */   private static boolean isFaceOccludedByNeighbor(Direction direction, float height, BlockState neighborState) {
/*  51 */     return isFaceOccludedByState(direction, height, neighborState);
/*     */   }
/*     */   
/*     */   private static boolean isFaceOccludedBySelf(BlockState state, Direction direction) {
/*  57 */     return isFaceOccludedByState(direction.getOpposite(), 1.0F, state);
/*     */   }
/*     */   
/*     */   public static boolean shouldRenderFace(FluidState fluidState, BlockState blockState, Direction direction, FluidState neighborFluidState) {
/*  61 */     return (!isNeighborSameFluid(fluidState, neighborFluidState) && !isFaceOccludedBySelf(blockState, direction));
/*     */   }
/*     */   
/*     */   public void tesselate(BlockAndTintGetter level, BlockPos pos, Output output, BlockState blockState, FluidState fluidState) {
/*     */     float heightNorthEast, heightNorthWest, heightSouthEast, heightSouthWest;
/*  65 */     BlockState blockStateDown = level.getBlockState(pos.relative(Direction.DOWN));
/*  66 */     FluidState fluidStateDown = blockStateDown.getFluidState();
/*  67 */     BlockState blockStateUp = level.getBlockState(pos.relative(Direction.UP));
/*  68 */     FluidState fluidStateUp = blockStateUp.getFluidState();
/*  69 */     BlockState blockStateNorth = level.getBlockState(pos.relative(Direction.NORTH));
/*  70 */     FluidState fluidStateNorth = blockStateNorth.getFluidState();
/*  71 */     BlockState blockStateSouth = level.getBlockState(pos.relative(Direction.SOUTH));
/*  72 */     FluidState fluidStateSouth = blockStateSouth.getFluidState();
/*  73 */     BlockState blockStateWest = level.getBlockState(pos.relative(Direction.WEST));
/*  74 */     FluidState fluidStateWest = blockStateWest.getFluidState();
/*  75 */     BlockState blockStateEast = level.getBlockState(pos.relative(Direction.EAST));
/*  76 */     FluidState fluidStateEast = blockStateEast.getFluidState();
/*  78 */     boolean renderUp = !isNeighborSameFluid(fluidState, fluidStateUp);
/*  79 */     boolean renderDown = (shouldRenderFace(fluidState, blockState, Direction.DOWN, fluidStateDown) && !isFaceOccludedByNeighbor(Direction.DOWN, 0.8888889F, blockStateDown));
/*  81 */     boolean renderNorth = shouldRenderFace(fluidState, blockState, Direction.NORTH, fluidStateNorth);
/*  82 */     boolean renderSouth = shouldRenderFace(fluidState, blockState, Direction.SOUTH, fluidStateSouth);
/*  83 */     boolean renderWest = shouldRenderFace(fluidState, blockState, Direction.WEST, fluidStateWest);
/*  84 */     boolean renderEast = shouldRenderFace(fluidState, blockState, Direction.EAST, fluidStateEast);
/*  86 */     if (!renderUp && !renderDown && !renderEast && !renderWest && !renderNorth && !renderSouth)
/*     */       return; 
/*  90 */     FluidModel model = this.fluidModels.get(fluidState);
/*  91 */     VertexConsumer builder = output.getBuilder(model.layer());
/*  93 */     int tintColor = (model.tintSource() != null) ? model.tintSource().colorInWorld(blockState, level, pos) : -1;
/*  95 */     CardinalLighting cardinalLighting = level.cardinalLighting();
/*  97 */     Fluid type = fluidState.getType();
/* 104 */     float heightSelf = getHeight(level, type, pos, blockState, fluidState);
/* 105 */     if (heightSelf >= 1.0F) {
/* 106 */       heightNorthEast = 1.0F;
/* 107 */       heightNorthWest = 1.0F;
/* 108 */       heightSouthEast = 1.0F;
/* 109 */       heightSouthWest = 1.0F;
/*     */     } else {
/* 111 */       float heightNorth = getHeight(level, type, pos.north(), blockStateNorth, fluidStateNorth);
/* 112 */       float heightSouth = getHeight(level, type, pos.south(), blockStateSouth, fluidStateSouth);
/* 113 */       float heightEast = getHeight(level, type, pos.east(), blockStateEast, fluidStateEast);
/* 114 */       float heightWest = getHeight(level, type, pos.west(), blockStateWest, fluidStateWest);
/* 116 */       heightNorthEast = calculateAverageHeight(level, type, heightSelf, heightNorth, heightEast, pos.relative(Direction.NORTH).relative(Direction.EAST));
/* 117 */       heightNorthWest = calculateAverageHeight(level, type, heightSelf, heightNorth, heightWest, pos.relative(Direction.NORTH).relative(Direction.WEST));
/* 118 */       heightSouthEast = calculateAverageHeight(level, type, heightSelf, heightSouth, heightEast, pos.relative(Direction.SOUTH).relative(Direction.EAST));
/* 119 */       heightSouthWest = calculateAverageHeight(level, type, heightSelf, heightSouth, heightWest, pos.relative(Direction.SOUTH).relative(Direction.WEST));
/*     */     } 
/* 122 */     float x = (pos.getX() & 0xF);
/* 123 */     float y = (pos.getY() & 0xF);
/* 124 */     float z = (pos.getZ() & 0xF);
/* 126 */     float offs = 0.001F;
/* 127 */     float bottomOffs = renderDown ? 0.001F : 0.0F;
/* 129 */     if (renderUp && !isFaceOccludedByNeighbor(Direction.UP, Math.min(Math.min(heightNorthWest, heightSouthWest), Math.min(heightSouthEast, heightNorthEast)), blockStateUp)) {
/*     */       float u00, u01, u10, u11, v00, v01, v10, v11;
/* 131 */       heightNorthWest -= 0.001F;
/* 132 */       heightSouthWest -= 0.001F;
/* 133 */       heightSouthEast -= 0.001F;
/* 134 */       heightNorthEast -= 0.001F;
/* 145 */       Vec3 flow = fluidState.getFlow((BlockGetter)level, pos);
/* 146 */       if (flow.x == 0.0D && flow.z == 0.0D) {
/* 147 */         TextureAtlasSprite stillSprite = model.stillMaterial().sprite();
/* 148 */         u00 = stillSprite.getU0();
/* 149 */         v00 = stillSprite.getV0();
/* 150 */         u01 = u00;
/* 151 */         v01 = stillSprite.getV1();
/* 152 */         u10 = stillSprite.getU1();
/* 153 */         v10 = v01;
/* 154 */         u11 = u10;
/* 155 */         v11 = v00;
/*     */       } else {
/* 157 */         float angle = (float)Mth.atan2(flow.z, flow.x) - 1.5707964F;
/* 158 */         float s = Mth.sin(angle) * 0.25F;
/* 159 */         float c = Mth.cos(angle) * 0.25F;
/* 160 */         float cc = 0.5F;
/* 161 */         TextureAtlasSprite flowingSprite = model.flowingMaterial().sprite();
/* 162 */         u00 = flowingSprite.getU(0.5F + -c - s);
/* 163 */         v00 = flowingSprite.getV(0.5F + -c + s);
/* 164 */         u01 = flowingSprite.getU(0.5F + -c + s);
/* 165 */         v01 = flowingSprite.getV(0.5F + c + s);
/* 166 */         u10 = flowingSprite.getU(0.5F + c + s);
/* 167 */         v10 = flowingSprite.getV(0.5F + c - s);
/* 168 */         u11 = flowingSprite.getU(0.5F + c - s);
/* 169 */         v11 = flowingSprite.getV(0.5F + -c - s);
/*     */       } 
/* 172 */       int topLightCoords = getLightCoords(level, pos);
/* 173 */       int topColor = ARGB.scaleRGB(tintColor, cardinalLighting.up());
/* 175 */       addFace(builder, x + 0.0F, y + heightNorthWest, z + 0.0F, u00, v00, x + 0.0F, y + heightSouthWest, z + 1.0F, u01, v01, x + 1.0F, y + heightSouthEast, z + 1.0F, u10, v10, x + 1.0F, y + heightNorthEast, z + 0.0F, u11, v11, topColor, topLightCoords, fluidState
/*     */           
/* 183 */           .shouldRenderBackwardUpFace((BlockGetter)level, pos.above()));
/*     */     } 
/* 187 */     if (renderDown) {
/* 188 */       TextureAtlasSprite stillSprite = model.stillMaterial().sprite();
/* 189 */       float u0 = stillSprite.getU0();
/* 190 */       float u1 = stillSprite.getU1();
/* 191 */       float v0 = stillSprite.getV0();
/* 192 */       float v1 = stillSprite.getV1();
/* 194 */       int belowLightCoords = getLightCoords(level, pos.below());
/* 195 */       int belowColor = ARGB.scaleRGB(tintColor, cardinalLighting.down());
/* 197 */       addFace(builder, x, y + bottomOffs, z, u0, v0, x + 1.0F, y + bottomOffs, z, u1, v0, x + 1.0F, y + bottomOffs, z + 1.0F, u1, v1, x, y + bottomOffs, z + 1.0F, u0, v1, belowColor, belowLightCoords, false);
/*     */     } 
/* 208 */     int sideLightCoords = getLightCoords(level, pos);
/* 210 */     for (Direction faceDir : Direction.Plane.HORIZONTAL) {
/*     */       float hh0;
/*     */       float hh1;
/*     */       float x0;
/*     */       float z0;
/*     */       float x1;
/*     */       float z1;
/*     */       boolean renderCondition;
/*     */       BlockState faceState;
/* 220 */       switch (faceDir) {
/*     */         case NORTH:
/* 222 */           hh0 = heightNorthWest;
/* 223 */           hh1 = heightNorthEast;
/* 224 */           x0 = x;
/* 225 */           x1 = x + 1.0F;
/* 226 */           z0 = z + 0.001F;
/* 227 */           z1 = z + 0.001F;
/* 228 */           renderCondition = renderNorth;
/* 229 */           faceState = blockStateNorth;
/*     */           break;
/*     */         case SOUTH:
/* 232 */           hh0 = heightSouthEast;
/* 233 */           hh1 = heightSouthWest;
/* 234 */           x0 = x + 1.0F;
/* 235 */           x1 = x;
/* 236 */           z0 = z + 1.0F - 0.001F;
/* 237 */           z1 = z + 1.0F - 0.001F;
/* 238 */           renderCondition = renderSouth;
/* 239 */           faceState = blockStateSouth;
/*     */           break;
/*     */         case WEST:
/* 242 */           hh0 = heightSouthWest;
/* 243 */           hh1 = heightNorthWest;
/* 244 */           x0 = x + 0.001F;
/* 245 */           x1 = x + 0.001F;
/* 246 */           z0 = z + 1.0F;
/* 247 */           z1 = z;
/* 248 */           renderCondition = renderWest;
/* 249 */           faceState = blockStateWest;
/*     */           break;
/*     */         case EAST:
/* 252 */           hh0 = heightNorthEast;
/* 253 */           hh1 = heightSouthEast;
/* 254 */           x0 = x + 1.0F - 0.001F;
/* 255 */           x1 = x + 1.0F - 0.001F;
/* 256 */           z0 = z;
/* 257 */           z1 = z + 1.0F;
/* 258 */           renderCondition = renderEast;
/* 259 */           faceState = blockStateEast;
/*     */           break;
/*     */         default:
/* 261 */           throw new UnsupportedOperationException();
/*     */       } 
/* 264 */       if (renderCondition && !isFaceOccludedByNeighbor(faceDir, Math.max(hh0, hh1), faceState)) {
/* 265 */         TextureAtlasSprite sprite = model.flowingMaterial().sprite();
/* 266 */         boolean isOverlay = false;
/* 268 */         if (model.overlayMaterial() != null) {
/* 269 */           Block relativeBlock = faceState.getBlock();
/* 270 */           if (relativeBlock instanceof net.minecraft.world.level.block.HalfTransparentBlock || relativeBlock instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 271 */             sprite = model.overlayMaterial().sprite();
/* 272 */             isOverlay = true;
/*     */           } 
/*     */         } 
/* 276 */         float u0 = sprite.getU(0.0F);
/* 277 */         float u1 = sprite.getU(0.5F);
/* 279 */         float v01 = sprite.getV((1.0F - hh0) * 0.5F);
/* 280 */         float v02 = sprite.getV((1.0F - hh1) * 0.5F);
/* 281 */         float v1 = sprite.getV(0.5F);
/* 283 */         float shadeSide = (faceDir.getAxis() == Direction.Axis.Z) ? cardinalLighting.north() : cardinalLighting.west();
/* 284 */         int faceColor = ARGB.scaleRGB(tintColor, cardinalLighting.up() * shadeSide);
/* 286 */         addFace(builder, x0, y + hh0, z0, u0, v01, x1, y + hh1, z1, u1, v02, x1, y + bottomOffs, z1, u1, v1, x0, y + bottomOffs, z0, u0, v1, faceColor, sideLightCoords, !isOverlay);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Output {
/*     */     VertexConsumer getBuilder(ChunkSectionLayer param1ChunkSectionLayer);
/*     */   }
/*     */   
/*     */   private void addFace(VertexConsumer builder, float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3, int color, int lightCoords, boolean addBackFace) {
/* 309 */     vertex(builder, x0, y0, z0, color, u0, v0, lightCoords);
/* 310 */     vertex(builder, x1, y1, z1, color, u1, v1, lightCoords);
/* 311 */     vertex(builder, x2, y2, z2, color, u2, v2, lightCoords);
/* 312 */     vertex(builder, x3, y3, z3, color, u3, v3, lightCoords);
/* 313 */     if (addBackFace) {
/* 315 */       vertex(builder, x0, y0, z0, color, u0, v0, lightCoords);
/* 316 */       vertex(builder, x3, y3, z3, color, u3, v3, lightCoords);
/* 317 */       vertex(builder, x2, y2, z2, color, u2, v2, lightCoords);
/* 318 */       vertex(builder, x1, y1, z1, color, u1, v1, lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   private float calculateAverageHeight(BlockAndTintGetter level, Fluid type, float heightSelf, float height2, float height1, BlockPos cornerPos) {
/* 323 */     if (height1 >= 1.0F || height2 >= 1.0F)
/* 324 */       return 1.0F; 
/* 326 */     float[] weightedHeight = new float[2];
/* 327 */     if (height1 > 0.0F || height2 > 0.0F) {
/* 328 */       float heightCorner = getHeight(level, type, cornerPos);
/* 329 */       if (heightCorner >= 1.0F)
/* 330 */         return 1.0F; 
/* 332 */       addWeightedHeight(weightedHeight, heightCorner);
/*     */     } 
/* 334 */     addWeightedHeight(weightedHeight, heightSelf);
/* 335 */     addWeightedHeight(weightedHeight, height1);
/* 336 */     addWeightedHeight(weightedHeight, height2);
/* 337 */     return weightedHeight[0] / weightedHeight[1];
/*     */   }
/*     */   
/*     */   private void addWeightedHeight(float[] weightedHeight, float height) {
/* 341 */     if (height >= 0.8F) {
/* 342 */       weightedHeight[0] = weightedHeight[0] + height * 10.0F;
/* 343 */       weightedHeight[1] = weightedHeight[1] + 10.0F;
/* 344 */     } else if (height >= 0.0F) {
/* 345 */       weightedHeight[0] = weightedHeight[0] + height;
/* 346 */       weightedHeight[1] = weightedHeight[1] + 1.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos) {
/* 351 */     BlockState state = level.getBlockState(pos);
/* 352 */     return getHeight(level, fluidType, pos, state, state.getFluidState());
/*     */   }
/*     */   
/*     */   private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos, BlockState state, FluidState fluidState) {
/* 356 */     if (fluidType.isSame(fluidState.getType())) {
/* 357 */       BlockState aboveState = level.getBlockState(pos.above());
/* 358 */       if (fluidType.isSame(aboveState.getFluidState().getType()))
/* 359 */         return 1.0F; 
/* 361 */       return fluidState.getOwnHeight();
/*     */     } 
/* 362 */     if (!state.isSolid())
/* 363 */       return 0.0F; 
/* 365 */     return -1.0F;
/*     */   }
/*     */   
/*     */   private void vertex(VertexConsumer builder, float x, float y, float z, int color, float u, float v, int lightCoords) {
/* 369 */     builder.addVertex(x, y, z, color, u, v, OverlayTexture.NO_OVERLAY, lightCoords, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */   
/*     */   private int getLightCoords(BlockAndTintGetter level, BlockPos pos) {
/* 373 */     return LightCoordsUtil.max(LightCoordsUtil.getLightCoords(level, pos), LightCoordsUtil.getLightCoords(level, pos.above()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\FluidRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */