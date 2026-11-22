/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.LootTableSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public final class VanillaGiftLoot extends Record implements LootTableSubProvider {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   public VanillaGiftLoot(HolderLookup.Provider registries) {
/*  33 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaGiftLoot;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #33	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaGiftLoot;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider registries() {
/*  33 */     return this.registries;
/*     */   }
/*     */   
/*     */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/*  36 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.CHICKEN_VARIANT);
/*  38 */     output.accept(BuiltInLootTables.CAT_MORNING_GIFT, 
/*  39 */         LootTable.lootTable()
/*  40 */         .withPool(LootPool.lootPool()
/*  41 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  42 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).setWeight(10))
/*  43 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT).setWeight(10))
/*  44 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).setWeight(10))
/*  45 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).setWeight(10))
/*  46 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).setWeight(10))
/*  47 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).setWeight(10))
/*  48 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).setWeight(2))));
/*  51 */     output.accept(BuiltInLootTables.ARMORER_GIFT, 
/*  52 */         LootTable.lootTable()
/*  53 */         .withPool(LootPool.lootPool()
/*  54 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  55 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_HELMET))
/*  56 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_CHESTPLATE))
/*  57 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_LEGGINGS))
/*  58 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHAINMAIL_BOOTS))));
/*  61 */     output.accept(BuiltInLootTables.BUTCHER_GIFT, 
/*  62 */         LootTable.lootTable()
/*  63 */         .withPool(LootPool.lootPool()
/*  64 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  65 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_RABBIT))
/*  66 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_CHICKEN))
/*  67 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_PORKCHOP))
/*  68 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_BEEF))
/*  69 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKED_MUTTON))));
/*  72 */     output.accept(BuiltInLootTables.CARTOGRAPHER_GIFT, 
/*  73 */         LootTable.lootTable()
/*  74 */         .withPool(LootPool.lootPool()
/*  75 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  76 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAP))
/*  77 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PAPER))));
/*  80 */     output.accept(BuiltInLootTables.CLERIC_GIFT, 
/*  81 */         LootTable.lootTable()
/*  82 */         .withPool(LootPool.lootPool()
/*  83 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  84 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE))
/*  85 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LAPIS_LAZULI))));
/*  88 */     output.accept(BuiltInLootTables.FARMER_GIFT, 
/*  89 */         LootTable.lootTable()
/*  90 */         .withPool(LootPool.lootPool()
/*  91 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  92 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREAD))
/*  93 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUMPKIN_PIE))
/*  94 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COOKIE))));
/*  98 */     output.accept(BuiltInLootTables.FISHERMAN_GIFT, 
/*  99 */         LootTable.lootTable()
/* 100 */         .withPool(LootPool.lootPool()
/* 101 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 102 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD))
/* 103 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON))));
/* 106 */     output.accept(BuiltInLootTables.FLETCHER_GIFT, 
/* 107 */         LootTable.lootTable()
/* 108 */         .withPool(LootPool.lootPool()
/* 109 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 110 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).setWeight(26))
/* 111 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SWIFTNESS)))
/* 112 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)))
/* 113 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.STRENGTH)))
/* 114 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HEALING)))
/* 115 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.HARMING)))
/* 116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.LEAPING)))
/* 117 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.REGENERATION)))
/* 118 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
/* 119 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
/* 120 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.INVISIBILITY)))
/* 121 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.NIGHT_VISION)))
/* 122 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)))
/* 123 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))));
/* 126 */     output.accept(BuiltInLootTables.LEATHERWORKER_GIFT, 
/* 127 */         LootTable.lootTable()
/* 128 */         .withPool(LootPool.lootPool()
/* 129 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 130 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER))));
/* 133 */     output.accept(BuiltInLootTables.LIBRARIAN_GIFT, 
/* 134 */         LootTable.lootTable()
/* 135 */         .withPool(LootPool.lootPool()
/* 136 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 137 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOOK))));
/* 140 */     output.accept(BuiltInLootTables.MASON_GIFT, 
/* 141 */         LootTable.lootTable()
/* 142 */         .withPool(LootPool.lootPool()
/* 143 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 144 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CLAY))));
/* 148 */     output.accept(BuiltInLootTables.SHEPHERD_GIFT, 
/* 149 */         LootTable.lootTable()
/* 150 */         .withPool(LootPool.lootPool()
/* 151 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 152 */           .addAll(Items.WOOL.map(LootItem::lootTableItem).asList())));
/* 155 */     output.accept(BuiltInLootTables.TOOLSMITH_GIFT, 
/* 156 */         LootTable.lootTable()
/* 157 */         .withPool(LootPool.lootPool()
/* 158 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 159 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_PICKAXE))
/* 160 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 161 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_HOE))
/* 162 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_SHOVEL))));
/* 165 */     output.accept(BuiltInLootTables.WEAPONSMITH_GIFT, 
/* 166 */         LootTable.lootTable()
/* 167 */         .withPool(LootPool.lootPool()
/* 168 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 169 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STONE_AXE))
/* 170 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLDEN_AXE))
/* 171 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_AXE))));
/* 174 */     output.accept(BuiltInLootTables.UNEMPLOYED_GIFT, 
/* 175 */         LootTable.lootTable()
/* 176 */         .withPool(LootPool.lootPool()
/* 177 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 178 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.WHEAT_SEEDS))));
/* 181 */     output.accept(BuiltInLootTables.BABY_VILLAGER_GIFT, 
/* 182 */         LootTable.lootTable()
/* 183 */         .withPool(LootPool.lootPool()
/* 184 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 185 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POPPY))));
/* 188 */     output.accept(BuiltInLootTables.SNIFFER_DIGGING, 
/* 189 */         LootTable.lootTable()
/* 190 */         .withPool(LootPool.lootPool()
/* 191 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 192 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TORCHFLOWER_SEEDS))
/* 193 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PITCHER_POD))));
/* 196 */     output.accept(BuiltInLootTables.PANDA_SNEEZE, 
/* 197 */         LootTable.lootTable()
/* 198 */         .withPool(LootPool.lootPool()
/* 199 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 200 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).setWeight(1))
/* 201 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(699))));
/* 204 */     output.accept(BuiltInLootTables.CHICKEN_LAY, 
/* 205 */         LootTable.lootTable()
/* 206 */         .withPool(LootPool.lootPool()
/* 207 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 208 */           .add((LootPoolEntryContainer.Builder)AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] { LootItem.lootTableItem((ItemLike)Items.EGG)
/* 210 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, registryLookup.getOrThrow(ChickenVariants.TEMPERATE))))), 
/* 211 */                 LootItem.lootTableItem((ItemLike)Items.BROWN_EGG)
/* 212 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, registryLookup.getOrThrow(ChickenVariants.WARM))))), 
/* 213 */                 LootItem.lootTableItem((ItemLike)Items.BLUE_EGG)
/* 214 */                 .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().components(DataComponentExactPredicate.expect(DataComponents.CHICKEN_VARIANT, registryLookup.getOrThrow(ChickenVariants.COLD))))) }))));
/* 216 */     output.accept(BuiltInLootTables.ARMADILLO_SHED, 
/* 217 */         LootTable.lootTable()
/* 218 */         .withPool(LootPool.lootPool()
/* 219 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 220 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARMADILLO_SCUTE))));
/* 223 */     output.accept(BuiltInLootTables.TURTLE_GROW, 
/* 224 */         LootTable.lootTable()
/* 225 */         .withPool(LootPool.lootPool()
/* 226 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 227 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TURTLE_SCUTE))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaGiftLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */