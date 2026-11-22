/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public interface ComponentContents {
/*    */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 10 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 14 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default MutableComponent resolve(ResolutionContext context, int recursionDepth) throws CommandSyntaxException {
/* 18 */     return MutableComponent.create(this);
/*    */   }
/*    */   
/*    */   MapCodec<? extends ComponentContents> codec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ComponentContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */