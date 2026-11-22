/*     */ package net.minecraft.client.gui.screens.recipebook;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ 
/*     */ abstract class OverlayRecipeButton extends AbstractWidget {
/*     */   private final RecipeDisplayId recipe;
/*     */   
/*     */   private final boolean isCraftable;
/*     */   
/*     */   private final List<Pos> slots;
/*     */   
/*     */   public OverlayRecipeButton(int x, int y, RecipeDisplayId recipe, boolean isCraftable, List<Pos> slots) {
/* 260 */     super(x, y, 24, 24, CommonComponents.EMPTY);
/* 261 */     this.slots = slots;
/* 262 */     this.recipe = recipe;
/* 263 */     this.isCraftable = isCraftable;
/*     */   }
/*     */   
/*     */   protected static Pos createGridPos(int gridXPos, int gridYPos, List<ItemStack> itemStacks) {
/* 267 */     return new Pos(3 + gridXPos * 7, 3 + gridYPos * 7, itemStacks);
/*     */   }
/*     */   
/*     */   protected abstract Identifier getSprite(boolean paramBoolean);
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 274 */     defaultButtonNarrationText(output);
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 279 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getSprite(this.isCraftable), getX(), getY(), this.width, this.height);
/* 281 */     float gridPosX = (getX() + 2);
/* 282 */     float gridPosY = (getY() + 2);
/* 284 */     for (Pos pos : this.slots) {
/* 285 */       graphics.pose().pushMatrix();
/* 287 */       graphics.pose().translate(gridPosX + pos.x, gridPosY + pos.y);
/* 288 */       graphics.pose().scale(0.375F, 0.375F);
/* 289 */       graphics.pose().translate(-8.0F, -8.0F);
/* 291 */       graphics.item(pos.selectIngredient(OverlayRecipeComponent.this.slotSelectTime.currentIndex()), 0, 0);
/* 293 */       graphics.pose().popMatrix();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static final class Pos extends Record {
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private final List<ItemStack> ingredients;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #297	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/recipebook/OverlayRecipeComponent$OverlayRecipeButton$Pos;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int x() {
/* 297 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/* 297 */       return this.y;
/*     */     }
/*     */     
/*     */     public List<ItemStack> ingredients() {
/* 297 */       return this.ingredients;
/*     */     }
/*     */     
/*     */     public Pos(int x, int y, List<ItemStack> ingredients) {
/* 299 */       if (ingredients.isEmpty())
/* 300 */         throw new IllegalArgumentException("Ingredient list must be non-empty"); 
/*     */       this.x = x;
/*     */       this.y = y;
/*     */       this.ingredients = ingredients;
/*     */     }
/*     */     
/*     */     public ItemStack selectIngredient(int currentIndex) {
/* 305 */       return this.ingredients.get(currentIndex % this.ingredients.size());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\OverlayRecipeComponent$OverlayRecipeButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */