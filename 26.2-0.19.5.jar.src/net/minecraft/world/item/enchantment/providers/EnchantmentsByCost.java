/*    */ package net.minecraft.world.item.enchantment.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class EnchantmentsByCost extends Record implements EnchantmentProvider {
/*    */   private final HolderSet<Enchantment> enchantments;
/*    */   
/*    */   private final IntProvider cost;
/*    */   
/*    */   public static final MapCodec<EnchantmentsByCost> CODEC;
/*    */   
/*    */   public EnchantmentsByCost(HolderSet<Enchantment> enchantments, IntProvider cost) {
/* 20 */     this.enchantments = enchantments;
/* 20 */     this.cost = cost;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/providers/EnchantmentsByCost;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Enchantment> enchantments() {
/* 20 */     return this.enchantments;
/*    */   }
/*    */   
/*    */   public IntProvider cost() {
/* 20 */     return this.cost;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentsByCost::enchantments), (App)IntProviders.CODEC.fieldOf("cost").forGetter(EnchantmentsByCost::cost)).apply((Applicative)i, EnchantmentsByCost::new));
/*    */   }
/*    */   
/*    */   public void enchant(ItemStack item, ItemEnchantments.Mutable itemEnchantments, RandomSource random, DifficultyInstance difficulty) {
/* 31 */     List<EnchantmentInstance> instances = EnchantmentHelper.selectEnchantment(random, item, this.cost.sample(random), this.enchantments.stream());
/* 32 */     for (EnchantmentInstance instance : instances)
/* 33 */       itemEnchantments.upgrade(instance.enchantment(), instance.level()); 
/*    */   }
/*    */   
/*    */   public MapCodec<EnchantmentsByCost> codec() {
/* 39 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\providers\EnchantmentsByCost.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */