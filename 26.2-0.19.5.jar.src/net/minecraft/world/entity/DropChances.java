/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class DropChances extends Record {
/*    */   private final Map<EquipmentSlot, Float> byEquipment;
/*    */   
/*    */   public static final float DEFAULT_EQUIPMENT_DROP_CHANCE = 0.085F;
/*    */   
/*    */   public static final float PRESERVE_ITEM_DROP_CHANCE_THRESHOLD = 1.0F;
/*    */   
/*    */   public static final int PRESERVE_ITEM_DROP_CHANCE = 2;
/*    */   
/*    */   public DropChances(Map<EquipmentSlot, Float> byEquipment) {
/* 10 */     this.byEquipment = byEquipment;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/DropChances;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/DropChances;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/DropChances;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/DropChances;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<EquipmentSlot, Float> byEquipment() {
/* 10 */     return this.byEquipment;
/*    */   }
/*    */   
/* 17 */   public static final DropChances DEFAULT = new DropChances(Util.makeEnumMap(EquipmentSlot.class, slot -> Float.valueOf(0.085F)));
/*    */   
/* 19 */   public static final Codec<DropChances> CODEC = Codec.unboundedMap((Codec)EquipmentSlot.CODEC, ExtraCodecs.NON_NEGATIVE_FLOAT)
/* 20 */     .xmap(DropChances::toEnumMap, DropChances::filterDefaultValues)
/* 21 */     .xmap(DropChances::new, DropChances::byEquipment);
/*    */   
/*    */   private static Map<EquipmentSlot, Float> filterDefaultValues(Map<EquipmentSlot, Float> map) {
/* 24 */     Map<EquipmentSlot, Float> filteredMap = new HashMap<>(map);
/* 25 */     filteredMap.values().removeIf(chance -> (chance.floatValue() == 0.085F));
/* 26 */     return filteredMap;
/*    */   }
/*    */   
/*    */   private static Map<EquipmentSlot, Float> toEnumMap(Map<EquipmentSlot, Float> map) {
/* 30 */     return Util.makeEnumMap(EquipmentSlot.class, slot -> (Float)map.getOrDefault(slot, Float.valueOf(0.085F)));
/*    */   }
/*    */   
/*    */   public DropChances withGuaranteedDrop(EquipmentSlot slot) {
/* 34 */     return withEquipmentChance(slot, 2.0F);
/*    */   }
/*    */   
/*    */   public DropChances withEquipmentChance(EquipmentSlot slot, float chance) {
/* 38 */     if (chance < 0.0F)
/* 39 */       throw new IllegalArgumentException("Tried to set invalid equipment chance " + chance + " for " + String.valueOf(slot)); 
/* 41 */     if (byEquipment(slot) == chance)
/* 42 */       return this; 
/* 44 */     return new DropChances(
/* 45 */         Util.makeEnumMap(EquipmentSlot.class, newSlot -> Float.valueOf((newSlot == slot) ? chance : byEquipment(newSlot))));
/*    */   }
/*    */   
/*    */   public float byEquipment(EquipmentSlot slot) {
/* 50 */     return ((Float)this.byEquipment.getOrDefault(slot, Float.valueOf(0.085F))).floatValue();
/*    */   }
/*    */   
/*    */   public boolean isPreserved(EquipmentSlot slot) {
/* 54 */     return (byEquipment(slot) > 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\DropChances.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */