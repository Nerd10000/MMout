/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public enum Icons {
/* 119 */   MOVEMENT_KEYS(Identifier.withDefaultNamespace("toast/movement_keys")),
/* 120 */   MOUSE(Identifier.withDefaultNamespace("toast/mouse")),
/* 121 */   TREE(Identifier.withDefaultNamespace("toast/tree")),
/* 122 */   RECIPE_BOOK(Identifier.withDefaultNamespace("toast/recipe_book")),
/* 123 */   WOODEN_PLANKS(Identifier.withDefaultNamespace("toast/wooden_planks")),
/* 124 */   SOCIAL_INTERACTIONS(Identifier.withDefaultNamespace("toast/social_interactions")),
/* 125 */   RIGHT_CLICK(Identifier.withDefaultNamespace("toast/right_click"));
/*     */   
/*     */   private final Identifier sprite;
/*     */   
/*     */   Icons(Identifier sprite) {
/* 131 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int x, int y) {
/* 135 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, 20, 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\TutorialToast$Icons.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */