/*     */ package net.minecraft.data.loot.packs;
/*     */ 
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.advancements.predicates.TagPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.CubeMobPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
/*     */ import net.minecraft.advancements.predicates.entity.RaiderPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.loot.EntityLootSubProvider;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariants;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ColorCollection;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootPool;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*     */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*     */ import net.minecraft.world.level.storage.loot.entries.TagEntry;
/*     */ import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetOminousBottleAmplifierFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*     */ 
/*     */ public class VanillaEntityLoot extends EntityLootSubProvider {
/*     */   public VanillaEntityLoot(HolderLookup.Provider registries) {
/*  56 */     super(FeatureFlags.REGISTRY.allFlags(), registries);
/*     */   }
/*     */   
/*     */   public void generate() {
/*  61 */     HolderLookup.RegistryLookup registryLookup1 = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
/*  62 */     HolderLookup.RegistryLookup registryLookup2 = this.registries.lookupOrThrow(Registries.FROG_VARIANT);
/*  64 */     add(EntityTypes.ALLAY, 
/*  65 */         LootTable.lootTable());
/*  68 */     add(EntityTypes.ARMADILLO, 
/*  69 */         LootTable.lootTable());
/*  72 */     add(EntityTypes.ARMOR_STAND, 
/*  73 */         LootTable.lootTable());
/*  76 */     add(EntityTypes.AXOLOTL, 
/*  77 */         LootTable.lootTable());
/*  80 */     add(EntityTypes.BAT, 
/*  81 */         LootTable.lootTable());
/*  84 */     add(EntityTypes.BEE, 
/*  85 */         LootTable.lootTable());
/*  88 */     add(EntityTypes.BLAZE, 
/*  89 */         LootTable.lootTable()
/*  90 */         .withPool(LootPool.lootPool()
/*  91 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/*  92 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BLAZE_ROD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/*  93 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/*  96 */     add(EntityTypes.BOGGED, 
/*  97 */         LootTable.lootTable()
/*  98 */         .withPool(LootPool.lootPool()
/*  99 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 100 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 102 */         .withPool(LootPool.lootPool()
/* 103 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 104 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 106 */         .withPool(LootPool.lootPool()
/* 107 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 108 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.POISON)))
/* 109 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 112 */     add(EntityTypes.CAT, 
/* 113 */         LootTable.lootTable()
/* 114 */         .withPool(LootPool.lootPool()
/* 115 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 116 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))))));
/* 120 */     add(EntityTypes.CAMEL, 
/* 121 */         LootTable.lootTable());
/* 124 */     add(EntityTypes.CAMEL_HUSK, 
/* 125 */         LootTable.lootTable()
/* 126 */         .withPool(LootPool.lootPool()
/* 127 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 128 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 131 */     add(EntityTypes.CAVE_SPIDER, 
/* 132 */         LootTable.lootTable()
/* 133 */         .withPool(LootPool.lootPool()
/* 134 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 135 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 137 */         .withPool(LootPool.lootPool()
/* 138 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 139 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 140 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 143 */     add(EntityTypes.CHICKEN, 
/* 144 */         LootTable.lootTable()
/* 145 */         .withPool(LootPool.lootPool()
/* 146 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 147 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 149 */         .withPool(LootPool.lootPool()
/* 150 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 151 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CHICKEN).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 155 */     add(EntityTypes.COD, 
/* 156 */         LootTable.lootTable()
/* 157 */         .withPool(LootPool.lootPool()
/* 158 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 159 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot()))))
/*     */         
/* 161 */         .withPool(LootPool.lootPool()
/* 162 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 163 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 164 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/* 167 */     add(EntityTypes.COPPER_GOLEM, 
/* 168 */         LootTable.lootTable()
/* 169 */         .withPool(LootPool.lootPool()
/* 170 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 171 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 175 */     add(EntityTypes.COW, 
/* 176 */         LootTable.lootTable()
/* 177 */         .withPool(LootPool.lootPool()
/* 178 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 179 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 181 */         .withPool(LootPool.lootPool()
/* 182 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 183 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 187 */     add(EntityTypes.CREEPER, 
/* 188 */         LootTable.lootTable()
/* 189 */         .withPool(LootPool.lootPool()
/* 190 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 191 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 193 */         .withPool(LootPool.lootPool()
/* 194 */           .add((LootPoolEntryContainer.Builder)TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS))
/* 195 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypeTags.SKELETONS)))));
/* 198 */     add(EntityTypes.DOLPHIN, 
/* 199 */         LootTable.lootTable()
/* 200 */         .withPool(LootPool.lootPool()
/* 201 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 202 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))));
/* 206 */     add(EntityTypes.DONKEY, 
/* 207 */         LootTable.lootTable()
/* 208 */         .withPool(LootPool.lootPool()
/* 209 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 210 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 214 */     add(EntityTypes.DROWNED, 
/* 215 */         LootTable.lootTable()
/* 216 */         .withPool(LootPool.lootPool()
/* 217 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 218 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 220 */         .withPool(LootPool.lootPool()
/* 221 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 222 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COPPER_INGOT))
/* 223 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 224 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.11F, 0.02F))));
/* 227 */     add(EntityTypes.ELDER_GUARDIAN, elderGuardianLootTable());
/* 229 */     add(EntityTypes.ENDER_DRAGON, 
/* 230 */         LootTable.lootTable());
/* 233 */     add(EntityTypes.ENDERMAN, 
/* 234 */         LootTable.lootTable()
/* 235 */         .withPool(LootPool.lootPool()
/* 236 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 237 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ENDER_PEARL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 241 */     add(EntityTypes.ENDERMITE, 
/* 242 */         LootTable.lootTable());
/* 245 */     add(EntityTypes.EVOKER, 
/* 246 */         LootTable.lootTable()
/* 247 */         .withPool(LootPool.lootPool()
/* 248 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 249 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TOTEM_OF_UNDYING)))
/*     */         
/* 251 */         .withPool(LootPool.lootPool()
/* 252 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 253 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 254 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 257 */     add(EntityTypes.BREEZE, 
/* 258 */         LootTable.lootTable()
/* 259 */         .withPool(LootPool.lootPool()
/* 260 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 261 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BREEZE_ROD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(1.0F, 2.0F))))
/* 262 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 265 */     add(EntityTypes.FOX, 
/* 266 */         LootTable.lootTable());
/* 269 */     add(EntityTypes.FROG, 
/* 270 */         LootTable.lootTable());
/* 273 */     add(EntityTypes.GHAST, 
/* 274 */         LootTable.lootTable()
/* 275 */         .withPool(LootPool.lootPool()
/* 276 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 277 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GHAST_TEAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 279 */         .withPool(LootPool.lootPool()
/* 280 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 281 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 283 */         .withPool(LootPool.lootPool()
/* 284 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 285 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_TEARS)).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))
/* 286 */           .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE)).direct(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.FIREBALL))))
/* 287 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 291 */     add(EntityTypes.HAPPY_GHAST, 
/* 292 */         LootTable.lootTable());
/* 295 */     add(EntityTypes.GIANT, 
/* 296 */         LootTable.lootTable());
/* 299 */     add(EntityTypes.GLOW_SQUID, 
/* 300 */         LootTable.lootTable()
/* 301 */         .withPool(LootPool.lootPool()
/* 302 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 303 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOW_INK_SAC).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 307 */     add(EntityTypes.GOAT, 
/* 308 */         LootTable.lootTable());
/* 311 */     add(EntityTypes.GUARDIAN, 
/* 312 */         LootTable.lootTable()
/* 313 */         .withPool(LootPool.lootPool()
/* 314 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 315 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 317 */         .withPool(LootPool.lootPool()
/* 318 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 319 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 320 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 321 */           .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */         
/* 323 */         .withPool(LootPool.lootPool()
/* 324 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 325 */           .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 326 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 327 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/* 330 */     add(EntityTypes.HORSE, 
/* 331 */         LootTable.lootTable()
/* 332 */         .withPool(LootPool.lootPool()
/* 333 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 334 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 338 */     add(EntityTypes.HUSK, 
/* 339 */         LootTable.lootTable()
/* 340 */         .withPool(LootPool.lootPool()
/* 341 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 342 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 344 */         .withPool(LootPool.lootPool()
/* 345 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 346 */           .add(LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 347 */             .when(
/* 348 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/* 349 */                 EntityPredicate.Builder.entity().vehicle(
/* 350 */                   EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of((HolderGetter)registryLookup1, EntityTypes.CAMEL_HUSK)))
/* 351 */                 .build()))))
/*     */         
/* 356 */         .withPool(LootPool.lootPool()
/* 357 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 358 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 359 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 360 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 361 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 362 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/* 365 */     add(EntityTypes.RAVAGER, 
/* 366 */         LootTable.lootTable()
/* 367 */         .withPool(LootPool.lootPool()
/* 368 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 369 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SADDLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/* 373 */     add(EntityTypes.ILLUSIONER, 
/* 374 */         LootTable.lootTable());
/* 377 */     add(EntityTypes.IRON_GOLEM, 
/* 378 */         LootTable.lootTable()
/* 379 */         .withPool(LootPool.lootPool()
/* 380 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 381 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.POPPY).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F)))))
/*     */         
/* 383 */         .withPool(LootPool.lootPool()
/* 384 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 385 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(3.0F, 5.0F))))));
/* 389 */     add(EntityTypes.LLAMA, 
/* 390 */         LootTable.lootTable()
/* 391 */         .withPool(LootPool.lootPool()
/* 392 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 393 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 397 */     add(EntityTypes.MAGMA_CUBE, 
/* 398 */         LootTable.lootTable()
/* 399 */         .withPool(LootPool.lootPool()
/* 400 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 401 */           .add(((LootPoolSingletonContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MAGMA_CREAM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-2.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 402 */             .when(killedByFrog((HolderGetter)registryLookup1).invert()))
/* 403 */             .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().cubeMob(CubeMobPredicate.sized(MinMaxBounds.Ints.atLeast(2))))))
/*     */           
/* 405 */           .add(LootItem.lootTableItem((ItemLike)Items.PEARLESCENT_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.WARM)))
/* 406 */           .add(LootItem.lootTableItem((ItemLike)Items.VERDANT_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.COLD)))
/* 407 */           .add(LootItem.lootTableItem((ItemLike)Items.OCHRE_FROGLIGHT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrogVariant((HolderGetter)registryLookup1, (HolderGetter)registryLookup2, FrogVariants.TEMPERATE)))));
/* 411 */     add(EntityTypes.SULFUR_CUBE, 
/* 412 */         LootTable.lootTable());
/* 415 */     add(EntityTypes.MANNEQUIN, 
/* 416 */         LootTable.lootTable());
/* 419 */     add(EntityTypes.MULE, 
/* 420 */         LootTable.lootTable()
/* 421 */         .withPool(LootPool.lootPool()
/* 422 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 423 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 427 */     add(EntityTypes.MOOSHROOM, 
/* 428 */         LootTable.lootTable()
/* 429 */         .withPool(LootPool.lootPool()
/* 430 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 431 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 433 */         .withPool(LootPool.lootPool()
/* 434 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 435 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BEEF).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 439 */     add(EntityTypes.NAUTILUS, 
/* 440 */         LootTable.lootTable()
/* 441 */         .withPool(LootPool.lootPool()
/* 442 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 443 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.NAUTILUS_SHELL))
/* 444 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 445 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.05F, 0.01F))));
/* 449 */     add(EntityTypes.OCELOT, 
/* 450 */         LootTable.lootTable());
/* 453 */     add(EntityTypes.PANDA, 
/* 454 */         LootTable.lootTable()
/* 455 */         .withPool(LootPool.lootPool()
/* 456 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 457 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.BAMBOO).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))))));
/* 461 */     add(EntityTypes.PARROT, 
/* 462 */         LootTable.lootTable()
/* 463 */         .withPool(LootPool.lootPool()
/* 464 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 465 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.FEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 469 */     add(EntityTypes.PHANTOM, 
/* 470 */         LootTable.lootTable()
/* 471 */         .withPool(LootPool.lootPool()
/* 472 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 473 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PHANTOM_MEMBRANE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 474 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 477 */     add(EntityTypes.PIG, 
/* 478 */         LootTable.lootTable()
/* 479 */         .withPool(LootPool.lootPool()
/* 480 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 481 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 485 */     add(EntityTypes.PILLAGER, 
/* 486 */         LootTable.lootTable()
/* 487 */         .withPool(LootPool.lootPool()
/* 488 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 489 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.OMINOUS_BOTTLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SetOminousBottleAmplifierFunction.setAmplifier((NumberProvider)UniformGenerator.between(0.0F, 4.0F))))
/* 490 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().raider(RaiderPredicate.CAPTAIN_WITHOUT_RAID)))));
/* 494 */     add(EntityTypes.PLAYER, 
/* 495 */         LootTable.lootTable());
/* 498 */     add(EntityTypes.POLAR_BEAR, 
/* 499 */         LootTable.lootTable()
/* 500 */         .withPool(LootPool.lootPool()
/* 501 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 502 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 503 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 507 */     add(EntityTypes.PUFFERFISH, 
/* 508 */         LootTable.lootTable()
/* 509 */         .withPool(LootPool.lootPool()
/* 510 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 511 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PUFFERFISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*     */         
/* 513 */         .withPool(LootPool.lootPool()
/* 514 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 515 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 516 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/* 519 */     add(EntityTypes.RABBIT, 
/* 520 */         LootTable.lootTable()
/* 521 */         .withPool(LootPool.lootPool()
/* 522 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 523 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_HIDE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 525 */         .withPool(LootPool.lootPool()
/* 526 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 527 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 529 */         .withPool(LootPool.lootPool()
/* 530 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 531 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.RABBIT_FOOT))
/* 532 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 533 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.1F, 0.03F))));
/* 536 */     add(EntityTypes.SALMON, 
/* 537 */         LootTable.lootTable()
/* 538 */         .withPool(LootPool.lootPool()
/* 539 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 540 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SALMON).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot()))))
/*     */         
/* 542 */         .withPool(LootPool.lootPool()
/* 543 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 544 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 545 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/* 548 */     add(EntityTypes.SHEEP, 
/* 549 */         LootTable.lootTable()
/* 550 */         .withPool(LootPool.lootPool()
/* 551 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 552 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUTTON).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 2.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 554 */         .withPool(
/* 555 */           createSheepDispatchPool(BuiltInLootTables.SHEEP)));
/* 559 */     ColorCollection.zipApply(BuiltInLootTables.SHEEP, Blocks.WOOL, (sheep, wool) -> add(EntityTypes.SHEEP, sheep, LootTable.lootTable().withPool(LootPool.lootPool().add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)wool)))));
/* 567 */     add(EntityTypes.SHULKER, 
/* 568 */         LootTable.lootTable()
/* 569 */         .withPool(LootPool.lootPool()
/* 570 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 571 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SHULKER_SHELL))
/* 572 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.5F, 0.0625F))));
/* 575 */     add(EntityTypes.SILVERFISH, 
/* 576 */         LootTable.lootTable());
/* 579 */     add(EntityTypes.SKELETON, 
/* 580 */         LootTable.lootTable()
/* 581 */         .withPool(LootPool.lootPool()
/* 582 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 583 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 585 */         .withPool(LootPool.lootPool()
/* 586 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 587 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 591 */     add(EntityTypes.SKELETON_HORSE, 
/* 592 */         LootTable.lootTable()
/* 593 */         .withPool(LootPool.lootPool()
/* 594 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 595 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 599 */     add(EntityTypes.SLIME, 
/* 600 */         LootTable.lootTable()
/* 601 */         .withPool(LootPool.lootPool()
/* 602 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 603 */           .add(LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).when(killedByFrog((HolderGetter)registryLookup1).invert()))
/* 604 */           .add(LootItem.lootTableItem((ItemLike)Items.SLIME_BALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F))).when(killedByFrog((HolderGetter)registryLookup1)))
/* 605 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().cubeMob(CubeMobPredicate.sized(MinMaxBounds.Ints.exactly(1)))))));
/* 609 */     add(EntityTypes.SNIFFER, 
/* 610 */         LootTable.lootTable());
/* 613 */     add(EntityTypes.SNOW_GOLEM, 
/* 614 */         LootTable.lootTable()
/* 615 */         .withPool(LootPool.lootPool()
/* 616 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 617 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SNOWBALL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 15.0F))))));
/* 621 */     add(EntityTypes.SPIDER, 
/* 622 */         LootTable.lootTable()
/* 623 */         .withPool(LootPool.lootPool()
/* 624 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 625 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 627 */         .withPool(LootPool.lootPool()
/* 628 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 629 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 630 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 633 */     add(EntityTypes.SQUID, 
/* 634 */         LootTable.lootTable()
/* 635 */         .withPool(LootPool.lootPool()
/* 636 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 637 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.INK_SAC).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 641 */     add(EntityTypes.STRAY, 
/* 642 */         LootTable.lootTable()
/* 643 */         .withPool(LootPool.lootPool()
/* 644 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 645 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 647 */         .withPool(LootPool.lootPool()
/* 648 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 649 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 651 */         .withPool(LootPool.lootPool()
/* 652 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 653 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.SLOWNESS)))
/* 654 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 657 */     add(EntityTypes.PARCHED, 
/* 658 */         LootTable.lootTable()
/* 659 */         .withPool(LootPool.lootPool()
/* 660 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 661 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 663 */         .withPool(LootPool.lootPool()
/* 664 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 665 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 667 */         .withPool(LootPool.lootPool()
/* 668 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 669 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIPPED_ARROW).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply((LootItemFunction.Builder)SetPotionFunction.setPotion(Potions.WEAKNESS)))
/* 670 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 673 */     add(EntityTypes.STRIDER, 
/* 674 */         LootTable.lootTable()
/* 675 */         .withPool(LootPool.lootPool()
/* 676 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 677 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STRING).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 5.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 681 */     add(EntityTypes.TADPOLE, 
/* 682 */         LootTable.lootTable());
/* 685 */     add(EntityTypes.TRADER_LLAMA, 
/* 686 */         LootTable.lootTable()
/* 687 */         .withPool(LootPool.lootPool()
/* 688 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 689 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 693 */     add(EntityTypes.TROPICAL_FISH, 
/* 694 */         LootTable.lootTable()
/* 695 */         .withPool(LootPool.lootPool()
/* 696 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 697 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TROPICAL_FISH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)ConstantValue.exactly(1.0F)))))
/*     */         
/* 699 */         .withPool(LootPool.lootPool()
/* 700 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 701 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE_MEAL))
/* 702 */           .when(LootItemRandomChanceCondition.randomChance(0.05F))));
/* 705 */     add(EntityTypes.TURTLE, 
/* 706 */         LootTable.lootTable()
/* 707 */         .withPool(LootPool.lootPool()
/* 708 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 709 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.SEAGRASS).setWeight(3).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 711 */         .withPool(LootPool.lootPool()
/* 712 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 713 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BOWL))
/* 714 */           .when(DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_LIGHTNING))))));
/* 718 */     add(EntityTypes.VEX, 
/* 719 */         LootTable.lootTable());
/* 722 */     add(EntityTypes.VILLAGER, 
/* 723 */         LootTable.lootTable());
/* 726 */     add(EntityTypes.WARDEN, 
/* 727 */         LootTable.lootTable()
/* 728 */         .withPool(LootPool.lootPool()
/* 729 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 730 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SCULK_CATALYST))));
/* 733 */     add(EntityTypes.WANDERING_TRADER, 
/* 734 */         LootTable.lootTable());
/* 737 */     add(EntityTypes.VINDICATOR, 
/* 738 */         LootTable.lootTable()
/* 739 */         .withPool(LootPool.lootPool()
/* 740 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 741 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.EMERALD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 742 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 745 */     add(EntityTypes.WITCH, 
/* 746 */         LootTable.lootTable()
/* 747 */         .withPool(LootPool.lootPool()
/* 748 */           .setRolls((NumberProvider)UniformGenerator.between(1.0F, 3.0F))
/* 749 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLOWSTONE_DUST).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 750 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SUGAR).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 751 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.SPIDER_EYE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 752 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GLASS_BOTTLE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 753 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GUNPOWDER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 754 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.STICK).setWeight(2).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 756 */         .withPool(LootPool.lootPool()
/* 757 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 758 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.REDSTONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(4.0F, 8.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 762 */     add(EntityTypes.WITHER, 
/* 763 */         LootTable.lootTable());
/* 766 */     add(EntityTypes.WITHER_SKELETON, 
/* 767 */         LootTable.lootTable()
/* 768 */         .withPool(LootPool.lootPool()
/* 769 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 770 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COAL).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(-1.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 772 */         .withPool(LootPool.lootPool()
/* 773 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 774 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.BONE).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 776 */         .withPool(LootPool.lootPool()
/* 777 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 778 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WITHER_SKELETON_SKULL))
/* 779 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 780 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/* 783 */     add(EntityTypes.WOLF, 
/* 784 */         LootTable.lootTable());
/* 787 */     add(EntityTypes.ZOGLIN, 
/* 788 */         LootTable.lootTable()
/* 789 */         .withPool(LootPool.lootPool()
/* 790 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 791 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(1.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 795 */     add(EntityTypes.CREAKING, 
/* 796 */         LootTable.lootTable());
/* 799 */     add(EntityTypes.ZOMBIE, 
/* 800 */         LootTable.lootTable()
/* 801 */         .withPool(LootPool.lootPool()
/* 802 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 803 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 805 */         .withPool(LootPool.lootPool()
/* 806 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 807 */           .add(LootItem.lootTableItem((ItemLike)Items.RED_MUSHROOM).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))
/* 808 */             .when(
/* 809 */               LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, 
/* 810 */                 EntityPredicate.Builder.entity().vehicle(
/* 811 */                   EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of((HolderGetter)registryLookup1, EntityTypes.ZOMBIE_HORSE)))
/* 812 */                 .build()))))
/*     */         
/* 817 */         .withPool(LootPool.lootPool()
/* 818 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 819 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 820 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 821 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 822 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 823 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F)))
/* 824 */         .withPool(LootPool.lootPool()
/* 825 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.MUSIC_DISC_LAVA_CHICKEN))
/* 826 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 827 */           .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity()
/* 828 */               .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(Boolean.valueOf(true)))
/* 829 */               .vehicle(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup1, EntityTypes.CHICKEN))))));
/* 832 */     add(EntityTypes.ZOMBIE_HORSE, 
/* 833 */         LootTable.lootTable()
/* 834 */         .withPool(LootPool.lootPool()
/* 835 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 836 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 840 */     add(EntityTypes.ZOMBIE_NAUTILUS, 
/* 841 */         LootTable.lootTable()
/* 842 */         .withPool(LootPool.lootPool()
/* 843 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 844 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 3.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 845 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())));
/* 849 */     add(EntityTypes.ZOMBIFIED_PIGLIN, 
/* 850 */         LootTable.lootTable()
/* 851 */         .withPool(LootPool.lootPool()
/* 852 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 853 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 855 */         .withPool(LootPool.lootPool()
/* 856 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 857 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_NUGGET).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 859 */         .withPool(LootPool.lootPool()
/* 860 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 861 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.GOLD_INGOT))
/* 862 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 863 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/* 866 */     add(EntityTypes.HOGLIN, 
/* 867 */         LootTable.lootTable()
/* 868 */         .withPool(LootPool.lootPool()
/* 869 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 870 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PORKCHOP).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(2.0F, 4.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 872 */         .withPool(LootPool.lootPool()
/* 873 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 874 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.LEATHER).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))));
/* 878 */     add(EntityTypes.PIGLIN, 
/* 879 */         LootTable.lootTable());
/* 882 */     add(EntityTypes.PIGLIN_BRUTE, 
/* 883 */         LootTable.lootTable());
/* 886 */     add(EntityTypes.ZOMBIE_VILLAGER, 
/* 887 */         LootTable.lootTable()
/* 888 */         .withPool(LootPool.lootPool()
/* 889 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 890 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.ROTTEN_FLESH).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */         
/* 892 */         .withPool(LootPool.lootPool()
/* 893 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 894 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.IRON_INGOT))
/* 895 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.CARROT))
/* 896 */           .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.POTATO).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 897 */           .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 898 */           .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F))));
/*     */   }
/*     */   
/*     */   public LootTable.Builder elderGuardianLootTable() {
/* 903 */     return LootTable.lootTable()
/* 904 */       .withPool(LootPool.lootPool()
/* 905 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 906 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_SHARD).apply((LootItemFunction.Builder)SetItemCountFunction.setCount((NumberProvider)UniformGenerator.between(0.0F, 2.0F))).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F)))))
/*     */       
/* 908 */       .withPool(LootPool.lootPool()
/* 909 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 910 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.COD).setWeight(3).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 911 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.PRISMARINE_CRYSTALS).setWeight(2).apply((LootItemFunction.Builder)EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, (NumberProvider)UniformGenerator.between(0.0F, 1.0F))))
/* 912 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem()))
/*     */       
/* 914 */       .withPool(LootPool.lootPool()
/* 915 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 916 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Blocks.WET_SPONGE))
/* 917 */         .when(LootItemKilledByPlayerCondition.killedByPlayer()))
/* 918 */       .withPool(LootPool.lootPool()
/* 919 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 920 */         .add((LootPoolEntryContainer.Builder)NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH).apply((LootItemFunction.Builder)SmeltItemFunction.smelted().when((LootItemCondition.Builder)shouldSmeltLoot())))
/* 921 */         .when(LootItemKilledByPlayerCondition.killedByPlayer())
/* 922 */         .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.025F, 0.01F)))
/* 923 */       .withPool(LootPool.lootPool()
/* 924 */         .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 925 */         .add((LootPoolEntryContainer.Builder)EmptyLootItem.emptyItem().setWeight(4))
/* 926 */         .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).setWeight(1)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaEntityLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */