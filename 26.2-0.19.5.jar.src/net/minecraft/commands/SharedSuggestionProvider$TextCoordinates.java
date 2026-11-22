/*    */ package net.minecraft.commands;
/*    */ 
/*    */ public class TextCoordinates {
/* 35 */   public static final TextCoordinates DEFAULT_LOCAL = new TextCoordinates("^", "^", "^");
/*    */   
/* 37 */   public static final TextCoordinates DEFAULT_GLOBAL = new TextCoordinates("~", "~", "~");
/*    */   
/*    */   public final String x;
/*    */   
/*    */   public final String y;
/*    */   
/*    */   public final String z;
/*    */   
/*    */   public TextCoordinates(String x, String y, String z) {
/* 46 */     this.x = x;
/* 47 */     this.y = y;
/* 48 */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\SharedSuggestionProvider$TextCoordinates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */