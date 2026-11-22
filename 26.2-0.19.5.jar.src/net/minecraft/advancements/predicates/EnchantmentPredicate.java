/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ 
/*    */ public final class EnchantmentPredicate extends Record {
/*    */   private final Optional<HolderSet<Enchantment>> enchantments;
/*    */   
/*    */   private final MinMaxBounds.Ints level;
/*    */   
/*    */   public static final Codec<EnchantmentPredicate> CODEC;
/*    */   
/*    */   public EnchantmentPredicate(Optional<HolderSet<Enchantment>> enchantments, MinMaxBounds.Ints level) {
/* 15 */     this.enchantments = enchantments;
/* 15 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/EnchantmentPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/EnchantmentPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/EnchantmentPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/EnchantmentPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/EnchantmentPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/EnchantmentPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<Enchantment>> enchantments() {
/* 15 */     return this.enchantments;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints level() {
/* 15 */     return this.level;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("enchantments").forGetter(EnchantmentPredicate::enchantments), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("levels", MinMaxBounds.Ints.ANY).forGetter(EnchantmentPredicate::level)).apply((Applicative)i, EnchantmentPredicate::new));
/*    */   }
/*    */   
/*    */   public EnchantmentPredicate(Holder<Enchantment> enchantment, MinMaxBounds.Ints level) {
/* 25 */     this((Optional)Optional.of(HolderSet.direct(new Holder[] { enchantment }, )), level);
/*    */   }
/*    */   
/*    */   public EnchantmentPredicate(HolderSet<Enchantment> enchantments, MinMaxBounds.Ints level) {
/* 29 */     this(Optional.of(enchantments), level);
/*    */   }
/*    */   
/*    */   public boolean containedIn(ItemEnchantments itemEnchantments) {
/* 33 */     if (this.enchantments.isPresent()) {
/* 35 */       for (Holder<Enchantment> enchantment : this.enchantments.get()) {
/* 36 */         if (matchesEnchantment(itemEnchantments, enchantment))
/* 37 */           return true; 
/*    */       } 
/* 40 */       return false;
/*    */     } 
/* 41 */     if (this.level != MinMaxBounds.Ints.ANY) {
/* 43 */       for (Object2IntMap.Entry<Holder<Enchantment>> entry : (Iterable<Object2IntMap.Entry<Holder<Enchantment>>>)itemEnchantments.entrySet()) {
/* 44 */         if (this.level.matches(entry.getIntValue()))
/* 45 */           return true; 
/*    */       } 
/* 48 */       return false;
/*    */     } 
/* 51 */     return !itemEnchantments.isEmpty();
/*    */   }
/*    */   
/*    */   private boolean matchesEnchantment(ItemEnchantments itemEnchantments, Holder<Enchantment> enchantment) {
/* 56 */     int level = itemEnchantments.getLevel(enchantment);
/* 57 */     if (level == 0)
/* 59 */       return false; 
/* 61 */     if (this.level == MinMaxBounds.Ints.ANY)
/* 63 */       return true; 
/* 65 */     return this.level.matches(level);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\EnchantmentPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */