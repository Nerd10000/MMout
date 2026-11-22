/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ class null implements FormattedText {
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 46 */     return output.accept(text);
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 51 */     return output.accept(style.applyTo(parentStyle), text);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FormattedText$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */