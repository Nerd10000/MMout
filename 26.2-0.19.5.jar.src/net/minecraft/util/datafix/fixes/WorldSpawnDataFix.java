/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class WorldSpawnDataFix extends DataFix {
/*    */   public WorldSpawnDataFix(Schema outputSchema) {
/* 13 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("WorldSpawnDataFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WorldSpawnDataFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */