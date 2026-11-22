/*     */ package net.minecraft.data.advancements.packs;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementType;
/*     */ import net.minecraft.advancements.predicates.DamagePredicate;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.TagPredicate;
/*     */ import net.minecraft.advancements.triggers.ChangeDimensionTrigger;
/*     */ import net.minecraft.advancements.triggers.CuredZombieVillagerTrigger;
/*     */ import net.minecraft.advancements.triggers.EnchantedItemTrigger;
/*     */ import net.minecraft.advancements.triggers.EntityHurtPlayerTrigger;
/*     */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.triggers.PlayerTrigger;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ 
/*     */ public class VanillaStoryAdvancements implements AdvancementSubProvider {
/*     */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/*  38 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.ITEM);
/*  43 */     AdvancementHolder root = Advancement.Builder.advancement().display((ItemLike)Blocks.GRASS_BLOCK, (Component)Component.translatable("advancements.story.root.title"), (Component)Component.translatable("advancements.story.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/stone"), AdvancementType.TASK, false, false, false).addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.CRAFTING_TABLE })).save(output, "story/root");
/*  49 */     AdvancementHolder mineStone = Advancement.Builder.advancement().parent(root).display((ItemLike)Items.WOODEN_PICKAXE, (Component)Component.translatable("advancements.story.mine_stone.title"), (Component)Component.translatable("advancements.story.mine_stone.description"), null, AdvancementType.TASK, true, true, false).addCriterion("get_stone", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of((HolderGetter)registryLookup, ItemTags.STONE_TOOL_MATERIALS) })).save(output, "story/mine_stone");
/*  55 */     AdvancementHolder upgradeTools = Advancement.Builder.advancement().parent(mineStone).display((ItemLike)Items.STONE_PICKAXE, (Component)Component.translatable("advancements.story.upgrade_tools.title"), (Component)Component.translatable("advancements.story.upgrade_tools.description"), null, AdvancementType.TASK, true, true, false).addCriterion("stone_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.STONE_PICKAXE })).save(output, "story/upgrade_tools");
/*  61 */     AdvancementHolder smeltIron = Advancement.Builder.advancement().parent(upgradeTools).display((ItemLike)Items.IRON_INGOT, (Component)Component.translatable("advancements.story.smelt_iron.title"), (Component)Component.translatable("advancements.story.smelt_iron.description"), null, AdvancementType.TASK, true, true, false).addCriterion("iron", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_INGOT })).save(output, "story/smelt_iron");
/*  67 */     AdvancementHolder ironTools = Advancement.Builder.advancement().parent(smeltIron).display((ItemLike)Items.IRON_PICKAXE, (Component)Component.translatable("advancements.story.iron_tools.title"), (Component)Component.translatable("advancements.story.iron_tools.description"), null, AdvancementType.TASK, true, true, false).addCriterion("iron_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_PICKAXE })).save(output, "story/iron_tools");
/*  73 */     AdvancementHolder mineDiamond = Advancement.Builder.advancement().parent(ironTools).display((ItemLike)Items.DIAMOND, (Component)Component.translatable("advancements.story.mine_diamond.title"), (Component)Component.translatable("advancements.story.mine_diamond.description"), null, AdvancementType.TASK, true, true, false).addCriterion("diamond", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND })).save(output, "story/mine_diamond");
/*  79 */     AdvancementHolder lavaBucket = Advancement.Builder.advancement().parent(smeltIron).display((ItemLike)Items.LAVA_BUCKET, (Component)Component.translatable("advancements.story.lava_bucket.title"), (Component)Component.translatable("advancements.story.lava_bucket.description"), null, AdvancementType.TASK, true, true, false).addCriterion("lava_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.LAVA_BUCKET })).save(output, "story/lava_bucket");
/*  89 */     AdvancementHolder obtainArmor = Advancement.Builder.advancement().parent(smeltIron).display((ItemLike)Items.IRON_CHESTPLATE, (Component)Component.translatable("advancements.story.obtain_armor.title"), (Component)Component.translatable("advancements.story.obtain_armor.description"), null, AdvancementType.TASK, true, true, false).requirements(AdvancementRequirements.Strategy.OR).addCriterion("iron_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_HELMET })).addCriterion("iron_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_CHESTPLATE })).addCriterion("iron_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_LEGGINGS })).addCriterion("iron_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.IRON_BOOTS })).save(output, "story/obtain_armor");
/*  91 */     Advancement.Builder.advancement()
/*  92 */       .parent(mineDiamond)
/*  93 */       .display((ItemLike)Items.ENCHANTED_BOOK, (Component)Component.translatable("advancements.story.enchant_item.title"), (Component)Component.translatable("advancements.story.enchant_item.description"), null, AdvancementType.TASK, true, true, false)
/*  94 */       .addCriterion("enchanted_item", EnchantedItemTrigger.TriggerInstance.enchantedItem())
/*  95 */       .save(output, "story/enchant_item");
/* 101 */     AdvancementHolder formObsidian = Advancement.Builder.advancement().parent(lavaBucket).display((ItemLike)Blocks.OBSIDIAN, (Component)Component.translatable("advancements.story.form_obsidian.title"), (Component)Component.translatable("advancements.story.form_obsidian.description"), null, AdvancementType.TASK, true, true, false).addCriterion("obsidian", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.OBSIDIAN })).save(output, "story/form_obsidian");
/* 103 */     Advancement.Builder.advancement()
/* 104 */       .parent(obtainArmor)
/* 105 */       .display((ItemLike)Items.SHIELD, (Component)Component.translatable("advancements.story.deflect_arrow.title"), (Component)Component.translatable("advancements.story.deflect_arrow.description"), null, AdvancementType.TASK, true, true, false)
/* 106 */       .addCriterion("deflected_projectile", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))).blocked(Boolean.valueOf(true))))
/* 107 */       .save(output, "story/deflect_arrow");
/* 109 */     Advancement.Builder.advancement()
/* 110 */       .parent(mineDiamond)
/* 111 */       .display((ItemLike)Items.DIAMOND_CHESTPLATE, (Component)Component.translatable("advancements.story.shiny_gear.title"), (Component)Component.translatable("advancements.story.shiny_gear.description"), null, AdvancementType.TASK, true, true, false)
/* 112 */       .requirements(AdvancementRequirements.Strategy.OR)
/* 113 */       .addCriterion("diamond_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_HELMET })).addCriterion("diamond_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_CHESTPLATE })).addCriterion("diamond_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_LEGGINGS })).addCriterion("diamond_boots", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DIAMOND_BOOTS })).save(output, "story/shiny_gear");
/* 123 */     AdvancementHolder enterTheNether = Advancement.Builder.advancement().parent(formObsidian).display((ItemLike)Items.FLINT_AND_STEEL, (Component)Component.translatable("advancements.story.enter_the_nether.title"), (Component)Component.translatable("advancements.story.enter_the_nether.description"), null, AdvancementType.TASK, true, true, false).addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(output, "story/enter_the_nether");
/* 125 */     Advancement.Builder.advancement()
/* 126 */       .parent(enterTheNether)
/* 127 */       .display((ItemLike)Items.GOLDEN_APPLE, (Component)Component.translatable("advancements.story.cure_zombie_villager.title"), (Component)Component.translatable("advancements.story.cure_zombie_villager.description"), null, AdvancementType.GOAL, true, true, false)
/* 128 */       .addCriterion("cured_zombie", CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
/* 129 */       .save(output, "story/cure_zombie_villager");
/* 135 */     AdvancementHolder followEnderEye = Advancement.Builder.advancement().parent(enterTheNether).display((ItemLike)Items.ENDER_EYE, (Component)Component.translatable("advancements.story.follow_ender_eye.title"), (Component)Component.translatable("advancements.story.follow_ender_eye.description"), null, AdvancementType.TASK, true, true, false).addCriterion("in_stronghold", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.STRONGHOLD)))).save(output, "story/follow_ender_eye");
/* 137 */     Advancement.Builder.advancement()
/* 138 */       .parent(followEnderEye)
/* 139 */       .display((ItemLike)Blocks.END_STONE, (Component)Component.translatable("advancements.story.enter_the_end.title"), (Component)Component.translatable("advancements.story.enter_the_end.description"), null, AdvancementType.TASK, true, true, false)
/* 140 */       .addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
/* 141 */       .save(output, "story/enter_the_end");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaStoryAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */