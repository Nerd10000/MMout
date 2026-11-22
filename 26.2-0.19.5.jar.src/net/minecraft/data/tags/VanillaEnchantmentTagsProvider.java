/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ 
/*     */ public class VanillaEnchantmentTagsProvider extends EnchantmentTagsProvider {
/*     */   public VanillaEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/*  12 */     super(output, lookupProvider);
/*     */   }
/*     */   
/*     */   protected void addTags(HolderLookup.Provider registries) {
/*  18 */     tooltipOrder(registries, (ResourceKey<Enchantment>[])new ResourceKey[] { 
/*  18 */           Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, Enchantments.RIPTIDE, Enchantments.CHANNELING, Enchantments.WIND_BURST, Enchantments.FROST_WALKER, Enchantments.LUNGE, Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, 
/*  18 */           Enchantments.IMPALING, Enchantments.POWER, Enchantments.DENSITY, Enchantments.BREACH, Enchantments.PIERCING, Enchantments.SWEEPING_EDGE, Enchantments.MULTISHOT, Enchantments.FIRE_ASPECT, Enchantments.FLAME, Enchantments.KNOCKBACK, 
/*  18 */           Enchantments.PUNCH, Enchantments.PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.FEATHER_FALLING, Enchantments.FORTUNE, Enchantments.LOOTING, Enchantments.SILK_TOUCH, Enchantments.LUCK_OF_THE_SEA, 
/*  18 */           Enchantments.EFFICIENCY, Enchantments.QUICK_CHARGE, Enchantments.LURE, Enchantments.RESPIRATION, Enchantments.AQUA_AFFINITY, Enchantments.SOUL_SPEED, Enchantments.SWIFT_SNEAK, Enchantments.DEPTH_STRIDER, Enchantments.THORNS, Enchantments.LOYALTY, 
/*  18 */           Enchantments.UNBREAKING, Enchantments.INFINITY, Enchantments.MENDING });
/*  74 */     tag(EnchantmentTags.ARMOR_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.PROJECTILE_PROTECTION });
/*  75 */     tag(EnchantmentTags.BOOTS_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FROST_WALKER, Enchantments.DEPTH_STRIDER });
/*  76 */     tag(EnchantmentTags.BOW_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.INFINITY, Enchantments.MENDING });
/*  77 */     tag(EnchantmentTags.CROSSBOW_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.MULTISHOT, Enchantments.PIERCING });
/*  78 */     tag(EnchantmentTags.DAMAGE_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.IMPALING, Enchantments.DENSITY, Enchantments.BREACH });
/*  79 */     tag(EnchantmentTags.MINING_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FORTUNE, Enchantments.SILK_TOUCH });
/*  80 */     tag(EnchantmentTags.RIPTIDE_EXCLUSIVE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.LOYALTY, Enchantments.CHANNELING });
/*  82 */     tag(EnchantmentTags.TREASURE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, Enchantments.SWIFT_SNEAK, Enchantments.SOUL_SPEED, Enchantments.FROST_WALKER, Enchantments.MENDING, Enchantments.WIND_BURST });
/*  83 */     tag(EnchantmentTags.NON_TREASURE).add((ResourceKey<Enchantment>[])new ResourceKey[] { 
/*  83 */           Enchantments.PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.FEATHER_FALLING, Enchantments.BLAST_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.RESPIRATION, Enchantments.AQUA_AFFINITY, Enchantments.THORNS, Enchantments.DEPTH_STRIDER, Enchantments.SHARPNESS, 
/*  83 */           Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING, Enchantments.SWEEPING_EDGE, Enchantments.EFFICIENCY, Enchantments.SILK_TOUCH, Enchantments.UNBREAKING, Enchantments.FORTUNE, 
/*  83 */           Enchantments.POWER, Enchantments.PUNCH, Enchantments.FLAME, Enchantments.INFINITY, Enchantments.LUCK_OF_THE_SEA, Enchantments.LURE, Enchantments.LOYALTY, Enchantments.IMPALING, Enchantments.RIPTIDE, Enchantments.CHANNELING, 
/*  83 */           Enchantments.MULTISHOT, Enchantments.QUICK_CHARGE, Enchantments.PIERCING, Enchantments.DENSITY, Enchantments.BREACH, Enchantments.LUNGE });
/*  94 */     tag(EnchantmentTags.DOUBLE_TRADE_PRICE).addTag(EnchantmentTags.TREASURE);
/*  95 */     tag(EnchantmentTags.IN_ENCHANTING_TABLE).addTag(EnchantmentTags.NON_TREASURE);
/*  96 */     tag(EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT).addTag(EnchantmentTags.NON_TREASURE);
/*  97 */     tag(EnchantmentTags.ON_TRADED_EQUIPMENT).addTag(EnchantmentTags.NON_TREASURE);
/*  98 */     tag(EnchantmentTags.ON_RANDOM_LOOT).addTag(EnchantmentTags.NON_TREASURE).add(new ResourceKey[] { Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, Enchantments.FROST_WALKER, Enchantments.MENDING });
/*  99 */     tag(EnchantmentTags.TRADEABLE).addTag(EnchantmentTags.NON_TREASURE).add(new ResourceKey[] { Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE, Enchantments.FROST_WALKER, Enchantments.MENDING });
/* 101 */     tag(EnchantmentTags.CURSE).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.BINDING_CURSE, Enchantments.VANISHING_CURSE });
/* 102 */     tag(EnchantmentTags.SMELTS_LOOT).add(Enchantments.FIRE_ASPECT);
/* 104 */     tag(EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING).add(Enchantments.SILK_TOUCH);
/* 105 */     tag(EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING).add(Enchantments.SILK_TOUCH);
/* 106 */     tag(EnchantmentTags.PREVENTS_ICE_MELTING).add(Enchantments.SILK_TOUCH);
/* 107 */     tag(EnchantmentTags.PREVENTS_INFESTED_SPAWNS).add(Enchantments.SILK_TOUCH);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VanillaEnchantmentTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */