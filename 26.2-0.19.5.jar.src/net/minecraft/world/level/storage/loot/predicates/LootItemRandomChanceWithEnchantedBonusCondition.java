/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.Enchantments;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class LootItemRandomChanceWithEnchantedBonusCondition extends Record implements LootItemCondition {
/*    */   private final float unenchantedChance;
/*    */   
/*    */   private final LevelBasedValue enchantedChance;
/*    */   
/*    */   private final Holder<Enchantment> enchantment;
/*    */   
/*    */   public static final MapCodec<LootItemRandomChanceWithEnchantedBonusCondition> MAP_CODEC;
/*    */   
/*    */   public LootItemRandomChanceWithEnchantedBonusCondition(float unenchantedChance, LevelBasedValue enchantedChance, Holder<Enchantment> enchantment) {
/* 21 */     this.unenchantedChance = unenchantedChance;
/* 21 */     this.enchantedChance = enchantedChance;
/* 21 */     this.enchantment = enchantment;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithEnchantedBonusCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float unenchantedChance() {
/* 21 */     return this.unenchantedChance;
/*    */   }
/*    */   
/*    */   public LevelBasedValue enchantedChance() {
/* 21 */     return this.enchantedChance;
/*    */   }
/*    */   
/*    */   public Holder<Enchantment> enchantment() {
/* 21 */     return this.enchantment;
/*    */   }
/*    */   
/*    */   static {
/* 26 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("unenchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::unenchantedChance), (App)LevelBasedValue.CODEC.fieldOf("enchanted_chance").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantedChance), (App)Enchantment.CODEC.fieldOf("enchantment").forGetter(LootItemRandomChanceWithEnchantedBonusCondition::enchantment)).apply((Applicative)i, LootItemRandomChanceWithEnchantedBonusCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<LootItemRandomChanceWithEnchantedBonusCondition> codec() {
/* 34 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 39 */     return Set.of(LootContextParams.ATTACKING_ENTITY);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 44 */     Entity killerEntity = (Entity)context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
/* 46 */     LivingEntity livingKiller = (LivingEntity)killerEntity;
/* 46 */     int enchantmentLevel = (killerEntity instanceof LivingEntity) ? EnchantmentHelper.getEnchantmentLevel(this.enchantment, livingKiller) : 0;
/* 47 */     float chance = (enchantmentLevel > 0) ? this.enchantedChance.calculate(enchantmentLevel) : this.unenchantedChance;
/* 48 */     return (context.getRandom().nextFloat() < chance);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChanceAndLootingBoost(HolderLookup.Provider registries, float chance, float perEnchantmentLevel) {
/* 52 */     HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
/* 53 */     return () -> new LootItemRandomChanceWithEnchantedBonusCondition(chance, (LevelBasedValue)new LevelBasedValue.Linear(chance + perEnchantmentLevel, perEnchantmentLevel), (Holder<Enchantment>)enchantments.getOrThrow(Enchantments.LOOTING));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceWithEnchantedBonusCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */