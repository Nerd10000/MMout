/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ class null extends Spliterators.AbstractSpliterator<SectionPos> {
/*     */   private final Cursor3D cursor;
/*     */   
/*     */   null(long est, int additionalCharacteristics) {
/* 280 */     super(est, additionalCharacteristics);
/* 281 */     this.cursor = new Cursor3D(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public boolean tryAdvance(Consumer<? super SectionPos> action) {
/* 285 */     if (this.cursor.advance()) {
/* 286 */       action.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
/* 287 */       return true;
/*     */     } 
/* 289 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\SectionPos$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */