/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractButton;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ abstract class BeaconScreenButton extends AbstractButton implements BeaconScreen.BeaconButton {
/*     */   private boolean selected;
/*     */   
/*     */   protected BeaconScreenButton(int x, int y) {
/* 146 */     super(x, y, 22, 22, CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   protected BeaconScreenButton(int x, int y, Component component) {
/* 150 */     super(x, y, 22, 22, component);
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier sprite;
/* 156 */     if (!this.active) {
/* 157 */       sprite = BeaconScreen.BUTTON_DISABLED_SPRITE;
/* 158 */     } else if (this.selected) {
/* 159 */       sprite = BeaconScreen.BUTTON_SELECTED_SPRITE;
/* 160 */     } else if (isHoveredOrFocused()) {
/* 161 */       sprite = BeaconScreen.BUTTON_HIGHLIGHTED_SPRITE;
/*     */     } else {
/* 163 */       sprite = BeaconScreen.BUTTON_SPRITE;
/*     */     } 
/* 166 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/* 167 */     extractIcon(graphics);
/*     */   }
/*     */   
/*     */   protected abstract void extractIcon(GuiGraphicsExtractor paramGuiGraphicsExtractor);
/*     */   
/*     */   public boolean isSelected() {
/* 173 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void setSelected(boolean selected) {
/* 177 */     this.selected = selected;
/*     */   }
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 182 */     defaultButtonNarrationText(output);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconScreenButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */