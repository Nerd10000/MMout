/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public final class ConditionalEffect<T> extends Record implements Validatable {
/*    */   private final T effect;
/*    */   
/*    */   private final Optional<LootItemCondition> requirements;
/*    */   
/*    */   public ConditionalEffect(T effect, Optional<LootItemCondition> requirements) {
/* 12 */     this.effect = effect;
/* 12 */     this.requirements = requirements;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/ConditionalEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/ConditionalEffect<TT;>;
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
/*    */   public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec) {
/* 17 */     return RecordCodecBuilder.create(i -> i.group((App)effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect), (App)LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)).apply((Applicative)i, ConditionalEffect::new));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 24 */     return (this.requirements.isEmpty() || ((LootItemCondition)this.requirements.get()).test(context));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 29 */     Validatable.validate(context, "requirements", this.requirements);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\ConditionalEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */