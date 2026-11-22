/*     */ package net.minecraft.world.entity.ai;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryMap;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemorySlot;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Brain<E extends LivingEntity> {
/*     */   private static final int SCHEDULE_UPDATE_DELAY = 20;
/*     */   
/*     */   public static final class Provider<E extends LivingEntity> {
/*     */     private final Collection<? extends MemoryModuleType<?>> memoryTypes;
/*     */     
/*     */     private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
/*     */     
/*     */     private final Brain.ActivitySupplier<E> activities;
/*     */     
/*     */     private Provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, Brain.ActivitySupplier<E> activities) {
/*  54 */       this.memoryTypes = memoryTypes;
/*  55 */       this.sensorTypes = sensorTypes;
/*  56 */       this.activities = activities;
/*     */     }
/*     */     
/*     */     public Brain<E> makeBrain(E body, Brain.Packed packed) {
/*  60 */       List<ActivityData<E>> activities = this.activities.createActivities(body);
/*  61 */       return new Brain<>(this.memoryTypes, this.sensorTypes, activities, packed.memories, body.getRandom());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> Provider<E> provider(Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes) {
/*  66 */     return new Provider<>((Collection<? extends MemoryModuleType<?>>)ImmutableList.of(), sensorTypes, paramLivingEntity -> List.of());
/*     */   }
/*     */   
/*     */   public static <E extends LivingEntity> Provider<E> provider(Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, ActivitySupplier<E> activities) {
/*  70 */     return new Provider<>((Collection<? extends MemoryModuleType<?>>)ImmutableList.of(), sensorTypes, activities);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static <E extends LivingEntity> Provider<E> provider(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, ActivitySupplier<E> activities) {
/*  76 */     return new Provider<>(memoryTypes, sensorTypes, activities);
/*     */   }
/*     */   
/*  81 */   private final Map<MemoryModuleType<?>, MemorySlot<?>> memories = Maps.newHashMap();
/*     */   
/*  84 */   private final Map<SensorType<? extends Sensor<? super E>>, Sensor<? super E>> sensors = Maps.newLinkedHashMap();
/*     */   
/*  87 */   private final Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority = Maps.newTreeMap();
/*     */   
/*     */   private EnvironmentAttribute<Activity> schedule;
/*     */   
/*  92 */   private final Map<Activity, Set<Pair<MemoryModuleType<?>, MemoryStatus>>> activityRequirements = Maps.newHashMap();
/*     */   
/*  94 */   private final Map<Activity, Set<MemoryModuleType<?>>> activityMemoriesToEraseWhenStopped = Maps.newHashMap();
/*     */   
/*  97 */   private Set<Activity> coreActivities = Sets.newHashSet();
/*     */   
/* 100 */   private final Set<Activity> activeActivities = Sets.newHashSet();
/*     */   
/* 103 */   private Activity defaultActivity = Activity.IDLE;
/*     */   
/* 105 */   private long lastScheduleUpdate = -9999L;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected Brain(Collection<? extends MemoryModuleType<?>> memoryTypes, Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes, List<ActivityData<E>> activities, MemoryMap memories, RandomSource randomSource) {
/* 109 */     for (MemoryModuleType<?> memoryType : memoryTypes)
/* 110 */       registerMemory(memoryType); 
/* 112 */     for (SensorType<? extends Sensor<? super E>> sensorType : sensorTypes) {
/* 113 */       Sensor<? super E> newSensor = sensorType.create();
/* 114 */       newSensor.randomlyDelayStart(randomSource);
/* 115 */       this.sensors.put(sensorType, newSensor);
/* 117 */       for (MemoryModuleType<?> type : (Iterable<MemoryModuleType<?>>)newSensor.requires())
/* 118 */         registerMemory(type); 
/*     */     } 
/* 122 */     for (ActivityData<E> activity : activities)
/* 123 */       addActivity(activity.activityType(), activity.behaviorPriorityPairs(), activity.conditions(), activity.memoriesToEraseWhenStopped()); 
/* 126 */     for (MemoryMap.Value<?> memory : (Iterable<MemoryMap.Value<?>>)memories)
/* 127 */       setMemoryInternal(memory); 
/* 130 */     setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
/* 131 */     useDefaultActivity();
/*     */   }
/*     */   
/*     */   private void registerMemory(MemoryModuleType<?> memoryType) {
/* 135 */     this.memories.putIfAbsent(memoryType, MemorySlot.create());
/*     */   }
/*     */   
/*     */   public Brain() {
/* 139 */     setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
/* 140 */     useDefaultActivity();
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 144 */     final MemoryMap.Builder builder = new MemoryMap.Builder();
/* 145 */     forEach(new Visitor(this) {
/*     */           public <U> void acceptEmpty(MemoryModuleType<U> type) {}
/*     */           
/*     */           public <U> void accept(MemoryModuleType<U> type, U value, long timeToLive) {
/* 152 */             if (type.canSerialize())
/* 153 */               builder.add(type, ExpirableValue.of(value, timeToLive)); 
/*     */           }
/*     */           
/*     */           public <U> void accept(MemoryModuleType<U> type, U value) {
/* 159 */             if (type.canSerialize())
/* 160 */               builder.add(type, ExpirableValue.of(value)); 
/*     */           }
/*     */         });
/* 164 */     return new Packed(builder.build());
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ActivitySupplier<E extends LivingEntity> {
/*     */     List<ActivityData<E>> createActivities(E param1E);
/*     */   }
/*     */   
/*     */   private <T> MemorySlot<T> getMemorySlotIfPresent(MemoryModuleType<T> memoryType) {
/* 169 */     return (MemorySlot<T>)this.memories.get(memoryType);
/*     */   }
/*     */   
/*     */   private <T> MemorySlot<T> getMemorySlot(MemoryModuleType<T> memoryType) {
/* 173 */     MemorySlot<T> result = getMemorySlotIfPresent(memoryType);
/* 174 */     if (result == null)
/* 175 */       throw new IllegalStateException("Unregistered memory fetched: " + String.valueOf(memoryType)); 
/* 177 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasMemoryValue(MemoryModuleType<?> type) {
/* 182 */     return checkMemory(type, MemoryStatus.VALUE_PRESENT);
/*     */   }
/*     */   
/*     */   public void clearMemories() {
/* 186 */     this.memories.values().forEach(MemorySlot::clear);
/*     */   }
/*     */   
/*     */   public <U> void eraseMemory(MemoryModuleType<U> type) {
/* 190 */     MemorySlot<U> slot = getMemorySlotIfPresent(type);
/* 191 */     if (slot != null)
/* 192 */       slot.clear(); 
/*     */   }
/*     */   
/*     */   public <U> void setMemory(MemoryModuleType<U> type, U value) {
/* 199 */     setMemoryInternal(type, value);
/*     */   }
/*     */   
/*     */   public <U> void setMemoryWithExpiry(MemoryModuleType<U> type, U value, long timeToLive) {
/* 206 */     setMemoryInternal(type, value, timeToLive);
/*     */   }
/*     */   
/*     */   public <U> void setMemory(MemoryModuleType<U> type, Optional<? extends U> optionalValue) {
/* 212 */     setMemoryInternal(type, optionalValue.orElse(null));
/*     */   }
/*     */   
/*     */   private <U> void setMemoryInternal(MemoryMap.Value<U> value) {
/* 216 */     ExpirableValue<U> expirableValue = value.value();
/* 217 */     if (expirableValue.timeToLive().isPresent()) {
/* 218 */       setMemoryInternal(value.type(), expirableValue.value(), ((Long)expirableValue.timeToLive().get()).longValue());
/*     */     } else {
/* 220 */       setMemoryInternal(value.type(), expirableValue.value());
/*     */     } 
/*     */   }
/*     */   
/*     */   private <U> void setMemoryInternal(MemoryModuleType<U> type, U value, long tileToLive) {
/* 228 */     MemorySlot<U> slot = getMemorySlotIfPresent(type);
/* 229 */     if (slot != null) {
/* 230 */       if (isEmptyCollection(value))
/* 231 */         value = null; 
/* 233 */       if (value == null) {
/* 234 */         slot.clear();
/*     */       } else {
/* 236 */         slot.set(value, tileToLive);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private <U> void setMemoryInternal(MemoryModuleType<U> type, U value) {
/* 245 */     MemorySlot<U> slot = getMemorySlotIfPresent(type);
/* 246 */     if (slot != null) {
/* 247 */       if (value != null && isEmptyCollection(value))
/* 248 */         value = null; 
/* 250 */       if (value == null) {
/* 251 */         slot.clear();
/*     */       } else {
/* 253 */         slot.set(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public <U> Optional<U> getMemory(MemoryModuleType<U> type) {
/* 259 */     return Optional.ofNullable((U)getMemorySlot(type).value());
/*     */   }
/*     */   
/*     */   public <U> Optional<U> getMemoryInternal(MemoryModuleType<U> type) {
/* 264 */     MemorySlot<U> slot = getMemorySlotIfPresent(type);
/* 265 */     if (slot == null)
/* 266 */       return null; 
/* 268 */     return Optional.ofNullable((U)slot.value());
/*     */   }
/*     */   
/*     */   public <U> long getTimeUntilExpiry(MemoryModuleType<U> type) {
/* 272 */     return getMemorySlot(type).timeToLive();
/*     */   }
/*     */   
/*     */   public void forEach(Visitor visitor) {
/* 276 */     this.memories.forEach((memoryModuleType, slot) -> callVisitor(visitor, memoryModuleType, slot));
/*     */   }
/*     */   
/*     */   private static <U> void callVisitor(Visitor visitor, MemoryModuleType<U> memoryModuleType, MemorySlot<?> slot) {
/* 281 */     slot.visit(memoryModuleType, visitor);
/*     */   }
/*     */   
/*     */   public <U> boolean isMemoryValue(MemoryModuleType<U> memoryType, U value) {
/* 286 */     MemorySlot<U> slot = getMemorySlotIfPresent(memoryType);
/* 287 */     return (slot != null && Objects.equals(value, slot.value()));
/*     */   }
/*     */   
/*     */   public boolean checkMemory(MemoryModuleType<?> type, MemoryStatus status) {
/* 291 */     MemorySlot<?> slot = getMemorySlotIfPresent(type);
/* 293 */     if (slot == null)
/* 294 */       return false; 
/* 297 */     return (status == MemoryStatus.REGISTERED || (status == MemoryStatus.VALUE_PRESENT && slot
/* 298 */       .hasValue()) || (status == MemoryStatus.VALUE_ABSENT && 
/* 299 */       !slot.hasValue()));
/*     */   }
/*     */   
/*     */   public void setSchedule(EnvironmentAttribute<Activity> schedule) {
/* 303 */     this.schedule = schedule;
/*     */   }
/*     */   
/*     */   public void setCoreActivities(Set<Activity> activities) {
/* 307 */     this.coreActivities = activities;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/*     */   public Set<Activity> getActiveActivities() {
/* 313 */     return this.activeActivities;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/*     */   public List<BehaviorControl<? super E>> getRunningBehaviors() {
/* 319 */     ObjectArrayList<BehaviorControl<? super E>> objectArrayList = new ObjectArrayList();
/* 320 */     for (Map<Activity, Set<BehaviorControl<? super E>>> behavioursByActivities : this.availableBehaviorsByPriority.values()) {
/* 321 */       for (Set<BehaviorControl<? super E>> behaviors : behavioursByActivities.values()) {
/* 322 */         for (BehaviorControl<? super E> behavior : behaviors) {
/* 323 */           if (behavior.getStatus() == Behavior.Status.RUNNING)
/* 324 */             objectArrayList.add(behavior); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 329 */     return (List<BehaviorControl<? super E>>)objectArrayList;
/*     */   }
/*     */   
/*     */   public void useDefaultActivity() {
/* 333 */     setActiveActivity(this.defaultActivity);
/*     */   }
/*     */   
/*     */   public Optional<Activity> getActiveNonCoreActivity() {
/* 337 */     for (Activity activity : this.activeActivities) {
/* 338 */       if (!this.coreActivities.contains(activity))
/* 339 */         return Optional.of(activity); 
/*     */     } 
/* 342 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void setActiveActivityIfPossible(Activity activity) {
/* 349 */     if (activityRequirementsAreMet(activity)) {
/* 350 */       setActiveActivity(activity);
/*     */     } else {
/* 352 */       useDefaultActivity();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setActiveActivity(Activity activity) {
/* 357 */     if (isActive(activity))
/*     */       return; 
/* 361 */     eraseMemoriesForOtherActivitesThan(activity);
/* 362 */     this.activeActivities.clear();
/* 363 */     this.activeActivities.addAll(this.coreActivities);
/* 364 */     this.activeActivities.add(activity);
/*     */   }
/*     */   
/*     */   private void eraseMemoriesForOtherActivitesThan(Activity activity) {
/* 368 */     for (Activity oldActivity : this.activeActivities) {
/* 369 */       if (oldActivity != activity) {
/* 370 */         Set<MemoryModuleType<?>> memoryModuleTypes = this.activityMemoriesToEraseWhenStopped.get(oldActivity);
/* 371 */         if (memoryModuleTypes != null)
/* 372 */           for (MemoryModuleType<?> memoryModuleType : memoryModuleTypes)
/* 373 */             eraseMemory(memoryModuleType);  
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateActivityFromSchedule(EnvironmentAttributeSystem environmentAttributes, long gameTime, Vec3 pos) {
/* 383 */     if (gameTime - this.lastScheduleUpdate > 20L) {
/* 384 */       this.lastScheduleUpdate = gameTime;
/* 385 */       Activity scheduledActivity = (this.schedule != null) ? (Activity)environmentAttributes.getValue(this.schedule, pos) : Activity.IDLE;
/* 386 */       if (!this.activeActivities.contains(scheduledActivity))
/* 387 */         setActiveActivityIfPossible(scheduledActivity); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setActiveActivityToFirstValid(List<Activity> activities) {
/* 394 */     for (Activity activity : activities) {
/* 395 */       if (activityRequirementsAreMet(activity)) {
/* 396 */         setActiveActivity(activity);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDefaultActivity(Activity activity) {
/* 403 */     this.defaultActivity = activity;
/*     */   }
/*     */   
/*     */   public void addActivity(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> behaviorPriorityPairs, Set<Pair<MemoryModuleType<?>, MemoryStatus>> conditions, Set<MemoryModuleType<?>> memoriesToEraseWhenStopped) {
/* 407 */     this.activityRequirements.put(activity, conditions);
/* 408 */     if (!memoriesToEraseWhenStopped.isEmpty())
/* 409 */       this.activityMemoriesToEraseWhenStopped.put(activity, memoriesToEraseWhenStopped); 
/* 411 */     for (UnmodifiableIterator<Pair<Integer, ? extends BehaviorControl<? super E>>> unmodifiableIterator = behaviorPriorityPairs.iterator(); unmodifiableIterator.hasNext(); ) {
/* 411 */       Pair<Integer, ? extends BehaviorControl<? super E>> pair = unmodifiableIterator.next();
/* 412 */       BehaviorControl<? super E> behavior = (BehaviorControl<? super E>)pair.getSecond();
/* 413 */       for (MemoryModuleType<?> requiredMemory : (Iterable<MemoryModuleType<?>>)behavior.getRequiredMemories())
/* 414 */         registerMemory(requiredMemory); 
/* 416 */       ((Set<BehaviorControl<? super E>>)((Map<Activity, Set<BehaviorControl<? super E>>>)this.availableBehaviorsByPriority
/* 417 */         .computeIfAbsent((Integer)pair.getFirst(), key -> Maps.newHashMap()))
/* 418 */         .computeIfAbsent(activity, key -> Sets.newLinkedHashSet()))
/* 419 */         .add(behavior);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void removeAllBehaviors() {
/* 425 */     this.availableBehaviorsByPriority.clear();
/*     */   }
/*     */   
/*     */   public boolean isActive(Activity activity) {
/* 429 */     return this.activeActivities.contains(activity);
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, E body) {
/* 433 */     forgetOutdatedMemories();
/* 434 */     tickSensors(level, body);
/* 435 */     startEachNonRunningBehavior(level, body);
/* 436 */     tickEachRunningBehavior(level, body);
/*     */   }
/*     */   
/*     */   private void tickSensors(ServerLevel level, E body) {
/* 440 */     for (Sensor<? super E> sensor : this.sensors.values())
/* 441 */       sensor.tick(level, (LivingEntity)body); 
/*     */   }
/*     */   
/*     */   private void forgetOutdatedMemories() {
/* 446 */     this.memories.values().forEach(MemorySlot::tick);
/*     */   }
/*     */   
/*     */   public void stopAll(ServerLevel level, E body) {
/* 450 */     long timestamp = body.level().getGameTime();
/* 451 */     for (BehaviorControl<? super E> behavior : getRunningBehaviors())
/* 452 */       behavior.doStop(level, (LivingEntity)body, timestamp); 
/*     */   }
/*     */   
/*     */   private void startEachNonRunningBehavior(ServerLevel level, E body) {
/* 458 */     long time = level.getGameTime();
/* 459 */     for (Map<Activity, Set<BehaviorControl<? super E>>> behavioursByActivities : this.availableBehaviorsByPriority.values()) {
/* 460 */       for (Map.Entry<Activity, Set<BehaviorControl<? super E>>> behavioursForActivity : behavioursByActivities.entrySet()) {
/* 461 */         Activity activity = behavioursForActivity.getKey();
/* 462 */         if (this.activeActivities.contains(activity)) {
/* 463 */           Set<BehaviorControl<? super E>> behaviors = behavioursForActivity.getValue();
/* 464 */           for (BehaviorControl<? super E> behavior : behaviors) {
/* 465 */             if (behavior.getStatus() == Behavior.Status.STOPPED)
/* 466 */               behavior.tryStart(level, (LivingEntity)body, time); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickEachRunningBehavior(ServerLevel level, E body) {
/* 477 */     long timestamp = level.getGameTime();
/* 478 */     for (BehaviorControl<? super E> behavior : getRunningBehaviors())
/* 479 */       behavior.tickOrStop(level, (LivingEntity)body, timestamp); 
/*     */   }
/*     */   
/*     */   private boolean activityRequirementsAreMet(Activity activity) {
/* 484 */     if (!this.activityRequirements.containsKey(activity))
/* 485 */       return false; 
/* 488 */     for (Pair<MemoryModuleType<?>, MemoryStatus> memoryRequirement : this.activityRequirements.get(activity)) {
/* 489 */       MemoryModuleType<?> memoryType = (MemoryModuleType)memoryRequirement.getFirst();
/* 490 */       MemoryStatus memoryStatus = (MemoryStatus)memoryRequirement.getSecond();
/* 491 */       if (!checkMemory(memoryType, memoryStatus))
/* 492 */         return false; 
/*     */     } 
/* 496 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isEmptyCollection(Object object) {
/* 500 */     if (object instanceof Collection) {
/* 500 */       Collection<?> collection = (Collection)object;
/* 500 */       if (collection.isEmpty());
/*     */     } 
/* 500 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBrainDead() {
/* 504 */     return (this.memories.isEmpty() && this.sensors.isEmpty() && this.availableBehaviorsByPriority.isEmpty());
/*     */   }
/*     */   
/*     */   public static interface Visitor {
/*     */     <U> void acceptEmpty(MemoryModuleType<U> param1MemoryModuleType);
/*     */     
/*     */     <U> void accept(MemoryModuleType<U> param1MemoryModuleType, U param1U);
/*     */     
/*     */     <U> void accept(MemoryModuleType<U> param1MemoryModuleType, U param1U, long param1Long);
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final MemoryMap memories;
/*     */     
/*     */     public Packed(MemoryMap memories) {
/* 507 */       this.memories = memories;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/Brain$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/Brain$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/Brain$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/Brain$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/Brain$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #507	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/Brain$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MemoryMap memories() {
/* 507 */       return this.memories;
/*     */     }
/*     */     
/* 510 */     public static final Packed EMPTY = new Packed(MemoryMap.EMPTY);
/*     */     
/*     */     public static final Codec<Packed> CODEC;
/*     */     
/*     */     static {
/* 512 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MemoryMap.CODEC.fieldOf("memories").forGetter(Packed::memories)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\Brain.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */