/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityArmorStandSilentFix extends NamedEntityFix {
/*    */   public EntityArmorStandSilentFix(Schema outputSchema, boolean changesType) {
/* 10 */     super(outputSchema, changesType, "EntityArmorStandSilentFix", References.ENTITY, "ArmorStand");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     if (input.get("Silent").asBoolean(false) && !input.get("Marker").asBoolean(false))
/* 15 */       return input.remove("Silent"); 
/* 17 */     return input;
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 22 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityArmorStandSilentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */