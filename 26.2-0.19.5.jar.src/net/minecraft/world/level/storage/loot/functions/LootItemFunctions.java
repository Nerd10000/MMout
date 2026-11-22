/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class LootItemFunctions {
/*    */   public static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY;
/*    */   
/*    */   public static final Codec<LootItemFunction> TYPED_CODEC;
/*    */   
/*    */   static {
/* 17 */     IDENTITY = ((stack, context) -> stack);
/* 21 */     TYPED_CODEC = BuiltInRegistries.LOOT_FUNCTION_TYPE.byNameCodec().dispatch("function", LootItemFunction::codec, c -> c);
/*    */   }
/*    */   
/* 24 */   public static final Codec<LootItemFunction> ROOT_CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, SequenceFunction.INLINE_CODEC));
/*    */   
/* 26 */   public static final Codec<Holder<LootItemFunction>> CODEC = (Codec<Holder<LootItemFunction>>)RegistryFileCodec.create(Registries.ITEM_MODIFIER, ROOT_CODEC);
/*    */   
/*    */   public static MapCodec<? extends LootItemFunction> bootstrap(Registry<MapCodec<? extends LootItemFunction>> registry) {
/* 29 */     Registry.register(registry, "set_count", SetItemCountFunction.MAP_CODEC);
/* 30 */     Registry.register(registry, "set_item", SetItemFunction.MAP_CODEC);
/* 31 */     Registry.register(registry, "enchant_with_levels", EnchantWithLevelsFunction.MAP_CODEC);
/* 32 */     Registry.register(registry, "enchant_randomly", EnchantRandomlyFunction.MAP_CODEC);
/* 33 */     Registry.register(registry, "set_enchantments", SetEnchantmentsFunction.MAP_CODEC);
/* 34 */     Registry.register(registry, "set_custom_data", SetCustomDataFunction.MAP_CODEC);
/* 35 */     Registry.register(registry, "set_components", SetComponentsFunction.MAP_CODEC);
/* 36 */     Registry.register(registry, "furnace_smelt", SmeltItemFunction.MAP_CODEC);
/* 37 */     Registry.register(registry, "enchanted_count_increase", EnchantedCountIncreaseFunction.MAP_CODEC);
/* 38 */     Registry.register(registry, "set_damage", SetItemDamageFunction.MAP_CODEC);
/* 39 */     Registry.register(registry, "set_attributes", SetAttributesFunction.MAP_CODEC);
/* 40 */     Registry.register(registry, "set_name", SetNameFunction.MAP_CODEC);
/* 41 */     Registry.register(registry, "exploration_map", ExplorationMapFunction.MAP_CODEC);
/* 42 */     Registry.register(registry, "set_stew_effect", SetStewEffectFunction.MAP_CODEC);
/* 43 */     Registry.register(registry, "copy_name", CopyNameFunction.MAP_CODEC);
/* 44 */     Registry.register(registry, "set_contents", SetContainerContents.MAP_CODEC);
/* 45 */     Registry.register(registry, "modify_contents", ModifyContainerContents.MAP_CODEC);
/* 46 */     Registry.register(registry, "filtered", FilteredFunction.MAP_CODEC);
/* 47 */     Registry.register(registry, "limit_count", LimitCount.MAP_CODEC);
/* 48 */     Registry.register(registry, "apply_bonus", ApplyBonusCount.MAP_CODEC);
/* 49 */     Registry.register(registry, "set_loot_table", SetContainerLootTable.MAP_CODEC);
/* 50 */     Registry.register(registry, "explosion_decay", ApplyExplosionDecay.MAP_CODEC);
/* 51 */     Registry.register(registry, "set_lore", SetLoreFunction.MAP_CODEC);
/* 52 */     Registry.register(registry, "fill_player_head", FillPlayerHead.MAP_CODEC);
/* 53 */     Registry.register(registry, "copy_custom_data", CopyCustomDataFunction.MAP_CODEC);
/* 54 */     Registry.register(registry, "copy_state", CopyBlockState.MAP_CODEC);
/* 55 */     Registry.register(registry, "set_banner_pattern", SetBannerPatternFunction.MAP_CODEC);
/* 56 */     Registry.register(registry, "set_potion", SetPotionFunction.MAP_CODEC);
/* 57 */     Registry.register(registry, "set_random_dyes", SetRandomDyesFunction.MAP_CODEC);
/* 58 */     Registry.register(registry, "set_random_potion", SetRandomPotionFunction.MAP_CODEC);
/* 59 */     Registry.register(registry, "set_instrument", SetInstrumentFunction.MAP_CODEC);
/* 60 */     Registry.register(registry, "reference", FunctionReference.MAP_CODEC);
/* 61 */     Registry.register(registry, "sequence", SequenceFunction.MAP_CODEC);
/* 62 */     Registry.register(registry, "copy_components", CopyComponentsFunction.MAP_CODEC);
/* 63 */     Registry.register(registry, "set_fireworks", SetFireworksFunction.MAP_CODEC);
/* 64 */     Registry.register(registry, "set_firework_explosion", SetFireworkExplosionFunction.MAP_CODEC);
/* 65 */     Registry.register(registry, "set_book_cover", SetBookCoverFunction.MAP_CODEC);
/* 66 */     Registry.register(registry, "set_written_book_pages", SetWrittenBookPagesFunction.MAP_CODEC);
/* 67 */     Registry.register(registry, "set_writable_book_pages", SetWritableBookPagesFunction.MAP_CODEC);
/* 68 */     Registry.register(registry, "toggle_tooltips", ToggleTooltips.MAP_CODEC);
/* 69 */     Registry.register(registry, "set_ominous_bottle_amplifier", SetOminousBottleAmplifierFunction.MAP_CODEC);
/* 70 */     Registry.register(registry, "set_custom_model_data", SetCustomModelDataFunction.MAP_CODEC);
/* 71 */     return (MapCodec<? extends LootItemFunction>)Registry.register(registry, "discard", DiscardItem.MAP_CODEC);
/*    */   }
/*    */   
/*    */   public static BiFunction<ItemStack, LootContext, ItemStack> compose(List<? extends BiFunction<ItemStack, LootContext, ItemStack>> functions) {
/*    */     BiFunction<ItemStack, LootContext, ItemStack> first, second;
/* 75 */     List<BiFunction<ItemStack, LootContext, ItemStack>> terms = List.copyOf(functions);
/* 76 */     switch (terms.size()) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 80 */         first = terms.get(0);
/* 81 */         second = terms.get(1);
/*    */     } 
/*    */     return (itemStack, context) -> {
/*    */         for (BiFunction<ItemStack, LootContext, ItemStack> function : (Iterable<BiFunction<ItemStack, LootContext, ItemStack>>)terms)
/*    */           itemStack = function.apply(itemStack, context); 
/*    */         return itemStack;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunctions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */