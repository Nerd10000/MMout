/*     */ package net.minecraft.client.gui.spectator;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ class CloseSpectatorItem implements SpectatorMenuItem {
/*     */   public void selectItem(SpectatorMenu menu) {
/* 134 */     menu.exit();
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 139 */     return SpectatorMenu.CLOSE_MENU_TEXT;
/*     */   }
/*     */   
/*     */   public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/* 144 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.CLOSE_SPRITE, 0, 0, 16, 16, ARGB.colorFromFloat(alpha, brightness, brightness, brightness));
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 149 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\SpectatorMenu$CloseSpectatorItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */