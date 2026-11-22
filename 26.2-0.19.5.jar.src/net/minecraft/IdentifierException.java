/*    */ package net.minecraft;
/*    */ 
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ public class IdentifierException extends RuntimeException {
/*    */   public IdentifierException(String message) {
/*  7 */     super(StringEscapeUtils.escapeJava(message));
/*    */   }
/*    */   
/*    */   public IdentifierException(String message, Throwable cause) {
/* 11 */     super(StringEscapeUtils.escapeJava(message), cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\IdentifierException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */