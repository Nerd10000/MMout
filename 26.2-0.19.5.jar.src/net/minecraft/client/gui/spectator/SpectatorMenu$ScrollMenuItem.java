/*     */ package net.minecraft.client.gui.spectator;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ class ScrollMenuItem implements SpectatorMenuItem {
/*     */   private final int direction;
/*     */   
/*     */   private final boolean enabled;
/*     */   
/*     */   public ScrollMenuItem(int direction, boolean enabled) {
/* 158 */     this.direction = direction;
/* 159 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public void selectItem(SpectatorMenu menu) {
/* 164 */     menu.page += this.direction;
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 169 */     return (this.direction < 0) ? SpectatorMenu.PREVIOUS_PAGE_TEXT : SpectatorMenu.NEXT_PAGE_TEXT;
/*     */   }
/*     */   
/*     */   public void extractIcon(GuiGraphicsExtractor graphics, float brightness, float alpha) {
/* 174 */     int color = ARGB.colorFromFloat(alpha, brightness, brightness, brightness);
/* 175 */     if (this.direction < 0) {
/* 176 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_LEFT_SPRITE, 0, 0, 16, 16, color);
/*     */     } else {
/* 178 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SpectatorMenu.SCROLL_RIGHT_SPRITE, 0, 0, 16, 16, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 184 */     return this.enabled;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\SpectatorMenu$ScrollMenuItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */