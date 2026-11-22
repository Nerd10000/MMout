/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class null extends ImageButton {
/*     */   null(int x, int y, int width, int height, WidgetSprites sprites, Button.OnPress onPress, Component message) {
/* 109 */     super(x, y, width, height, sprites, onPress, message);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 112 */     return PlayerEntry.this.getEntryNarationMessage(super.createNarrationMessage());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\PlayerEntry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */