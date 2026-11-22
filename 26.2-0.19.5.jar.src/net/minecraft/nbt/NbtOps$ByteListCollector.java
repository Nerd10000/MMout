/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ 
/*     */ class ByteListCollector implements NbtOps.ListCollector {
/* 501 */   private final ByteArrayList values = new ByteArrayList();
/*     */   
/*     */   public ByteListCollector(byte[] initialValues) {
/* 504 */     this.values.addElements(0, initialValues);
/*     */   }
/*     */   
/*     */   public NbtOps.ListCollector accept(Tag tag) {
/* 509 */     if (tag instanceof ByteTag) {
/* 509 */       ByteTag byteTag = (ByteTag)tag;
/* 510 */       this.values.add(byteTag.byteValue());
/* 511 */       return this;
/*     */     } 
/* 513 */     return (new NbtOps.GenericListCollector(this.values)).accept(tag);
/*     */   }
/*     */   
/*     */   public Tag result() {
/* 518 */     return new ByteArrayTag(this.values.toByteArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$ByteListCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */