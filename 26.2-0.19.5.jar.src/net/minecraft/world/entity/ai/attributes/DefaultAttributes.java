/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.allay.Allay;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.animal.camel.Camel;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.cow.Cow;
/*     */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*     */ import net.minecraft.world.entity.animal.equine.ZombieHorse;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.Frog;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.entity.animal.goat.Goat;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*     */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*     */ import net.minecraft.world.entity.animal.nautilus.Nautilus;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.panda.Panda;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.Pig;
/*     */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*     */ import net.minecraft.world.entity.animal.squid.Squid;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.monster.Blaze;
/*     */ import net.minecraft.world.entity.monster.Creeper;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.entity.monster.EnderMan;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Giant;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Ravager;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.monster.cubemob.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.cubemob.SulfurCube;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.illager.Evoker;
/*     */ import net.minecraft.world.entity.monster.illager.Illusioner;
/*     */ import net.minecraft.world.entity.monster.illager.Pillager;
/*     */ import net.minecraft.world.entity.monster.illager.Vindicator;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
/*     */ import net.minecraft.world.entity.monster.skeleton.Bogged;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.entity.monster.spider.CaveSpider;
/*     */ import net.minecraft.world.entity.monster.spider.Spider;
/*     */ import net.minecraft.world.entity.monster.warden.Warden;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DefaultAttributes {
/*  96 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  98 */   private static final Map<EntityType<? extends LivingEntity>, AttributeSupplier> SUPPLIERS = (Map<EntityType<? extends LivingEntity>, AttributeSupplier>)ImmutableMap.builder()
/*  99 */     .put(EntityTypes.ALLAY, Allay.createAttributes().build())
/* 100 */     .put(EntityTypes.ARMADILLO, Armadillo.createAttributes().build())
/* 101 */     .put(EntityTypes.ARMOR_STAND, ArmorStand.createAttributes().build())
/* 102 */     .put(EntityTypes.AXOLOTL, Axolotl.createAttributes().build())
/* 103 */     .put(EntityTypes.BAT, Bat.createAttributes().build())
/* 104 */     .put(EntityTypes.BEE, Bee.createAttributes().build())
/* 105 */     .put(EntityTypes.BLAZE, Blaze.createAttributes().build())
/* 106 */     .put(EntityTypes.BOGGED, Bogged.createAttributes().build())
/* 107 */     .put(EntityTypes.CAT, Cat.createAttributes().build())
/* 108 */     .put(EntityTypes.CAMEL, Camel.createAttributes().build())
/* 109 */     .put(EntityTypes.CAMEL_HUSK, Camel.createAttributes().build())
/* 110 */     .put(EntityTypes.CAVE_SPIDER, CaveSpider.createCaveSpider().build())
/* 111 */     .put(EntityTypes.CHICKEN, Chicken.createAttributes().build())
/* 112 */     .put(EntityTypes.COD, AbstractFish.createAttributes().build())
/* 113 */     .put(EntityTypes.COPPER_GOLEM, CopperGolem.createAttributes().build())
/* 114 */     .put(EntityTypes.COW, Cow.createAttributes().build())
/* 115 */     .put(EntityTypes.CREAKING, Creaking.createAttributes().build())
/* 116 */     .put(EntityTypes.CREEPER, Creeper.createAttributes().build())
/* 117 */     .put(EntityTypes.DOLPHIN, Dolphin.createAttributes().build())
/* 118 */     .put(EntityTypes.DONKEY, AbstractChestedHorse.createBaseChestedHorseAttributes().build())
/* 119 */     .put(EntityTypes.DROWNED, Drowned.createAttributes().build())
/* 120 */     .put(EntityTypes.ELDER_GUARDIAN, ElderGuardian.createAttributes().build())
/* 121 */     .put(EntityTypes.ENDERMAN, EnderMan.createAttributes().build())
/* 122 */     .put(EntityTypes.ENDERMITE, Endermite.createAttributes().build())
/* 123 */     .put(EntityTypes.ENDER_DRAGON, EnderDragon.createAttributes().build())
/* 124 */     .put(EntityTypes.EVOKER, Evoker.createAttributes().build())
/* 125 */     .put(EntityTypes.BREEZE, Breeze.createAttributes().build())
/* 126 */     .put(EntityTypes.FOX, Fox.createAttributes().build())
/* 127 */     .put(EntityTypes.FROG, Frog.createAttributes().build())
/* 128 */     .put(EntityTypes.GHAST, Ghast.createAttributes().build())
/* 129 */     .put(EntityTypes.HAPPY_GHAST, HappyGhast.createAttributes().build())
/* 130 */     .put(EntityTypes.GIANT, Giant.createAttributes().build())
/* 131 */     .put(EntityTypes.GLOW_SQUID, GlowSquid.createAttributes().build())
/* 132 */     .put(EntityTypes.GOAT, Goat.createAttributes().build())
/* 133 */     .put(EntityTypes.GUARDIAN, Guardian.createAttributes().build())
/* 134 */     .put(EntityTypes.HOGLIN, Hoglin.createAttributes().build())
/* 135 */     .put(EntityTypes.HORSE, AbstractHorse.createBaseHorseAttributes().build())
/* 136 */     .put(EntityTypes.HUSK, Zombie.createAttributes().build())
/* 137 */     .put(EntityTypes.ILLUSIONER, Illusioner.createAttributes().build())
/* 138 */     .put(EntityTypes.IRON_GOLEM, IronGolem.createAttributes().build())
/* 139 */     .put(EntityTypes.LLAMA, Llama.createAttributes().build())
/* 140 */     .put(EntityTypes.MAGMA_CUBE, MagmaCube.createAttributes().build())
/* 141 */     .put(EntityTypes.SULFUR_CUBE, SulfurCube.createSulfurCubeAttributes().build())
/* 142 */     .put(EntityTypes.MANNEQUIN, LivingEntity.createLivingAttributes().build())
/* 143 */     .put(EntityTypes.MOOSHROOM, Cow.createAttributes().build())
/* 144 */     .put(EntityTypes.MULE, AbstractChestedHorse.createBaseChestedHorseAttributes().build())
/* 145 */     .put(EntityTypes.NAUTILUS, Nautilus.createAttributes().build())
/* 146 */     .put(EntityTypes.OCELOT, Ocelot.createAttributes().build())
/* 147 */     .put(EntityTypes.PANDA, Panda.createAttributes().build())
/* 148 */     .put(EntityTypes.PARCHED, Parched.createAttributes().build())
/* 149 */     .put(EntityTypes.PARROT, Parrot.createAttributes().build())
/* 150 */     .put(EntityTypes.PHANTOM, Monster.createMonsterAttributes().build())
/* 151 */     .put(EntityTypes.PIG, Pig.createAttributes().build())
/* 152 */     .put(EntityTypes.PIGLIN, Piglin.createAttributes().build())
/* 153 */     .put(EntityTypes.PIGLIN_BRUTE, PiglinBrute.createAttributes().build())
/* 154 */     .put(EntityTypes.PILLAGER, Pillager.createAttributes().build())
/* 155 */     .put(EntityTypes.PLAYER, Player.createAttributes().build())
/* 156 */     .put(EntityTypes.POLAR_BEAR, PolarBear.createAttributes().build())
/* 157 */     .put(EntityTypes.PUFFERFISH, AbstractFish.createAttributes().build())
/* 158 */     .put(EntityTypes.RABBIT, Rabbit.createAttributes().build())
/* 159 */     .put(EntityTypes.RAVAGER, Ravager.createAttributes().build())
/* 160 */     .put(EntityTypes.SALMON, AbstractFish.createAttributes().build())
/* 161 */     .put(EntityTypes.SHEEP, Sheep.createAttributes().build())
/* 162 */     .put(EntityTypes.SHULKER, Shulker.createAttributes().build())
/* 163 */     .put(EntityTypes.SILVERFISH, Silverfish.createAttributes().build())
/* 164 */     .put(EntityTypes.SKELETON, AbstractSkeleton.createAttributes().build())
/* 165 */     .put(EntityTypes.SKELETON_HORSE, SkeletonHorse.createAttributes().build())
/* 166 */     .put(EntityTypes.SLIME, Monster.createMonsterAttributes().build())
/* 167 */     .put(EntityTypes.SNIFFER, Sniffer.createAttributes().build())
/* 168 */     .put(EntityTypes.SNOW_GOLEM, SnowGolem.createAttributes().build())
/* 169 */     .put(EntityTypes.SPIDER, Spider.createAttributes().build())
/* 170 */     .put(EntityTypes.SQUID, Squid.createAttributes().build())
/* 171 */     .put(EntityTypes.STRAY, AbstractSkeleton.createAttributes().build())
/* 172 */     .put(EntityTypes.STRIDER, Strider.createAttributes().build())
/* 173 */     .put(EntityTypes.TADPOLE, Tadpole.createAttributes().build())
/* 174 */     .put(EntityTypes.TRADER_LLAMA, Llama.createAttributes().build())
/* 175 */     .put(EntityTypes.TROPICAL_FISH, AbstractFish.createAttributes().build())
/* 176 */     .put(EntityTypes.TURTLE, Turtle.createAttributes().build())
/* 177 */     .put(EntityTypes.VEX, Vex.createAttributes().build())
/* 178 */     .put(EntityTypes.VILLAGER, Villager.createAttributes().build())
/* 179 */     .put(EntityTypes.VINDICATOR, Vindicator.createAttributes().build())
/* 180 */     .put(EntityTypes.WARDEN, Warden.createAttributes().build())
/* 181 */     .put(EntityTypes.WANDERING_TRADER, Mob.createMobAttributes().build())
/* 182 */     .put(EntityTypes.WITCH, Witch.createAttributes().build())
/* 183 */     .put(EntityTypes.WITHER, WitherBoss.createAttributes().build())
/* 184 */     .put(EntityTypes.WITHER_SKELETON, AbstractSkeleton.createAttributes().build())
/* 185 */     .put(EntityTypes.WOLF, Wolf.createAttributes().build())
/* 186 */     .put(EntityTypes.ZOGLIN, Zoglin.createAttributes().build())
/* 187 */     .put(EntityTypes.ZOMBIE, Zombie.createAttributes().build())
/* 188 */     .put(EntityTypes.ZOMBIE_HORSE, ZombieHorse.createAttributes().build())
/* 189 */     .put(EntityTypes.ZOMBIE_NAUTILUS, ZombieNautilus.createAttributes().build())
/* 190 */     .put(EntityTypes.ZOMBIE_VILLAGER, Zombie.createAttributes().build())
/* 191 */     .put(EntityTypes.ZOMBIFIED_PIGLIN, ZombifiedPiglin.createAttributes().build())
/* 192 */     .build();
/*     */   
/*     */   public static AttributeSupplier getSupplier(EntityType<? extends LivingEntity> type) {
/* 195 */     return SUPPLIERS.get(type);
/*     */   }
/*     */   
/*     */   public static boolean hasSupplier(EntityType<?> type) {
/* 199 */     return SUPPLIERS.containsKey(type);
/*     */   }
/*     */   
/*     */   public static void validate() {
/* 206 */     Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE);
/* 206 */     BuiltInRegistries.ENTITY_TYPE.stream().filter(entityType -> (entityType.getCategory() != MobCategory.MISC)).filter(entityType -> !hasSupplier(entityType)).map(BuiltInRegistries.ENTITY_TYPE::getKey)
/* 207 */       .forEach(id -> Util.logAndPauseIfInIde("Entity " + String.valueOf(id) + " has no attributes"));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\DefaultAttributes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */