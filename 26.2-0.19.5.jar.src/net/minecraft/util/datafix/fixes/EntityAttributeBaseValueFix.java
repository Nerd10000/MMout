/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.DoubleUnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EntityAttributeBaseValueFix extends NamedEntityFix {
/*    */   private final String attributeId;
/*    */   
/*    */   private final DoubleUnaryOperator valueFixer;
/*    */   
/*    */   public EntityAttributeBaseValueFix(Schema outputSchema, String name, String entityName, String attributeId, DoubleUnaryOperator valueFixer) {
/* 16 */     super(outputSchema, false, name, References.ENTITY, entityName);
/* 17 */     this.attributeId = attributeId;
/* 18 */     this.valueFixer = valueFixer;
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 23 */     return entity.update(DSL.remainderFinder(), this::fixValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixValue(Dynamic<?> tag) {
/* 27 */     return tag.update("attributes", attributes -> tag.createList(attributes.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityAttributeBaseValueFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */