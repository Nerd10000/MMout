/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class MatchObjectNode implements NbtPathArgument.Node {
/*     */   private final String name;
/*     */   
/*     */   private final CompoundTag pattern;
/*     */   
/*     */   private final Predicate<Tag> predicate;
/*     */   
/*     */   public MatchObjectNode(String name, CompoundTag pattern) {
/* 643 */     this.name = name;
/* 644 */     this.pattern = pattern;
/* 645 */     this.predicate = NbtPathArgument.createTagPredicate(pattern);
/*     */   }
/*     */   
/*     */   public void getTag(Tag parent, List<Tag> output) {
/* 650 */     if (parent instanceof CompoundTag) {
/* 650 */       CompoundTag compoundTag = (CompoundTag)parent;
/* 651 */       Tag result = compoundTag.get(this.name);
/* 652 */       if (this.predicate.test(result))
/* 653 */         output.add(result); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 660 */     if (parent instanceof CompoundTag) {
/* 660 */       CompoundTag compoundTag1, compound = (CompoundTag)parent;
/* 661 */       Tag result = compound.get(this.name);
/* 662 */       if (result == null) {
/* 663 */         compoundTag1 = this.pattern.copy();
/* 664 */         compound.put(this.name, (Tag)compoundTag1);
/* 665 */         output.add(compoundTag1);
/* 666 */       } else if (this.predicate.test(compoundTag1)) {
/* 667 */         output.add(compoundTag1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag createPreferredParentTag() {
/* 674 */     return (Tag)new CompoundTag();
/*     */   }
/*     */   
/*     */   public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 679 */     if (parent instanceof CompoundTag) {
/* 679 */       CompoundTag compound = (CompoundTag)parent;
/* 680 */       Tag currentValue = compound.get(this.name);
/* 681 */       if (this.predicate.test(currentValue)) {
/* 682 */         Tag newValue = toAdd.get();
/* 683 */         if (!newValue.equals(currentValue)) {
/* 684 */           compound.put(this.name, newValue);
/* 685 */           return 1;
/*     */         } 
/*     */       } 
/*     */     } 
/* 690 */     return 0;
/*     */   }
/*     */   
/*     */   public int removeTag(Tag parent) {
/* 695 */     if (parent instanceof CompoundTag) {
/* 695 */       CompoundTag compound = (CompoundTag)parent;
/* 696 */       Tag current = compound.get(this.name);
/* 697 */       if (this.predicate.test(current)) {
/* 698 */         compound.remove(this.name);
/* 699 */         return 1;
/*     */       } 
/*     */     } 
/* 703 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$MatchObjectNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */