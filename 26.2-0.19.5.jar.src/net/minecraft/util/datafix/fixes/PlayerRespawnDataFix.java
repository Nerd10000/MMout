/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class PlayerRespawnDataFix extends DataFix {
/*    */   public PlayerRespawnDataFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("PlayerRespawnDataFix", getInputSchema().getType(References.PLAYER), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\PlayerRespawnDataFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */