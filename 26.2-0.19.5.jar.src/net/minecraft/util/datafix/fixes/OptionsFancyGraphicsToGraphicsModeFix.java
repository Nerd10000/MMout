/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsFancyGraphicsToGraphicsModeFix extends DataFix {
/*    */   public OptionsFancyGraphicsToGraphicsModeFix(Schema outputSchema) {
/* 11 */     super(outputSchema, true);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("fancyGraphics to graphicsMode", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixGraphicsMode(Dynamic<T> field) {
/* 22 */     if ("true".equals(field.asString("true")))
/* 23 */       return field.createString("1"); 
/* 25 */     return field.createString("0");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\OptionsFancyGraphicsToGraphicsModeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */