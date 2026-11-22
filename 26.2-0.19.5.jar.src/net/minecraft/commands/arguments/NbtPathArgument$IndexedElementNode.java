/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class IndexedElementNode implements NbtPathArgument.Node {
/*     */   private final int index;
/*     */   
/*     */   public IndexedElementNode(int index) {
/* 427 */     this.index = index;
/*     */   }
/*     */   
/*     */   public void getTag(Tag parent, List<Tag> output) {
/* 432 */     if (parent instanceof CollectionTag) {
/* 432 */       CollectionTag list = (CollectionTag)parent;
/* 433 */       int size = list.size();
/* 434 */       int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/* 436 */       if (0 <= actualIndex && actualIndex < size)
/* 437 */         output.add(list.get(actualIndex)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 444 */     getTag(parent, output);
/*     */   }
/*     */   
/*     */   public Tag createPreferredParentTag() {
/* 449 */     return (Tag)new ListTag();
/*     */   }
/*     */   
/*     */   public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 454 */     if (parent instanceof CollectionTag) {
/* 454 */       CollectionTag list = (CollectionTag)parent;
/* 455 */       int size = list.size();
/* 456 */       int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/* 458 */       if (0 <= actualIndex && actualIndex < size) {
/* 459 */         Tag previousValue = list.get(actualIndex);
/* 460 */         Tag newValue = toAdd.get();
/* 461 */         if (!newValue.equals(previousValue) && list.setTag(actualIndex, newValue))
/* 462 */           return 1; 
/*     */       } 
/*     */     } 
/* 467 */     return 0;
/*     */   }
/*     */   
/*     */   public int removeTag(Tag parent) {
/* 472 */     if (parent instanceof CollectionTag) {
/* 472 */       CollectionTag list = (CollectionTag)parent;
/* 473 */       int size = list.size();
/* 474 */       int actualIndex = (this.index < 0) ? (size + this.index) : this.index;
/* 476 */       if (0 <= actualIndex && actualIndex < size) {
/* 477 */         list.remove(actualIndex);
/* 478 */         return 1;
/*     */       } 
/*     */     } 
/* 482 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$IndexedElementNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */