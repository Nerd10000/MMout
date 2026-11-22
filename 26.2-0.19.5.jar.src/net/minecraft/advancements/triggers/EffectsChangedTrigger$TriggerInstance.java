/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.MobEffectsPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<MobEffectsPredicate> effects;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> source;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<MobEffectsPredicate> effects, Optional<ContextAwarePredicate> source) {
/* 28 */     this.player = player;
/* 28 */     this.effects = effects;
/* 28 */     this.source = source;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/EffectsChangedTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 28 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<MobEffectsPredicate> effects() {
/* 28 */     return this.effects;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> source() {
/* 28 */     return this.source;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)MobEffectsPredicate.CODEC.optionalFieldOf("effects").forGetter(TriggerInstance::effects), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("source").forGetter(TriggerInstance::source)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> hasEffects(MobEffectsPredicate.Builder effects) {
/* 40 */     return CriteriaTriggers.EFFECTS_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(effects.build()), Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> gotEffectsFrom(EntityPredicate.Builder source) {
/* 44 */     return CriteriaTriggers.EFFECTS_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(source.build()))));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerPlayer player, LootContext source) {
/* 48 */     if (this.effects.isPresent() && !((MobEffectsPredicate)this.effects.get()).matches((LivingEntity)player))
/* 49 */       return false; 
/* 52 */     if (this.source.isPresent() && (
/* 53 */       source == null || !((ContextAwarePredicate)this.source.get()).matches(source)))
/* 54 */       return false; 
/* 58 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 63 */     super.validate(validator);
/* 65 */     Validatable.validate(validator.entityContext(), "source", this.source);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\EffectsChangedTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */