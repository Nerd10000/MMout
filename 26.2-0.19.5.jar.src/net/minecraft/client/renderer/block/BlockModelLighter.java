/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.QuadInstance;
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CardinalLighting;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BlockModelLighter {
/*     */   private static final int CACHE_SIZE = 100;
/*     */   
/*  19 */   private static final ThreadLocal<Cache> CACHE = ThreadLocal.withInitial(Cache::new);
/*     */   
/*     */   public static final int CHECK_LIGHT = -1;
/*     */   
/*     */   private final Cache cache;
/*     */   
/*  26 */   private final BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private boolean faceCubic;
/*     */   
/*     */   private boolean facePartial;
/*     */   
/*  29 */   private final float[] faceShape = new float[SizeInfo.COUNT];
/*     */   
/*     */   public BlockModelLighter() {
/*  33 */     this.cache = CACHE.get();
/*     */   }
/*     */   
/*     */   public int getLightCoords(BlockState state, BlockAndTintGetter level, BlockPos relativePos) {
/*  37 */     return this.cache.getLightCoords(state, level, relativePos);
/*     */   }
/*     */   
/*     */   public void prepareQuadAmbientOcclusion(BlockAndTintGetter level, BlockState state, BlockPos centerPosition, BakedQuad quad, QuadInstance outputInstance) {
/*     */     float shadeCorner02, shadeCorner03, shadeCorner12, shadeCorner13;
/*     */     int lightCorner02, lightCorner03, lightCorner12, lightCorner13;
/*  41 */     prepareQuadShape(level, state, centerPosition, quad, true);
/*  43 */     Direction direction = quad.direction();
/*  44 */     BlockPos basePosition = this.faceCubic ? centerPosition.relative(direction) : centerPosition;
/*  45 */     AdjacencyInfo info = AdjacencyInfo.fromFacing(direction);
/*  47 */     BlockPos.MutableBlockPos pos = this.scratchPos;
/*  49 */     pos.setWithOffset((Vec3i)basePosition, info.corners[0]);
/*  50 */     BlockState state0 = level.getBlockState((BlockPos)pos);
/*  51 */     int light0 = this.cache.getLightCoords(state0, level, (BlockPos)pos);
/*  52 */     float shade0 = this.cache.getShadeBrightness(state0, level, (BlockPos)pos);
/*  54 */     pos.setWithOffset((Vec3i)basePosition, info.corners[1]);
/*  55 */     BlockState state1 = level.getBlockState((BlockPos)pos);
/*  56 */     int light1 = this.cache.getLightCoords(state1, level, (BlockPos)pos);
/*  57 */     float shade1 = this.cache.getShadeBrightness(state1, level, (BlockPos)pos);
/*  59 */     pos.setWithOffset((Vec3i)basePosition, info.corners[2]);
/*  60 */     BlockState state2 = level.getBlockState((BlockPos)pos);
/*  61 */     int light2 = this.cache.getLightCoords(state2, level, (BlockPos)pos);
/*  62 */     float shade2 = this.cache.getShadeBrightness(state2, level, (BlockPos)pos);
/*  64 */     pos.setWithOffset((Vec3i)basePosition, info.corners[3]);
/*  65 */     BlockState state3 = level.getBlockState((BlockPos)pos);
/*  66 */     int light3 = this.cache.getLightCoords(state3, level, (BlockPos)pos);
/*  67 */     float shade3 = this.cache.getShadeBrightness(state3, level, (BlockPos)pos);
/*  69 */     BlockState corner0 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(direction));
/*  70 */     boolean translucent0 = (!corner0.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner0.getLightDampening() == 0);
/*  71 */     BlockState corner1 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(direction));
/*  72 */     boolean translucent1 = (!corner1.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner1.getLightDampening() == 0);
/*  73 */     BlockState corner2 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[2]).move(direction));
/*  74 */     boolean translucent2 = (!corner2.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner2.getLightDampening() == 0);
/*  75 */     BlockState corner3 = level.getBlockState((BlockPos)pos.setWithOffset((Vec3i)basePosition, info.corners[3]).move(direction));
/*  76 */     boolean translucent3 = (!corner3.isViewBlocking((BlockGetter)level, (BlockPos)pos) || corner3.getLightDampening() == 0);
/*  87 */     if (translucent2 || translucent0) {
/*  88 */       pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(info.corners[2]);
/*  89 */       BlockState state02 = level.getBlockState((BlockPos)pos);
/*  90 */       shadeCorner02 = this.cache.getShadeBrightness(state02, level, (BlockPos)pos);
/*  91 */       lightCorner02 = this.cache.getLightCoords(state02, level, (BlockPos)pos);
/*     */     } else {
/*  93 */       shadeCorner02 = shade0;
/*  94 */       lightCorner02 = light0;
/*     */     } 
/*  96 */     if (translucent3 || translucent0) {
/*  97 */       pos.setWithOffset((Vec3i)basePosition, info.corners[0]).move(info.corners[3]);
/*  98 */       BlockState state03 = level.getBlockState((BlockPos)pos);
/*  99 */       shadeCorner03 = this.cache.getShadeBrightness(state03, level, (BlockPos)pos);
/* 100 */       lightCorner03 = this.cache.getLightCoords(state03, level, (BlockPos)pos);
/*     */     } else {
/* 102 */       shadeCorner03 = shade0;
/* 103 */       lightCorner03 = light0;
/*     */     } 
/* 105 */     if (translucent2 || translucent1) {
/* 106 */       pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(info.corners[2]);
/* 107 */       BlockState state12 = level.getBlockState((BlockPos)pos);
/* 108 */       shadeCorner12 = this.cache.getShadeBrightness(state12, level, (BlockPos)pos);
/* 109 */       lightCorner12 = this.cache.getLightCoords(state12, level, (BlockPos)pos);
/*     */     } else {
/* 111 */       shadeCorner12 = shade0;
/* 112 */       lightCorner12 = light0;
/*     */     } 
/* 114 */     if (translucent3 || translucent1) {
/* 115 */       pos.setWithOffset((Vec3i)basePosition, info.corners[1]).move(info.corners[3]);
/* 116 */       BlockState state13 = level.getBlockState((BlockPos)pos);
/* 117 */       shadeCorner13 = this.cache.getShadeBrightness(state13, level, (BlockPos)pos);
/* 118 */       lightCorner13 = this.cache.getLightCoords(state13, level, (BlockPos)pos);
/*     */     } else {
/* 120 */       shadeCorner13 = shade0;
/* 121 */       lightCorner13 = light0;
/*     */     } 
/* 124 */     int lightCenter = this.cache.getLightCoords(state, level, centerPosition);
/* 125 */     pos.setWithOffset((Vec3i)centerPosition, direction);
/* 126 */     BlockState nextState = level.getBlockState((BlockPos)pos);
/* 127 */     if (this.faceCubic || !nextState.isSolidRender())
/* 128 */       lightCenter = this.cache.getLightCoords(nextState, level, (BlockPos)pos); 
/* 133 */     float shadeCenter = this.faceCubic ? this.cache.getShadeBrightness(level.getBlockState(basePosition), level, basePosition) : this.cache.getShadeBrightness(level.getBlockState(centerPosition), level, centerPosition);
/* 135 */     AmbientVertexRemap remap = AmbientVertexRemap.fromFacing(direction);
/* 137 */     if (!this.facePartial || !info.doNonCubicWeight) {
/* 138 */       float lightLevel1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
/* 139 */       float lightLevel2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
/* 140 */       float lightLevel3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
/* 141 */       float lightLevel4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
/* 143 */       outputInstance.setLightCoords(remap.vert0, LightCoordsUtil.smoothBlend(light3, light0, lightCorner03, lightCenter));
/* 144 */       outputInstance.setLightCoords(remap.vert1, LightCoordsUtil.smoothBlend(light2, light0, lightCorner02, lightCenter));
/* 145 */       outputInstance.setLightCoords(remap.vert2, LightCoordsUtil.smoothBlend(light2, light1, lightCorner12, lightCenter));
/* 146 */       outputInstance.setLightCoords(remap.vert3, LightCoordsUtil.smoothBlend(light3, light1, lightCorner13, lightCenter));
/* 148 */       outputInstance.setColor(remap.vert0, ARGB.gray(lightLevel1));
/* 149 */       outputInstance.setColor(remap.vert1, ARGB.gray(lightLevel2));
/* 150 */       outputInstance.setColor(remap.vert2, ARGB.gray(lightLevel3));
/* 151 */       outputInstance.setColor(remap.vert3, ARGB.gray(lightLevel4));
/*     */     } else {
/* 153 */       float tempShade1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
/* 154 */       float tempShade2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
/* 155 */       float tempShade3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
/* 156 */       float tempShade4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
/* 158 */       float vert0weight01 = this.faceShape[(info.vert0Weights[0]).index] * this.faceShape[(info.vert0Weights[1]).index];
/* 159 */       float vert0weight23 = this.faceShape[(info.vert0Weights[2]).index] * this.faceShape[(info.vert0Weights[3]).index];
/* 160 */       float vert0weight45 = this.faceShape[(info.vert0Weights[4]).index] * this.faceShape[(info.vert0Weights[5]).index];
/* 161 */       float vert0weight67 = this.faceShape[(info.vert0Weights[6]).index] * this.faceShape[(info.vert0Weights[7]).index];
/* 163 */       float vert1weight01 = this.faceShape[(info.vert1Weights[0]).index] * this.faceShape[(info.vert1Weights[1]).index];
/* 164 */       float vert1weight23 = this.faceShape[(info.vert1Weights[2]).index] * this.faceShape[(info.vert1Weights[3]).index];
/* 165 */       float vert1weight45 = this.faceShape[(info.vert1Weights[4]).index] * this.faceShape[(info.vert1Weights[5]).index];
/* 166 */       float vert1weight67 = this.faceShape[(info.vert1Weights[6]).index] * this.faceShape[(info.vert1Weights[7]).index];
/* 168 */       float vert2weight01 = this.faceShape[(info.vert2Weights[0]).index] * this.faceShape[(info.vert2Weights[1]).index];
/* 169 */       float vert2weight23 = this.faceShape[(info.vert2Weights[2]).index] * this.faceShape[(info.vert2Weights[3]).index];
/* 170 */       float vert2weight45 = this.faceShape[(info.vert2Weights[4]).index] * this.faceShape[(info.vert2Weights[5]).index];
/* 171 */       float vert2weight67 = this.faceShape[(info.vert2Weights[6]).index] * this.faceShape[(info.vert2Weights[7]).index];
/* 173 */       float vert3weight01 = this.faceShape[(info.vert3Weights[0]).index] * this.faceShape[(info.vert3Weights[1]).index];
/* 174 */       float vert3weight23 = this.faceShape[(info.vert3Weights[2]).index] * this.faceShape[(info.vert3Weights[3]).index];
/* 175 */       float vert3weight45 = this.faceShape[(info.vert3Weights[4]).index] * this.faceShape[(info.vert3Weights[5]).index];
/* 176 */       float vert3weight67 = this.faceShape[(info.vert3Weights[6]).index] * this.faceShape[(info.vert3Weights[7]).index];
/* 178 */       outputInstance.setColor(remap.vert0, ARGB.gray(Math.clamp(tempShade1 * vert0weight01 + tempShade2 * vert0weight23 + tempShade3 * vert0weight45 + tempShade4 * vert0weight67, 0.0F, 1.0F)));
/* 179 */       outputInstance.setColor(remap.vert1, ARGB.gray(Math.clamp(tempShade1 * vert1weight01 + tempShade2 * vert1weight23 + tempShade3 * vert1weight45 + tempShade4 * vert1weight67, 0.0F, 1.0F)));
/* 180 */       outputInstance.setColor(remap.vert2, ARGB.gray(Math.clamp(tempShade1 * vert2weight01 + tempShade2 * vert2weight23 + tempShade3 * vert2weight45 + tempShade4 * vert2weight67, 0.0F, 1.0F)));
/* 181 */       outputInstance.setColor(remap.vert3, ARGB.gray(Math.clamp(tempShade1 * vert3weight01 + tempShade2 * vert3weight23 + tempShade3 * vert3weight45 + tempShade4 * vert3weight67, 0.0F, 1.0F)));
/* 183 */       int _tc1 = LightCoordsUtil.smoothBlend(light3, light0, lightCorner03, lightCenter);
/* 184 */       int _tc2 = LightCoordsUtil.smoothBlend(light2, light0, lightCorner02, lightCenter);
/* 185 */       int _tc3 = LightCoordsUtil.smoothBlend(light2, light1, lightCorner12, lightCenter);
/* 186 */       int _tc4 = LightCoordsUtil.smoothBlend(light3, light1, lightCorner13, lightCenter);
/* 188 */       outputInstance.setLightCoords(remap.vert0, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert0weight01, vert0weight23, vert0weight45, vert0weight67));
/* 189 */       outputInstance.setLightCoords(remap.vert1, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert1weight01, vert1weight23, vert1weight45, vert1weight67));
/* 190 */       outputInstance.setLightCoords(remap.vert2, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert2weight01, vert2weight23, vert2weight45, vert2weight67));
/* 191 */       outputInstance.setLightCoords(remap.vert3, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert3weight01, vert3weight23, vert3weight45, vert3weight67));
/*     */     } 
/* 194 */     CardinalLighting cardinalLighting = level.cardinalLighting();
/* 195 */     outputInstance.scaleColor(quad.materialInfo().shade() ? cardinalLighting.byFace(direction) : cardinalLighting.up());
/*     */   }
/*     */   
/*     */   public void prepareQuadFlat(BlockAndTintGetter level, BlockState state, BlockPos pos, int lightCoords, BakedQuad quad, QuadInstance outputInstance) {
/* 199 */     if (lightCoords == -1) {
/* 200 */       prepareQuadShape(level, state, pos, quad, false);
/* 202 */       BlockPos lightPos = this.faceCubic ? (BlockPos)this.scratchPos.setWithOffset((Vec3i)pos, quad.direction()) : pos;
/* 203 */       outputInstance.setLightCoords(this.cache.getLightCoords(state, level, lightPos));
/*     */     } else {
/* 205 */       outputInstance.setLightCoords(lightCoords);
/*     */     } 
/* 208 */     CardinalLighting cardinalLighting = level.cardinalLighting();
/* 209 */     float directionalBrightness = quad.materialInfo().shade() ? cardinalLighting.byFace(quad.direction()) : cardinalLighting.up();
/* 210 */     outputInstance.setColor(ARGB.gray(directionalBrightness));
/*     */   }
/*     */   
/*     */   private void prepareQuadShape(BlockAndTintGetter level, BlockState state, BlockPos pos, BakedQuad quad, boolean ambientOcclusion) {
/* 214 */     float minX = 32.0F;
/* 215 */     float minY = 32.0F;
/* 216 */     float minZ = 32.0F;
/* 217 */     float maxX = -32.0F;
/* 218 */     float maxY = -32.0F;
/* 219 */     float maxZ = -32.0F;
/* 220 */     for (int i = 0; i < 4; i++) {
/* 221 */       Vector3fc position = quad.position(i);
/* 222 */       float x = position.x();
/* 223 */       float y = position.y();
/* 224 */       float z = position.z();
/* 225 */       minX = Math.min(minX, x);
/* 226 */       minY = Math.min(minY, y);
/* 227 */       minZ = Math.min(minZ, z);
/* 228 */       maxX = Math.max(maxX, x);
/* 229 */       maxY = Math.max(maxY, y);
/* 230 */       maxZ = Math.max(maxZ, z);
/*     */     } 
/* 233 */     if (ambientOcclusion) {
/* 234 */       this.faceShape[SizeInfo.WEST.index] = minX;
/* 235 */       this.faceShape[SizeInfo.EAST.index] = maxX;
/* 236 */       this.faceShape[SizeInfo.DOWN.index] = minY;
/* 237 */       this.faceShape[SizeInfo.UP.index] = maxY;
/* 238 */       this.faceShape[SizeInfo.NORTH.index] = minZ;
/* 239 */       this.faceShape[SizeInfo.SOUTH.index] = maxZ;
/* 240 */       this.faceShape[SizeInfo.FLIP_WEST.index] = 1.0F - minX;
/* 241 */       this.faceShape[SizeInfo.FLIP_EAST.index] = 1.0F - maxX;
/* 242 */       this.faceShape[SizeInfo.FLIP_DOWN.index] = 1.0F - minY;
/* 243 */       this.faceShape[SizeInfo.FLIP_UP.index] = 1.0F - maxY;
/* 244 */       this.faceShape[SizeInfo.FLIP_NORTH.index] = 1.0F - minZ;
/* 245 */       this.faceShape[SizeInfo.FLIP_SOUTH.index] = 1.0F - maxZ;
/*     */     } 
/* 248 */     float minEpsilon = 1.0E-4F;
/* 249 */     float maxEpsilon = 0.9999F;
/* 250 */     switch (quad.direction()) {
/*     */       default:
/* 250 */         throw new MatchException(null, null);
/*     */       case DOWN:
/*     */       case UP:
/* 251 */         if (minX >= 1.0E-4F || minZ >= 1.0E-4F || maxX <= 0.9999F || maxZ <= 0.9999F);
/*     */       case NORTH:
/*     */       case SOUTH:
/* 252 */         if (minX >= 1.0E-4F || minY >= 1.0E-4F || maxX <= 0.9999F || maxY <= 0.9999F);
/*     */       case WEST:
/*     */       case EAST:
/* 253 */         if (minY >= 1.0E-4F || minZ >= 1.0E-4F || maxY <= 0.9999F || maxZ <= 0.9999F);
/*     */         break;
/*     */     } 
/*     */     this.facePartial = false;
/* 256 */     switch (quad.direction()) {
/*     */       default:
/* 256 */         throw new MatchException(null, null);
/*     */       case DOWN:
/* 257 */         if (minY == maxY && (minY < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */       case UP:
/* 258 */         if (minY == maxY && (maxY > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */       case NORTH:
/* 259 */         if (minZ == maxZ && (minZ < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */       case SOUTH:
/* 260 */         if (minZ == maxZ && (maxZ > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */       case WEST:
/* 261 */         if (minX == maxX && (minX < 1.0E-4F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */       case EAST:
/* 262 */         if (minX == maxX && (maxX > 0.9999F || state.isCollisionShapeFullBlock((BlockGetter)level, pos)));
/*     */         break;
/*     */     } 
/*     */     this.faceCubic = false;
/*     */   }
/*     */   
/*     */   public static void enableCaching() {
/* 267 */     ((Cache)CACHE.get()).enable();
/*     */   }
/*     */   
/*     */   public static void clearCache() {
/* 271 */     ((Cache)CACHE.get()).disable();
/*     */   }
/*     */   
/*     */   public static class Cache {
/*     */     private boolean enabled;
/*     */     
/*     */     private final Long2IntLinkedOpenHashMap colorCache;
/*     */     
/*     */     private final Long2FloatLinkedOpenHashMap brightnessCache;
/*     */     
/*     */     private final LightCoordsUtil.BrightnessGetter cachedBrightnessGetter;
/*     */     
/*     */     public Cache() {
/* 277 */       this.colorCache = (Long2IntLinkedOpenHashMap)Util.make(() -> {
/*     */             Long2IntLinkedOpenHashMap map = new Long2IntLinkedOpenHashMap(100, 0.25F) {
/*     */                 protected void rehash(int newN) {}
/*     */               };
/*     */             map.defaultReturnValue(2147483647);
/*     */             return map;
/*     */           });
/* 287 */       this.brightnessCache = (Long2FloatLinkedOpenHashMap)Util.make(() -> {
/*     */             Long2FloatLinkedOpenHashMap map = new Long2FloatLinkedOpenHashMap(100, 0.25F) {
/*     */                 protected void rehash(int newN) {}
/*     */               };
/*     */             map.defaultReturnValue(Float.NaN);
/*     */             return map;
/*     */           });
/* 297 */       this.cachedBrightnessGetter = ((level, pos) -> {
/*     */           long key = pos.asLong();
/*     */           int cached = this.colorCache.get(key);
/*     */           if (cached != Integer.MAX_VALUE)
/*     */             return cached; 
/*     */           int value = LightCoordsUtil.BrightnessGetter.DEFAULT.packedBrightness(level, pos);
/*     */           if (this.colorCache.size() == 100)
/*     */             this.colorCache.removeFirstInt(); 
/*     */           this.colorCache.put(key, value);
/*     */           return value;
/*     */         });
/*     */     }
/*     */     
/*     */     public void enable() {
/* 314 */       this.enabled = true;
/*     */     }
/*     */     
/*     */     public void disable() {
/* 318 */       this.enabled = false;
/* 319 */       this.colorCache.clear();
/* 320 */       this.brightnessCache.clear();
/*     */     }
/*     */     
/*     */     public int getLightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 324 */       return LightCoordsUtil.getLightCoords(this.enabled ? this.cachedBrightnessGetter : LightCoordsUtil.BrightnessGetter.DEFAULT, level, state, pos);
/*     */     }
/*     */     
/*     */     public float getShadeBrightness(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 328 */       long key = pos.asLong();
/* 329 */       if (this.enabled) {
/* 330 */         float cached = this.brightnessCache.get(key);
/* 331 */         if (!Float.isNaN(cached))
/* 332 */           return cached; 
/*     */       } 
/* 336 */       float brightness = state.getShadeBrightness((BlockGetter)level, pos);
/* 337 */       if (this.enabled) {
/* 338 */         if (this.brightnessCache.size() == 100)
/* 339 */           this.brightnessCache.removeFirstFloat(); 
/* 341 */         this.brightnessCache.put(key, brightness);
/*     */       } 
/* 343 */       return brightness;
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends Long2IntLinkedOpenHashMap {
/*     */     null(int expected, float f) {
/*     */       super(expected, f);
/*     */     }
/*     */     
/*     */     protected void rehash(int newN) {}
/*     */   }
/*     */   
/*     */   class null extends Long2FloatLinkedOpenHashMap {
/*     */     null(int expected, float f) {
/*     */       super(expected, f);
/*     */     }
/*     */     
/*     */     protected void rehash(int newN) {}
/*     */   }
/*     */   
/*     */   private enum AmbientVertexRemap {
/* 348 */     DOWN(0, 1, 2, 3),
/* 349 */     UP(2, 3, 0, 1),
/* 350 */     NORTH(3, 0, 1, 2),
/* 351 */     SOUTH(0, 1, 2, 3),
/* 352 */     WEST(3, 0, 1, 2),
/* 353 */     EAST(1, 2, 3, 0);
/*     */     
/*     */     private final int vert0;
/*     */     
/*     */     private final int vert1;
/*     */     
/*     */     private final int vert2;
/*     */     
/*     */     private final int vert3;
/*     */     
/*     */     private static final AmbientVertexRemap[] BY_FACING;
/*     */     
/*     */     static {
/* 360 */       BY_FACING = (AmbientVertexRemap[])Util.make(new AmbientVertexRemap[6], map -> {
/*     */             map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */             map[Direction.UP.get3DDataValue()] = UP;
/*     */             map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */             map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */             map[Direction.WEST.get3DDataValue()] = WEST;
/*     */             map[Direction.EAST.get3DDataValue()] = EAST;
/*     */           });
/*     */     }
/*     */     
/*     */     AmbientVertexRemap(int vert0, int vert1, int vert2, int vert3) {
/* 370 */       this.vert0 = vert0;
/* 371 */       this.vert1 = vert1;
/* 372 */       this.vert2 = vert2;
/* 373 */       this.vert3 = vert3;
/*     */     }
/*     */     
/*     */     public static AmbientVertexRemap fromFacing(Direction direction) {
/* 377 */       return BY_FACING[direction.get3DDataValue()];
/*     */     }
/*     */   }
/*     */   
/*     */   private enum SizeInfo {
/* 382 */     DOWN(0),
/* 383 */     UP(1),
/* 384 */     NORTH(2),
/* 385 */     SOUTH(3),
/* 386 */     WEST(4),
/* 387 */     EAST(5),
/* 388 */     FLIP_DOWN(6),
/* 389 */     FLIP_UP(7),
/* 390 */     FLIP_NORTH(8),
/* 391 */     FLIP_SOUTH(9),
/* 392 */     FLIP_WEST(10),
/* 393 */     FLIP_EAST(11);
/*     */     
/* 395 */     public static final int COUNT = (values()).length;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     SizeInfo(int index) {
/* 400 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum AdjacencyInfo {
/* 405 */     DOWN((String)new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.SOUTH }),
/* 411 */     UP((String)new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.SOUTH }),
/* 417 */     NORTH((String)new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_WEST }),
/* 423 */     SOUTH((String)new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.EAST }),
/* 429 */     WEST((String)new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.SOUTH }),
/* 435 */     EAST((String)new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.SOUTH });
/*     */     
/*     */     private final Direction[] corners;
/*     */     
/*     */     private final boolean doNonCubicWeight;
/*     */     
/*     */     private final BlockModelLighter.SizeInfo[] vert0Weights;
/*     */     
/*     */     private final BlockModelLighter.SizeInfo[] vert1Weights;
/*     */     
/*     */     private final BlockModelLighter.SizeInfo[] vert2Weights;
/*     */     
/*     */     private final BlockModelLighter.SizeInfo[] vert3Weights;
/*     */     
/*     */     private static final AdjacencyInfo[] BY_FACING;
/*     */     
/*     */     static {
/* 449 */       BY_FACING = (AdjacencyInfo[])Util.make(new AdjacencyInfo[6], map -> {
/*     */             map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */             map[Direction.UP.get3DDataValue()] = UP;
/*     */             map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */             map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */             map[Direction.WEST.get3DDataValue()] = WEST;
/*     */             map[Direction.EAST.get3DDataValue()] = EAST;
/*     */           });
/*     */     }
/*     */     
/*     */     AdjacencyInfo(Direction[] corners, float shadeWeight, boolean doNonCubicWeight, BlockModelLighter.SizeInfo[] vert0Weights, BlockModelLighter.SizeInfo[] vert1Weights, BlockModelLighter.SizeInfo[] vert2Weights, BlockModelLighter.SizeInfo[] vert3Weights) {
/* 459 */       this.corners = corners;
/* 460 */       this.doNonCubicWeight = doNonCubicWeight;
/* 461 */       this.vert0Weights = vert0Weights;
/* 462 */       this.vert1Weights = vert1Weights;
/* 463 */       this.vert2Weights = vert2Weights;
/* 464 */       this.vert3Weights = vert3Weights;
/*     */     }
/*     */     
/*     */     public static AdjacencyInfo fromFacing(Direction direction) {
/* 468 */       return BY_FACING[direction.get3DDataValue()];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelLighter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */