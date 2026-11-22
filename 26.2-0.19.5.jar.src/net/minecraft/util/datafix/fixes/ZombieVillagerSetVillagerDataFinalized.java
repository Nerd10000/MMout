/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ZombieVillagerSetVillagerDataFinalized extends NamedEntityFix {
/*    */   private static final String VILLAGER_DATA_FINALIZED = "VillagerDataFinalized";
/*    */   
/*    */   public ZombieVillagerSetVillagerDataFinalized(Schema outputSchema) {
/* 12 */     super(outputSchema, true, "Zombie Villager VillagerDataFinalized default value", References.ENTITY, "minecraft:zombie_villager");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     return entity.update(DSL.remainderFinder(), ZombieVillagerSetVillagerDataFinalized::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 21 */     return tag.set("VillagerDataFinalized", tag.createBoolean(true));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ZombieVillagerSetVillagerDataFinalized.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */