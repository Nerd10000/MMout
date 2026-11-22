/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class LevelDatDifficultyFix extends DataFix {
/*    */   public LevelDatDifficultyFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("LevelDatDifficultyFix", getInputSchema().getType(References.LIGHTWEIGHT_LEVEL), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LevelDatDifficultyFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */