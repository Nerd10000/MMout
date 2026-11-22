/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ 
/*     */ public class AnimalMakeLove extends Behavior<Animal> {
/*     */   private static final int BREED_RANGE = 3;
/*     */   
/*     */   private static final int MIN_DURATION = 60;
/*     */   
/*     */   private static final int MAX_DURATION = 110;
/*     */   
/*     */   private final EntityType<? extends Animal> partnerType;
/*     */   
/*     */   private final float speedModifier;
/*     */   
/*     */   private final int closeEnoughDistance;
/*     */   
/*     */   private static final int DEFAULT_CLOSE_ENOUGH_DISTANCE = 2;
/*     */   
/*     */   private long spawnChildAtTime;
/*     */   
/*     */   public AnimalMakeLove(EntityType<? extends Animal> partnerType) {
/*  29 */     this(partnerType, 1.0F, 2);
/*     */   }
/*     */   
/*     */   public AnimalMakeLove(EntityType<? extends Animal> partnerType, float speedModifier, int closeEnoughDistance) {
/*  32 */     super(
/*  33 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 110);
/*  41 */     this.partnerType = partnerType;
/*  42 */     this.speedModifier = speedModifier;
/*  43 */     this.closeEnoughDistance = closeEnoughDistance;
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Animal body) {
/*  48 */     return (body.isInLove() && findValidBreedPartner(body).isPresent());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Animal body, long timestamp) {
/*  53 */     Animal partner = findValidBreedPartner(body).get();
/*  55 */     body.getBrain().setMemory(MemoryModuleType.BREED_TARGET, partner);
/*  56 */     partner.getBrain().setMemory(MemoryModuleType.BREED_TARGET, body);
/*  58 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)partner, this.speedModifier, this.closeEnoughDistance);
/*  60 */     int duration = 60 + body.getRandom().nextInt(50);
/*  61 */     this.spawnChildAtTime = timestamp + duration;
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Animal body, long timestamp) {
/*  66 */     if (!hasBreedTargetOfRightType(body))
/*  67 */       return false; 
/*  69 */     Animal partner = getBreedTarget(body);
/*  71 */     return (partner.isAlive() && body
/*  72 */       .canMate(partner) && 
/*  73 */       BehaviorUtils.entityIsVisible(body.getBrain(), (LivingEntity)partner) && timestamp <= this.spawnChildAtTime && 
/*     */       
/*  75 */       !body.isPanicking() && !partner.isPanicking());
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Animal body, long timestamp) {
/*  80 */     Animal partner = getBreedTarget(body);
/*  82 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)body, (LivingEntity)partner, this.speedModifier, this.closeEnoughDistance);
/*  83 */     if (!body.closerThan((Entity)partner, 3.0D))
/*     */       return; 
/*  86 */     if (timestamp >= this.spawnChildAtTime) {
/*  87 */       body.spawnChildFromBreeding(level, partner);
/*  88 */       body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  89 */       partner.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Animal body, long timestamp) {
/*  95 */     body.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  96 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  97 */     body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*  98 */     this.spawnChildAtTime = 0L;
/*     */   }
/*     */   
/*     */   private Animal getBreedTarget(Animal body) {
/* 102 */     return body.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */   }
/*     */   
/*     */   private boolean hasBreedTargetOfRightType(Animal body) {
/* 106 */     Brain<?> brain = body.getBrain();
/* 107 */     return (brain.hasMemoryValue(MemoryModuleType.BREED_TARGET) && ((AgeableMob)brain
/* 108 */       .getMemory(MemoryModuleType.BREED_TARGET).get()).is(this.partnerType));
/*     */   }
/*     */   
/*     */   private Optional<? extends Animal> findValidBreedPartner(Animal body) {
/* 115 */     Objects.requireNonNull(Animal.class);
/*     */     return ((NearestVisibleLivingEntities)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).findClosest(entity -> {
/*     */           // Byte code:
/*     */           //   0: aload_2
/*     */           //   1: aload_0
/*     */           //   2: getfield partnerType : Lnet/minecraft/world/entity/EntityType;
/*     */           //   5: invokevirtual is : (Ljava/lang/Object;)Z
/*     */           //   8: ifeq -> 42
/*     */           //   11: aload_2
/*     */           //   12: instanceof net/minecraft/world/entity/animal/Animal
/*     */           //   15: ifeq -> 42
/*     */           //   18: aload_2
/*     */           //   19: checkcast net/minecraft/world/entity/animal/Animal
/*     */           //   22: astore_3
/*     */           //   23: aload_1
/*     */           //   24: aload_3
/*     */           //   25: invokevirtual canMate : (Lnet/minecraft/world/entity/animal/Animal;)Z
/*     */           //   28: ifeq -> 42
/*     */           //   31: aload_3
/*     */           //   32: invokevirtual isPanicking : ()Z
/*     */           //   35: ifne -> 42
/*     */           //   38: iconst_1
/*     */           //   39: goto -> 43
/*     */           //   42: iconst_0
/*     */           //   43: ireturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #114	-> 0
/*     */           //   #113	-> 5
/*     */           //   #114	-> 25
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   23	19	3	animal	Lnet/minecraft/world/entity/animal/Animal;
/*     */           //   0	44	0	this	Lnet/minecraft/world/entity/ai/behavior/AnimalMakeLove;
/*     */           //   0	44	1	body	Lnet/minecraft/world/entity/animal/Animal;
/*     */           //   0	44	2	entity	Lnet/minecraft/world/entity/LivingEntity;
/* 115 */         }).map(Animal.class::cast);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\AnimalMakeLove.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */