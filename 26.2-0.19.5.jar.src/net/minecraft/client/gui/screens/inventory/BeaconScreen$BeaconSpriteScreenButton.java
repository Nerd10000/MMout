/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ abstract class BeaconSpriteScreenButton extends BeaconScreen.BeaconScreenButton {
/*     */   private final Identifier sprite;
/*     */   
/*     */   protected BeaconSpriteScreenButton(int x, int y, Identifier sprite, Component label) {
/* 269 */     super(x, y, label);
/* 270 */     setTooltip(Tooltip.create(label));
/* 271 */     this.sprite = sprite;
/*     */   }
/*     */   
/*     */   protected void extractIcon(GuiGraphicsExtractor graphics) {
/* 276 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconSpriteScreenButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */