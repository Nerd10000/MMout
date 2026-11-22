/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ public class LoadingDotsText {
/*  4 */   private static final String[] FRAMES = new String[] { "O o o", "o O o", "o o O", "o O o" };
/*    */   
/*    */   private static final long INTERVAL_MS = 300L;
/*    */   
/*    */   public static String get(long timeMs) {
/* 14 */     int index = (int)(timeMs / 300L % FRAMES.length);
/* 15 */     return FRAMES[index];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\LoadingDotsText.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */