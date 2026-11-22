/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.gui.screens.LoadingDotsText;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class LoadingDotsWidget extends AbstractWidget {
/*    */   private static final int Y_PADDING = 2;
/*    */   
/*    */   private final Font font;
/*    */   
/*    */   public LoadingDotsWidget(Font font, Component message) {
/* 21 */     super(0, 0, font.width((FormattedText)message), 2 + 9 + 6 + 9 + 2, message);
/* 22 */     this.font = font;
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 27 */     int centerX = getX() + getWidth() / 2;
/* 29 */     Component message = getMessage();
/* 30 */     graphics.text(this.font, message, centerX - this.font.width((FormattedText)message) / 2, getY() + 2, -1);
/* 32 */     String dots = LoadingDotsText.get(Util.getMillis());
/* 33 */     Objects.requireNonNull(this.font);
/* 33 */     graphics.text(this.font, dots, centerX - this.font.width(dots) / 2, getBottom() - 9 - 2, -8355712);
/*    */   }
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */   
/*    */   public boolean isActive() {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\LoadingDotsWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */