/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class SleepInBed extends Behavior<LivingEntity> {
/*     */   public static final int COOLDOWN_AFTER_BEING_WOKEN = 100;
/*     */   
/*     */   private long nextOkStartTime;
/*     */   
/*     */   public SleepInBed() {
/*  26 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED, MemoryModuleType.LAST_SLEPT, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity body) {
/*  37 */     if (body.isPassenger())
/*  38 */       return false; 
/*  40 */     Brain<?> brain = body.getBrain();
/*  42 */     GlobalPos target = brain.getMemory(MemoryModuleType.HOME).get();
/*  43 */     if (level.dimension() != target.dimension())
/*  44 */       return false; 
/*  47 */     Optional<Long> lastWokenMemory = brain.getMemory(MemoryModuleType.LAST_WOKEN);
/*  48 */     if (lastWokenMemory.isPresent()) {
/*  49 */       long timeSinceLastWoken = level.getGameTime() - ((Long)lastWokenMemory.get()).longValue();
/*  50 */       if (timeSinceLastWoken > 0L && timeSinceLastWoken < 100L)
/*  52 */         return false; 
/*     */     } 
/*  56 */     BlockState blockState = level.getBlockState(target.pos());
/*  57 */     return (target.pos().closerToCenterThan((Position)body.position(), 2.0D) && blockState.is(BlockTags.BEDS) && !((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue());
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, LivingEntity body, long timestamp) {
/*  62 */     Optional<GlobalPos> memory = body.getBrain().getMemory(MemoryModuleType.HOME);
/*  64 */     if (memory.isEmpty())
/*  65 */       return false; 
/*  68 */     BlockPos bedPos = ((GlobalPos)memory.get()).pos();
/*  69 */     return (body.getBrain().isActive(Activity.REST) && body.getY() > bedPos.getY() + 0.4D && bedPos.closerToCenterThan((Position)body.position(), 1.14D));
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, LivingEntity body, long timestamp) {
/*  74 */     if (timestamp > this.nextOkStartTime) {
/*  75 */       Brain<?> brain = body.getBrain();
/*  77 */       if (brain.hasMemoryValue(MemoryModuleType.DOORS_TO_CLOSE)) {
/*     */         Optional<List<LivingEntity>> nearestEntities;
/*  78 */         Set<GlobalPos> doors = brain.getMemory(MemoryModuleType.DOORS_TO_CLOSE).get();
/*  80 */         if (brain.hasMemoryValue(MemoryModuleType.NEAREST_LIVING_ENTITIES)) {
/*  81 */           nearestEntities = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*     */         } else {
/*  83 */           nearestEntities = Optional.empty();
/*     */         } 
/*  86 */         InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(level, body, null, null, doors, nearestEntities);
/*     */       } 
/*  88 */       body.startSleeping(((GlobalPos)body.getBrain().getMemory(MemoryModuleType.HOME).get()).pos());
/*  89 */       brain.setMemory(MemoryModuleType.LAST_SLEPT, Long.valueOf(timestamp));
/*  90 */       brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/*  91 */       brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, LivingEntity body, long timestamp) {
/* 102 */     if (body.isSleeping()) {
/* 103 */       body.stopSleeping();
/* 104 */       this.nextOkStartTime = timestamp + 40L;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SleepInBed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */