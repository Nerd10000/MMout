/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ class LineComponent implements FormattedText {
/*     */   private final String contents;
/*     */   
/*     */   private final Style style;
/*     */   
/*     */   public LineComponent(String contents, Style style) {
/* 289 */     this.contents = contents;
/* 290 */     this.style = style;
/*     */   }
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 295 */     return output.accept(this.contents);
/*     */   }
/*     */   
/*     */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style parentStyle) {
/* 300 */     return output.accept(this.style.applyTo(parentStyle), this.contents);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\StringSplitter$LineComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */