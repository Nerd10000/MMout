/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class LegacyDragonFightFix extends DataFix {
/*    */   public LegacyDragonFightFix(Schema outputSchema) {
/* 13 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixDragonFight(Dynamic<T> tag) {
/* 17 */     return tag.update("ExitPortalLocation", ExtraDataFixUtils::fixBlockPos);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped("LegacyDragonFightFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LegacyDragonFightFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */