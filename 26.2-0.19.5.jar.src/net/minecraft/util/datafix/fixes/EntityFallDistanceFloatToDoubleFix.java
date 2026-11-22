/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityFallDistanceFloatToDoubleFix extends DataFix {
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public EntityFallDistanceFloatToDoubleFix(Schema outputSchema, DSL.TypeReference type) {
/* 13 */     super(outputSchema, false);
/* 14 */     this.type = type;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     return fixTypeEverywhereTyped("EntityFallDistanceFloatToDoubleFixFor" + this.type.typeName(), getOutputSchema().getType(this.type), EntityFallDistanceFloatToDoubleFix::fixEntity);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixEntity(Typed<?> entity) {
/* 23 */     return entity.update(DSL.remainderFinder(), remainder -> remainder.renameAndFixField("FallDistance", "fall_distance", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityFallDistanceFloatToDoubleFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */