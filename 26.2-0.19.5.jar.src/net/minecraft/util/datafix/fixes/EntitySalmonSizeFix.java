/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntitySalmonSizeFix extends NamedEntityFix {
/*    */   public EntitySalmonSizeFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "EntitySalmonSizeFix", References.ENTITY, "minecraft:salmon");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> {
/*    */           String type = tag.get("type").asString("medium");
/*    */           return type.equals("large") ? tag : tag.set("type", tag.createString("medium"));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntitySalmonSizeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */