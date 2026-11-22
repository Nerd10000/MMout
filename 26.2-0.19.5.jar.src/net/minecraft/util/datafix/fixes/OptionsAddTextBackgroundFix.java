/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAddTextBackgroundFix extends DataFix {
/*    */   public OptionsAddTextBackgroundFix(Schema outputSchema, boolean changesType) {
/* 11 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAddTextBackgroundFix", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private double calculateBackground(String textOpacity) {
/*    */     try {
/* 26 */       double textAlpha = 0.9D * Double.parseDouble(textOpacity) + 0.1D;
/* 27 */       return textAlpha / 2.0D;
/* 28 */     } catch (NumberFormatException e) {
/* 29 */       return 0.5D;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\OptionsAddTextBackgroundFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */