/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*    */   private final Optional<ContextAwarePredicate> parent;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> partner;
/*    */   
/*    */   private final Optional<ContextAwarePredicate> child;
/*    */   
/*    */   public static final Codec<TriggerInstance> CODEC;
/*    */   
/*    */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> parent, Optional<ContextAwarePredicate> partner, Optional<ContextAwarePredicate> child) {
/* 31 */     this.player = player;
/* 31 */     this.parent = parent;
/* 31 */     this.partner = partner;
/* 31 */     this.child = child;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/BredAnimalsTrigger$TriggerInstance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> player() {
/* 31 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> parent() {
/* 31 */     return this.parent;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> partner() {
/* 31 */     return this.partner;
/*    */   }
/*    */   
/*    */   public Optional<ContextAwarePredicate> child() {
/* 31 */     return this.child;
/*    */   }
/*    */   
/*    */   static {
/* 37 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("parent").forGetter(TriggerInstance::parent), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("partner").forGetter(TriggerInstance::partner), (App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("child").forGetter(TriggerInstance::child)).apply((Applicative)i, TriggerInstance::new));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> bredAnimals() {
/* 45 */     return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> bredAnimals(EntityPredicate.Builder child) {
/* 49 */     return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(EntityPredicate.wrap(child))));
/*    */   }
/*    */   
/*    */   public static Criterion<TriggerInstance> bredAnimals(Optional<EntityPredicate> parent1, Optional<EntityPredicate> parent2, Optional<EntityPredicate> child) {
/* 53 */     return CriteriaTriggers.BRED_ANIMALS.createCriterion(new TriggerInstance(Optional.empty(), EntityPredicate.wrap(parent1), EntityPredicate.wrap(parent2), EntityPredicate.wrap(child)));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext parent, LootContext partner, LootContext child) {
/* 57 */     if (this.child.isPresent() && (child == null || !((ContextAwarePredicate)this.child.get()).matches(child)))
/* 58 */       return false; 
/* 61 */     return ((matches(this.parent, parent) && matches(this.partner, partner)) || (matches(this.parent, partner) && matches(this.partner, parent)));
/*    */   }
/*    */   
/*    */   private static boolean matches(Optional<ContextAwarePredicate> predicate, LootContext context) {
/* 65 */     return (predicate.isEmpty() || ((ContextAwarePredicate)predicate.get()).matches(context));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContextSource validator) {
/* 70 */     super.validate(validator);
/* 72 */     Validatable.validate(validator.entityContext(), "parent", this.parent);
/* 73 */     Validatable.validate(validator.entityContext(), "partner", this.partner);
/* 74 */     Validatable.validate(validator.entityContext(), "child", this.child);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\BredAnimalsTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */