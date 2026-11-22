/*    */ package net.minecraft.world.entity.ai;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public final class ActivityData<E extends LivingEntity> extends Record {
/*    */   private final Activity activityType;
/*    */   
/*    */   private final ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs;
/*    */   
/*    */   private final Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions;
/*    */   
/*    */   private final Set<MemoryModuleType<?>> memoriesToEraseWhenStopped;
/*    */   
/*    */   public ActivityData(Activity activityType, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions, Set<MemoryModuleType<?>> memoriesToEraseWhenStopped) {
/* 15 */     this.activityType = activityType;
/* 15 */     this.behaviorPriorityPairs = behaviorPriorityPairs;
/* 15 */     this.conditions = conditions;
/* 15 */     this.memoriesToEraseWhenStopped = memoriesToEraseWhenStopped;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/ActivityData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/ActivityData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/ActivityData<TE;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/ActivityData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/ActivityData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/ActivityData<TE;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/ActivityData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/ActivityData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/ActivityData<TE;>;
/*    */   }
/*    */   
/*    */   public Activity activityType() {
/* 15 */     return this.activityType;
/*    */   }
/*    */   
/*    */   public ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs() {
/* 15 */     return this.behaviorPriorityPairs;
/*    */   }
/*    */   
/*    */   public Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions() {
/* 15 */     return this.conditions;
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> memoriesToEraseWhenStopped() {
/* 15 */     return this.memoriesToEraseWhenStopped;
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList) {
/* 23 */     return create(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList));
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList, MemoryModuleType<?> memoryThatMustHaveValueAndWillBeErasedAfter) {
/* 30 */     ImmutableSet immutableSet1 = ImmutableSet.of(
/* 31 */         Pair.of(memoryThatMustHaveValueAndWillBeErasedAfter, MemoryStatus.VALUE_PRESENT));
/* 33 */     ImmutableSet immutableSet2 = ImmutableSet.of(memoryThatMustHaveValueAndWillBeErasedAfter);
/* 34 */     return create(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList), (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)immutableSet1, (Set<MemoryModuleType<?>>)immutableSet2);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs) {
/* 39 */     return create(activity, behaviorPriorityPairs, (Set<Pair<MemoryModuleType<?>, MemoryStatus>>)ImmutableSet.of(), Sets.newHashSet());
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions) {
/* 45 */     return create(activity, createPriorityPairs(priorityOfFirstBehavior, behaviorList), conditions);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions) {
/* 51 */     return create(activity, behaviorPriorityPairs, conditions, Sets.newHashSet());
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ActivityData<E> create(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions, Set<MemoryModuleType<?>> memoriesToEraseWhenStopped) {
/* 58 */     return new ActivityData<>(activity, behaviorPriorityPairs, conditions, memoriesToEraseWhenStopped);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> createPriorityPairs(int priorityOfFirstBehavior, ImmutableList<? extends BehaviorControl<? super E>> behaviorList) {
/* 63 */     int nextPrio = priorityOfFirstBehavior;
/* 64 */     ImmutableList.Builder<Pair<Integer, ? extends BehaviorControl<? super E>>> listBuilder = ImmutableList.builder();
/* 65 */     for (UnmodifiableIterator<BehaviorControl<? super E>> unmodifiableIterator = behaviorList.iterator(); unmodifiableIterator.hasNext(); ) {
/* 65 */       BehaviorControl<? super E> behavior = unmodifiableIterator.next();
/* 66 */       listBuilder.add(Pair.of(Integer.valueOf(nextPrio++), behavior));
/*    */     } 
/* 68 */     return listBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\ActivityData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */