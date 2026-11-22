/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.DamagePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<DamagePredicate> damage;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> entity;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DamagePredicate> damage, Optional<ContextAwarePredicate> entity) {
/* 28 */     this.player = player;
/* 28 */     this.damage = damage;
/* 28 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/PlayerHurtEntityTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 28 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<DamagePredicate> damage() {
/* 28 */     return this.damage;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> entity() {
/* 28 */     return this.entity;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntity() {
/* 40 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntityWithDamage(Optional<DamagePredicate> damage) {
/* 44 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), damage, Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntityWithDamage(DamagePredicate.Builder damage) {
/* 48 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build()), Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntity(Optional<EntityPredicate> entity) {
/* 52 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), EntityPredicate.wrap(entity)));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntity(Optional<DamagePredicate> damage, Optional<EntityPredicate> entity) {
/* 56 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), damage, EntityPredicate.wrap(entity)));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> playerHurtEntity(DamagePredicate.Builder damage, Optional<EntityPredicate> entity) {
/* 60 */     return CriteriaTriggers.PLAYER_HURT_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build()), EntityPredicate.wrap(entity)));
/*    */   }
/*    */   
/*    */   public boolean matches(ServerPlayer player, LootContext victim, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 64 */     if (this.damage.isPresent() && !((DamagePredicate)this.damage.get()).matches(player, source, originalDamage, actualDamage, blocked))
/* 65 */       return false; 
/* 67 */     if (this.entity.isPresent() && !((ContextAwarePredicate)this.entity.get()).matches(victim))
/* 68 */       return false; 
/* 70 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 75 */     super.validate(validator);
/* 77 */     Validatable.validate(validator.entityContext(), "entity", this.entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\PlayerHurtEntityTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */