/*    */ package net.minecraft.data.loot.packs;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ final class Entry extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   private final EntityType<?> entityType;
/*    */   
/*    */   private final Item item;
/*    */   
/*    */   private Entry(ResourceKey<LootTable> lootTable, EntityType<?> entityType, Item item) {
/* 65 */     this.lootTable = lootTable;
/* 65 */     this.entityType = entityType;
/* 65 */     this.item = item;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/loot/packs/VanillaChargedCreeperExplosionLoot$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<LootTable> lootTable() {
/* 65 */     return this.lootTable;
/*    */   }
/*    */   
/*    */   public EntityType<?> entityType() {
/* 65 */     return this.entityType;
/*    */   }
/*    */   
/*    */   public Item item() {
/* 65 */     return this.item;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\packs\VanillaChargedCreeperExplosionLoot$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */