/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final MinMaxBounds.Ints level;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints level) {
/* 22 */     this.player = player;
/* 22 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/ConstructBeaconTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 22 */     return this.player;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints level() {
/* 22 */     return this.level;
/*    */   }
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(TriggerInstance::level)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> constructedBeacon() {
/* 32 */     return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> constructedBeacon(MinMaxBounds.Ints level) {
/* 36 */     return CriteriaTriggers.CONSTRUCT_BEACON.createCriterion(new TriggerInstance(Optional.empty(), level));
/*    */   }
/*    */   
/*    */   public boolean matches(int levels) {
/* 40 */     return this.level.matches(levels);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\ConstructBeaconTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */