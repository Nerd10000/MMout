/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Cache {
/*     */   private boolean enabled;
/*     */   
/*     */   private final Long2IntLinkedOpenHashMap colorCache;
/*     */   
/*     */   private final Long2FloatLinkedOpenHashMap brightnessCache;
/*     */   
/*     */   private final LightCoordsUtil.BrightnessGetter cachedBrightnessGetter;
/*     */   
/*     */   public Cache() {
/* 277 */     this.colorCache = (Long2IntLinkedOpenHashMap)Util.make(() -> {
/*     */           Long2IntLinkedOpenHashMap map = new Long2IntLinkedOpenHashMap(100, 0.25F) {
/*     */               protected void rehash(int newN) {}
/*     */             };
/*     */           map.defaultReturnValue(2147483647);
/*     */           return map;
/*     */         });
/* 287 */     this.brightnessCache = (Long2FloatLinkedOpenHashMap)Util.make(() -> {
/*     */           Long2FloatLinkedOpenHashMap map = new Long2FloatLinkedOpenHashMap(100, 0.25F) {
/*     */               protected void rehash(int newN) {}
/*     */             };
/*     */           map.defaultReturnValue(Float.NaN);
/*     */           return map;
/*     */         });
/* 297 */     this.cachedBrightnessGetter = ((level, pos) -> {
/*     */         long key = pos.asLong();
/*     */         int cached = this.colorCache.get(key);
/*     */         if (cached != Integer.MAX_VALUE)
/*     */           return cached; 
/*     */         int value = LightCoordsUtil.BrightnessGetter.DEFAULT.packedBrightness(level, pos);
/*     */         if (this.colorCache.size() == 100)
/*     */           this.colorCache.removeFirstInt(); 
/*     */         this.colorCache.put(key, value);
/*     */         return value;
/*     */       });
/*     */   }
/*     */   
/*     */   public void enable() {
/* 314 */     this.enabled = true;
/*     */   }
/*     */   
/*     */   public void disable() {
/* 318 */     this.enabled = false;
/* 319 */     this.colorCache.clear();
/* 320 */     this.brightnessCache.clear();
/*     */   }
/*     */   
/*     */   public int getLightCoords(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 324 */     return LightCoordsUtil.getLightCoords(this.enabled ? this.cachedBrightnessGetter : LightCoordsUtil.BrightnessGetter.DEFAULT, level, state, pos);
/*     */   }
/*     */   
/*     */   public float getShadeBrightness(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 328 */     long key = pos.asLong();
/* 329 */     if (this.enabled) {
/* 330 */       float cached = this.brightnessCache.get(key);
/* 331 */       if (!Float.isNaN(cached))
/* 332 */         return cached; 
/*     */     } 
/* 336 */     float brightness = state.getShadeBrightness((BlockGetter)level, pos);
/* 337 */     if (this.enabled) {
/* 338 */       if (this.brightnessCache.size() == 100)
/* 339 */         this.brightnessCache.removeFirstFloat(); 
/* 341 */       this.brightnessCache.put(key, brightness);
/*     */     } 
/* 343 */     return brightness;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelLighter$Cache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */