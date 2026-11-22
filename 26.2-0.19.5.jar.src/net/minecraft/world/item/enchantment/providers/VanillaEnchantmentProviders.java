/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ 
/*    */ public interface VanillaEnchantmentProviders {
/* 15 */   public static final ResourceKey<EnchantmentProvider> MOB_SPAWN_EQUIPMENT = create("mob_spawn_equipment");
/*    */   
/* 16 */   public static final ResourceKey<EnchantmentProvider> PILLAGER_SPAWN_CROSSBOW = create("pillager_spawn_crossbow");
/*    */   
/* 18 */   public static final ResourceKey<EnchantmentProvider> RAID_PILLAGER_POST_WAVE_3 = create("raid/pillager_post_wave_3");
/*    */   
/* 19 */   public static final ResourceKey<EnchantmentProvider> RAID_PILLAGER_POST_WAVE_5 = create("raid/pillager_post_wave_5");
/*    */   
/* 20 */   public static final ResourceKey<EnchantmentProvider> RAID_VINDICATOR = create("raid/vindicator");
/*    */   
/* 21 */   public static final ResourceKey<EnchantmentProvider> RAID_VINDICATOR_POST_WAVE_5 = create("raid/vindicator_post_wave_5");
/*    */   
/* 23 */   public static final ResourceKey<EnchantmentProvider> ENDERMAN_LOOT_DROP = create("enderman_loot_drop");
/*    */   
/*    */   static void bootstrap(BootstrapContext<EnchantmentProvider> context) {
/* 26 */     HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
/* 27 */     context.register(MOB_SPAWN_EQUIPMENT, new EnchantmentsByCostWithDifficulty((HolderSet<Enchantment>)enchantments
/*    */           
/* 30 */           .getOrThrow(EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT), 5, 17));
/* 35 */     context.register(PILLAGER_SPAWN_CROSSBOW, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 38 */           .getOrThrow(Enchantments.PIERCING), 
/* 39 */           (IntProvider)ConstantInt.of(1)));
/* 43 */     context.register(RAID_PILLAGER_POST_WAVE_3, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 46 */           .getOrThrow(Enchantments.QUICK_CHARGE), 
/* 47 */           (IntProvider)ConstantInt.of(1)));
/* 50 */     context.register(RAID_PILLAGER_POST_WAVE_5, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 53 */           .getOrThrow(Enchantments.QUICK_CHARGE), 
/* 54 */           (IntProvider)ConstantInt.of(2)));
/* 57 */     context.register(RAID_VINDICATOR, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 60 */           .getOrThrow(Enchantments.SHARPNESS), 
/* 61 */           (IntProvider)ConstantInt.of(1)));
/* 64 */     context.register(RAID_VINDICATOR_POST_WAVE_5, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 67 */           .getOrThrow(Enchantments.SHARPNESS), 
/* 68 */           (IntProvider)ConstantInt.of(2)));
/* 72 */     context.register(ENDERMAN_LOOT_DROP, new SingleEnchantment((Holder<Enchantment>)enchantments
/*    */           
/* 75 */           .getOrThrow(Enchantments.SILK_TOUCH), 
/* 76 */           (IntProvider)ConstantInt.of(1)));
/*    */   }
/*    */   
/*    */   static ResourceKey<EnchantmentProvider> create(String id) {
/* 82 */     return ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\providers\VanillaEnchantmentProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */