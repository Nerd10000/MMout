/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.DamagePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ 
/*    */ public class EntityHurtPlayerTrigger extends SimpleCriterionTrigger<EntityHurtPlayerTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 16 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 20 */     trigger(player, t -> t.matches(player, source, originalDamage, actualDamage, blocked));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<DamagePredicate> damage;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DamagePredicate> damage) {
/* 23 */       this.player = player;
/* 23 */       this.damage = damage;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/EntityHurtPlayerTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 23 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<DamagePredicate> damage() {
/* 23 */       return this.damage;
/*    */     }
/*    */     
/*    */     static {
/* 27 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(TriggerInstance::damage)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> entityHurtPlayer() {
/* 33 */       return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> entityHurtPlayer(DamagePredicate damage) {
/* 37 */       return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage)));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> entityHurtPlayer(DamagePredicate.Builder damage) {
/* 41 */       return CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(damage.build())));
/*    */     }
/*    */     
/*    */     public boolean matches(ServerPlayer player, DamageSource source, float originalDamage, float actualDamage, boolean blocked) {
/* 45 */       if (this.damage.isPresent() && !((DamagePredicate)this.damage.get()).matches(player, source, originalDamage, actualDamage, blocked))
/* 46 */         return false; 
/* 48 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\EntityHurtPlayerTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */