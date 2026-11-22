/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */   private final Optional<ContextAwarePredicate> player;
/*    */   
/*    */   private final Optional<ResourceKey<Level>> from;
/*    */   
/*    */   private final Optional<ResourceKey<Level>> to;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceKey<Level>> from, Optional<ResourceKey<Level>> to) {
/* 24 */     this.player = player;
/* 24 */     this.from = from;
/* 24 */     this.to = to;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/ChangeDimensionTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 24 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<ResourceKey<Level>> from() {
/* 24 */     return this.from;
/*    */   }
/*    */   
/*    */   public Optional<ResourceKey<Level>> to() {
/* 24 */     return this.to;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("from").forGetter(TriggerInstance::from), (App)ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("to").forGetter(TriggerInstance::to)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> changedDimension() {
/* 36 */     return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> changedDimension(ResourceKey<Level> from, ResourceKey<Level> to) {
/* 40 */     return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(from), Optional.of(to)));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> changedDimensionTo(ResourceKey<Level> to) {
/* 44 */     return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(to)));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> changedDimensionFrom(ResourceKey<Level> from) {
/* 48 */     return CriteriaTriggers.CHANGED_DIMENSION.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(from), Optional.empty()));
/*    */   }
/*    */   
/*    */   public boolean matches(ResourceKey<Level> from, ResourceKey<Level> to) {
/* 52 */     if (this.from.isPresent() && this.from.get() != from)
/* 53 */       return false; 
/* 55 */     if (this.to.isPresent() && this.to.get() != to)
/* 56 */       return false; 
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\ChangeDimensionTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */