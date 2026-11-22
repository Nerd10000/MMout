/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class VillagerPanicTrigger extends Behavior<Villager> {
/*    */   public VillagerPanicTrigger() {
/* 14 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 19 */     return (isHurt((LivingEntity)body) || hasHostile((LivingEntity)body));
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 24 */     if (isHurt((LivingEntity)body) || hasHostile((LivingEntity)body)) {
/* 25 */       Brain<?> brain = body.getBrain();
/* 28 */       if (!brain.isActive(Activity.PANIC)) {
/* 29 */         brain.eraseMemory(MemoryModuleType.PATH);
/* 30 */         brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 31 */         brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 32 */         brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 33 */         brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*    */       } 
/* 35 */       brain.setActiveActivityIfPossible(Activity.PANIC);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 41 */     if (timestamp % 100L == 0L)
/* 42 */       body.spawnGolemIfNeeded(level, timestamp, 3); 
/*    */   }
/*    */   
/*    */   public static boolean hasHostile(LivingEntity myBody) {
/* 47 */     return myBody.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
/*    */   }
/*    */   
/*    */   public static boolean isHurt(LivingEntity myBody) {
/* 51 */     return myBody.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\VillagerPanicTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */