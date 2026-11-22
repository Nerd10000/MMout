/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ 
/*     */ public class DamageTypeTagsProvider extends TagsProvider<DamageType> {
/*     */   public DamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/*  14 */     super(output, Registries.DAMAGE_TYPE, lookupProvider);
/*     */   }
/*     */   
/*     */   protected void addTags(HolderLookup.Provider registries) {
/*  19 */     tag(DamageTypeTags.DAMAGES_HELMET).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_BLOCK, DamageTypes.FALLING_STALACTITE });
/*  25 */     tag(DamageTypeTags.BYPASSES_ARMOR).add((ResourceKey<DamageType>[])new ResourceKey[] { 
/*  25 */           DamageTypes.ON_FIRE, DamageTypes.IN_WALL, DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.FLY_INTO_WALL, DamageTypes.GENERIC, DamageTypes.WITHER, DamageTypes.DRAGON_BREATH, DamageTypes.STARVE, DamageTypes.FALL, 
/*  25 */           DamageTypes.ENDER_PEARL, DamageTypes.FREEZE, DamageTypes.STALAGMITE, DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL, DamageTypes.SONIC_BOOM, DamageTypes.OUTSIDE_BORDER });
/*  47 */     tag(DamageTypeTags.BYPASSES_SHIELD).addTag(DamageTypeTags.BYPASSES_ARMOR).add(new ResourceKey[] { 
/*  47 */           DamageTypes.CACTUS, DamageTypes.CAMPFIRE, DamageTypes.DRY_OUT, DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_STALACTITE, DamageTypes.HOT_FLOOR, DamageTypes.SULFUR_CUBE_HOT, DamageTypes.IN_FIRE, DamageTypes.LAVA, DamageTypes.LIGHTNING_BOLT, 
/*  47 */           DamageTypes.SWEET_BERRY_BUSH });
/*  61 */     tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL });
/*  66 */     tag(DamageTypeTags.BYPASSES_EFFECTS).add(DamageTypes.STARVE);
/*  70 */     tag(DamageTypeTags.BYPASSES_RESISTANCE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL });
/*  75 */     tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(DamageTypes.SONIC_BOOM);
/*  79 */     tag(DamageTypeTags.IS_FIRE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE, DamageTypes.ON_FIRE, DamageTypes.LAVA, DamageTypes.HOT_FLOOR, DamageTypes.SULFUR_CUBE_HOT, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL });
/*  90 */     tag(DamageTypeTags.IS_PROJECTILE).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.MOB_PROJECTILE, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.THROWN, DamageTypes.WIND_CHARGE });
/* 101 */     tag(DamageTypeTags.WITCH_RESISTANT_TO).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS });
/* 108 */     tag(DamageTypeTags.IS_EXPLOSION).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FIREWORKS, DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, DamageTypes.BAD_RESPAWN_POINT });
/* 115 */     tag(DamageTypeTags.IS_FALL).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.FALL, DamageTypes.ENDER_PEARL, DamageTypes.STALAGMITE });
/* 121 */     tag(DamageTypeTags.IS_DROWNING).add(DamageTypes.DROWN);
/* 125 */     tag(DamageTypeTags.IS_FREEZING).add(DamageTypes.FREEZE);
/* 129 */     tag(DamageTypeTags.IS_LIGHTNING).add(DamageTypes.LIGHTNING_BOLT);
/* 133 */     tag(DamageTypeTags.NO_ANGER).add(DamageTypes.MOB_ATTACK_NO_AGGRO);
/* 137 */     tag(DamageTypeTags.NO_IMPACT).add(DamageTypes.DROWN);
/* 141 */     tag(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL).add(DamageTypes.FELL_OUT_OF_WORLD);
/* 145 */     tag(DamageTypeTags.WITHER_IMMUNE_TO).add(DamageTypes.DROWN);
/* 149 */     tag(DamageTypeTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO).add((ResourceKey<DamageType>[])new ResourceKey[] { 
/* 149 */           DamageTypes.ARROW, DamageTypes.CACTUS, DamageTypes.DRY_OUT, DamageTypes.FALL, DamageTypes.FALLING_ANVIL, DamageTypes.FALLING_BLOCK, DamageTypes.FALLING_STALACTITE, DamageTypes.FREEZE, DamageTypes.MACE_SMASH, DamageTypes.HOT_FLOOR, 
/* 149 */           DamageTypes.MOB_ATTACK, DamageTypes.MOB_ATTACK_NO_AGGRO, DamageTypes.MOB_PROJECTILE, DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.SPIT, DamageTypes.STALAGMITE, DamageTypes.STING, DamageTypes.SULFUR_CUBE_HOT, DamageTypes.SWEET_BERRY_BUSH, 
/* 149 */           DamageTypes.THROWN, DamageTypes.TRIDENT, DamageTypes.WIND_CHARGE }).addTag(DamageTypeTags.IS_EXPLOSION);
/* 175 */     tag(DamageTypeTags.IGNITES_ARMOR_STANDS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.IN_FIRE, DamageTypes.CAMPFIRE });
/* 180 */     tag(DamageTypeTags.BURNS_ARMOR_STANDS).add(DamageTypes.ON_FIRE);
/* 184 */     tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.MAGIC, DamageTypes.THORNS }).addTag(DamageTypeTags.IS_EXPLOSION);
/* 189 */     tag(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH).add(DamageTypes.MAGIC);
/* 193 */     tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).addTag(DamageTypeTags.IS_EXPLOSION);
/* 198 */     tag(DamageTypeTags.NO_KNOCKBACK).add((ResourceKey<DamageType>[])new ResourceKey[] { 
/* 198 */           DamageTypes.EXPLOSION, DamageTypes.PLAYER_EXPLOSION, DamageTypes.BAD_RESPAWN_POINT, DamageTypes.IN_FIRE, DamageTypes.LIGHTNING_BOLT, DamageTypes.ON_FIRE, DamageTypes.LAVA, DamageTypes.HOT_FLOOR, DamageTypes.SULFUR_CUBE_HOT, DamageTypes.IN_WALL, 
/* 198 */           DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.STARVE, DamageTypes.CACTUS, DamageTypes.FALL, DamageTypes.ENDER_PEARL, DamageTypes.FLY_INTO_WALL, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC, DamageTypes.MAGIC, 
/* 198 */           DamageTypes.WITHER, DamageTypes.DRAGON_BREATH, DamageTypes.DRY_OUT, DamageTypes.SWEET_BERRY_BUSH, DamageTypes.FREEZE, DamageTypes.STALAGMITE, DamageTypes.OUTSIDE_BORDER, DamageTypes.GENERIC_KILL, DamageTypes.CAMPFIRE, DamageTypes.SPEAR });
/* 231 */     tag(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.ARROW, DamageTypes.TRIDENT, DamageTypes.FIREBALL, DamageTypes.WITHER_SKULL, DamageTypes.WIND_CHARGE });
/* 239 */     tag(DamageTypeTags.CAN_BREAK_ARMOR_STAND).add(DamageTypes.PLAYER_EXPLOSION)
/*     */       
/* 241 */       .addTag(DamageTypeTags.IS_PLAYER_ATTACK);
/* 243 */     tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).addTag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(new ResourceKey[] { 
/* 243 */           DamageTypes.CRAMMING, DamageTypes.DROWN, DamageTypes.DRY_OUT, DamageTypes.FREEZE, DamageTypes.IN_WALL, DamageTypes.INDIRECT_MAGIC, DamageTypes.MAGIC, DamageTypes.OUTSIDE_BORDER, DamageTypes.STARVE, DamageTypes.THORNS, 
/* 243 */           DamageTypes.WITHER });
/* 257 */     tag(DamageTypeTags.IS_PLAYER_ATTACK).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.PLAYER_ATTACK, DamageTypes.SPEAR, DamageTypes.MACE_SMASH });
/* 264 */     tag(DamageTypeTags.BURN_FROM_STEPPING).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.CAMPFIRE, DamageTypes.HOT_FLOOR, DamageTypes.SULFUR_CUBE_HOT });
/* 270 */     tag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add((ResourceKey<DamageType>[])new ResourceKey[] { DamageTypes.CACTUS, DamageTypes.FREEZE, DamageTypes.HOT_FLOOR, DamageTypes.SULFUR_CUBE_HOT, DamageTypes.IN_FIRE, DamageTypes.LAVA, DamageTypes.LIGHTNING_BOLT, DamageTypes.ON_FIRE });
/* 281 */     tag(DamageTypeTags.PANIC_CAUSES).addTag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES).add(new ResourceKey[] { 
/* 281 */           DamageTypes.ARROW, DamageTypes.DRAGON_BREATH, DamageTypes.EXPLOSION, DamageTypes.FIREBALL, DamageTypes.FIREWORKS, DamageTypes.INDIRECT_MAGIC, DamageTypes.MAGIC, DamageTypes.MOB_ATTACK, DamageTypes.MOB_PROJECTILE, DamageTypes.PLAYER_EXPLOSION, 
/* 281 */           DamageTypes.SONIC_BOOM, DamageTypes.STING, DamageTypes.THROWN, DamageTypes.TRIDENT, DamageTypes.UNATTRIBUTED_FIREBALL, DamageTypes.WIND_CHARGE, DamageTypes.WITHER, DamageTypes.WITHER_SKULL }).addTag(DamageTypeTags.IS_PLAYER_ATTACK);
/* 302 */     tag(DamageTypeTags.IS_MACE_SMASH).add(DamageTypes.MACE_SMASH);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\DamageTypeTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */