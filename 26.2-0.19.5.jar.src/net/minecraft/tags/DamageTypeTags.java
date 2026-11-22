/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.damagesource.DamageType;
/*    */ 
/*    */ public interface DamageTypeTags {
/*  8 */   public static final TagKey<DamageType> DAMAGES_HELMET = create("damages_helmet");
/*    */   
/*  9 */   public static final TagKey<DamageType> BYPASSES_ARMOR = create("bypasses_armor");
/*    */   
/* 10 */   public static final TagKey<DamageType> BYPASSES_SHIELD = create("bypasses_shield");
/*    */   
/* 11 */   public static final TagKey<DamageType> BYPASSES_INVULNERABILITY = create("bypasses_invulnerability");
/*    */   
/* 12 */   public static final TagKey<DamageType> BYPASSES_COOLDOWN = create("bypasses_cooldown");
/*    */   
/* 13 */   public static final TagKey<DamageType> BYPASSES_EFFECTS = create("bypasses_effects");
/*    */   
/* 14 */   public static final TagKey<DamageType> BYPASSES_RESISTANCE = create("bypasses_resistance");
/*    */   
/* 15 */   public static final TagKey<DamageType> BYPASSES_ENCHANTMENTS = create("bypasses_enchantments");
/*    */   
/* 16 */   public static final TagKey<DamageType> IS_FIRE = create("is_fire");
/*    */   
/* 17 */   public static final TagKey<DamageType> IS_PROJECTILE = create("is_projectile");
/*    */   
/* 18 */   public static final TagKey<DamageType> WITCH_RESISTANT_TO = create("witch_resistant_to");
/*    */   
/* 19 */   public static final TagKey<DamageType> IS_EXPLOSION = create("is_explosion");
/*    */   
/* 20 */   public static final TagKey<DamageType> IS_FALL = create("is_fall");
/*    */   
/* 21 */   public static final TagKey<DamageType> IS_DROWNING = create("is_drowning");
/*    */   
/* 22 */   public static final TagKey<DamageType> IS_FREEZING = create("is_freezing");
/*    */   
/* 23 */   public static final TagKey<DamageType> IS_LIGHTNING = create("is_lightning");
/*    */   
/* 24 */   public static final TagKey<DamageType> NO_ANGER = create("no_anger");
/*    */   
/* 25 */   public static final TagKey<DamageType> NO_IMPACT = create("no_impact");
/*    */   
/* 26 */   public static final TagKey<DamageType> ALWAYS_MOST_SIGNIFICANT_FALL = create("always_most_significant_fall");
/*    */   
/* 27 */   public static final TagKey<DamageType> WITHER_IMMUNE_TO = create("wither_immune_to");
/*    */   
/* 28 */   public static final TagKey<DamageType> SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO = create("sulfur_cube_with_block_immune_to");
/*    */   
/* 29 */   public static final TagKey<DamageType> IGNITES_ARMOR_STANDS = create("ignites_armor_stands");
/*    */   
/* 30 */   public static final TagKey<DamageType> BURNS_ARMOR_STANDS = create("burns_armor_stands");
/*    */   
/* 31 */   public static final TagKey<DamageType> AVOIDS_GUARDIAN_THORNS = create("avoids_guardian_thorns");
/*    */   
/* 32 */   public static final TagKey<DamageType> ALWAYS_TRIGGERS_SILVERFISH = create("always_triggers_silverfish");
/*    */   
/* 33 */   public static final TagKey<DamageType> ALWAYS_HURTS_ENDER_DRAGONS = create("always_hurts_ender_dragons");
/*    */   
/* 34 */   public static final TagKey<DamageType> NO_KNOCKBACK = create("no_knockback");
/*    */   
/* 35 */   public static final TagKey<DamageType> ALWAYS_KILLS_ARMOR_STANDS = create("always_kills_armor_stands");
/*    */   
/* 36 */   public static final TagKey<DamageType> CAN_BREAK_ARMOR_STAND = create("can_break_armor_stand");
/*    */   
/* 37 */   public static final TagKey<DamageType> BYPASSES_WOLF_ARMOR = create("bypasses_wolf_armor");
/*    */   
/* 38 */   public static final TagKey<DamageType> IS_PLAYER_ATTACK = create("is_player_attack");
/*    */   
/* 39 */   public static final TagKey<DamageType> BURN_FROM_STEPPING = create("burn_from_stepping");
/*    */   
/* 40 */   public static final TagKey<DamageType> PANIC_CAUSES = create("panic_causes");
/*    */   
/* 41 */   public static final TagKey<DamageType> PANIC_ENVIRONMENTAL_CAUSES = create("panic_environmental_causes");
/*    */   
/* 42 */   public static final TagKey<DamageType> IS_MACE_SMASH = create("mace_smash");
/*    */   
/*    */   private static TagKey<DamageType> create(String name) {
/* 45 */     return TagKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\DamageTypeTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */