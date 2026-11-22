/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import java.util.IllegalFormatException;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.locale.Language;
/*    */ 
/*    */ public class I18n {
/*    */   public static String get(String id, Object... args) {
/* 13 */     String value = Language.getInstance().getOrDefault(id);
/*    */     try {
/* 15 */       return String.format(Locale.ROOT, value, args);
/* 16 */     } catch (IllegalFormatException ignored) {
/* 17 */       return "Format error: " + value;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\language\I18n.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */