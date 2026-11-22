/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public final class EquipmentTable extends Record {
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   private final Map<EquipmentSlot, Float> slotDropChances;
/*    */   
/*    */   public static final Codec<Map<EquipmentSlot, Float>> DROP_CHANCES_CODEC;
/*    */   
/*    */   public static final Codec<EquipmentTable> CODEC;
/*    */   
/*    */   public EquipmentTable(ResourceKey<LootTable> lootTable, Map<EquipmentSlot, Float> slotDropChances) {
/* 14 */     this.lootTable = lootTable;
/* 14 */     this.slotDropChances = slotDropChances;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EquipmentTable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EquipmentTable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/EquipmentTable;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EquipmentTable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/EquipmentTable;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<LootTable> lootTable() {
/* 14 */     return this.lootTable;
/*    */   }
/*    */   
/*    */   public Map<EquipmentSlot, Float> slotDropChances() {
/* 14 */     return this.slotDropChances;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     DROP_CHANCES_CODEC = Codec.either((Codec)Codec.FLOAT, (Codec)Codec.unboundedMap((Codec)EquipmentSlot.CODEC, (Codec)Codec.FLOAT)).xmap(either -> (Map)either.map(EquipmentTable::createForAllSlots, Function.identity()), provider -> {
/*    */           boolean dropChancesTheSame = (provider.values().stream().distinct().count() == 1L);
/*    */           boolean allSlotsArePresent = provider.keySet().containsAll(EquipmentSlot.VALUES);
/* 21 */           return (dropChancesTheSame && allSlotsArePresent) ? Either.left(provider.values().stream().findFirst().orElse(Float.valueOf(0.0F))) : Either.right(provider);
/*    */         });
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(EquipmentTable::lootTable), (App)DROP_CHANCES_CODEC.optionalFieldOf("slot_drop_chances", Map.of()).forGetter(EquipmentTable::slotDropChances)).apply((Applicative)i, EquipmentTable::new));
/*    */   }
/*    */   
/*    */   public EquipmentTable(ResourceKey<LootTable> lootTable, float dropChance) {
/*    */     this(lootTable, createForAllSlots(dropChance));
/*    */   }
/*    */   
/*    */   private static Map<EquipmentSlot, Float> createForAllSlots(float dropChance) {
/* 39 */     return createForAllSlots(List.of(EquipmentSlot.values()), dropChance);
/*    */   }
/*    */   
/*    */   private static Map<EquipmentSlot, Float> createForAllSlots(List<EquipmentSlot> slots, float dropChance) {
/* 43 */     Map<EquipmentSlot, Float> values = Maps.newHashMap();
/* 44 */     for (EquipmentSlot slot : slots)
/* 45 */       values.put(slot, Float.valueOf(dropChance)); 
/* 47 */     return values;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EquipmentTable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */