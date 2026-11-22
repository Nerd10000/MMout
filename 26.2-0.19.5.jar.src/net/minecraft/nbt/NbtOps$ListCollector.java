/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ interface ListCollector {
/*     */   ListCollector accept(Tag paramTag);
/*     */   
/*     */   default ListCollector acceptAll(Iterable<Tag> tags) {
/* 413 */     ListCollector collector = this;
/* 414 */     for (Tag tag : tags)
/* 415 */       collector = collector.accept(tag); 
/* 417 */     return collector;
/*     */   }
/*     */   
/*     */   Tag result();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$ListCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */