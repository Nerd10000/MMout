/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.InstrumentTags;
/*     */ import net.minecraft.world.item.Instrument;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetInstrumentFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class TradeRebalanceChestLoot extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   public TradeRebalanceChestLoot(HolderLookup.Provider registries) {
/*  38 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/TradeRebalanceChestLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider registries() {
/*  38 */     return this.registries;
/*     */   }
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  41 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  42 */     HolderLookup.RegistryLookup<Biome> biomes = this.registries.lookupOrThrow(Registries.BIOME);
/*  43 */     output.accept(BuiltInLootTables.ABANDONED_MINESHAFT, 
/*  44 */         LootTable.lootTable()
/*  45 */         .withPool(LootPool.lootPool()
/*  46 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  47 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE))
/*  49 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAME_TAG).setWeight(30))
/*  50 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/*  51 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_PICKAXE).setWeight(5))
/*  52 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(5)))
/*     */         
/*  54 */         .withPool(LootPool.lootPool()
/*  55 */           .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/*  56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/*  57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*  59 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 9.0F))))
/*  60 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/*  61 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 8.0F))))
/*  62 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*  63 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 6.0F))))
/*  64 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MELON_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  65 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))))
/*  66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEETROOT_SEEDS).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F)))))
/*     */         
/*  68 */         .withPool(LootPool.lootPool()
/*  69 */           .setRolls((NumberProvider)ConstantValue.exactly(3.0F))
/*  70 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.RAIL).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))))
/*  71 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POWERED_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  72 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DETECTOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  73 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.ACTIVATOR_RAIL).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/*  74 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.TORCH).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 16.0F))))
/*  75 */           .add((LootPoolEntryContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_BOUNCE)
/*  76 */             .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes((HolderSet)HolderSet.direct(new Holder[] { (Holder)biomes
/*  77 */                       .getOrThrow(Biomes.SULFUR_CAVES) }))))).setWeight(10)))
/*     */         
/*  81 */         .withPool(LootPool.lootPool()
/*  82 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  83 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(4))
/*  84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.EFFICIENCY))))));
/*  88 */     output.accept(BuiltInLootTables.ANCIENT_CITY, ancientCityLootTable());
/*  89 */     output.accept(BuiltInLootTables.DESERT_PYRAMID, desertPyramidLootTable());
/*  90 */     output.accept(BuiltInLootTables.JUNGLE_TEMPLE, jungleTempleLootTable());
/*  91 */     output.accept(BuiltInLootTables.PILLAGER_OUTPOST, pillagerOutpostLootTable());
/*     */   }
/*     */   
/*     */   public LootTable.Builder pillagerOutpostLootTable() {
/*  95 */     HolderLookup.RegistryLookup<Instrument> instruments = this.registries.lookupOrThrow(Registries.INSTRUMENT);
/*  96 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  97 */     return LootTable.lootTable()
/*  98 */       .withPool(LootPool.lootPool()
/*  99 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 100 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CROSSBOW)))
/*     */       
/* 102 */       .withPool(LootPool.lootPool()
/* 103 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 104 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))
/* 105 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))))
/* 106 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F)))))
/*     */       
/* 108 */       .withPool(LootPool.lootPool()
/* 109 */         .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 110 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.DARK_OAK_LOG).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F)))))
/*     */       
/* 112 */       .withPool(LootPool.lootPool()
/* 113 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 3.0F))
/* 114 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(7))
/* 115 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 6.0F))))
/* 116 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 117 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TRIPWIRE_HOOK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 118 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 119 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(1).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries))))
/*     */       
/* 121 */       .withPool(LootPool.lootPool()
/* 122 */         .setRolls((NumberProvider)UniformGenerator.between(0.0F, 1.0F))
/* 123 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOAT_HORN)).apply((LootItemFunction.Builder)SetInstrumentFunction.setInstrumentOptions((HolderSet)instruments.getOrThrow(InstrumentTags.REGULAR_GOAT_HORNS))))
/*     */       
/* 125 */       .withPool(LootPool.lootPool()
/* 126 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 127 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(3))
/* 128 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*     */       
/* 130 */       .withPool(LootPool.lootPool()
/* 131 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 132 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/* 133 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.QUICK_CHARGE)))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder desertPyramidLootTable() {
/* 138 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 139 */     return LootTable.lootTable()
/* 140 */       .withPool(LootPool.lootPool()
/* 141 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 4.0F))
/* 142 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 143 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 144 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 145 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 146 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 147 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 148 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(25).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 149 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(20))
/* 150 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR).setWeight(15))
/* 151 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR).setWeight(15))
/* 152 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR).setWeight(10))
/* 153 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(5))
/* 154 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(10).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 155 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_APPLE).setWeight(20))
/* 156 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
/* 157 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(15)))
/*     */       
/* 159 */       .withPool(LootPool.lootPool()
/* 160 */         .setRolls((NumberProvider)ConstantValue.exactly(4.0F))
/* 161 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 162 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 163 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 164 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F))))
/* 165 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SAND).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 8.0F)))))
/*     */       
/* 167 */       .withPool(LootPool.lootPool()
/* 168 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 169 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(4))
/* 170 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F))))
/* 171 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(2).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.UNBREAKING)))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder ancientCityLootTable() {
/* 176 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 177 */     return LootTable.lootTable()
/* 178 */       .withPool(LootPool.lootPool()
/* 179 */         .setRolls((NumberProvider)UniformGenerator.between(5.0F, 10.0F))
/*     */         
/* 182 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENCHANTED_GOLDEN_APPLE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 183 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_OTHERSIDE).setWeight(1))
/*     */         
/* 186 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COMPASS).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 187 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_CATALYST).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 188 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HOE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetItemDamageFunction.setDamage((NumberProvider)UniformGenerator.between(0.8F, 1.0F))).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/* 189 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEAD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 190 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 191 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))
/* 192 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_13).setWeight(2))
/* 193 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_CAT).setWeight(2))
/* 194 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_LEGGINGS).setWeight(2).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(30.0F, 50.0F))))
/*     */         
/* 197 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(3).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.SWIFT_SNEAK))))
/* 198 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 10.0F))))
/* 199 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_SENSOR).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 200 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CANDLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 4.0F))))
/* 201 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.AMETHYST_SHARD).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 202 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EXPERIENCE_BOTTLE).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 203 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_BERRIES).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 204 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_LEGGINGS).setWeight(3).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)UniformGenerator.between(20.0F, 39.0F))))
/*     */         
/* 206 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ECHO_SHARD).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 207 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DISC_FRAGMENT_5).setWeight(4).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/*     */         
/* 210 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTION).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRONG_REGENERATION)))
/* 211 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
/* 212 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 10.0F))))
/* 213 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/* 214 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SOUL_TORCH).setWeight(5).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 15.0F))))
/*     */         
/* 217 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).setWeight(7).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(6.0F, 15.0F)))))
/*     */       
/* 219 */       .withPool(LootPool.lootPool()
/* 220 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 221 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(71))
/* 222 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).setWeight(4).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.MENDING))))
/* 223 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(4))
/* 224 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*     */   }
/*     */   
/*     */   public LootTable.Builder jungleTempleLootTable() {
/* 229 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 230 */     return LootTable.lootTable()
/* 231 */       .withPool(LootPool.lootPool()
/* 232 */         .setRolls((NumberProvider)UniformGenerator.between(2.0F, 6.0F))
/* 233 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 234 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).setWeight(10).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 5.0F))))
/* 235 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 7.0F))))
/* 236 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).setWeight(15).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 237 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))))
/* 238 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).setWeight(20).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 6.0F))))
/* 239 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(16).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 7.0F))))
/* 240 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).setWeight(3))
/* 241 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_HORSE_ARMOR))
/* 242 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_HORSE_ARMOR))
/* 243 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_HORSE_ARMOR))
/* 244 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.DIAMOND_HORSE_ARMOR))
/* 245 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)EnchantWithLevelsFunction.enchantWithLevels(this.registries, (NumberProvider)ConstantValue.exactly(30.0F)))))
/*     */       
/* 247 */       .withPool(LootPool.lootPool()
/* 248 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 249 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(2))
/* 250 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(2.0F)))))
/*     */       
/* 252 */       .withPool(LootPool.lootPool()
/* 253 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 254 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(1))
/* 255 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK).apply((LootItemFunction.Builder)(new EnchantRandomlyFunction.Builder()).withEnchantment((Holder)enchantments.getOrThrow(Enchantments.UNBREAKING)))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\TradeRebalanceChestLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */