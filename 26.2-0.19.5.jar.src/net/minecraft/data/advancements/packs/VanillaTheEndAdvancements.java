/*    */ package net.minecraft.data.advancements.packs;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.advancements.predicates.DistancePredicate;
/*    */ import net.minecraft.advancements.predicates.LocationPredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.advancements.triggers.ChangeDimensionTrigger;
/*    */ import net.minecraft.advancements.triggers.EnterBlockTrigger;
/*    */ import net.minecraft.advancements.triggers.InventoryChangeTrigger;
/*    */ import net.minecraft.advancements.triggers.KilledTrigger;
/*    */ import net.minecraft.advancements.triggers.LevitationTrigger;
/*    */ import net.minecraft.advancements.triggers.PlayerTrigger;
/*    */ import net.minecraft.advancements.triggers.SummonedEntityTrigger;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.advancements.AdvancementSubProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*    */ 
/*    */ public class VanillaTheEndAdvancements implements AdvancementSubProvider {
/*    */   public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
/* 37 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 42 */     AdvancementHolder root = Advancement.Builder.advancement().display((ItemLike)Blocks.END_STONE, (Component)Component.translatable("advancements.end.root.title"), (Component)Component.translatable("advancements.end.root.description"), Identifier.withDefaultNamespace("gui/advancements/backgrounds/end"), AdvancementType.TASK, false, false, false).addCriterion("entered_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(output, "end/root");
/* 48 */     AdvancementHolder killDragon = Advancement.Builder.advancement().parent(root).display((ItemLike)Blocks.DRAGON_HEAD, (Component)Component.translatable("advancements.end.kill_dragon.title"), (Component)Component.translatable("advancements.end.kill_dragon.description"), null, AdvancementType.TASK, true, true, false).addCriterion("killed_dragon", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup, EntityTypes.ENDER_DRAGON))).save(output, "end/kill_dragon");
/* 54 */     AdvancementHolder enterEndGateway = Advancement.Builder.advancement().parent(killDragon).display((ItemLike)Items.ENDER_PEARL, (Component)Component.translatable("advancements.end.enter_end_gateway.title"), (Component)Component.translatable("advancements.end.enter_end_gateway.description"), null, AdvancementType.TASK, true, true, false).addCriterion("entered_end_gateway", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY)).save(output, "end/enter_end_gateway");
/* 56 */     Advancement.Builder.advancement()
/* 57 */       .parent(killDragon)
/* 58 */       .display((ItemLike)Items.END_CRYSTAL, (Component)Component.translatable("advancements.end.respawn_dragon.title"), (Component)Component.translatable("advancements.end.respawn_dragon.description"), null, AdvancementType.GOAL, true, true, false)
/* 59 */       .addCriterion("summoned_dragon", SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of((HolderGetter)registryLookup, EntityTypes.ENDER_DRAGON)))
/* 60 */       .save(output, "end/respawn_dragon");
/* 66 */     AdvancementHolder findEndCity = Advancement.Builder.advancement().parent(enterEndGateway).display((ItemLike)Blocks.PURPUR_BLOCK, (Component)Component.translatable("advancements.end.find_end_city.title"), (Component)Component.translatable("advancements.end.find_end_city.description"), null, AdvancementType.TASK, true, true, false).addCriterion("in_city", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure((Holder)registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(BuiltinStructures.END_CITY)))).save(output, "end/find_end_city");
/* 68 */     Advancement.Builder.advancement()
/* 69 */       .parent(killDragon)
/* 70 */       .display((ItemLike)Items.DRAGON_BREATH, (Component)Component.translatable("advancements.end.dragon_breath.title"), (Component)Component.translatable("advancements.end.dragon_breath.description"), null, AdvancementType.GOAL, true, true, false)
/* 71 */       .addCriterion("dragon_breath", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.DRAGON_BREATH })).save(output, "end/dragon_breath");
/* 74 */     Advancement.Builder.advancement()
/* 75 */       .parent(findEndCity)
/* 76 */       .display((ItemLike)Items.SHULKER_SHELL, (Component)Component.translatable("advancements.end.levitate.title"), (Component)Component.translatable("advancements.end.levitate.description"), null, AdvancementType.CHALLENGE, true, true, false)
/* 77 */       .rewards(AdvancementRewards.Builder.experience(50))
/* 78 */       .addCriterion("levitated", LevitationTrigger.TriggerInstance.levitated(DistancePredicate.vertical(MinMaxBounds.Doubles.atLeast(50.0D))))
/* 79 */       .save(output, "end/levitate");
/* 81 */     Advancement.Builder.advancement()
/* 82 */       .parent(findEndCity)
/* 83 */       .display((ItemLike)Items.ELYTRA, (Component)Component.translatable("advancements.end.elytra.title"), (Component)Component.translatable("advancements.end.elytra.description"), null, AdvancementType.GOAL, true, true, false)
/* 84 */       .addCriterion("elytra", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Items.ELYTRA })).save(output, "end/elytra");
/* 87 */     Advancement.Builder.advancement()
/* 88 */       .parent(killDragon)
/* 89 */       .display((ItemLike)Blocks.DRAGON_EGG, (Component)Component.translatable("advancements.end.dragon_egg.title"), (Component)Component.translatable("advancements.end.dragon_egg.description"), null, AdvancementType.GOAL, true, true, false)
/* 90 */       .addCriterion("dragon_egg", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[] { (ItemLike)Blocks.DRAGON_EGG })).save(output, "end/dragon_egg");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\advancements\packs\VanillaTheEndAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */