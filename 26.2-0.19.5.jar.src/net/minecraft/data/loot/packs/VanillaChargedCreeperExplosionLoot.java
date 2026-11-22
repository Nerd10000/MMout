/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.loot.LootTableSubProvider;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootPool;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*    */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*    */ import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ public final class VanillaChargedCreeperExplosionLoot extends Record implements LootTableSubProvider {
/*    */   private final HolderLookup.Provider registries;
/*    */   
/*    */   public VanillaChargedCreeperExplosionLoot(HolderLookup.Provider registries) {
/* 32 */     this.registries = registries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderLookup.Provider registries() {
/* 32 */     return this.registries;
/*    */   }
/*    */   
/* 33 */   private static final List<Entry> ENTRIES = List.of(new Entry(BuiltInLootTables.CHARGED_CREEPER_PIGLIN, EntityTypes.PIGLIN, Items.PIGLIN_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_CREEPER, EntityTypes.CREEPER, Items.CREEPER_HEAD), new Entry(BuiltInLootTables.CHARGED_CREEPER_SKELETON, EntityTypes.SKELETON, Items.SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_WITHER_SKELETON, EntityTypes.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL), new Entry(BuiltInLootTables.CHARGED_CREEPER_ZOMBIE, EntityTypes.ZOMBIE, Items.ZOMBIE_HEAD));
/*    */   
/*    */   public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
/* 43 */     HolderLookup.RegistryLookup registryLookup = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
/* 45 */     List<LootPoolEntryContainer.Builder<?>> alternatives = new ArrayList<>(ENTRIES.size());
/* 46 */     for (Entry entry : ENTRIES) {
/* 47 */       output.accept(entry.lootTable, LootTable.lootTable()
/* 48 */           .withPool(LootPool.lootPool()
/* 49 */             .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 50 */             .add((LootPoolEntryContainer.Builder)LootItem.lootTableItem((ItemLike)entry.item))));
/* 53 */       LootItemCondition.Builder predicate = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of((HolderGetter)registryLookup, entry.entityType)));
/* 54 */       alternatives.add(NestedLootTable.lootTableReference(entry.lootTable).when(predicate));
/*    */     } 
/* 57 */     output.accept(BuiltInLootTables.CHARGED_CREEPER, LootTable.lootTable()
/* 58 */         .withPool(LootPool.lootPool()
/* 59 */           .setRolls((NumberProvider)ConstantValue.exactly(1.0F))
/* 60 */           .add((LootPoolEntryContainer.Builder)AlternativesEntry.alternatives((LootPoolEntryContainer.Builder[])alternatives.toArray(x$0 -> new LootPoolEntryContainer.Builder[x$0])))));
/*    */   }
/*    */   
/*    */   private static final class Entry extends Record {
/*    */     private final ResourceKey<LootTable> lootTable;
/*    */     
/*    */     private final EntityType<?> entityType;
/*    */     
/*    */     private final Item item;
/*    */     
/*    */     private Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) {
/* 65 */       this.lootTable = lootTable;
/* 65 */       this.entityType = entityType;
/* 65 */       this.item = item;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ResourceKey<LootTable> lootTable() {
/* 65 */       return this.lootTable;
/*    */     }
/*    */     
/*    */     public EntityType<?> entityType() {
/* 65 */       return this.entityType;
/*    */     }
/*    */     
/*    */     public Item item() {
/* 65 */       return this.item;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaChargedCreeperExplosionLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */