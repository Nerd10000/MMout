/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WeaponSmithChestLootTableFix extends NamedEntityFix {
/*    */   public WeaponSmithChestLootTableFix(Schema outputSchema, boolean changesType) {
/*  9 */     super(outputSchema, changesType, "WeaponSmithChestLootTableFix", References.BLOCK_ENTITY, "minecraft:chest");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> {
/*    */           String lootTable = tag.get("LootTable").asString("");
/*    */           return lootTable.equals("minecraft:chests/village_blacksmith") ? tag.set("LootTable", tag.createString("minecraft:chests/village/village_weaponsmith")) : tag;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WeaponSmithChestLootTableFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */