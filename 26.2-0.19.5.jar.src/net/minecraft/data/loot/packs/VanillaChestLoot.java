/*      */ package net.minecraft.data.loot.packs;
/*      */ 
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.advancements.predicates.LocationPredicate;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.loot.LootTableSubProvider;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.InstrumentTags;
/*      */ import net.minecraft.tags.StructureTags;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.item.Instrument;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.alchemy.Potions;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.biome.Biomes;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.level.storage.loot.LootPool;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*      */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetStewEffectFunction;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*      */ 
/*      */ public final class VanillaChestLoot extends Record implements LootTableSubProvider {
/*      */   private final HolderLookup.Provider registries;
/*      */   
/*      */   public VanillaChestLoot(HolderLookup.Provider registries) {
/*   50 */     this.registries = registries;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #50	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #50	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChestLoot;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #50	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChestLoot;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public HolderLookup.Provider registries() {
/*   50 */     return this.registries;
/*      */   }
/*      */   
/*      */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*   53 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*   54 */     HolderLookup.RegistryLookup<Biome> biomes = this.registries.lookupOrThrow(Registries.BIOME);
/*   56 */     output.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*   57 */         LootTable.lootTable()
/*   58 */         .withPool(LootPool.lootPool()
/*   59 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*   60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/*   61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/*   62 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(30))
/*   63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*   64 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*   65 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5)))
/*      */         
/*   67 */         .withPool(LootPool.lootPool()
/*   68 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/*   69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*   70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*   71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*   72 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*   73 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*   74 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/*   75 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*   76 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/*   77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*   78 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*   79 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*   81 */         .withPool(LootPool.lootPool()
/*   82 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*   83 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.RAIL).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/*   84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POWERED_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DETECTOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   86 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACTIVATOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*   87 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 16.0F))))
/*   88 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_BOUNCE)
/*   89 */             .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)biomes
/*   90 */                       .getOrThrow(Biomes.SULFUR_CAVES) }))))).setWeight(10))));
/*   96 */     output.accept(BuiltInLootTables.BASTION_BRIDGE, bastionBridgeLootTable());
/*   97 */     output.accept(BuiltInLootTables.BASTION_HOGLIN_STABLE, bastionHoglinStableLootTable());
/*   98 */     output.accept(BuiltInLootTables.BASTION_OTHER, bastionOtherLootTable());
/*   99 */     output.accept(BuiltInLootTables.BASTION_TREASURE, bastionTreasureLootTable());
/*  101 */     output.accept(BuiltInLootTables.BURIED_TREASURE, 
/*  102 */         LootTable.lootTable()
/*  103 */         .withPool(LootPool.lootPool()
/*  104 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  105 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEART_OF_THE_SEA)))
/*      */         
/*  107 */         .withPool(LootPool.lootPool()
/*  108 */           .setRolls((NumberProvider)UniformGenerator.between(5.0F, 8.0F))
/*  109 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  110 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  113 */         .withPool(LootPool.lootPool()
/*  114 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  115 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/*  116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  117 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  119 */         .withPool(LootPool.lootPool()
/*  120 */           .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/*  121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  122 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD))
/*  123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SPEAR)))
/*      */         
/*  125 */         .withPool(LootPool.lootPool()
/*  126 */           .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/*  127 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  128 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_SALMON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  130 */         .withPool(LootPool.lootPool()
/*  131 */           .setRolls((NumberProvider)UniformGenerator.between(0.0F, 2.0F))
/*  132 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/*      */         
/*  134 */         .withPool(LootPool.lootPool()
/*  135 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  136 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  138 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  139 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  140 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*  144 */     output.accept(BuiltInLootTables.ANCIENT_CITY, ancientCityLootTable());
/*  146 */     output.accept(BuiltInLootTables.ANCIENT_CITY_ICE_BOX, 
/*  147 */         LootTable.lootTable()
/*  148 */         .withPool(LootPool.lootPool()
/*  149 */           .setRolls((NumberProvider)UniformGenerator.between(4.0F, 10.0F))
/*      */           
/*  152 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).setWeight(1).apply((LootItemFunction.Builder)SetStewEffectFunction.stewEffect()
/*  153 */               .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/*  154 */               .withEffect(MobEffects.BLINDNESS, (NumberProvider)UniformGenerator.between(5.0F, 7.0F)))
/*  155 */             .apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/*  156 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/*  157 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/*      */           
/*  160 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PACKED_ICE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/*      */           
/*  163 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))));
/*  167 */     output.accept(BuiltInLootTables.DESERT_PYRAMID, desertPyramidLootTable());
/*  168 */     output.accept(BuiltInLootTables.END_CITY_TREASURE, endCityTreasureLootTable());
/*  170 */     output.accept(BuiltInLootTables.IGLOO_CHEST, 
/*  171 */         LootTable.lootTable()
/*  172 */         .withPool(LootPool.lootPool()
/*  173 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  174 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  175 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  176 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  177 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/*  178 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  179 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  180 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  182 */         .withPool(LootPool.lootPool()
/*  183 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  184 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))));
/*  188 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE, jungleTempleLootTable());
/*  190 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER, 
/*  191 */         LootTable.lootTable()
/*  192 */         .withPool(LootPool.lootPool()
/*  193 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 2.0F))
/*  194 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(30).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))));
/*  198 */     output.accept(BuiltInLootTables.NETHER_BRIDGE, netherBridgeLootTable());
/*  200 */     output.accept(BuiltInLootTables.PILLAGER_OUTPOST, pillagerOutpostLootTable());
/*  202 */     output.accept(BuiltInLootTables.SHIPWRECK_MAP, shipwreckMapLootTable());
/*  204 */     output.accept(BuiltInLootTables.SHIPWRECK_SUPPLY, shipwreckSupplyLootTable());
/*  206 */     output.accept(BuiltInLootTables.SHIPWRECK_TREASURE, shipwreckTreasureLootTable());
/*  208 */     output.accept(BuiltInLootTables.SIMPLE_DUNGEON, 
/*  209 */         LootTable.lootTable()
/*  210 */         .withPool(LootPool.lootPool()
/*  211 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  212 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  213 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/*  214 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/*  215 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE).setWeight(2))
/*  216 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/*  217 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/*  218 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(20))
/*  219 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/*  220 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(15))
/*  221 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/*  222 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/*  223 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */         
/*  225 */         .withPool(LootPool.lootPool()
/*  226 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/*  227 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  228 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  229 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/*  230 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  231 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/*  232 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  233 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  234 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  235 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  236 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */         
/*  238 */         .withPool(LootPool.lootPool()
/*  239 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*  240 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  241 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  242 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  243 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))));
/*  247 */     output.accept(BuiltInLootTables.SPAWN_BONUS_CHEST, 
/*  248 */         LootTable.lootTable()
/*  249 */         .withPool(LootPool.lootPool()
/*  250 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  251 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/*  252 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_AXE).setWeight(3)))
/*      */         
/*  254 */         .withPool(LootPool.lootPool()
/*  255 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  256 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/*  257 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_PICKAXE).setWeight(3)))
/*      */         
/*  259 */         .withPool(LootPool.lootPool()
/*  260 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*  261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  262 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  263 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  265 */         .withPool(LootPool.lootPool()
/*  266 */           .setRolls((NumberProvider)ConstantValue.exactly(4.0F))
/*  267 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/*  268 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_PLANKS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/*  269 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  270 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  271 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BIRCH_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  272 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.JUNGLE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  273 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  274 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  275 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.MANGROVE_LOG).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*  279 */     output.accept(BuiltInLootTables.STRONGHOLD_CORRIDOR, strongholdCorridorLootTable());
/*  281 */     output.accept(BuiltInLootTables.STRONGHOLD_CROSSING, 
/*  282 */         LootTable.lootTable()
/*  283 */         .withPool(LootPool.lootPool()
/*  284 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/*  285 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  286 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  287 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*  288 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/*  289 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  290 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  291 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE))
/*  292 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F))))));
/*  296 */     output.accept(BuiltInLootTables.STRONGHOLD_LIBRARY, strongholdLibraryLootTable());
/*  298 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_BIG, 
/*  299 */         LootTable.lootTable()
/*  300 */         .withPool(LootPool.lootPool()
/*  301 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  302 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  303 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  304 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  305 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SPEAR).setWeight(2))
/*  306 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  308 */         .withPool(LootPool.lootPool()
/*  309 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  310 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/*  311 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  312 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  313 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/*  314 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  315 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  317 */         .withPool(LootPool.lootPool()
/*  318 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  319 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  320 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  321 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  322 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  323 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*  327 */     output.accept(BuiltInLootTables.UNDERWATER_RUIN_SMALL, 
/*  328 */         LootTable.lootTable()
/*  329 */         .withPool(LootPool.lootPool()
/*  330 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 8.0F))
/*  331 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  332 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).setWeight(2))
/*  333 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SPEAR).setWeight(2))
/*  334 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5))
/*  335 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD))
/*  336 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */         
/*  338 */         .withPool(LootPool.lootPool()
/*  339 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  340 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE))
/*  341 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET))
/*  342 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FISHING_ROD).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  343 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(5).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */         
/*  345 */         .withPool(LootPool.lootPool()
/*  346 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  347 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/*  348 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  349 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  350 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  351 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*  355 */     output.accept(BuiltInLootTables.VILLAGE_WEAPONSMITH, 
/*  356 */         LootTable.lootTable()
/*  357 */         .withPool(LootPool.lootPool()
/*  358 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  359 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  360 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  361 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  362 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  363 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  364 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  365 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/*  366 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SPEAR).setWeight(5))
/*  367 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_SPEAR).setWeight(7))
/*  368 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/*  369 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/*  370 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/*  371 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/*  372 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/*  373 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/*  374 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/*  375 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/*  376 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/*  377 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/*  378 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR)))
/*      */         
/*  380 */         .withPool(LootPool.lootPool()
/*  381 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  382 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  383 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  386 */     output.accept(BuiltInLootTables.VILLAGE_TOOLSMITH, 
/*  387 */         LootTable.lootTable()
/*  388 */         .withPool(LootPool.lootPool()
/*  389 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  390 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  391 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  392 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  393 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  394 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  395 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  396 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  397 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(5))));
/*  401 */     output.accept(BuiltInLootTables.VILLAGE_CARTOGRAPHER, 
/*  402 */         LootTable.lootTable()
/*  403 */         .withPool(LootPool.lootPool()
/*  404 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  405 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  406 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  407 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(5))
/*  408 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  409 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*  410 */         .withPool(LootPool.lootPool()
/*  411 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  412 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  413 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  416 */     output.accept(BuiltInLootTables.VILLAGE_MASON, 
/*  417 */         LootTable.lootTable()
/*  418 */         .withPool(LootPool.lootPool()
/*  419 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  420 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  421 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOWER_POT).setWeight(1))
/*  422 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE).setWeight(2))
/*  423 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.STONE_BRICKS).setWeight(2))
/*  424 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  425 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DYE.yellow()).setWeight(1))
/*  426 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SMOOTH_STONE).setWeight(1))
/*  427 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*  431 */     output.accept(BuiltInLootTables.VILLAGE_ARMORER, 
/*  432 */         LootTable.lootTable()
/*  433 */         .withPool(LootPool.lootPool()
/*  434 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  435 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  436 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  437 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(1))
/*  438 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))));
/*  442 */     output.accept(BuiltInLootTables.VILLAGE_SHEPHERD, 
/*  443 */         LootTable.lootTable()
/*  444 */         .withPool(LootPool.lootPool()
/*  445 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  446 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WOOL.white()).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/*  447 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WOOL.black()).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  448 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WOOL.gray()).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  449 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WOOL.brown()).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  450 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WOOL.lightGray()).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  451 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  452 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHEARS).setWeight(1))
/*  453 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))));
/*  457 */     output.accept(BuiltInLootTables.VILLAGE_BUTCHER, 
/*  458 */         LootTable.lootTable()
/*  459 */         .withPool(LootPool.lootPool()
/*  460 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  461 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  462 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  463 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  464 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  465 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  466 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*  470 */     output.accept(BuiltInLootTables.VILLAGE_FLETCHER, 
/*  471 */         LootTable.lootTable()
/*  472 */         .withPool(LootPool.lootPool()
/*  473 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  474 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  475 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  476 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  477 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EGG).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  478 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  479 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*  483 */     output.accept(BuiltInLootTables.VILLAGE_FISHER, 
/*  484 */         LootTable.lootTable()
/*  485 */         .withPool(LootPool.lootPool()
/*  486 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  487 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1))
/*  488 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  489 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  490 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  491 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BARREL).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  492 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  493 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*  497 */     output.accept(BuiltInLootTables.VILLAGE_TANNERY, 
/*  498 */         LootTable.lootTable()
/*  499 */         .withPool(LootPool.lootPool()
/*  500 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 5.0F))
/*  501 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  502 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(2))
/*  503 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(2))
/*  504 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(2))
/*  505 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  506 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(2))
/*  507 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/*  508 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  510 */         .withPool(LootPool.lootPool()
/*  511 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  512 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  513 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  516 */     output.accept(BuiltInLootTables.VILLAGE_TEMPLE, 
/*  517 */         LootTable.lootTable()
/*  518 */         .withPool(LootPool.lootPool()
/*  519 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  520 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  521 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  522 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  523 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  524 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  525 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))));
/*  529 */     output.accept(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, 
/*  530 */         LootTable.lootTable()
/*  531 */         .withPool(LootPool.lootPool()
/*  532 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  533 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  534 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DANDELION).setWeight(2))
/*  535 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POPPY).setWeight(1))
/*  536 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  537 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  538 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  539 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/*  540 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(1))
/*  541 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  542 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OAK_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  544 */         .withPool(LootPool.lootPool()
/*  545 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  546 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  547 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  550 */     output.accept(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, 
/*  551 */         LootTable.lootTable()
/*  552 */         .withPool(LootPool.lootPool()
/*  553 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  554 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  555 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FERN).setWeight(2))
/*  556 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LARGE_FERN).setWeight(2))
/*  557 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  558 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SWEET_BERRIES).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  559 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  560 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  561 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE).setWeight(1))
/*  562 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  563 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SPRUCE_SAPLING).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  564 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_SIGN).setWeight(1))
/*  565 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPRUCE_LOG).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */         
/*  567 */         .withPool(LootPool.lootPool()
/*  568 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  569 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  570 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  573 */     output.accept(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, 
/*  574 */         LootTable.lootTable()
/*  575 */         .withPool(LootPool.lootPool()
/*  576 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  577 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  578 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHORT_GRASS).setWeight(5))
/*  579 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TALL_GRASS).setWeight(5))
/*  580 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  581 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  582 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  583 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACACIA_SAPLING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  584 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(1))
/*  585 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  586 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(1)))
/*      */         
/*  588 */         .withPool(LootPool.lootPool()
/*  589 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  590 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  591 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  594 */     output.accept(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, 
/*  595 */         LootTable.lootTable()
/*  596 */         .withPool(LootPool.lootPool()
/*  597 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  598 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BLUE_ICE).setWeight(1))
/*  599 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SNOW_BLOCK).setWeight(4))
/*  600 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  601 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  602 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  603 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SOUP).setWeight(1))
/*  604 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FURNACE).setWeight(1))
/*  605 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  606 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  607 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F)))))
/*      */         
/*  609 */         .withPool(LootPool.lootPool()
/*  610 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  611 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  612 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  615 */     output.accept(BuiltInLootTables.VILLAGE_DESERT_HOUSE, 
/*  616 */         LootTable.lootTable()
/*  617 */         .withPool(LootPool.lootPool()
/*  618 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 8.0F))
/*  619 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY_BALL).setWeight(1))
/*  620 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DYE.green()).setWeight(1))
/*  621 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CACTUS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  622 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 7.0F))))
/*  623 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  624 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1))
/*  625 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DEAD_BUSH).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  626 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F)))))
/*      */         
/*  628 */         .withPool(LootPool.lootPool()
/*  629 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  630 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUNDLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  631 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))));
/*  634 */     output.accept(BuiltInLootTables.WOODLAND_MANSION, woodlandMansionLootTable());
/*  636 */     output.accept(BuiltInLootTables.RUINED_PORTAL, 
/*  637 */         LootTable.lootTable()
/*  638 */         .withPool(LootPool.lootPool()
/*  639 */           .setRolls((NumberProvider)UniformGenerator.between(4.0F, 8.0F))
/*  640 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OBSIDIAN).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  641 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  642 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(9.0F, 18.0F))))
/*  643 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(40))
/*  644 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).setWeight(40))
/*      */           
/*  646 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/*  647 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 24.0F))))
/*  648 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  649 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  650 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HOE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  651 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  652 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_PICKAXE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  653 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  654 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  655 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  656 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).setWeight(15).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*      */           
/*  658 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLISTERING_MELON_SLICE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  659 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(5))
/*  660 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LIGHT_WEIGHTED_PRESSURE_PLATE).setWeight(5))
/*  661 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  662 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK).setWeight(5))
/*  663 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/*      */           
/*  665 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BELL).setWeight(1))
/*  666 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
/*  667 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_BLOCK).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F)))))
/*      */         
/*  669 */         .withPool(LootPool.lootPool()
/*  670 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  671 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/*  672 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LODESTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))));
/*  677 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_DISPENSER, 
/*  678 */         LootTable.lootTable()
/*  679 */         .withPool(LootPool.lootPool()
/*  680 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  681 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))));
/*  686 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_WATER_DISPENSER, 
/*  687 */         LootTable.lootTable()
/*  688 */         .withPool(LootPool.lootPool()
/*  689 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  690 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*  695 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CHAMBER_DISPENSER, 
/*  696 */         LootTable.lootTable()
/*  697 */         .withPool(LootPool.lootPool()
/*  698 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  699 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WATER_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(4))
/*  700 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(4))
/*  701 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  702 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EGG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(2))
/*  703 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).setWeight(6))
/*  704 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  705 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  706 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPLASH_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  707 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  708 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  709 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  710 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HEALING)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))));
/*  716 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT, 
/*  717 */         LootTable.lootTable()
/*  718 */         .withPool(LootPool.lootPool()
/*  719 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  720 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(125))
/*  721 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(100))
/*  722 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(100))
/*  723 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(10))
/*  724 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CREATOR_MUSIC_BOX).setWeight(5))
/*  725 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(5))
/*  726 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(5))
/*  727 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))));
/*  732 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY, 
/*  733 */         LootTable.lootTable()
/*  734 */         .withPool(LootPool.lootPool()
/*  735 */           .setRolls((NumberProvider)UniformGenerator.between(3.0F, 5.0F))
/*  736 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 14.0F))).setWeight(2))
/*  737 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)).setWeight(1))
/*  738 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)).setWeight(1))
/*  739 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))).setWeight(2))
/*  740 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  741 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ACACIA_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  742 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MOSS_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  743 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(1))
/*  744 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TUFF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 10.0F))).setWeight(1))
/*  745 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(1))
/*  746 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/*  747 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/*  748 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  749 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MILK_BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/*  755 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE, 
/*  756 */         LootTable.lootTable()
/*  757 */         .withPool(LootPool.lootPool()
/*  758 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/*  759 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))
/*  760 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).setWeight(5))
/*  761 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WOODEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(10))
/*  762 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEYCOMB).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(10))
/*  763 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 10.0F))).setWeight(10))));
/*  769 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION, 
/*  770 */         LootTable.lootTable()
/*  771 */         .withPool(LootPool.lootPool()
/*  772 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  773 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).setWeight(1))
/*  774 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(5))
/*  775 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  776 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).setWeight(5))
/*  777 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(10))
/*  778 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CAKE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))).setWeight(20))
/*  779 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.AMETHYST_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 20.0F))).setWeight(20))
/*  780 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(20))));
/*  786 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL, 
/*  787 */         LootTable.lootTable()
/*  788 */         .withPool(LootPool.lootPool()
/*  789 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  790 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.4F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  791 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  792 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).setWeight(1))
/*  793 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(1))
/*  794 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(1))
/*  795 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  796 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(4))
/*  797 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 15.0F))).setWeight(10))
/*  798 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 10.0F))).setWeight(10))));
/*  805 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR, 
/*  806 */         LootTable.lootTable()
/*  807 */         .withPool(LootPool.lootPool()
/*  808 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  809 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.4F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)).setWeight(1))
/*  810 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEYCOMB).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).setWeight(1))
/*  811 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  812 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).setWeight(2))
/*  813 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).setWeight(2))
/*  814 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_HANGING_SIGN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))).setWeight(2))
/*  815 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAMBOO_PLANKS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  816 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCAFFOLDING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))).setWeight(2))
/*  817 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))).setWeight(2))
/*  818 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TUFF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 20.0F))).setWeight(3))));
/*  822 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE, 
/*  823 */         LootTable.lootTable()
/*  824 */         .withPool(LootPool.lootPool()
/*  825 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  826 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  827 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHIELD).setWeight(3).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.5F, 1.0F))))
/*  828 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOW).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/*  829 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 20.0F))))
/*  830 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 10.0F))))
/*  831 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 10.0F))))
/*  832 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  833 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder())
/*  834 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] { (Holder)enchantments.getOrThrow(Enchantments.SHARPNESS), (Holder)enchantments
/*  836 */                     .getOrThrow(Enchantments.BANE_OF_ARTHROPODS), (Holder)enchantments
/*  837 */                     .getOrThrow(Enchantments.EFFICIENCY), (Holder)enchantments
/*  838 */                     .getOrThrow(Enchantments.FORTUNE), (Holder)enchantments
/*  839 */                     .getOrThrow(Enchantments.SILK_TOUCH), (Holder)enchantments
/*  840 */                     .getOrThrow(Enchantments.FEATHER_FALLING) })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder())
/*  844 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] { (Holder)enchantments.getOrThrow(Enchantments.RIPTIDE), (Holder)enchantments
/*  846 */                     .getOrThrow(Enchantments.LOYALTY), (Holder)enchantments
/*  847 */                     .getOrThrow(Enchantments.CHANNELING), (Holder)enchantments
/*  848 */                     .getOrThrow(Enchantments.IMPALING), (Holder)enchantments
/*  849 */                     .getOrThrow(Enchantments.MENDING) })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(1).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/*  852 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).setWeight(1).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 15.0F))))));
/*  856 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON, 
/*  857 */         LootTable.lootTable()
/*  858 */         .withPool(LootPool.lootPool()
/*  859 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  860 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/*  861 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/*  862 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  863 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  864 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  865 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HONEY_BOTTLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  866 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/*  867 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))))
/*  868 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))));
/*  872 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE, 
/*  873 */         LootTable.lootTable()
/*  874 */         .withPool(LootPool.lootPool()
/*  875 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  876 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(4))
/*  877 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  878 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUSTER_BANNER_PATTERN).setWeight(2))
/*  879 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_PRECIPICE).setWeight(2))
/*  880 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIDENT).setWeight(1))));
/*  884 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD, 
/*  885 */         LootTable.lootTable()
/*  886 */         .withPool(LootPool.lootPool()
/*  887 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  888 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_RARE).setWeight(8))
/*  889 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON).setWeight(2)))
/*      */         
/*  891 */         .withPool(LootPool.lootPool()
/*  892 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  893 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON)))
/*      */         
/*  895 */         .withPool(LootPool.lootPool()
/*  896 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  897 */           .when(LootItemRandomChanceCondition.randomChance(0.25F))
/*  898 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_UNIQUE))));
/*  902 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE, 
/*  903 */         LootTable.lootTable()
/*  904 */         .withPool(LootPool.lootPool()
/*  905 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  906 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD_BLOCK).setWeight(5))
/*  907 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BLOCK).setWeight(4))
/*  908 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(4).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(5.0F, 20.0F))))
/*  909 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(3))
/*  910 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_AXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))))
/*  911 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))))
/*  912 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder())
/*  913 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] { (Holder)enchantments.getOrThrow(Enchantments.KNOCKBACK), (Holder)enchantments
/*  915 */                     .getOrThrow(Enchantments.PUNCH), (Holder)enchantments
/*  916 */                     .getOrThrow(Enchantments.SMITE), (Holder)enchantments
/*  917 */                     .getOrThrow(Enchantments.LOOTING), (Holder)enchantments
/*  918 */                     .getOrThrow(Enchantments.MULTISHOT) })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder())
/*  921 */               .withOneOf((HolderSet)HolderSet.direct(new Holder[] { (Holder)enchantments.getOrThrow(Enchantments.BREACH), (Holder)enchantments
/*  923 */                     .getOrThrow(Enchantments.DENSITY) })))).add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new SetEnchantmentsFunction.Builder())
/*  926 */               .withEnchantment((Holder)enchantments.getOrThrow(Enchantments.WIND_BURST), (NumberProvider)ConstantValue.exactly(1.0F))))
/*  927 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BLOCK).setWeight(1))));
/*  931 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON, 
/*  932 */         LootTable.lootTable()
/*  933 */         .withPool(LootPool.lootPool()
/*  934 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  935 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 10.0F))))
/*  936 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 12.0F))))
/*  937 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 12.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/*  938 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))))
/*  939 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))));
/*  943 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE, 
/*  944 */         LootTable.lootTable()
/*  945 */         .withPool(LootPool.lootPool()
/*  946 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  947 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(3))
/*  948 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(3))
/*  949 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLOW_BANNER_PATTERN).setWeight(2))
/*  950 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CREATOR).setWeight(1))
/*  951 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.HEAVY_CORE).setWeight(1))));
/*  955 */     output.accept(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS, 
/*  956 */         LootTable.lootTable()
/*  957 */         .withPool(LootPool.lootPool()
/*  958 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  959 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE).setWeight(8))
/*  960 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON).setWeight(2)))
/*      */         
/*  962 */         .withPool(LootPool.lootPool()
/*  963 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/*  964 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_COMMON)))
/*      */         
/*  966 */         .withPool(LootPool.lootPool()
/*  967 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  968 */           .when(LootItemRandomChanceCondition.randomChance(0.75F))
/*  969 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_UNIQUE))));
/*  973 */     spawnerLootTables(output);
/*      */   }
/*      */   
/*      */   public void spawnerLootTables(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  977 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  981 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY, 
/*  982 */         LootTable.lootTable()
/*  983 */         .withPool(LootPool.lootPool()
/*  984 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  985 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIAL_KEY))));
/*  992 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES, 
/*  993 */         LootTable.lootTable()
/*  994 */         .withPool(LootPool.lootPool()
/*  995 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  996 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_CHICKEN).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/*  997 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  998 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  999 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 1000 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))));
/* 1004 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 
/* 1005 */         LootTable.lootTable()
/* 1006 */         .withPool(LootPool.lootPool()
/* 1007 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1008 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_TRIAL_KEY))));
/* 1012 */     output.accept(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 
/* 1013 */         LootTable.lootTable()
/* 1014 */         .withPool(LootPool.lootPool()
/* 1015 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1016 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_BEEF).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1017 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BAKED_POTATO).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1018 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1019 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 1020 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))));
/* 1024 */     output.accept(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS, 
/* 1025 */         LootTable.lootTable()
/* 1026 */         .withPool(LootPool.lootPool()
/* 1027 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1028 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WIND_CHARGED)))
/* 1029 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.OOZING)))
/* 1030 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAVING)))
/* 1031 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.INFESTED)))
/* 1032 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 1033 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 1034 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LINGERING_POTION).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOW_FALLING))))
/*      */         
/* 1036 */         .withPool(LootPool.lootPool()
/* 1037 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1038 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1039 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/* 1040 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_SLOWNESS)))
/* 1041 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FIRE_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1042 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WIND_CHARGE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder shipwreckSupplyLootTable() {
/* 1048 */     return LootTable.lootTable()
/* 1049 */       .withPool(LootPool.lootPool()
/* 1050 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 10.0F))
/* 1051 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 12.0F))))
/* 1052 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1053 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MOSS_BLOCK).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1054 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POISONOUS_POTATO).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1055 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/* 1056 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 21.0F))))
/* 1057 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUSPICIOUS_STEW).setWeight(10).apply((LootItemFunction.Builder)SetStewEffectFunction.stewEffect()
/* 1058 */             .withEffect(MobEffects.NIGHT_VISION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/* 1059 */             .withEffect(MobEffects.JUMP_BOOST, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))
/* 1060 */             .withEffect(MobEffects.WEAKNESS, (NumberProvider)UniformGenerator.between(6.0F, 8.0F))
/* 1061 */             .withEffect(MobEffects.BLINDNESS, (NumberProvider)UniformGenerator.between(5.0F, 7.0F))
/* 1062 */             .withEffect(MobEffects.POISON, (NumberProvider)UniformGenerator.between(10.0F, 20.0F))
/* 1063 */             .withEffect(MobEffects.SATURATION, (NumberProvider)UniformGenerator.between(7.0F, 10.0F))))
/*      */         
/* 1065 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1066 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 24.0F))))
/* 1067 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.PUMPKIN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1068 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1069 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1070 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TNT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1071 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1072 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1073 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1074 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1076 */       .withPool(LootPool.lootPool()
/* 1077 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1078 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1079 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1081 */       .withPool(LootPool.lootPool()
/* 1082 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1083 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1084 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1085 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1086 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1087 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder shipwreckMapLootTable() {
/* 1092 */     return LootTable.lootTable()
/* 1093 */       .withPool(LootPool.lootPool()
/* 1094 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1095 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP).apply((LootItemFunction.Builder)ExplorationMapFunction.makeExplorationMap().setDestination(StructureTags.ON_TREASURE_MAPS).setMapDecoration(MapDecorationTypes.RED_X).setZoom((byte)1).setSkipKnownStructures(false)).apply((LootItemFunction.Builder)SetNameFunction.setName((Component)Component.translatable("filled_map.buried_treasure"), SetNameFunction.Target.ITEM_NAME))))
/*      */       
/* 1097 */       .withPool(LootPool.lootPool()
/* 1098 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1099 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 1100 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 1101 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLOCK))
/* 1102 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1103 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1104 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1106 */       .withPool(LootPool.lootPool()
/* 1107 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1108 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1109 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1111 */       .withPool(LootPool.lootPool()
/* 1112 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1113 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1114 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1115 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1116 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1117 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder bastionHoglinStableLootTable() {
/* 1122 */     return LootTable.lootTable()
/* 1123 */       .withPool(LootPool.lootPool()
/* 1124 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1125 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(15).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.8F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1126 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(12).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.15F, 0.95F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1127 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1128 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1129 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))))
/* 1130 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1131 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1132 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 17.0F))))
/* 1133 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1135 */       .withPool(LootPool.lootPool()
/* 1136 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1137 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1138 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1139 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GLOWSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/* 1140 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1141 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SOUL_SAND).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1142 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_NYLIUM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1143 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1144 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1145 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1146 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/* 1147 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1148 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1149 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_FUNGUS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1150 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRIMSON_ROOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F)))))
/*      */       
/* 1152 */       .withPool(LootPool.lootPool()
/* 1153 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1154 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1155 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1157 */       .withPool(LootPool.lootPool()
/* 1158 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1159 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1160 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder bastionBridgeLootTable() {
/* 1165 */     return LootTable.lootTable()
/* 1166 */       .withPool(LootPool.lootPool()
/* 1167 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1168 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.LODESTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1170 */       .withPool(LootPool.lootPool()
/* 1171 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 2.0F))
/* 1172 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.5F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1173 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(10.0F, 28.0F))))
/* 1174 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 12.0F))))
/* 1175 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/* 1176 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1177 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1178 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1179 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1180 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1181 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1182 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1183 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1184 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1186 */       .withPool(LootPool.lootPool()
/* 1187 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1188 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1189 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1190 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1191 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1192 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F)))))
/*      */       
/* 1194 */       .withPool(LootPool.lootPool()
/* 1195 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1196 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1197 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1199 */       .withPool(LootPool.lootPool()
/* 1200 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1201 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1202 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder endCityTreasureLootTable() {
/* 1207 */     return LootTable.lootTable()
/* 1208 */       .withPool(LootPool.lootPool()
/* 1209 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 6.0F))
/* 1210 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1211 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/* 1212 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1213 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1214 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1215 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 1216 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1217 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1218 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1219 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1220 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1221 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1222 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1223 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1224 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1225 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1226 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1227 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1228 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1229 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1230 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1231 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1232 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1233 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/* 1234 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SHOVEL).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F)))))
/*      */       
/* 1236 */       .withPool(LootPool.lootPool()
/* 1237 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1238 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(14))
/* 1239 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder netherBridgeLootTable() {
/* 1244 */     return LootTable.lootTable()
/* 1245 */       .withPool(LootPool.lootPool()
/* 1246 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1247 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1248 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1249 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1250 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).setWeight(5))
/* 1251 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).setWeight(5))
/* 1252 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FLINT_AND_STEEL).setWeight(5))
/* 1253 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHER_WART).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1254 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(10))
/* 1255 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(8))
/* 1256 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(5))
/* 1257 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(5))
/* 1258 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(3))
/* 1259 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1261 */       .withPool(LootPool.lootPool()
/* 1262 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1263 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(14))
/* 1264 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder bastionTreasureLootTable() {
/* 1269 */     return LootTable.lootTable()
/* 1270 */       .withPool(LootPool.lootPool()
/* 1271 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1272 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1273 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1274 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(8).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1275 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ANCIENT_DEBRIS).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))))
/* 1276 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1277 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1278 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1279 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1280 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1281 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1282 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SWORD).setWeight(6))
/* 1283 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SPEAR).setWeight(6))
/* 1284 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1285 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HELMET).setWeight(5))
/* 1286 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_BOOTS).setWeight(5))
/* 1287 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(5))
/* 1288 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1289 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1291 */       .withPool(LootPool.lootPool()
/* 1292 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1293 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(12.0F, 25.0F))))
/* 1294 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1295 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1296 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 9.0F))))
/* 1297 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 9.0F))))
/* 1298 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))
/* 1299 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.QUARTZ).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(8.0F, 23.0F))))
/* 1300 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 15.0F))))
/* 1301 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F)))))
/*      */       
/* 1303 */       .withPool(LootPool.lootPool()
/* 1304 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1305 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1306 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1308 */       .withPool(LootPool.lootPool()
/* 1309 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1310 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder bastionOtherLootTable() {
/* 1315 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1316 */     return LootTable.lootTable()
/* 1317 */       .withPool(LootPool.lootPool()
/* 1318 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1319 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_PICKAXE).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1320 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_SHOVEL).setWeight(6).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1321 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).setWeight(6).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1322 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ANCIENT_DEBRIS).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1323 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_SCRAP).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1324 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPECTRAL_ARROW).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(10.0F, 22.0F))))
/* 1325 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PIGLIN_BANNER_PATTERN).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1326 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_PIGSTEP).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1327 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CARROT).setWeight(12).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 17.0F))))
/* 1328 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(9).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1329 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED)))))
/*      */       
/* 1331 */       .withPool(LootPool.lootPool()
/* 1332 */         .setRolls((NumberProvider)ConstantValue.exactly(2.0F))
/* 1333 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(2).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.1F, 0.9F))).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1334 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1335 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SOUL_SPEED))))
/* 1336 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1337 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GOLD_BLOCK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1338 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1339 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1340 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1341 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_SWORD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1342 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_CHESTPLATE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1343 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HELMET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1344 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_LEGGINGS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1345 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_BOOTS).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1346 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.CRYING_OBSIDIAN).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F)))))
/*      */       
/* 1348 */       .withPool(LootPool.lootPool()
/* 1349 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 4.0F))
/* 1350 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.GILDED_BLACKSTONE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1351 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.IRON_CHAIN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 10.0F))))
/* 1352 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 6.0F))))
/* 1353 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BONE_BLOCK).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/* 1354 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1355 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.OBSIDIAN).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1356 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 8.0F))))
/* 1357 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1358 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(5.0F, 17.0F))))
/* 1359 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*      */       
/* 1361 */       .withPool(LootPool.lootPool()
/* 1362 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1363 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(11))
/* 1364 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)))
/*      */       
/* 1366 */       .withPool(LootPool.lootPool()
/* 1367 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1368 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1369 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder woodlandMansionLootTable() {
/* 1374 */     return LootTable.lootTable()
/* 1375 */       .withPool(LootPool.lootPool()
/* 1376 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 1377 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(20))
/* 1378 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(15))
/* 1379 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1380 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(15))
/* 1381 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(15))
/* 1382 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE).setWeight(10))
/* 1383 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(15))
/* 1384 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_CHESTPLATE).setWeight(5))
/* 1385 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1387 */       .withPool(LootPool.lootPool()
/* 1388 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 4.0F))
/* 1389 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1390 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1391 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(20))
/* 1392 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1393 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BUCKET).setWeight(10))
/* 1394 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1395 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1396 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1397 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1398 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/* 1399 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RESIN_CLUMP).setWeight(50).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*      */       
/* 1401 */       .withPool(LootPool.lootPool()
/* 1402 */         .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/* 1403 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1404 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1405 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1406 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1408 */       .withPool(LootPool.lootPool()
/* 1409 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1410 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/* 1411 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder strongholdLibraryLootTable() {
/* 1416 */     return LootTable.lootTable()
/* 1417 */       .withPool(LootPool.lootPool()
/* 1418 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 10.0F))
/* 1419 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1420 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1421 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/* 1422 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS))
/* 1423 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1425 */       .withPool(LootPool.lootPool()
/* 1426 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1427 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder strongholdCorridorLootTable() {
/* 1432 */     return LootTable.lootTable()
/* 1433 */       .withPool(LootPool.lootPool()
/* 1434 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1435 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).setWeight(10))
/* 1436 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1437 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1438 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1439 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/* 1440 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1441 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.APPLE).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1442 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/* 1443 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_SWORD).setWeight(5))
/* 1444 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_CHESTPLATE).setWeight(5))
/* 1445 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HELMET).setWeight(5))
/* 1446 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(5))
/* 1447 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_BOOTS).setWeight(5))
/* 1448 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE))
/* 1449 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1450 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1451 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1452 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1453 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1454 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE))
/* 1455 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1457 */       .withPool(LootPool.lootPool()
/* 1458 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1459 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(9))
/* 1460 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder ancientCityLootTable() {
/* 1465 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 1466 */     return LootTable.lootTable()
/* 1467 */       .withPool(LootPool.lootPool()
/* 1468 */         .setRolls((NumberProvider)UniformGenerator.between(5.0F, 10.0F))
/*      */         
/* 1471 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1472 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE).setWeight(1))
/*      */         
/* 1475 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1476 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_CATALYST).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 1477 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/* 1478 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1479 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1480 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1481 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(2))
/* 1482 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(2))
/* 1483 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/*      */         
/* 1486 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(3).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SWIFT_SNEAK))))
/* 1487 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 10.0F))))
/* 1488 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_SENSOR).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1489 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CANDLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 1490 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.AMETHYST_SHARD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1491 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1492 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1493 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/*      */         
/* 1495 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ECHO_SHARD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1496 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DISC_FRAGMENT_5).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*      */         
/* 1499 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
/* 1500 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1501 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 10.0F))))
/* 1502 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 1503 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SOUL_TORCH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/*      */         
/* 1506 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 15.0F)))))
/*      */       
/* 1508 */       .withPool(LootPool.lootPool()
/* 1509 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1510 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(75))
/* 1511 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(4))
/* 1512 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*      */   }
/*      */   
/*      */   public LootTable.Builder jungleTempleLootTable() {
/* 1517 */     return LootTable.lootTable()
/* 1518 */       .withPool(LootPool.lootPool()
/* 1519 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 6.0F))
/* 1520 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1521 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1522 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1523 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1524 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1525 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1526 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1527 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1528 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 1529 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 1530 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 1531 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 1532 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*      */       
/* 1534 */       .withPool(LootPool.lootPool()
/* 1535 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1536 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))
/* 1537 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder shipwreckTreasureLootTable() {
/* 1542 */     return LootTable.lootTable()
/* 1543 */       .withPool(LootPool.lootPool()
/* 1544 */         .setRolls((NumberProvider)UniformGenerator.between(3.0F, 6.0F))
/* 1545 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(90).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1546 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1547 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(40).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1548 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5))
/* 1549 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(5)))
/*      */       
/* 1551 */       .withPool(LootPool.lootPool()
/* 1552 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 5.0F))
/* 1553 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NUGGET).setWeight(50).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1554 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F))))
/* 1555 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 10.0F)))))
/*      */       
/* 1557 */       .withPool(LootPool.lootPool()
/* 1558 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1559 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5))
/* 1560 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*      */       
/* 1562 */       .withPool(LootPool.lootPool()
/* 1563 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1564 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(148))
/* 1565 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_NAUTILUS_ARMOR).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1566 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_NAUTILUS_ARMOR).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1567 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_NAUTILUS_ARMOR).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 1568 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_NAUTILUS_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder pillagerOutpostLootTable() {
/* 1573 */     HolderLookup.RegistryLookup<Instrument> instruments = this.registries.lookupOrThrow(Registries.INSTRUMENT);
/* 1574 */     return LootTable.lootTable()
/* 1575 */       .withPool(LootPool.lootPool()
/* 1576 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 1577 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW)))
/*      */       
/* 1579 */       .withPool(LootPool.lootPool()
/* 1580 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1581 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))
/* 1582 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 1583 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F)))))
/*      */       
/* 1585 */       .withPool(LootPool.lootPool()
/* 1586 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 1587 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*      */       
/* 1589 */       .withPool(LootPool.lootPool()
/* 1590 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 1591 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 1592 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 1593 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1594 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIPWIRE_HOOK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1595 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1596 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*      */       
/* 1598 */       .withPool(LootPool.lootPool()
/* 1599 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 1600 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOAT_HORN)).apply((LootItemFunction.Builder)SetInstrumentFunction.setInstrumentOptions((HolderSet)instruments.getOrThrow(InstrumentTags.REGULAR_GOAT_HORNS))))
/*      */       
/* 1602 */       .withPool(LootPool.lootPool()
/* 1603 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1604 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(3))
/* 1605 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */   
/*      */   public LootTable.Builder desertPyramidLootTable() {
/* 1610 */     return LootTable.lootTable()
/* 1611 */       .withPool(LootPool.lootPool()
/* 1612 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 1613 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1614 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1615 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 1616 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1617 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 1618 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 1619 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 1620 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 1621 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(15))
/* 1622 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/* 1623 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 1624 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 1625 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(20).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 1626 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/* 1627 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 1628 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(15)))
/*      */       
/* 1630 */       .withPool(LootPool.lootPool()
/* 1631 */         .setRolls((NumberProvider)ConstantValue.exactly(4.0F))
/* 1632 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1633 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1634 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1635 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 1636 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SAND).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F)))))
/*      */       
/* 1638 */       .withPool(LootPool.lootPool()
/* 1639 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 1640 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(6))
/* 1641 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaChestLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */