/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ class ItemDisplayBuilder implements CreativeModeTab.Output {
/* 210 */   public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   
/* 211 */   public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndComponentsSet();
/*     */   
/*     */   private final CreativeModeTab tab;
/*     */   
/*     */   private final FeatureFlagSet featureFlagSet;
/*     */   
/*     */   public ItemDisplayBuilder(CreativeModeTab tab, FeatureFlagSet featureFlagSet) {
/* 216 */     this.tab = tab;
/* 217 */     this.featureFlagSet = featureFlagSet;
/*     */   }
/*     */   
/*     */   public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
/* 222 */     if (stack.getCount() != 1)
/* 223 */       throw new IllegalArgumentException("Stack size must be exactly 1"); 
/* 227 */     boolean foundDuplicateStack = (this.tabContents.contains(stack) && tabVisibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
/* 229 */     if (foundDuplicateStack)
/* 230 */       throw new IllegalStateException("Accidentally adding the same item stack twice " + stack
/* 231 */           .getDisplayName().getString() + " to a Creative Mode Tab: " + this.tab
/*     */           
/* 233 */           .getDisplayName().getString()); 
/* 236 */     if (stack.getItem().isEnabled(this.featureFlagSet))
/* 237 */       switch (tabVisibility.ordinal()) {
/*     */         case 0:
/* 239 */           this.tabContents.add(stack);
/* 240 */           this.searchTabContents.add(stack);
/*     */           break;
/*     */         case 1:
/* 242 */           this.tabContents.add(stack);
/*     */           break;
/*     */         case 2:
/* 243 */           this.searchTabContents.add(stack);
/*     */           break;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CreativeModeTab$ItemDisplayBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */