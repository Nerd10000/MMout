/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> lightning;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> bystander;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> lightning, Optional<ContextAwarePredicate> bystander) {
/* 30 */     this.player = player;
/* 30 */     this.lightning = lightning;
/* 30 */     this.bystander = bystander;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/LightningStrikeTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 30 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> lightning() {
/* 30 */     return this.lightning;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> bystander() {
/* 30 */     return this.bystander;
/*    */   }
/*    */   
/*    */   static {
/* 35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("lightning").forGetter(TriggerInstance::lightning), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("bystander").forGetter(TriggerInstance::bystander)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> lightningStrike(Optional<EntityPredicate> lightning, Optional<EntityPredicate> bystander) {
/* 42 */     return CriteriaTriggers.LIGHTNING_STRIKE.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(lightning), EntityPredicate.wrap(bystander)));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext bolt, List<LootContext> entitiesAround) {
/* 46 */     if (this.lightning.isPresent() && !((ContextAwarePredicate)this.lightning.get()).matches(bolt))
/* 47 */       return false; 
/* 50 */     Objects.requireNonNull(this.bystander.get());
/* 50 */     if (this.bystander.isPresent() && entitiesAround.stream().noneMatch((ContextAwarePredicate)this.bystander.get()::matches))
/* 51 */       return false; 
/* 54 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 59 */     super.validate(validator);
/* 61 */     Validatable.validate(validator.entityContext(), "lightning", this.lightning);
/* 62 */     Validatable.validate(validator.entityContext(), "bystander", this.bystander);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\LightningStrikeTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */