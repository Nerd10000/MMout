/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*     */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public final class TabInfo extends Record {
/*     */   private final ItemStack primaryIcon;
/*     */   
/*     */   private final Optional<ItemStack> secondaryIcon;
/*     */   
/*     */   private final ExtendedRecipeBookCategory category;
/*     */   
/*     */   public TabInfo(ItemStack primaryIcon, Optional<ItemStack> secondaryIcon, ExtendedRecipeBookCategory category) {
/* 659 */     this.primaryIcon = primaryIcon;
/* 659 */     this.secondaryIcon = secondaryIcon;
/* 659 */     this.category = category;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #659	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #659	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #659	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent$TabInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ItemStack primaryIcon() {
/* 659 */     return this.primaryIcon;
/*     */   }
/*     */   
/*     */   public Optional<ItemStack> secondaryIcon() {
/* 659 */     return this.secondaryIcon;
/*     */   }
/*     */   
/*     */   public ExtendedRecipeBookCategory category() {
/* 659 */     return this.category;
/*     */   }
/*     */   
/*     */   public TabInfo(SearchRecipeBookCategory category) {
/* 666 */     this(new ItemStack((ItemLike)Items.COMPASS), Optional.empty(), category);
/*     */   }
/*     */   
/*     */   public TabInfo(Item icon, RecipeBookCategory category) {
/* 670 */     this(new ItemStack((ItemLike)icon), Optional.empty(), (ExtendedRecipeBookCategory)category);
/*     */   }
/*     */   
/*     */   public TabInfo(Item primaryIcon, Item secondaryIcon, RecipeBookCategory category) {
/* 674 */     this(new ItemStack((ItemLike)primaryIcon), Optional.of(new ItemStack((ItemLike)secondaryIcon)), (ExtendedRecipeBookCategory)category);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\RecipeBookComponent$TabInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */