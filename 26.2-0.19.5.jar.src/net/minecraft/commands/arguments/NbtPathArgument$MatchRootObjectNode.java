/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class MatchRootObjectNode implements NbtPathArgument.Node {
/*     */   private final Predicate<Tag> predicate;
/*     */   
/*     */   public MatchRootObjectNode(CompoundTag pattern) {
/* 711 */     this.predicate = NbtPathArgument.createTagPredicate(pattern);
/*     */   }
/*     */   
/*     */   public void getTag(Tag self, List<Tag> output) {
/* 716 */     if (self instanceof CompoundTag && this.predicate.test(self))
/* 717 */       output.add(self); 
/*     */   }
/*     */   
/*     */   public void getOrCreateTag(Tag self, Supplier<Tag> child, List<Tag> output) {
/* 723 */     getTag(self, output);
/*     */   }
/*     */   
/*     */   public Tag createPreferredParentTag() {
/* 728 */     return (Tag)new CompoundTag();
/*     */   }
/*     */   
/*     */   public int setTag(Tag parent, Supplier<Tag> toAdd) {
/* 733 */     return 0;
/*     */   }
/*     */   
/*     */   public int removeTag(Tag parent) {
/* 738 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$MatchRootObjectNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */