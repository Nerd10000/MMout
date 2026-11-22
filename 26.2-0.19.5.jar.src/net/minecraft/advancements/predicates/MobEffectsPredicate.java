/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class MobEffectsPredicate extends Record {
/*    */   private final Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap;
/*    */   
/*    */   public MobEffectsPredicate(Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap) {
/* 16 */     this.effectMap = effectMap;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<Holder<MobEffect>, MobEffectInstancePredicate> effectMap() {
/* 16 */     return this.effectMap;
/*    */   }
/*    */   
/* 19 */   public static final Codec<MobEffectsPredicate> CODEC = Codec.unboundedMap(MobEffect.CODEC, MobEffectInstancePredicate.CODEC).xmap(MobEffectsPredicate::new, MobEffectsPredicate::effectMap);
/*    */   
/*    */   public boolean matches(Entity entity) {
/* 22 */     if (entity instanceof LivingEntity) {
/* 22 */       LivingEntity living = (LivingEntity)entity;
/* 22 */       if (matches(living.getActiveEffectsMap()));
/*    */     } 
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   public boolean matches(LivingEntity entity) {
/* 26 */     return matches(entity.getActiveEffectsMap());
/*    */   }
/*    */   
/*    */   public boolean matches(Map<Holder<MobEffect>, MobEffectInstance> effects) {
/* 30 */     for (Map.Entry<Holder<MobEffect>, MobEffectInstancePredicate> entry : this.effectMap.entrySet()) {
/* 31 */       MobEffectInstance instance = effects.get(entry.getKey());
/* 32 */       if (!((MobEffectInstancePredicate)entry.getValue()).matches(instance))
/* 33 */         return false; 
/*    */     } 
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 41 */     private final ImmutableMap.Builder<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = ImmutableMap.builder();
/*    */     
/*    */     public static Builder effects() {
/* 44 */       return new Builder();
/*    */     }
/*    */     
/*    */     public Builder and(Holder<MobEffect> effect) {
/* 48 */       this.effectMap.put(effect, new MobEffectsPredicate.MobEffectInstancePredicate());
/* 49 */       return this;
/*    */     }
/*    */     
/*    */     public Builder and(Holder<MobEffect> effect, MobEffectsPredicate.MobEffectInstancePredicate predicate) {
/* 53 */       this.effectMap.put(effect, predicate);
/* 54 */       return this;
/*    */     }
/*    */     
/*    */     public MobEffectsPredicate build() {
/* 58 */       return new MobEffectsPredicate((Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate>)this.effectMap.build());
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class MobEffectInstancePredicate extends Record {
/*    */     private final MinMaxBounds.Ints amplifier;
/*    */     
/*    */     private final MinMaxBounds.Ints duration;
/*    */     
/*    */     private final Optional<Boolean> ambient;
/*    */     
/*    */     private final Optional<Boolean> visible;
/*    */     
/*    */     public static final Codec<MobEffectInstancePredicate> CODEC;
/*    */     
/*    */     public MobEffectInstancePredicate(MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration, Optional<Boolean> ambient, Optional<Boolean> visible) {
/* 62 */       this.amplifier = amplifier;
/* 62 */       this.duration = duration;
/* 62 */       this.ambient = ambient;
/* 62 */       this.visible = visible;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public MinMaxBounds.Ints amplifier() {
/* 62 */       return this.amplifier;
/*    */     }
/*    */     
/*    */     public MinMaxBounds.Ints duration() {
/* 62 */       return this.duration;
/*    */     }
/*    */     
/*    */     public Optional<Boolean> ambient() {
/* 62 */       return this.ambient;
/*    */     }
/*    */     
/*    */     public Optional<Boolean> visible() {
/* 62 */       return this.visible;
/*    */     }
/*    */     
/*    */     static {
/* 68 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("amplifier", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::amplifier), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::duration), (App)Codec.BOOL.optionalFieldOf("ambient").forGetter(MobEffectInstancePredicate::ambient), (App)Codec.BOOL.optionalFieldOf("visible").forGetter(MobEffectInstancePredicate::visible)).apply((Applicative)i, MobEffectInstancePredicate::new));
/*    */     }
/*    */     
/*    */     public MobEffectInstancePredicate() {
/* 76 */       this(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, Optional.empty(), Optional.empty());
/*    */     }
/*    */     
/*    */     public boolean matches(MobEffectInstance instance) {
/* 80 */       if (instance == null)
/* 81 */         return false; 
/* 83 */       if (!this.amplifier.matches(instance.getAmplifier()))
/* 84 */         return false; 
/* 86 */       if (!this.duration.matches(instance.getDuration()))
/* 87 */         return false; 
/* 89 */       if (this.ambient.isPresent() && ((Boolean)this.ambient.get()).booleanValue() != instance.isAmbient())
/* 90 */         return false; 
/* 92 */       if (this.visible.isPresent() && ((Boolean)this.visible.get()).booleanValue() != instance.isVisible())
/* 93 */         return false; 
/* 95 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MobEffectsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */