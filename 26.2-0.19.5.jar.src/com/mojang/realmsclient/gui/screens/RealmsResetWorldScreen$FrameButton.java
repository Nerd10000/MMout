/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ class FrameButton extends Button {
/* 204 */   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*     */   
/*     */   private static final int FRAME_SIZE = 60;
/*     */   
/*     */   private static final int FRAME_WIDTH = 2;
/*     */   
/*     */   private static final int IMAGE_SIZE = 56;
/*     */   
/*     */   private final Identifier image;
/*     */   
/*     */   private FrameButton(Font font, Component text, Identifier image, Button.OnPress onPress) {
/* 211 */     super(0, 0, 60, 60 + 9, text, onPress, DEFAULT_NARRATION);
/* 212 */     this.image = image;
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 217 */     boolean hoveredOrFocused = isHoveredOrFocused();
/* 218 */     int color = -1;
/* 219 */     if (hoveredOrFocused)
/* 220 */       color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F); 
/* 222 */     int x = getX();
/* 223 */     int y = getY();
/* 224 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.image, x + 2, y + 2, 0.0F, 0.0F, 56, 56, 56, 56, 56, 56, color);
/* 225 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, 60, 60, color);
/* 227 */     int textColor = hoveredOrFocused ? -6250336 : -1;
/* 228 */     graphics.centeredText(RealmsResetWorldScreen.access$100(RealmsResetWorldScreen.this), getMessage(), x + 28, y - 14, textColor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsResetWorldScreen$FrameButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */