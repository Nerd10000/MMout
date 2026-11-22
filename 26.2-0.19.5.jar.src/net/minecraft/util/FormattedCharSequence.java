/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface FormattedCharSequence {
/*    */   public static final FormattedCharSequence EMPTY = output -> true;
/*    */   
/*    */   static FormattedCharSequence codepoint(int codepoint, Style style) {
/* 16 */     return output -> output.accept(0, style, codepoint);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence forward(String plainText, Style style) {
/* 20 */     if (plainText.isEmpty())
/* 21 */       return EMPTY; 
/* 23 */     return output -> StringDecomposer.iterate(plainText, style, output);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence forward(String plainText, Style style, Int2IntFunction modifier) {
/* 27 */     if (plainText.isEmpty())
/* 28 */       return EMPTY; 
/* 30 */     return output -> StringDecomposer.iterate(plainText, style, decorateOutput(output, modifier));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String plainText, Style style) {
/* 34 */     if (plainText.isEmpty())
/* 35 */       return EMPTY; 
/* 37 */     return output -> StringDecomposer.iterateBackwards(plainText, style, output);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence backward(String plainText, Style style, Int2IntFunction modifier) {
/* 41 */     if (plainText.isEmpty())
/* 42 */       return EMPTY; 
/* 44 */     return output -> StringDecomposer.iterateBackwards(plainText, style, decorateOutput(output, modifier));
/*    */   }
/*    */   
/*    */   static FormattedCharSink decorateOutput(FormattedCharSink output, Int2IntFunction modifier) {
/* 48 */     return (p, s, ch) -> output.accept(p, s, ((Integer)modifier.apply(Integer.valueOf(ch))).intValue());
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite() {
/* 52 */     return EMPTY;
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence part) {
/* 56 */     return part;
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence first, FormattedCharSequence second) {
/* 60 */     return fromPair(first, second);
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(FormattedCharSequence... parts) {
/* 64 */     return fromList((List<FormattedCharSequence>)ImmutableList.copyOf((Object[])parts));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence composite(List<FormattedCharSequence> parts) {
/* 68 */     int size = parts.size();
/* 69 */     switch (size) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 69 */     return 
/*    */       
/* 73 */       fromList((List<FormattedCharSequence>)ImmutableList.copyOf(parts));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence fromPair(FormattedCharSequence first, FormattedCharSequence second) {
/* 78 */     return output -> (first.accept(output) && second.accept(output));
/*    */   }
/*    */   
/*    */   static FormattedCharSequence fromList(List<FormattedCharSequence> partCopy) {
/* 82 */     return output -> {
/*    */         for (FormattedCharSequence part : partCopy) {
/*    */           if (!part.accept(output))
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */   
/*    */   boolean accept(FormattedCharSink paramFormattedCharSink);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\FormattedCharSequence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */