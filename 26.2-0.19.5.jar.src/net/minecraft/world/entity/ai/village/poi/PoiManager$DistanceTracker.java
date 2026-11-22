/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import net.minecraft.server.level.SectionTracker;
/*     */ 
/*     */ final class DistanceTracker extends SectionTracker {
/*     */   private final Long2ByteMap levels;
/*     */   
/*     */   DistanceTracker() {
/* 271 */     super(7, 16, 256);
/* 272 */     this.levels = (Long2ByteMap)new Long2ByteOpenHashMap();
/* 273 */     this.levels.defaultReturnValue((byte)7);
/*     */   }
/*     */   
/*     */   protected int getLevelFromSource(long to) {
/* 278 */     return PoiManager.this.isVillageCenter(to) ? 0 : 7;
/*     */   }
/*     */   
/*     */   protected int getLevel(long node) {
/* 283 */     return this.levels.get(node);
/*     */   }
/*     */   
/*     */   protected void setLevel(long node, int level) {
/* 288 */     if (level > 6) {
/* 289 */       this.levels.remove(node);
/*     */     } else {
/* 291 */       this.levels.put(node, (byte)level);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void runAllUpdates() {
/* 296 */     runUpdates(2147483647);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiManager$DistanceTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */