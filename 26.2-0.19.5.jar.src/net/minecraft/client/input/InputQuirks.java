/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class InputQuirks {
/*  7 */   private static final boolean ON_OSX = (Util.getPlatform() == Util.OS.OSX);
/*    */   
/*  9 */   public static final boolean REPLACE_CTRL_KEY_WITH_CMD_KEY = ON_OSX;
/*    */   
/* 11 */   public static final int EDIT_SHORTCUT_KEY_MODIFIER = REPLACE_CTRL_KEY_WITH_CMD_KEY ? 8 : 2;
/*    */   
/* 14 */   public static final boolean SIMULATE_RIGHT_CLICK_WITH_LONG_LEFT_CLICK = ON_OSX;
/*    */   
/* 17 */   public static final boolean RESTORE_KEY_STATE_AFTER_MOUSE_GRAB = !ON_OSX;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\InputQuirks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */