/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class TitleConfirmScreen extends ConfirmScreen {
/*     */   public TitleConfirmScreen(BooleanConsumer callback, Component title, Component message, Component yesButton, Component noButton) {
/* 168 */     super(callback, title, message, yesButton, noButton);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 173 */     DeathScreen.extractDeathBackground(graphics, this.width, this.height);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\DeathScreen$TitleConfirmScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */