/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public interface Output {
/*     */   default void accept(ItemStack stack) {
/* 259 */     accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */   }
/*     */   
/*     */   default void accept(ItemLike item, CreativeModeTab.TabVisibility tabVisibility) {
/* 263 */     accept(new ItemStack(item), tabVisibility);
/*     */   }
/*     */   
/*     */   default void accept(ItemLike item) {
/* 267 */     accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */   }
/*     */   
/*     */   default void acceptAll(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility) {
/* 271 */     stacks.forEach(stack -> accept(stack, tabVisibility));
/*     */   }
/*     */   
/*     */   default void acceptAll(Collection<ItemStack> stacks) {
/* 275 */     acceptAll(stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*     */   }
/*     */   
/*     */   void accept(ItemStack paramItemStack, CreativeModeTab.TabVisibility paramTabVisibility);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CreativeModeTab$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */