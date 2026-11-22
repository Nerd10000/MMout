/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityGoatMissingStateFix extends NamedEntityFix {
/*    */   public EntityGoatMissingStateFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "EntityGoatMissingStateFix", References.ENTITY, "minecraft:goat");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> tag.set("HasLeftHorn", tag.createBoolean(true)).set("HasRightHorn", tag.createBoolean(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityGoatMissingStateFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */