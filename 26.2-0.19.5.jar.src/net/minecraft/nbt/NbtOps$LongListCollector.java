/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ 
/*     */ class LongListCollector implements NbtOps.ListCollector {
/* 523 */   private final LongArrayList values = new LongArrayList();
/*     */   
/*     */   public LongListCollector(long[] initialValues) {
/* 526 */     this.values.addElements(0, initialValues);
/*     */   }
/*     */   
/*     */   public NbtOps.ListCollector accept(Tag tag) {
/* 531 */     if (tag instanceof LongTag) {
/* 531 */       LongTag longTag = (LongTag)tag;
/* 532 */       this.values.add(longTag.longValue());
/* 533 */       return this;
/*     */     } 
/* 535 */     return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */   }
/*     */   
/*     */   public Tag result() {
/* 540 */     return new LongArrayTag(this.values.toLongArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$LongListCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */