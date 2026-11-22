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
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LevitationTrigger extends SimpleCriterionTrigger<LevitationTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 17 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, Vec3 start, int duration) {
/* 21 */     trigger(player, t -> t.matches(player, start, duration));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final Optional<DistancePredicate> distance;
/*    */     
/*    */     private final MinMaxBounds.Ints duration;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DistancePredicate> distance, MinMaxBounds.Ints duration) {
/* 24 */       this.player = player;
/* 24 */       this.distance = distance;
/* 24 */       this.duration = duration;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/LevitationTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 24 */       return this.player;
/*    */     }
/*    */     
/*    */     public Optional<DistancePredicate> distance() {
/* 24 */       return this.distance;
/*    */     }
/*    */     
/*    */     public MinMaxBounds.Ints duration() {
/* 24 */       return this.duration;
/*    */     }
/*    */     
/*    */     static {
/* 29 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)DistancePredicate.CODEC.optionalFieldOf("distance").forGetter(TriggerInstance::distance), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("duration", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::duration)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> levitated(DistancePredicate distance) {
/* 36 */       return CriteriaTriggers.LEVITATION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(distance), MinMaxBounds.Ints.ANY));
/*    */     }
/*    */     
/*    */     public boolean matches(ServerPlayer player, Vec3 start, int duration) {
/* 40 */       if (this.distance.isPresent() && !((DistancePredicate)this.distance.get()).matches(start.x, start.y, start.z, player.getX(), player.getY(), player.getZ()))
/* 41 */         return false; 
/* 43 */       if (!this.duration.matches(duration))
/* 44 */         return false; 
/* 46 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\LevitationTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */