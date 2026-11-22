/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ 
/*     */ class null implements FormattedText.StyledContentConsumer<FormattedText> {
/* 119 */   private final ComponentCollector collector = new ComponentCollector();
/*     */   
/*     */   null(StringSplitter this$0) {}
/*     */   
/*     */   public Optional<FormattedText> accept(Style style, String contents) {
/* 123 */     output.resetPosition();
/* 124 */     if (!StringDecomposer.iterateFormatted(contents, style, output)) {
/* 125 */       String partial = contents.substring(0, output.getPosition());
/* 126 */       if (!partial.isEmpty())
/* 127 */         this.collector.append(FormattedText.of(partial, style)); 
/* 129 */       return Optional.of(this.collector.getResultOrEmpty());
/*     */     } 
/* 131 */     if (!contents.isEmpty())
/* 132 */       this.collector.append(FormattedText.of(contents, style)); 
/* 134 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\StringSplitter$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */