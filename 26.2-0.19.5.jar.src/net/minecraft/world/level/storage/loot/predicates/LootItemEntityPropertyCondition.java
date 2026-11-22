/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LootItemEntityPropertyCondition extends Record implements LootItemCondition {
/*    */   private final Optional<EntityPredicate> predicate;
/*    */   
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public static final MapCodec<LootItemEntityPropertyCondition> MAP_CODEC;
/*    */   
/*    */   public LootItemEntityPropertyCondition(Optional<EntityPredicate> predicate, LootContext.EntityTarget entityTarget) {
/* 15 */     this.predicate = predicate;
/* 15 */     this.entityTarget = entityTarget;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<EntityPredicate> predicate() {
/* 15 */     return this.predicate;
/*    */   }
/*    */   
/*    */   public LootContext.EntityTarget entityTarget() {
/* 15 */     return this.entityTarget;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(LootItemEntityPropertyCondition::predicate), (App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(LootItemEntityPropertyCondition::entityTarget)).apply((Applicative)i, LootItemEntityPropertyCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<LootItemEntityPropertyCondition> codec() {
/* 26 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 31 */     return Set.of(LootContextParams.ORIGIN, this.entityTarget.contextParam());
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 36 */     Entity entity = (Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/* 37 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 38 */     return (this.predicate.isEmpty() || ((EntityPredicate)this.predicate.get()).matches(context.getLevel(), pos, entity));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget target) {
/* 42 */     return hasProperties(target, EntityPredicate.Builder.entity());
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate.Builder predicate) {
/* 46 */     return () -> new LootItemEntityPropertyCondition(Optional.of(predicate.build()), target);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget target, EntityPredicate predicate) {
/* 50 */     return () -> new LootItemEntityPropertyCondition(Optional.of(predicate), target);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemEntityPropertyCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */