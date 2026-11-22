/*    */ package net.minecraft.client.gui.contextualbar;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Window;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public interface ContextualBar {
/*    */   public static final int WIDTH = 182;
/*    */   
/*    */   public static final int HEIGHT = 5;
/*    */   
/*    */   public static final int MARGIN_BOTTOM = 24;
/*    */   
/* 15 */   public static final ContextualBar EMPTY = new ContextualBar() {
/*    */       public void extractBackground(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {}
/*    */       
/*    */       public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {}
/*    */     };
/*    */   
/*    */   default int left(Window window) {
/* 24 */     return (window.getGuiScaledWidth() - 182) / 2;
/*    */   }
/*    */   
/*    */   default int top(Window window) {
/* 28 */     return window.getGuiScaledHeight() - 24 - 5;
/*    */   }
/*    */   
/*    */   void extractBackground(GuiGraphicsExtractor paramGuiGraphicsExtractor, DeltaTracker paramDeltaTracker);
/*    */   
/*    */   void extractRenderState(GuiGraphicsExtractor paramGuiGraphicsExtractor, DeltaTracker paramDeltaTracker);
/*    */   
/*    */   static void extractExperienceLevel(GuiGraphicsExtractor graphics, Font font, int experienceLevel) {
/* 35 */     MutableComponent mutableComponent = Component.translatable("gui.experience.level", new Object[] { Integer.valueOf(experienceLevel) });
/* 36 */     int x = (graphics.guiWidth() - font.width((FormattedText)mutableComponent)) / 2;
/* 37 */     Objects.requireNonNull(font);
/* 37 */     int y = graphics.guiHeight() - 24 - 9 - 2;
/* 39 */     graphics.text(font, (Component)mutableComponent, x + 1, y, -16777216, false);
/* 40 */     graphics.text(font, (Component)mutableComponent, x - 1, y, -16777216, false);
/* 41 */     graphics.text(font, (Component)mutableComponent, x, y + 1, -16777216, false);
/* 42 */     graphics.text(font, (Component)mutableComponent, x, y - 1, -16777216, false);
/* 43 */     graphics.text(font, (Component)mutableComponent, x, y, -8323296, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\contextualbar\ContextualBar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */