/*     */ package net.minecraft.advancements.triggers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.DamageSourcePredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*     */ 
/*     */ public class KilledTrigger extends SimpleCriterionTrigger<KilledTrigger.TriggerInstance> {
/*     */   public Codec<TriggerInstance> codec() {
/*  20 */     return TriggerInstance.CODEC;
/*     */   }
/*     */   
/*     */   public void trigger(ServerPlayer player, Entity entity, DamageSource killingBlow) {
/*  24 */     LootContext entityContext = EntityPredicate.createContext(player, entity);
/*  25 */     trigger(player, t -> t.matches(player, entityContext, killingBlow));
/*     */   }
/*     */   
/*     */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*     */     private final Optional<ContextAwarePredicate> player;
/*     */     
/*     */     private final Optional<ContextAwarePredicate> entity;
/*     */     
/*     */     private final Optional<DamageSourcePredicate> killingBlow;
/*     */     
/*     */     public static final Codec<TriggerInstance> CODEC;
/*     */     
/*     */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity, Optional<DamageSourcePredicate> killingBlow) {
/*  28 */       this.player = player;
/*  28 */       this.entity = entity;
/*  28 */       this.killingBlow = killingBlow;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/KilledTrigger$TriggerInstance;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<ContextAwarePredicate> player() {
/*  28 */       return this.player;
/*     */     }
/*     */     
/*     */     public Optional<ContextAwarePredicate> entity() {
/*  28 */       return this.entity;
/*     */     }
/*     */     
/*     */     public Optional<DamageSourcePredicate> killingBlow() {
/*  28 */       return this.killingBlow;
/*     */     }
/*     */     
/*     */     static {
/*  33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity), (App)DamageSourcePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(TriggerInstance::killingBlow)).apply((Applicative)i, TriggerInstance::new));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity) {
/*  40 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity) {
/*  44 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity() {
/*  48 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity, Optional<DamageSourcePredicate> killingBlow) {
/*  52 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity, Optional<DamageSourcePredicate> killingBlow) {
/*  56 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(Optional<EntityPredicate> entity, DamageSourcePredicate.Builder killingBlow) {
/*  60 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build())));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntity(EntityPredicate.Builder entity, DamageSourcePredicate.Builder killingBlow) {
/*  64 */       return CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build())));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> playerKilledEntityNearSculkCatalyst() {
/*  68 */       return CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity) {
/*  72 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity) {
/*  76 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer() {
/*  80 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity, Optional<DamageSourcePredicate> killingBlow) {
/*  84 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), killingBlow));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity, Optional<DamageSourcePredicate> killingBlow) {
/*  88 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), killingBlow));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(Optional<EntityPredicate> entity, DamageSourcePredicate.Builder killingBlow) {
/*  92 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(entity), Optional.of(killingBlow.build())));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> entityKilledPlayer(EntityPredicate.Builder entity, DamageSourcePredicate.Builder killingBlow) {
/*  96 */       return CriteriaTriggers.ENTITY_KILLED_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(entity)), Optional.of(killingBlow.build())));
/*     */     }
/*     */     
/*     */     public boolean matches(ServerPlayer player, LootContext entity, DamageSource killingBlow) {
/* 100 */       if (this.killingBlow.isPresent() && !((DamageSourcePredicate)this.killingBlow.get()).matches(player, killingBlow))
/* 101 */         return false; 
/* 103 */       return (this.entity.isEmpty() || ((ContextAwarePredicate)this.entity.get()).matches(entity));
/*     */     }
/*     */     
/*     */     public void validate(ValidationContextSource validator) {
/* 108 */       super.validate(validator);
/* 110 */       Validatable.validate(validator.entityContext(), "entity", this.entity);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\KilledTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */