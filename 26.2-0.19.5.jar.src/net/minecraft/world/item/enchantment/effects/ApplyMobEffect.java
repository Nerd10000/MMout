/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.item.enchantment.LevelBasedValue;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ApplyMobEffect extends Record implements EnchantmentEntityEffect {
/*    */   private final HolderSet<MobEffect> toApply;
/*    */   
/*    */   private final LevelBasedValue minDuration;
/*    */   
/*    */   private final LevelBasedValue maxDuration;
/*    */   
/*    */   private final LevelBasedValue minAmplifier;
/*    */   
/*    */   private final LevelBasedValue maxAmplifier;
/*    */   
/*    */   public static final MapCodec<ApplyMobEffect> CODEC;
/*    */   
/*    */   public ApplyMobEffect(HolderSet<MobEffect> toApply, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) {
/* 23 */     this.toApply = toApply;
/* 23 */     this.minDuration = minDuration;
/* 23 */     this.maxDuration = maxDuration;
/* 23 */     this.minAmplifier = minAmplifier;
/* 23 */     this.maxAmplifier = maxAmplifier;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ApplyMobEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<MobEffect> toApply() {
/* 23 */     return this.toApply;
/*    */   }
/*    */   
/*    */   public LevelBasedValue minDuration() {
/* 23 */     return this.minDuration;
/*    */   }
/*    */   
/*    */   public LevelBasedValue maxDuration() {
/* 23 */     return this.maxDuration;
/*    */   }
/*    */   
/*    */   public LevelBasedValue minAmplifier() {
/* 23 */     return this.minAmplifier;
/*    */   }
/*    */   
/*    */   public LevelBasedValue maxAmplifier() {
/* 23 */     return this.maxAmplifier;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply), (App)LevelBasedValue.CODEC.fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration), (App)LevelBasedValue.CODEC.fieldOf("max_duration").forGetter(ApplyMobEffect::maxDuration), (App)LevelBasedValue.CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier), (App)LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier)).apply((Applicative)i, ApplyMobEffect::new));
/*    */   }
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 41 */     if (entity instanceof LivingEntity) {
/* 41 */       LivingEntity living = (LivingEntity)entity;
/* 42 */       RandomSource random = living.getRandom();
/* 43 */       Optional<Holder<MobEffect>> selected = this.toApply.getRandomElement(random);
/* 44 */       if (selected.isPresent()) {
/* 45 */         int ticks = Math.round(Mth.randomBetween(random, this.minDuration.calculate(enchantmentLevel), this.maxDuration.calculate(enchantmentLevel)) * 20.0F);
/* 46 */         int amplifier = Math.max(0, Math.round(Mth.randomBetween(random, this.minAmplifier.calculate(enchantmentLevel), this.maxAmplifier.calculate(enchantmentLevel))));
/* 47 */         living.addEffect(new MobEffectInstance(selected.get(), ticks, amplifier));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public MapCodec<ApplyMobEffect> codec() {
/* 54 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\ApplyMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */