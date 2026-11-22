/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ class null implements FormattedText {
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 64 */     for (FormattedText part : parts) {
/* 65 */       Optional<T> result = part.visit(output);
/* 66 */       if (result.isPresent())
/* 67 */         return result; 
/*    */     } 
/* 71 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 76 */     for (FormattedText part : parts) {
/* 77 */       Optional<T> result = part.visit(output, parentStyle);
/* 78 */       if (result.isPresent())
/* 79 */         return result; 
/*    */     } 
/* 83 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FormattedText$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */