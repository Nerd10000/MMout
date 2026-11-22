/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*    */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*    */ 
/*    */ public final class VanillaPiglinBarterLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/*    */   public VanillaPiglinBarterLoot(HolderLookup.Provider registries) {
/* 25 */     this.registries = registries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaPiglinBarterLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderLookup.Provider registries() {
/* 25 */     return this.registries;
/*    */   }
/*    */   
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 28 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 29 */     output.accept(BuiltInLootTables.PIGLIN_BARTERING, 
/* 30 */         LootTable.lootTable()
/* 31 */         .withPool(LootPool.lootPool()
/* 32 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*    */           
/* 35 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/*    */           
/* 38 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(8).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/* 39 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(8).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/* 40 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).setWeight(8).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/*    */           
/* 43 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(10).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER)))
/* 44 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(10.0F, 36.0F))))
/* 45 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 46 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DRIED_GHAST).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*    */           
/* 49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 9.0F))))
/* 50 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.QUARTZ).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 12.0F))))
/*    */           
/* 53 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OBSIDIAN).setWeight(40))
/* 54 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CRYING_OBSIDIAN).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).setWeight(40))
/* 56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SOUL_SAND).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_BRICK).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 12.0F))))
/* 60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GRAVEL).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 16.0F))))
/* 61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLACKSTONE).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 16.0F))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaPiglinBarterLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */