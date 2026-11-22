/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ 
/*     */ class IntListCollector implements NbtOps.ListCollector {
/* 479 */   private final IntArrayList values = new IntArrayList();
/*     */   
/*     */   public IntListCollector(int[] initialValues) {
/* 482 */     this.values.addElements(0, initialValues);
/*     */   }
/*     */   
/*     */   public NbtOps.ListCollector accept(Tag tag) {
/* 487 */     if (tag instanceof IntTag) {
/* 487 */       IntTag intTag = (IntTag)tag;
/* 488 */       this.values.add(intTag.intValue());
/* 489 */       return this;
/*     */     } 
/* 491 */     return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */   }
/*     */   
/*     */   public Tag result() {
/* 496 */     return new IntArrayTag(this.values.toIntArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$IntListCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */