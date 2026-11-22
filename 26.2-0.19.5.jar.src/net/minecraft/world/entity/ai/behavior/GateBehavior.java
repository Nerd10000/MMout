/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ public class GateBehavior<E extends LivingEntity> implements BehaviorControl<E> {
/*     */   private final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*     */   
/*     */   private final Set<MemoryModuleType<?>> exitErasedMemories;
/*     */   
/*     */   private final OrderPolicy orderPolicy;
/*     */   
/*     */   private final RunningPolicy runningPolicy;
/*     */   
/*  26 */   private final ShufflingList<BehaviorControl<? super E>> behaviors = new ShufflingList<>();
/*     */   
/*     */   private Behavior.Status status;
/*     */   
/*     */   public GateBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, Set<MemoryModuleType<?>> exitErasedMemories, OrderPolicy orderPolicy, RunningPolicy runningPolicy, List<Pair<? extends BehaviorControl<? super E>, Integer>> behaviors) {
/*  36 */     this.status = Behavior.Status.STOPPED;
/*     */     this.entryCondition = entryCondition;
/*     */     this.exitErasedMemories = exitErasedMemories;
/*     */     this.orderPolicy = orderPolicy;
/*     */     this.runningPolicy = runningPolicy;
/*     */     behaviors.forEach(entry -> this.behaviors.add((BehaviorControl<? super E>)entry.getFirst(), ((Integer)entry.getSecond()).intValue()));
/*     */   }
/*     */   
/*     */   public Behavior.Status getStatus() {
/*  40 */     return this.status;
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> getRequiredMemories() {
/*  45 */     Set<MemoryModuleType<?>> memories = new HashSet<>(this.entryCondition.keySet());
/*  46 */     for (BehaviorControl<? super E> behavior : this.behaviors)
/*  47 */       memories.addAll(behavior.getRequiredMemories()); 
/*  49 */     return memories;
/*     */   }
/*     */   
/*     */   private boolean hasRequiredMemories(E body) {
/*  53 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/*  54 */       MemoryModuleType<?> memoryType = entry.getKey();
/*  55 */       MemoryStatus requiredStatus = entry.getValue();
/*  56 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus))
/*  57 */         return false; 
/*     */     } 
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  66 */     if (hasRequiredMemories(body)) {
/*  67 */       this.status = Behavior.Status.RUNNING;
/*  68 */       this.orderPolicy.apply(this.behaviors);
/*  69 */       this.runningPolicy.apply(this.behaviors.stream(), level, body, timestamp);
/*  70 */       return true;
/*     */     } 
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  78 */     this.behaviors.stream()
/*  79 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  80 */       .forEach(goal -> goal.tickOrStop(level, body, timestamp));
/*  83 */     if (this.behaviors.stream().noneMatch(g -> (g.getStatus() == Behavior.Status.RUNNING)))
/*  84 */       doStop(level, body, timestamp); 
/*     */   }
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  90 */     this.status = Behavior.Status.STOPPED;
/*  92 */     this.behaviors.stream()
/*  93 */       .filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING))
/*  94 */       .forEach(goal -> goal.doStop(level, body, timestamp));
/*  96 */     Objects.requireNonNull(body.getBrain());
/*  96 */     this.exitErasedMemories.forEach(body.getBrain()::eraseMemory);
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 104 */     Set<String> runningBehaviours = (Set<String>)this.behaviors.stream().filter(goal -> (goal.getStatus() == Behavior.Status.RUNNING)).map(b -> b.getClass().getSimpleName()).collect(Collectors.toSet());
/* 106 */     return getClass().getSimpleName() + ": " + getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public enum OrderPolicy {
/* 110 */     ORDERED((String)(t -> {
/*     */       
/*     */       })),
/* 111 */     SHUFFLED((String)ShufflingList::shuffle);
/*     */     
/*     */     private final Consumer<ShufflingList<?>> consumer;
/*     */     
/*     */     OrderPolicy(Consumer<ShufflingList<?>> consumer) {
/* 117 */       this.consumer = consumer;
/*     */     }
/*     */     
/*     */     public void apply(ShufflingList<?> list) {
/* 121 */       this.consumer.accept(list);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum RunningPolicy {
/* 126 */     RUN_ONE {
/*     */       public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 129 */         behaviors
/* 130 */           .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 131 */           .filter(goal -> goal.tryStart(level, body, timestamp))
/* 132 */           .findFirst();
/*     */       }
/*     */     },
/* 135 */     TRY_ALL {
/*     */       public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 138 */         behaviors
/* 139 */           .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 140 */           .forEach(goal -> goal.tryStart(level, body, timestamp));
/*     */       }
/*     */     };
/*     */     
/*     */     public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> param1Stream, ServerLevel param1ServerLevel, E param1E, long param1Long);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/*     */       behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED)).filter(goal -> goal.tryStart(level, body, timestamp)).findFirst();
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 140 */       behaviors.filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED)).forEach(goal -> goal.tryStart(level, body, timestamp));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */