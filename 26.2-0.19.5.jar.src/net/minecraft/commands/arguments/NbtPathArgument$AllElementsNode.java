/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class AllElementsNode implements NbtPathArgument.Node {
/* 568 */   public static final AllElementsNode INSTANCE = new AllElementsNode();
/*     */   
/*     */   public void getTag(Tag parent, List<Tag> output) {
/* 572 */     if (parent instanceof CollectionTag) {
/* 572 */       CollectionTag collection = (CollectionTag)parent;
/* 573 */       Iterables.addAll(output, (Iterable)collection);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getOrCreateTag(Tag parent, Supplier<Tag> child, List<Tag> output) {
/* 579 */     if (parent instanceof CollectionTag) {
/* 579 */       CollectionTag list = (CollectionTag)parent;
/* 580 */       if (list.isEmpty()) {
/* 581 */         Tag result = child.get();
/* 582 */         if (list.addTag(0, result))
/* 583 */           output.add(result); 
/*     */       } else {
/* 586 */         Iterables.addAll(output, (Iterable)list);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag createPreferredParentTag() {
/* 593 */     return (Tag)new ListTag();
/*     */   }
/*     */   
/*     */   public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 598 */     if (parent instanceof CollectionTag) {
/* 598 */       CollectionTag list = (CollectionTag)parent;
/* 599 */       int size = list.size();
/* 600 */       if (size == 0) {
/* 601 */         list.addTag(0, toAdd.get());
/* 602 */         return 1;
/*     */       } 
/* 604 */       Tag newValue = toAdd.get();
/* 605 */       Objects.requireNonNull(newValue);
/* 605 */       int changedCount = size - (int)list.stream().filter(newValue::equals).count();
/* 606 */       if (changedCount == 0)
/* 607 */         return 0; 
/* 609 */       list.clear();
/* 610 */       if (!list.addTag(0, newValue))
/* 611 */         return 0; 
/* 613 */       for (int i = 1; i < size; i++)
/* 614 */         list.addTag(i, toAdd.get()); 
/* 617 */       return changedCount;
/*     */     } 
/* 620 */     return 0;
/*     */   }
/*     */   
/*     */   public int removeTag(Tag parent) {
/* 625 */     if (parent instanceof CollectionTag) {
/* 625 */       CollectionTag list = (CollectionTag)parent;
/* 626 */       int size = list.size();
/* 627 */       if (size > 0) {
/* 628 */         list.clear();
/* 629 */         return size;
/*     */       } 
/*     */     } 
/* 633 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$AllElementsNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */