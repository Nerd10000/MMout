/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ 
/*    */ public class TextCursorUtils {
/*    */   public static final int CURSOR_INSERT_WIDTH = 1;
/*    */   
/*    */   private static final String CURSOR_APPEND_CHARACTER = "_";
/*    */   
/*    */   private static final int CURSOR_BLINK_INTERVAL_MS = 300;
/*    */   
/*    */   public static void extractInsertCursor(GuiGraphicsExtractor graphics, int x, int y, int color, int lineHeight) {
/* 13 */     graphics.fill(x, y - 1, x + 1, y + lineHeight, color);
/*    */   }
/*    */   
/*    */   public static void extractAppendCursor(GuiGraphicsExtractor graphics, Font font, int x, int y, int color, boolean shadow) {
/* 17 */     graphics.text(font, "_", x, y, color, shadow);
/*    */   }
/*    */   
/*    */   public static boolean isCursorVisible(long timeInMs) {
/* 21 */     return (timeInMs / 300L % 2L == 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\TextCursorUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */