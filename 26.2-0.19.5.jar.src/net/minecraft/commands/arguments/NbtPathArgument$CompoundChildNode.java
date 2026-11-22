/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class CompoundChildNode implements NbtPathArgument.Node {
/*     */   private final String name;
/*     */   
/*     */   public CompoundChildNode(String name) {
/* 364 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void getTag(Tag parent, List<Tag> output) {
/* 369 */     if (parent instanceof CompoundTag) {
/* 369 */       CompoundTag compoundTag = (CompoundTag)parent;
/* 370 */       Tag result = compoundTag.get(this.name);
/* 371 */       if (result != null)
/* 372 */         output.add(result); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 379 */     if (parent instanceof CompoundTag) {
/*     */       Tag result;
/* 379 */       CompoundTag compound = (CompoundTag)parent;
/* 381 */       if (compound.contains(this.name)) {
/* 382 */         result = compound.get(this.name);
/*     */       } else {
/* 384 */         result = child.get();
/* 385 */         compound.put(this.name, result);
/*     */       } 
/* 388 */       output.add(result);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag createPreferredParentTag() {
/* 394 */     return (Tag)new CompoundTag();
/*     */   }
/*     */   
/*     */   public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 399 */     if (parent instanceof CompoundTag) {
/* 399 */       CompoundTag compound = (CompoundTag)parent;
/* 400 */       Tag newValue = toAdd.get();
/* 401 */       Tag previousValue = compound.put(this.name, newValue);
/* 402 */       if (!newValue.equals(previousValue))
/* 403 */         return 1; 
/*     */     } 
/* 407 */     return 0;
/*     */   }
/*     */   
/*     */   public int removeTag(Tag parent) {
/* 412 */     if (parent instanceof CompoundTag) {
/* 412 */       CompoundTag compound = (CompoundTag)parent;
/* 413 */       if (compound.contains(this.name)) {
/* 414 */         compound.remove(this.name);
/* 415 */         return 1;
/*     */       } 
/*     */     } 
/* 419 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$CompoundChildNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */