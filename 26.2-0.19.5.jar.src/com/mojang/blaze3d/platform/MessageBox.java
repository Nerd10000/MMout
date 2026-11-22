/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import org.lwjgl.util.tinyfd.TinyFileDialogs;
/*    */ 
/*    */ public class MessageBox {
/*    */   private static final String DEFAULT_TITLE = "Minecraft";
/*    */   
/*    */   public static final String TYPE_OK = "ok";
/*    */   
/*    */   public static final String TYPE_OK_CANCEL = "okcancel";
/*    */   
/*    */   public static final String TYPE_YES_NO = "yesno";
/*    */   
/*    */   public static final String TYPE_YES_NO_CANCEL = "yesnocancel";
/*    */   
/*    */   public static final String ICON_INFO = "info";
/*    */   
/*    */   public static final String ICON_WARNING = "warning";
/*    */   
/*    */   public static final String ICON_ERROR = "error";
/*    */   
/*    */   public static final String ICON_QUESTION = "question";
/*    */   
/*    */   public static final int BUTTON_CANCEL_OR_NO = 0;
/*    */   
/*    */   public static final int BUTTON_OK_OR_YES = 1;
/*    */   
/*    */   public static final int BUTTON_NO = 2;
/*    */   
/*    */   public static void error(String message) {
/* 25 */     TinyFileDialogs.tinyfd_messageBox("Minecraft", message, "ok", "error", 1);
/*    */   }
/*    */   
/*    */   public static boolean errorWithContinue(String message) {
/* 35 */     return (TinyFileDialogs.tinyfd_messageBox("Minecraft", message, "yesno", "error", 1) == 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\MessageBox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */