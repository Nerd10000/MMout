/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ public enum Occupancy {
/* 251 */   HAS_SPACE(PoiRecord::hasSpace),
/* 252 */   IS_OCCUPIED(PoiRecord::isOccupied),
/* 253 */   ANY(poiRecord -> true);
/*     */   
/*     */   private final Predicate<? super PoiRecord> test;
/*     */   
/*     */   Occupancy(Predicate<? super PoiRecord> test) {
/* 258 */     this.test = test;
/*     */   }
/*     */   
/*     */   public Predicate<? super PoiRecord> getTest() {
/* 262 */     return this.test;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiManager$Occupancy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */