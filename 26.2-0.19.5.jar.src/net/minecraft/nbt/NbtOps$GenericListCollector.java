/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ 
/*     */ class GenericListCollector implements NbtOps.ListCollector {
/* 445 */   private final ListTag result = new ListTag();
/*     */   
/*     */   private GenericListCollector(ListTag initial) {
/* 451 */     this.result.addAll(initial);
/*     */   }
/*     */   
/*     */   public GenericListCollector(IntArrayList initials) {
/* 455 */     initials.forEach(v -> this.result.add(IntTag.valueOf(v)));
/*     */   }
/*     */   
/*     */   public GenericListCollector(ByteArrayList initials) {
/* 459 */     initials.forEach(v -> this.result.add(ByteTag.valueOf(v)));
/*     */   }
/*     */   
/*     */   public GenericListCollector(LongArrayList initials) {
/* 463 */     initials.forEach(v -> this.result.add(LongTag.valueOf(v)));
/*     */   }
/*     */   
/*     */   public NbtOps.ListCollector accept(Tag tag) {
/* 468 */     this.result.add(tag);
/* 469 */     return this;
/*     */   }
/*     */   
/*     */   public Tag result() {
/* 474 */     return this.result;
/*     */   }
/*     */   
/*     */   private GenericListCollector() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$GenericListCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */