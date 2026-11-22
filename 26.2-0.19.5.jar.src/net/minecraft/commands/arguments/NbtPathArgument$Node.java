/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ interface Node {
/*     */   default List<Tag> get(List<Tag> tags) {
/* 342 */     return collect(tags, this::getTag);
/*     */   }
/*     */   
/*     */   default List<Tag> getOrCreate(List<Tag> tags, Supplier<Tag> child) {
/* 346 */     return collect(tags, (tag, output) -> getOrCreateTag(tag, child, output));
/*     */   }
/*     */   
/*     */   default List<Tag> collect(List<Tag> tags, BiConsumer<Tag, List<Tag>> collector) {
/* 350 */     List<Tag> result = Lists.newArrayList();
/* 352 */     for (Tag tag : tags)
/* 353 */       collector.accept(tag, result); 
/* 356 */     return result;
/*     */   }
/*     */   
/*     */   void getTag(Tag paramTag, List<Tag> paramList);
/*     */   
/*     */   void getOrCreateTag(Tag paramTag, Supplier<Tag> paramSupplier, List<Tag> paramList);
/*     */   
/*     */   Tag createPreferredParentTag();
/*     */   
/*     */   int setTag(Tag paramTag, Supplier<Tag> paramSupplier);
/*     */   
/*     */   int removeTag(Tag paramTag);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */