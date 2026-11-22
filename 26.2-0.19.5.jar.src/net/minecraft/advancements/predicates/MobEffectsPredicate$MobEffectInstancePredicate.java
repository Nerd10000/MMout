/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ 
/*    */ public final class MobEffectInstancePredicate extends Record {
/*    */   private final MinMaxBounds.Ints amplifier;
/*    */   
/*    */   private final MinMaxBounds.Ints duration;
/*    */   
/*    */   private final Optional<Boolean> ambient;
/*    */   
/*    */   private final Optional<Boolean> visible;
/*    */   
/*    */   public static final Codec<MobEffectInstancePredicate> CODEC;
/*    */   
/*    */   public MobEffectInstancePredicate(MinMaxBounds.Ints amplifier, MinMaxBounds.Ints duration, Optional<Boolean> ambient, Optional<Boolean> visible) {
/* 62 */     this.amplifier = amplifier;
/* 62 */     this.duration = duration;
/* 62 */     this.ambient = ambient;
/* 62 */     this.visible = visible;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #62	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/MobEffectsPredicate$MobEffectInstancePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints amplifier() {
/* 62 */     return this.amplifier;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints duration() {
/* 62 */     return this.duration;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> ambient() {
/* 62 */     return this.ambient;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> visible() {
/* 62 */     return this.visible;
/*    */   }
/*    */   
/*    */   static {
/* 68 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("amplifier", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::amplifier), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(MobEffectInstancePredicate::duration), (App)Codec.BOOL.optionalFieldOf("ambient").forGetter(MobEffectInstancePredicate::ambient), (App)Codec.BOOL.optionalFieldOf("visible").forGetter(MobEffectInstancePredicate::visible)).apply((Applicative)i, MobEffectInstancePredicate::new));
/*    */   }
/*    */   
/*    */   public MobEffectInstancePredicate() {
/* 76 */     this(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, Optional.empty(), Optional.empty());
/*    */   }
/*    */   
/*    */   public boolean matches(MobEffectInstance instance) {
/* 80 */     if (instance == null)
/* 81 */       return false; 
/* 83 */     if (!this.amplifier.matches(instance.getAmplifier()))
/* 84 */       return false; 
/* 86 */     if (!this.duration.matches(instance.getDuration()))
/* 87 */       return false; 
/* 89 */     if (this.ambient.isPresent() && ((Boolean)this.ambient.get()).booleanValue() != instance.isAmbient())
/* 90 */       return false; 
/* 92 */     if (this.visible.isPresent() && ((Boolean)this.visible.get()).booleanValue() != instance.isVisible())
/* 93 */       return false; 
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MobEffectsPredicate$MobEffectInstancePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */