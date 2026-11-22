/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class Pos extends Record {
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private final List<ItemStack> ingredients;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int x() {
/* 297 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/* 297 */     return this.y;
/*     */   }
/*     */   
/*     */   public List<ItemStack> ingredients() {
/* 297 */     return this.ingredients;
/*     */   }
/*     */   
/*     */   public Pos(int x, int y, List<ItemStack> ingredients) {
/* 299 */     if (ingredients.isEmpty())
/* 300 */       throw new IllegalArgumentException("Ingredient list must be non-empty"); 
/*     */     this.x = x;
/*     */     this.y = y;
/*     */     this.ingredients = ingredients;
/*     */   }
/*     */   
/*     */   public ItemStack selectIngredient(int currentIndex) {
/* 305 */     return this.ingredients.get(currentIndex % this.ingredients.size());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\OverlayRecipeComponent$OverlayRecipeButton$Pos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */