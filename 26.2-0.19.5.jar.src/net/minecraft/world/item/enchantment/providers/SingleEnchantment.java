/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class SingleEnchantment extends Record implements EnchantmentProvider {
/*    */   private final Holder<Enchantment> enchantment;
/*    */   
/*    */   private final IntProvider level;
/*    */   
/*    */   public static final MapCodec<SingleEnchantment> CODEC;
/*    */   
/*    */   public SingleEnchantment(Holder<Enchantment> enchantment, IntProvider level) {
/* 15 */     this.enchantment = enchantment;
/* 15 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/SingleEnchantment;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<Enchantment> enchantment() {
/* 15 */     return this.enchantment;
/*    */   }
/*    */   
/*    */   public IntProvider level() {
/* 15 */     return this.level;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(SingleEnchantment::enchantment), (App)IntProviders.CODEC.fieldOf("level").forGetter(SingleEnchantment::level)).apply((Applicative)i, SingleEnchantment::new));
/*    */   }
/*    */   
/*    */   public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
/* 26 */     itemEnchantments.upgrade(this.enchantment, Mth.clamp(this.level.sample(random), ((Enchantment)this.enchantment.value()).getMinLevel(), ((Enchantment)this.enchantment.value()).getMaxLevel()));
/*    */   }
/*    */   
/*    */   public MapCodec<SingleEnchantment> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\providers\SingleEnchantment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */