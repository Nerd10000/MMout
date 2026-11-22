/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ 
/*    */ public class StringWidget extends AbstractStringWidget {
/*    */   private static final int TEXT_MARGIN = 2;
/*    */   
/* 10 */   private int maxWidth = 0;
/*    */   
/* 11 */   private int cachedWidth = 0;
/*    */   
/*    */   private boolean cachedWidthDirty = true;
/*    */   
/* 14 */   private TextOverflow textOverflow = TextOverflow.CLAMPED;
/*    */   
/*    */   public StringWidget(Component message, Font font) {
/* 17 */     this(0, 0, font.width(message.getVisualOrderText()), 9, message, font);
/*    */   }
/*    */   
/*    */   public StringWidget(int width, int height, Component message, Font font) {
/* 21 */     this(0, 0, width, height, message, font);
/*    */   }
/*    */   
/*    */   public StringWidget(int x, int y, int width, int height, Component message, Font font) {
/* 25 */     super(x, y, width, height, message, font);
/* 26 */     this.active = false;
/*    */   }
/*    */   
/*    */   public void setMessage(Component message) {
/* 31 */     super.setMessage(message);
/* 32 */     this.cachedWidthDirty = true;
/*    */   }
/*    */   
/*    */   public StringWidget setMaxWidth(int maxWidth) {
/* 36 */     return setMaxWidth(maxWidth, TextOverflow.CLAMPED);
/*    */   }
/*    */   
/*    */   public StringWidget setMaxWidth(int maxWidth, TextOverflow textOverflow) {
/* 40 */     this.maxWidth = maxWidth;
/* 41 */     this.textOverflow = textOverflow;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 47 */     if (this.maxWidth > 0) {
/* 48 */       if (this.cachedWidthDirty) {
/* 49 */         this.cachedWidth = Math.min(this.maxWidth, getFont().width(getMessage().getVisualOrderText()));
/* 50 */         this.cachedWidthDirty = false;
/*    */       } 
/* 52 */       return this.cachedWidth;
/*    */     } 
/* 54 */     return super.getWidth();
/*    */   }
/*    */   
/*    */   public void visitLines(ActiveTextCollector output) {
/* 59 */     Component message = getMessage();
/* 60 */     Font font = getFont();
/* 61 */     int maxWidth = (this.maxWidth > 0) ? this.maxWidth : getWidth();
/* 62 */     int textWidth = font.width((FormattedText)message);
/* 63 */     int x = getX();
/* 64 */     Objects.requireNonNull(font);
/* 64 */     int y = getY() + (getHeight() - 9) / 2;
/* 66 */     boolean textOverflow = (textWidth > maxWidth);
/* 67 */     if (textOverflow) {
/* 68 */       switch (this.textOverflow.ordinal()) {
/*    */         case 0:
/* 69 */           output.accept(x, y, ComponentRenderUtils.clipText(message, font, maxWidth));
/*    */           break;
/*    */         case 1:
/* 70 */           extractScrollingStringOverContents(output, message, 2);
/*    */           break;
/*    */       } 
/*    */     } else {
/* 73 */       output.accept(x, y, message.getVisualOrderText());
/*    */     } 
/*    */   }
/*    */   
/*    */   public enum TextOverflow {
/* 78 */     CLAMPED, SCROLLING;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\StringWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */