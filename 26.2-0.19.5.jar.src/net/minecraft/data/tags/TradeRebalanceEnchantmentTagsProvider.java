/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ 
/*    */ public class TradeRebalanceEnchantmentTagsProvider extends TagsProvider<Enchantment> {
/*    */   public TradeRebalanceEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.ENCHANTMENT, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(EnchantmentTags.TRADES_DESERT_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FIRE_PROTECTION, Enchantments.THORNS, Enchantments.INFINITY });
/* 20 */     tag(EnchantmentTags.TRADES_JUNGLE_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.FEATHER_FALLING, Enchantments.PROJECTILE_PROTECTION, Enchantments.POWER });
/* 21 */     tag(EnchantmentTags.TRADES_PLAINS_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.PUNCH, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS });
/* 22 */     tag(EnchantmentTags.TRADES_SAVANNA_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.KNOCKBACK, Enchantments.BINDING_CURSE, Enchantments.SWEEPING_EDGE });
/* 23 */     tag(EnchantmentTags.TRADES_SNOW_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.AQUA_AFFINITY, Enchantments.LOOTING, Enchantments.FROST_WALKER });
/* 24 */     tag(EnchantmentTags.TRADES_SWAMP_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.DEPTH_STRIDER, Enchantments.RESPIRATION, Enchantments.VANISHING_CURSE });
/* 25 */     tag(EnchantmentTags.TRADES_TAIGA_COMMON).add((ResourceKey<Enchantment>[])new ResourceKey[] { Enchantments.BLAST_PROTECTION, Enchantments.FIRE_ASPECT, Enchantments.FLAME });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\TradeRebalanceEnchantmentTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */