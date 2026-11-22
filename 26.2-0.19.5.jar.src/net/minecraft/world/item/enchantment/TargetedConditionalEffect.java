/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public final class TargetedConditionalEffect<T> extends Record implements Validatable {
/*    */   private final EnchantmentTarget enchanted;
/*    */   
/*    */   private final EnchantmentTarget affected;
/*    */   
/*    */   private final T effect;
/*    */   
/*    */   private final Optional<LootItemCondition> requirements;
/*    */   
/*    */   public TargetedConditionalEffect(EnchantmentTarget enchanted, EnchantmentTarget affected, T effect, Optional<LootItemCondition> requirements) {
/* 12 */     this.enchanted = enchanted;
/* 12 */     this.affected = affected;
/* 12 */     this.effect = effect;
/* 12 */     this.requirements = requirements;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/TargetedConditionalEffect<TT;>;
/*    */   }
/*    */   
/*    */   public EnchantmentTarget enchanted() {
/* 12 */     return this.enchanted;
/*    */   }
/*    */   
/*    */   public EnchantmentTarget affected() {
/* 12 */     return this.affected;
/*    */   }
/*    */   
/*    */   public T effect() {
/* 12 */     return this.effect;
/*    */   }
/*    */   
/*    */   public Optional<LootItemCondition> requirements() {
/* 12 */     return this.requirements;
/*    */   }
/*    */   
/*    */   public static <S> Codec<TargetedConditionalEffect<S>> codec(Codec<S> effectCodec) {
/* 19 */     return RecordCodecBuilder.create(i -> i.group((App)EnchantmentTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), (App)EnchantmentTarget.CODEC.fieldOf("affected").forGetter(TargetedConditionalEffect::affected), (App)effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect), (App)LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((Applicative)i, TargetedConditionalEffect::new));
/*    */   }
/*    */   
/*    */   public static <S> Codec<TargetedConditionalEffect<S>> equipmentDropsCodec(Codec<S> effectCodec) {
/* 28 */     return RecordCodecBuilder.create(i -> i.group((App)EnchantmentTarget.NON_DAMAGE_CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted), (App)effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect), (App)LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 36 */     return (this.requirements.isEmpty() || ((LootItemCondition)this.requirements.get()).test(context));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 41 */     Validatable.validate(context, "requirements", this.requirements);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\TargetedConditionalEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */