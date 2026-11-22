/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public final class LootItemRandomChanceCondition extends Record implements LootItemCondition {
/*    */   private final NumberProvider chance;
/*    */   
/*    */   public static final MapCodec<LootItemRandomChanceCondition> MAP_CODEC;
/*    */   
/*    */   public LootItemRandomChanceCondition(NumberProvider chance) {
/* 10 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NumberProvider chance() {
/* 10 */     return this.chance;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("chance").forGetter(LootItemRandomChanceCondition::chance)).apply((Applicative)i, LootItemRandomChanceCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<LootItemRandomChanceCondition> codec() {
/* 19 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 24 */     float probability = this.chance.getFloat(context);
/* 25 */     return (context.getRandom().nextFloat() < probability);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChance(float probability) {
/* 29 */     return () -> new LootItemRandomChanceCondition((NumberProvider)ConstantValue.exactly(probability));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder randomChance(NumberProvider probability) {
/* 33 */     return () -> new LootItemRandomChanceCondition(probability);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemRandomChanceCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */