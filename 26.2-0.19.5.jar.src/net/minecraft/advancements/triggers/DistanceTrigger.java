/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.DistancePredicate;
/*    */ import net.minecraft.advancements.predicates.LocationPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DistanceTrigger extends SimpleCriterionTrigger<DistanceTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 18 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, Vec3 startPosition) {
/* 22 */     Vec3 playerPosition = player.position();
/* 23 */     trigger(player, t -> t.matches(player.level(), startPosition, playerPosition));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<LocationPredicate> startPosition;
/*    */     
/*    */     private final Optional<DistancePredicate> distance;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> startPosition, Optional<DistancePredicate> distance) {
/* 26 */       this.player = player;
/* 26 */       this.startPosition = startPosition;
/* 26 */       this.distance = distance;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #26	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/DistanceTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 26 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<LocationPredicate> startPosition() {
/* 26 */       return this.startPosition;
/*    */     }
/*    */     
/*    */     public Optional<DistancePredicate> distance() {
/* 26 */       return this.distance;
/*    */     }
/*    */     
/*    */     static {
/* 31 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)LocationPredicate.CODEC.optionalFieldOf("start_position").forGetter(TriggerInstance::startPosition), (App)DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(TriggerInstance::distance)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> fallFromHeight(EntityPredicate.Builder player, DistancePredicate distance, LocationPredicate.Builder startPosition) {
/* 38 */       return CriteriaTriggers.FALL_FROM_HEIGHT.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.of(startPosition.build()), Optional.of(distance)));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> rideEntityInLava(EntityPredicate.Builder player, DistancePredicate distance) {
/* 42 */       return CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(player)), Optional.empty(), Optional.of(distance)));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> travelledThroughNether(DistancePredicate distance) {
/* 46 */       return CriteriaTriggers.NETHER_TRAVEL.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(distance)));
/*    */     }
/*    */     
/*    */     public boolean matches(ServerLevel level, Vec3 enteredPosition, Vec3 playerPosition) {
/* 50 */       if (this.startPosition.isPresent() && !((LocationPredicate)this.startPosition.get()).matches(level, enteredPosition.x, enteredPosition.y, enteredPosition.z))
/* 51 */         return false; 
/* 53 */       if (this.distance.isPresent() && !((DistancePredicate)this.distance.get()).matches(enteredPosition.x, enteredPosition.y, enteredPosition.z, playerPosition.x, playerPosition.y, playerPosition.z))
/* 54 */         return false; 
/* 56 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\DistanceTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */