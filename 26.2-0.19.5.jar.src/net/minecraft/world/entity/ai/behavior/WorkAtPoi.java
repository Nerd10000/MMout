/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ 
/*    */ public class WorkAtPoi extends Behavior<Villager> {
/*    */   private static final int CHECK_COOLDOWN = 300;
/*    */   
/*    */   private static final double DISTANCE = 1.73D;
/*    */   
/*    */   private long lastCheck;
/*    */   
/*    */   public WorkAtPoi() {
/* 23 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 31 */     if (level.getGameTime() - this.lastCheck < 300L)
/* 32 */       return false; 
/* 35 */     if (level.getRandom().nextInt(2) != 0)
/* 36 */       return false; 
/* 39 */     this.lastCheck = level.getGameTime();
/* 41 */     GlobalPos target = body.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
/* 42 */     return (target.dimension() == level.dimension() && target.pos().closerToCenterThan((Position)body.position(), 1.73D));
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 47 */     Brain<Villager> brain = body.getBrain();
/* 48 */     brain.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, Long.valueOf(timestamp));
/* 49 */     brain.getMemory(MemoryModuleType.JOB_SITE).ifPresent(globalPos -> brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(globalPos.pos())));
/* 53 */     body.playWorkSound();
/* 54 */     useWorkstation(level, body);
/* 56 */     if (body.shouldRestock(level))
/* 57 */       body.restock(); 
/*    */   }
/*    */   
/*    */   protected void useWorkstation(ServerLevel level, Villager body) {}
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 66 */     Optional<GlobalPos> jobSiteMemory = body.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 67 */     if (jobSiteMemory.isEmpty())
/* 68 */       return false; 
/* 71 */     GlobalPos target = jobSiteMemory.get();
/* 72 */     return (target.dimension() == level.dimension() && target
/* 73 */       .pos().closerToCenterThan((Position)body.position(), 1.73D));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\WorkAtPoi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */