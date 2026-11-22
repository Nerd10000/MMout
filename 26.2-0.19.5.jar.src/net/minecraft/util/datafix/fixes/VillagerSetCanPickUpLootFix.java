/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class VillagerSetCanPickUpLootFix extends NamedEntityFix {
/*    */   private static final String CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*    */   
/*    */   public VillagerSetCanPickUpLootFix(Schema outputSchema) {
/* 14 */     super(outputSchema, true, "Villager CanPickUpLoot default value", References.ENTITY, "Villager");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 19 */     return entity.update(DSL.remainderFinder(), VillagerSetCanPickUpLootFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 23 */     return tag.set("CanPickUpLoot", tag.createBoolean(true));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\VillagerSetCanPickUpLootFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */