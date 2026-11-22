/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.ibm.icu.lang.UCharacter;
/*    */ import com.ibm.icu.text.ArabicShaping;
/*    */ import com.ibm.icu.text.Bidi;
/*    */ import com.ibm.icu.text.BidiRun;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.SubStringSource;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class FormattedBidiReorder {
/*    */   public static FormattedCharSequence reorder(FormattedText text, boolean defaultRightToLeft) {
/* 16 */     SubStringSource source = SubStringSource.create(text, UCharacter::getMirror, FormattedBidiReorder::shape);
/* 17 */     Bidi bidi = new Bidi(source.getPlainText(), defaultRightToLeft ? 127 : 126);
/* 18 */     bidi.setReorderingMode(0);
/* 20 */     List<FormattedCharSequence> result = Lists.newArrayList();
/* 21 */     int runCount = bidi.countRuns();
/* 22 */     for (int i = 0; i < runCount; i++) {
/* 23 */       BidiRun run = bidi.getVisualRun(i);
/* 24 */       result.addAll(source.substring(run.getStart(), run.getLength(), run.isOddRun()));
/*    */     } 
/* 27 */     return FormattedCharSequence.composite(result);
/*    */   }
/*    */   
/*    */   private static String shape(String text) {
/*    */     try {
/* 32 */       return (new ArabicShaping(8)).shape(text);
/* 33 */     } catch (Exception e) {
/* 34 */       return text;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\language\FormattedBidiReorder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */