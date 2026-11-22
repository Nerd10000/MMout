/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.data.loot.EntityLootSubProvider;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ColorCollection;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*    */ 
/*    */ public final class VanillaShearingLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/*    */   public VanillaShearingLoot(HolderLookup.Provider registries) {
/* 30 */     this.registries = registries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaShearingLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaShearingLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderLookup.Provider registries() {
/* 30 */     return this.registries;
/*    */   }
/*    */   
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 33 */     output.accept(BuiltInLootTables.BOGGED_SHEAR, LootTable.lootTable()
/* 34 */         .withPool(LootPool.lootPool()
/* 35 */           .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/* 36 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 37 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/* 41 */     ColorCollection.zipApply(BuiltInLootTables.SHEAR_DYED_SHEEP, Blocks.WOOL, (dyedSheep, wool) -> output.accept(dyedSheep, LootTable.lootTable().withPool(LootPool.lootPool().setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F)).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)wool)))));
/* 50 */     output.accept(BuiltInLootTables.SHEAR_SHEEP, LootTable.lootTable()
/* 51 */         .withPool(
/* 52 */           EntityLootSubProvider.createSheepDispatchPool(BuiltInLootTables.SHEAR_DYED_SHEEP)));
/* 56 */     output.accept(BuiltInLootTables.SHEAR_MOOSHROOM, LootTable.lootTable()
/* 57 */         .withPool(LootPool.lootPool()
/* 58 */           .add((LootPoolEntryContainer.Builder)AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] { NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_RED_MOOSHROOM)
/* 60 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.RED)))), 
/* 61 */                 NestedLootTable.lootTableReference(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM)
/* 62 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.MOOSHROOM_VARIANT, MushroomCow.Variant.BROWN)))) }))));
/* 68 */     output.accept(BuiltInLootTables.SHEAR_RED_MOOSHROOM, LootTable.lootTable()
/* 69 */         .withPool(LootPool.lootPool()
/* 70 */           .setRolls((NumberProvider)ConstantValue.exactly(5.0F))
/* 71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM))));
/* 75 */     output.accept(BuiltInLootTables.SHEAR_BROWN_MOOSHROOM, LootTable.lootTable()
/* 76 */         .withPool(LootPool.lootPool()
/* 77 */           .setRolls((NumberProvider)ConstantValue.exactly(5.0F))
/* 78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BROWN_MUSHROOM))));
/* 82 */     output.accept(BuiltInLootTables.SHEAR_SNOW_GOLEM, LootTable.lootTable()
/* 83 */         .withPool(LootPool.lootPool()
/* 84 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARVED_PUMPKIN))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaShearingLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */