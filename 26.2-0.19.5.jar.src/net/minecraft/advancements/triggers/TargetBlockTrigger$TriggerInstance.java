/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final MinMaxBounds.Ints signalStrength;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> projectile;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints signalStrength, Optional<ContextAwarePredicate> projectile) {
/* 28 */     this.player = player;
/* 28 */     this.signalStrength = signalStrength;
/* 28 */     this.projectile = projectile;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/TargetBlockTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 28 */     return this.player;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints signalStrength() {
/* 28 */     return this.signalStrength;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> projectile() {
/* 28 */     return this.projectile;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("signal_strength", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::signalStrength), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("projectile").forGetter(TriggerInstance::projectile)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> targetHit(MinMaxBounds.Ints redstoneSignalStrength, Optional<ContextAwarePredicate> projectile) {
/* 40 */     return CriteriaTriggers.TARGET_BLOCK_HIT.createCriterion(new TriggerInstance(Optional.empty(), redstoneSignalStrength, projectile));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext projectile, Vec3 hitPosition, int signalStrength) {
/* 44 */     if (!this.signalStrength.matches(signalStrength))
/* 45 */       return false; 
/* 47 */     if (this.projectile.isPresent() && !((ContextAwarePredicate)this.projectile.get()).matches(projectile))
/* 48 */       return false; 
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 55 */     super.validate(validator);
/* 57 */     Validatable.validate(validator.entityContext(), "projectile", this.projectile);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\TargetBlockTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */