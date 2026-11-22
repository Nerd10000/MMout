/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityShulkerRotationFix extends NamedEntityFix {
/*    */   public EntityShulkerRotationFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 16 */     List<Double> rotation = input.get("Rotation").asList(d -> Double.valueOf(d.asDouble(180.0D)));
/* 17 */     if (!rotation.isEmpty()) {
/* 18 */       rotation.set(0, Double.valueOf(((Double)rotation.get(0)).doubleValue() - 180.0D));
/* 19 */       Objects.requireNonNull(input);
/* 19 */       return input.set("Rotation", input.createList(rotation.stream().map(input::createDouble)));
/*    */     } 
/* 21 */     return input;
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 26 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityShulkerRotationFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */