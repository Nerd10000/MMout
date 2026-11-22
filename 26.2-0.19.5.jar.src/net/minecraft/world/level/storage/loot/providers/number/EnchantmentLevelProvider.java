/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class EnchantmentLevelProvider extends Record implements NumberProvider {
/*    */   private final LevelBasedValue amount;
/*    */   
/*    */   public static final MapCodec<EnchantmentLevelProvider> MAP_CODEC;
/*    */   
/*    */   public EnchantmentLevelProvider(LevelBasedValue amount) {
/*  9 */     this.amount = amount;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnchantmentLevelProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LevelBasedValue amount() {
/*  9 */     return this.amount;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentLevelProvider::amount)).apply((Applicative)i, EnchantmentLevelProvider::new));
/*    */   }
/*    */   
/*    */   public MapCodec<EnchantmentLevelProvider> codec() {
/* 16 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 21 */     int level = ((Integer)context.getParameter(LootContextParams.ENCHANTMENT_LEVEL)).intValue();
/* 22 */     return this.amount.calculate(level);
/*    */   }
/*    */   
/*    */   public static EnchantmentLevelProvider forEnchantmentLevel(LevelBasedValue amount) {
/* 26 */     return new EnchantmentLevelProvider(amount);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\EnchantmentLevelProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */