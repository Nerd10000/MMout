/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ public final class EnchantmentDefinition extends Record {
/*    */   private final HolderSet<Item> supportedItems;
/*    */   
/*    */   private final Optional<HolderSet<Item>> primaryItems;
/*    */   
/*    */   private final int weight;
/*    */   
/*    */   private final int maxLevel;
/*    */   
/*    */   private final Enchantment.Cost minCost;
/*    */   
/*    */   private final Enchantment.Cost maxCost;
/*    */   
/*    */   private final int anvilCost;
/*    */   
/*    */   private final List<EquipmentSlotGroup> slots;
/*    */   
/*    */   public static final MapCodec<EnchantmentDefinition> CODEC;
/*    */   
/*    */   public EnchantmentDefinition(HolderSet<Item> supportedItems, Optional<HolderSet<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots) {
/* 88 */     this.supportedItems = supportedItems;
/* 88 */     this.primaryItems = primaryItems;
/* 88 */     this.weight = weight;
/* 88 */     this.maxLevel = maxLevel;
/* 88 */     this.minCost = minCost;
/* 88 */     this.maxCost = maxCost;
/* 88 */     this.anvilCost = anvilCost;
/* 88 */     this.slots = slots;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Item> supportedItems() {
/* 88 */     return this.supportedItems;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<Item>> primaryItems() {
/* 88 */     return this.primaryItems;
/*    */   }
/*    */   
/*    */   public int weight() {
/* 88 */     return this.weight;
/*    */   }
/*    */   
/*    */   public int maxLevel() {
/* 88 */     return this.maxLevel;
/*    */   }
/*    */   
/*    */   public Enchantment.Cost minCost() {
/* 88 */     return this.minCost;
/*    */   }
/*    */   
/*    */   public Enchantment.Cost maxCost() {
/* 88 */     return this.maxCost;
/*    */   }
/*    */   
/*    */   public int anvilCost() {
/* 88 */     return this.anvilCost;
/*    */   }
/*    */   
/*    */   public List<EquipmentSlotGroup> slots() {
/* 88 */     return this.slots;
/*    */   }
/*    */   
/*    */   static {
/* 98 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentDefinition::supportedItems), (App)RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("primary_items").forGetter(EnchantmentDefinition::primaryItems), (App)ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight), (App)ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel), (App)Enchantment.Cost.CODEC.fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost), (App)Enchantment.Cost.CODEC.fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost), (App)EquipmentSlotGroup.CODEC.listOf().fieldOf("slots").forGetter(EnchantmentDefinition::slots)).apply((Applicative)i, EnchantmentDefinition::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\Enchantment$EnchantmentDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */