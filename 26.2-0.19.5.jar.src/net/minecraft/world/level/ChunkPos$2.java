/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ class null extends Spliterators.AbstractSpliterator<ChunkPos> {
/*     */   private ChunkPos pos;
/*     */   
/*     */   null(long est, int additionalCharacteristics) {
/* 226 */     super(est, additionalCharacteristics);
/*     */   }
/*     */   
/*     */   public boolean tryAdvance(Consumer<? super ChunkPos> action) {
/* 231 */     if (this.pos == null) {
/* 232 */       this.pos = from;
/*     */     } else {
/* 234 */       int x = this.pos.x;
/* 235 */       int z = this.pos.z;
/* 236 */       if (x == to.x) {
/* 237 */         if (z == to.z)
/* 238 */           return false; 
/* 240 */         this.pos = new ChunkPos(from.x, z + zDiff);
/*     */       } else {
/* 242 */         this.pos = new ChunkPos(x + xDiff, z);
/*     */       } 
/*     */     } 
/* 245 */     action.accept(this.pos);
/* 246 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ChunkPos$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */