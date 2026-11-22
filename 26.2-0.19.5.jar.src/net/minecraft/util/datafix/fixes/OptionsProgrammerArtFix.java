/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsProgrammerArtFix extends DataFix {
/*    */   public OptionsProgrammerArtFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsProgrammerArtFix", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixList(Dynamic<T> entry) {
/* 24 */     return entry.asString().result().map(s -> entry.createString(s.replace("\"programer_art\"", "\"programmer_art\""))).orElse(entry);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\OptionsProgrammerArtFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */