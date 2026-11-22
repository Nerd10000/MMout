/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAndFollowTradingPlayerSink extends Behavior<Villager> {
/*    */   private final float speedModifier;
/*    */   
/*    */   public LookAndFollowTradingPlayerSink(float speedModifier) {
/* 16 */     super(
/* 17 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 2147483647);
/* 23 */     this.speedModifier = speedModifier;
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 28 */     Player tradingPlayer = body.getTradingPlayer();
/* 30 */     return (body.isAlive() && tradingPlayer != null && 
/*    */       
/* 32 */       !body.isInWater() && !body.hurtMarked && body
/*    */       
/* 34 */       .distanceToSqr((Entity)tradingPlayer) <= 16.0D);
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 40 */     return checkExtraStartConditions(level, body);
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 45 */     followPlayer(body);
/*    */   }
/*    */   
/*    */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 50 */     Brain<?> brain = body.getBrain();
/* 51 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 52 */     brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 57 */     followPlayer(body);
/*    */   }
/*    */   
/*    */   protected boolean timedOut(long timestamp) {
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   private void followPlayer(Villager body) {
/* 66 */     Brain<?> brain = body.getBrain();
/* 67 */     brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker((Entity)body.getTradingPlayer(), false), this.speedModifier, 2));
/* 68 */     brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)body.getTradingPlayer(), true));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\LookAndFollowTradingPlayerSink.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */