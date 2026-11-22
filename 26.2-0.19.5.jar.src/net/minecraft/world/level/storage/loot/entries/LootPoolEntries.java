/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class LootPoolEntries {
/*    */   public static final Codec<LootPoolEntryContainer> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.byNameCodec().dispatch(LootPoolEntryContainer::codec, c -> c);
/*    */   }
/*    */   
/*    */   public static MapCodec<? extends LootPoolEntryContainer> bootstrap(Registry<MapCodec<? extends LootPoolEntryContainer>> registry) {
/* 13 */     Registry.register(registry, "empty", EmptyLootItem.MAP_CODEC);
/* 14 */     Registry.register(registry, "item", LootItem.MAP_CODEC);
/* 15 */     Registry.register(registry, "loot_table", NestedLootTable.MAP_CODEC);
/* 16 */     Registry.register(registry, "dynamic", DynamicLoot.MAP_CODEC);
/* 17 */     Registry.register(registry, "tag", TagEntry.MAP_CODEC);
/* 18 */     Registry.register(registry, "slots", SlotLoot.MAP_CODEC);
/* 20 */     Registry.register(registry, "alternatives", AlternativesEntry.MAP_CODEC);
/* 21 */     Registry.register(registry, "sequence", SequentialEntry.MAP_CODEC);
/* 22 */     return (MapCodec<? extends LootPoolEntryContainer>)Registry.register(registry, "group", EntryGroup.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntries.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */