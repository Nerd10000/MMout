/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class LootItemConditions {
/*    */   public static MapCodec<? extends LootItemCondition> bootstrap(Registry<MapCodec<? extends LootItemCondition>> registry) {
/*  8 */     Registry.register(registry, "inverted", InvertedLootItemCondition.MAP_CODEC);
/*  9 */     Registry.register(registry, "any_of", AnyOfCondition.MAP_CODEC);
/* 10 */     Registry.register(registry, "all_of", AllOfCondition.MAP_CODEC);
/* 11 */     Registry.register(registry, "random_chance", LootItemRandomChanceCondition.MAP_CODEC);
/* 12 */     Registry.register(registry, "random_chance_with_enchanted_bonus", LootItemRandomChanceWithEnchantedBonusCondition.MAP_CODEC);
/* 13 */     Registry.register(registry, "entity_properties", LootItemEntityPropertyCondition.MAP_CODEC);
/* 14 */     Registry.register(registry, "killed_by_player", LootItemKilledByPlayerCondition.MAP_CODEC);
/* 15 */     Registry.register(registry, "entity_scores", EntityHasScoreCondition.MAP_CODEC);
/* 16 */     Registry.register(registry, "block_state_property", LootItemBlockStatePropertyCondition.MAP_CODEC);
/* 17 */     Registry.register(registry, "match_tool", MatchTool.MAP_CODEC);
/* 18 */     Registry.register(registry, "table_bonus", BonusLevelTableCondition.MAP_CODEC);
/* 19 */     Registry.register(registry, "survives_explosion", ExplosionCondition.MAP_CODEC);
/* 20 */     Registry.register(registry, "damage_source_properties", DamageSourceCondition.MAP_CODEC);
/* 21 */     Registry.register(registry, "location_check", LocationCheck.MAP_CODEC);
/* 22 */     Registry.register(registry, "weather_check", WeatherCheck.MAP_CODEC);
/* 23 */     Registry.register(registry, "reference", ConditionReference.MAP_CODEC);
/* 24 */     Registry.register(registry, "time_check", TimeCheck.MAP_CODEC);
/* 25 */     Registry.register(registry, "value_check", ValueCheckCondition.MAP_CODEC);
/* 26 */     Registry.register(registry, "enchantment_active_check", EnchantmentActiveCheck.MAP_CODEC);
/* 27 */     return (MapCodec<? extends LootItemCondition>)Registry.register(registry, "environment_attribute_check", EnvironmentAttributeCheck.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemConditions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */