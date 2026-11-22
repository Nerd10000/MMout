/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypeIds;
/*    */ 
/*    */ public class EntityTypeTagsProvider extends TagsProvider<EntityType<?>> {
/*    */   public EntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.ENTITY_TYPE, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(EntityTypeTags.SKELETONS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.SKELETON, EntityTypeIds.STRAY, EntityTypeIds.WITHER_SKELETON, EntityTypeIds.SKELETON_HORSE, EntityTypeIds.BOGGED, EntityTypeIds.PARCHED });
/* 20 */     tag(EntityTypeTags.ZOMBIES).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.ZOMBIE_HORSE, EntityTypeIds.CAMEL_HUSK, EntityTypeIds.ZOMBIE, EntityTypeIds.ZOMBIE_VILLAGER, EntityTypeIds.ZOMBIFIED_PIGLIN, EntityTypeIds.ZOGLIN, EntityTypeIds.DROWNED, EntityTypeIds.HUSK, EntityTypeIds.ZOMBIE_NAUTILUS });
/* 21 */     tag(EntityTypeTags.RAIDERS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.EVOKER, EntityTypeIds.PILLAGER, EntityTypeIds.RAVAGER, EntityTypeIds.VINDICATOR, EntityTypeIds.ILLUSIONER, EntityTypeIds.WITCH });
/* 22 */     tag(EntityTypeTags.UNDEAD).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.ZOMBIES).add(EntityTypeIds.WITHER).add(EntityTypeIds.PHANTOM);
/* 23 */     tag(EntityTypeTags.BURN_IN_DAYLIGHT).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.SKELETON, EntityTypeIds.STRAY, EntityTypeIds.WITHER_SKELETON, EntityTypeIds.BOGGED }).add(new ResourceKey[] { EntityTypeIds.ZOMBIE, EntityTypeIds.ZOMBIE_HORSE, EntityTypeIds.ZOMBIE_VILLAGER, EntityTypeIds.DROWNED, EntityTypeIds.ZOMBIE_NAUTILUS }).add(EntityTypeIds.PHANTOM);
/* 24 */     tag(EntityTypeTags.BEEHIVE_INHABITORS).add(EntityTypeIds.BEE);
/* 25 */     tag(EntityTypeTags.ARROWS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.ARROW, EntityTypeIds.SPECTRAL_ARROW });
/* 26 */     tag(EntityTypeTags.IMPACT_PROJECTILES).addTag(EntityTypeTags.ARROWS).add(EntityTypeIds.FIREWORK_ROCKET).add(new ResourceKey[] { EntityTypeIds.SNOWBALL, EntityTypeIds.FIREBALL, EntityTypeIds.SMALL_FIREBALL, EntityTypeIds.EGG, EntityTypeIds.TRIDENT, EntityTypeIds.DRAGON_FIREBALL, EntityTypeIds.WITHER_SKULL, EntityTypeIds.WIND_CHARGE, EntityTypeIds.BREEZE_WIND_CHARGE });
/* 27 */     tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.RABBIT, EntityTypeIds.ENDERMITE, EntityTypeIds.SILVERFISH, EntityTypeIds.FOX });
/* 28 */     tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.TROPICAL_FISH, EntityTypeIds.PUFFERFISH, EntityTypeIds.SALMON, EntityTypeIds.COD, EntityTypeIds.SQUID, EntityTypeIds.GLOW_SQUID, EntityTypeIds.TADPOLE });
/* 29 */     tag(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.DROWNED, EntityTypeIds.GUARDIAN, EntityTypeIds.ELDER_GUARDIAN });
/* 30 */     tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.STRAY, EntityTypeIds.POLAR_BEAR, EntityTypeIds.SNOW_GOLEM, EntityTypeIds.WITHER });
/* 31 */     tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.STRIDER, EntityTypeIds.BLAZE, EntityTypeIds.MAGMA_CUBE });
/* 32 */     tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).addTag(EntityTypeTags.UNDEAD).add(new ResourceKey[] { 
/* 32 */           EntityTypeIds.AXOLOTL, EntityTypeIds.FROG, EntityTypeIds.GUARDIAN, EntityTypeIds.ELDER_GUARDIAN, EntityTypeIds.TURTLE, EntityTypeIds.GLOW_SQUID, EntityTypeIds.COD, EntityTypeIds.PUFFERFISH, EntityTypeIds.SALMON, EntityTypeIds.SQUID, 
/* 32 */           EntityTypeIds.TROPICAL_FISH, EntityTypeIds.TADPOLE, EntityTypeIds.ARMOR_STAND, EntityTypeIds.COPPER_GOLEM, EntityTypeIds.NAUTILUS });
/* 33 */     tag(EntityTypeTags.FROG_FOOD).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.SLIME, EntityTypeIds.MAGMA_CUBE });
/* 34 */     tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { 
/* 34 */           EntityTypeIds.COPPER_GOLEM, EntityTypeIds.IRON_GOLEM, EntityTypeIds.SNOW_GOLEM, EntityTypeIds.SHULKER, EntityTypeIds.ALLAY, EntityTypeIds.BAT, EntityTypeIds.BEE, EntityTypeIds.BLAZE, EntityTypeIds.CAT, EntityTypeIds.CHICKEN, 
/* 34 */           EntityTypeIds.GHAST, EntityTypeIds.HAPPY_GHAST, EntityTypeIds.PHANTOM, EntityTypeIds.MAGMA_CUBE, EntityTypeIds.OCELOT, EntityTypeIds.PARROT, EntityTypeIds.WITHER, EntityTypeIds.BREEZE });
/* 35 */     tag(EntityTypeTags.DISMOUNTS_UNDERWATER).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { 
/* 35 */           EntityTypeIds.CAMEL, EntityTypeIds.CHICKEN, EntityTypeIds.DONKEY, EntityTypeIds.HAPPY_GHAST, EntityTypeIds.HORSE, EntityTypeIds.LLAMA, EntityTypeIds.MULE, EntityTypeIds.PIG, EntityTypeIds.RAVAGER, EntityTypeIds.SPIDER, 
/* 35 */           EntityTypeIds.STRIDER, EntityTypeIds.TRADER_LLAMA, EntityTypeIds.ZOMBIE_HORSE });
/* 36 */     tag(EntityTypeTags.NON_CONTROLLING_RIDER).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.SLIME, EntityTypeIds.MAGMA_CUBE, EntityTypeIds.SULFUR_CUBE });
/* 37 */     tag(EntityTypeTags.ILLAGER).add(EntityTypeIds.EVOKER).add(EntityTypeIds.ILLUSIONER).add(EntityTypeIds.PILLAGER).add(EntityTypeIds.VINDICATOR);
/* 38 */     tag(EntityTypeTags.AQUATIC).add(EntityTypeIds.TURTLE).add(EntityTypeIds.AXOLOTL).add(EntityTypeIds.GUARDIAN).add(EntityTypeIds.ELDER_GUARDIAN).add(EntityTypeIds.COD).add(EntityTypeIds.PUFFERFISH).add(EntityTypeIds.SALMON).add(EntityTypeIds.TROPICAL_FISH).add(EntityTypeIds.DOLPHIN).add(EntityTypeIds.SQUID).add(EntityTypeIds.GLOW_SQUID).add(EntityTypeIds.TADPOLE).add(EntityTypeIds.NAUTILUS).add(EntityTypeIds.ZOMBIE_NAUTILUS);
/* 39 */     tag(EntityTypeTags.ARTHROPOD).add(EntityTypeIds.BEE).add(EntityTypeIds.ENDERMITE).add(EntityTypeIds.SILVERFISH).add(EntityTypeIds.SPIDER).add(EntityTypeIds.CAVE_SPIDER);
/* 40 */     tag(EntityTypeTags.IGNORES_POISON_AND_REGEN).addTag(EntityTypeTags.UNDEAD);
/* 41 */     tag(EntityTypeTags.INVERTED_HEALING_AND_HARM).addTag(EntityTypeTags.UNDEAD);
/* 42 */     tag(EntityTypeTags.WITHER_FRIENDS).addTag(EntityTypeTags.UNDEAD);
/* 43 */     tag(EntityTypeTags.ILLAGER_FRIENDS).addTag(EntityTypeTags.ILLAGER);
/* 45 */     tag(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH).add(EntityTypeIds.TURTLE).add(EntityTypeIds.GUARDIAN).add(EntityTypeIds.ELDER_GUARDIAN).add(EntityTypeIds.COD).add(EntityTypeIds.PUFFERFISH).add(EntityTypeIds.SALMON).add(EntityTypeIds.TROPICAL_FISH).add(EntityTypeIds.DOLPHIN).add(EntityTypeIds.SQUID).add(EntityTypeIds.GLOW_SQUID).add(EntityTypeIds.TADPOLE).add(EntityTypeIds.NAUTILUS).add(EntityTypeIds.ZOMBIE_NAUTILUS).add(EntityTypeIds.SULFUR_CUBE);
/* 46 */     tag(EntityTypeTags.SENSITIVE_TO_IMPALING).addTag(EntityTypeTags.AQUATIC);
/* 47 */     tag(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS).addTag(EntityTypeTags.ARTHROPOD);
/* 48 */     tag(EntityTypeTags.SENSITIVE_TO_SMITE).addTag(EntityTypeTags.UNDEAD);
/* 49 */     tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.FIREBALL, EntityTypeIds.WIND_CHARGE, EntityTypeIds.BREEZE_WIND_CHARGE });
/* 50 */     tag(EntityTypeTags.DEFLECTS_PROJECTILES).add(EntityTypeIds.BREEZE);
/* 51 */     tag(EntityTypeTags.CAN_TURN_IN_BOATS).add(EntityTypeIds.BREEZE);
/* 52 */     tag(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.BREEZE, EntityTypeIds.SKELETON, EntityTypeIds.BOGGED, EntityTypeIds.STRAY, EntityTypeIds.ZOMBIE, EntityTypeIds.HUSK, EntityTypeIds.SPIDER, EntityTypeIds.CAVE_SPIDER, EntityTypeIds.SLIME });
/* 53 */     tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(EntityTypeIds.SILVERFISH);
/* 54 */     tag(EntityTypeTags.IMMUNE_TO_OOZING).add(EntityTypeIds.SLIME);
/* 55 */     tag(EntityTypeTags.BOAT).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.OAK_BOAT, EntityTypeIds.SPRUCE_BOAT, EntityTypeIds.BIRCH_BOAT, EntityTypeIds.JUNGLE_BOAT, EntityTypeIds.ACACIA_BOAT, EntityTypeIds.CHERRY_BOAT, EntityTypeIds.DARK_OAK_BOAT, EntityTypeIds.PALE_OAK_BOAT, EntityTypeIds.MANGROVE_BOAT, EntityTypeIds.BAMBOO_RAFT });
/* 56 */     tag(EntityTypeTags.CAN_EQUIP_SADDLE).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { 
/* 56 */           EntityTypeIds.HORSE, EntityTypeIds.SKELETON_HORSE, EntityTypeIds.ZOMBIE_HORSE, EntityTypeIds.DONKEY, EntityTypeIds.MULE, EntityTypeIds.PIG, EntityTypeIds.STRIDER, EntityTypeIds.CAMEL, EntityTypeIds.CAMEL_HUSK, EntityTypeIds.NAUTILUS, 
/* 56 */           EntityTypeIds.ZOMBIE_NAUTILUS });
/* 57 */     tag(EntityTypeTags.CAN_EQUIP_HARNESS).add(EntityTypeIds.HAPPY_GHAST);
/* 58 */     tag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR).add(EntityTypeIds.HORSE).add(EntityTypeIds.ZOMBIE_HORSE);
/* 59 */     tag(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.NAUTILUS, EntityTypeIds.ZOMBIE_NAUTILUS });
/* 60 */     tag(EntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { 
/* 60 */           EntityTypeIds.ARMADILLO, EntityTypeIds.BEE, EntityTypeIds.CAMEL, EntityTypeIds.CAT, EntityTypeIds.CHICKEN, EntityTypeIds.COW, EntityTypeIds.DONKEY, EntityTypeIds.FOX, EntityTypeIds.GOAT, EntityTypeIds.HAPPY_GHAST, 
/* 60 */           EntityTypeIds.HORSE, EntityTypeIds.SKELETON_HORSE, EntityTypeIds.LLAMA, EntityTypeIds.MULE, EntityTypeIds.OCELOT, EntityTypeIds.PANDA, EntityTypeIds.PARROT, EntityTypeIds.PIG, EntityTypeIds.POLAR_BEAR, EntityTypeIds.RABBIT, 
/* 60 */           EntityTypeIds.SHEEP, EntityTypeIds.SNIFFER, EntityTypeIds.STRIDER, EntityTypeIds.VILLAGER, EntityTypeIds.WOLF });
/* 61 */     tag(EntityTypeTags.CANNOT_BE_PUSHED_ONTO_BOATS).add(EntityTypeIds.PLAYER).add(EntityTypeIds.ELDER_GUARDIAN).add(EntityTypeIds.COD).add(EntityTypeIds.PUFFERFISH).add(EntityTypeIds.SALMON).add(EntityTypeIds.TROPICAL_FISH).add(EntityTypeIds.DOLPHIN).add(EntityTypeIds.SQUID).add(EntityTypeIds.GLOW_SQUID).add(EntityTypeIds.TADPOLE).add(EntityTypeIds.CREAKING).add(EntityTypeIds.NAUTILUS).add(EntityTypeIds.ZOMBIE_NAUTILUS).add(EntityTypeIds.SULFUR_CUBE);
/* 62 */     tag(EntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT).add(EntityTypeIds.COPPER_GOLEM);
/* 63 */     tag(EntityTypeTags.CANDIDATE_FOR_IRON_GOLEM_GIFT).add(EntityTypeIds.VILLAGER).addTag(EntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT);
/* 64 */     tag(EntityTypeTags.NAUTILUS_HOSTILES).add(EntityTypeIds.PUFFERFISH);
/* 65 */     tag(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.HORSE, EntityTypeIds.ZOMBIE_HORSE, EntityTypeIds.MULE, EntityTypeIds.DONKEY, EntityTypeIds.CAMEL, EntityTypeIds.CAMEL_HUSK });
/* 66 */     tag(EntityTypeTags.CANNOT_BE_AGE_LOCKED).add((ResourceKey<EntityType<?>>[])new ResourceKey[] { EntityTypeIds.ZOMBIE_HORSE, EntityTypeIds.SKELETON_HORSE, EntityTypeIds.VILLAGER });
/* 67 */     tag(EntityTypeTags.NOT_AFFECTED_BY_GEYSERS).add(EntityTypeIds.ENDER_DRAGON);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\EntityTypeTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */