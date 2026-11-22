/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.FormattedCharSink;
/*    */ import net.minecraft.util.StringDecomposer;
/*    */ 
/*    */ class null extends Language {
/*    */   public String getOrDefault(String elementId, String defaultValue) {
/* 51 */     return (String)storage.getOrDefault(elementId, defaultValue);
/*    */   }
/*    */   
/*    */   public boolean has(String elementId) {
/* 56 */     return storage.containsKey(elementId);
/*    */   }
/*    */   
/*    */   public boolean isDefaultRightToLeft() {
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   public FormattedCharSequence getVisualOrder(FormattedText logicalOrderText) {
/* 67 */     return output -> logicalOrderText.visit((), Style.EMPTY).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\locale\Language$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */