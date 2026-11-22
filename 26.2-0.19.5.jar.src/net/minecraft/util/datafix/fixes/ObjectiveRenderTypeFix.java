/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ObjectiveRenderTypeFix extends DataFix {
/*    */   public ObjectiveRenderTypeFix(Schema outputSchema) {
/* 13 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   private static String getRenderType(String criteriaName) {
/* 17 */     return criteriaName.equals("health") ? "hearts" : "integer";
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type<?> objectiveType = getInputSchema().getType(References.OBJECTIVE);
/* 23 */     return fixTypeEverywhereTyped("ObjectiveRenderTypeFix", objectiveType, typed -> typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ObjectiveRenderTypeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */