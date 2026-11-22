/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ public class GameModeSlot extends AbstractWidget {
/*     */   private final GameModeSwitcherScreen.GameModeIcon icon;
/*     */   
/*     */   private boolean isSelected;
/*     */   
/*     */   public GameModeSlot(GameModeSwitcherScreen.GameModeIcon icon, int x, int y) {
/* 216 */     super(x, y, 26, 26, icon.name);
/* 217 */     this.icon = icon;
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 222 */     extractSlot(graphics);
/* 223 */     if (this.isSelected)
/* 224 */       extractSelection(graphics); 
/* 226 */     this.icon.extractIcon(graphics, getX() + 5, getY() + 5);
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 231 */     defaultButtonNarrationText(output);
/*     */   }
/*     */   
/*     */   public boolean isHoveredOrFocused() {
/* 236 */     return (super.isHoveredOrFocused() || this.isSelected);
/*     */   }
/*     */   
/*     */   public void setSelected(boolean isSelected) {
/* 240 */     this.isSelected = isSelected;
/*     */   }
/*     */   
/*     */   private void extractSlot(GuiGraphicsExtractor graphics) {
/* 244 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SLOT_SPRITE, getX(), getY(), 26, 26);
/*     */   }
/*     */   
/*     */   private void extractSelection(GuiGraphicsExtractor graphics) {
/* 248 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, GameModeSwitcherScreen.SELECTION_SPRITE, getX(), getY(), 26, 26);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\GameModeSwitcherScreen$GameModeSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */