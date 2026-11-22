/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class WanderToPositionGoal extends Goal {
/*     */   private final WanderingTrader trader;
/*     */   
/*     */   private final double stopDistance;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   public WanderToPositionGoal(WanderingTrader trader, double stopDistance, double speedModifier) {
/* 228 */     this.trader = trader;
/* 229 */     this.stopDistance = stopDistance;
/* 230 */     this.speedModifier = speedModifier;
/* 231 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public void stop() {
/* 236 */     this.trader.setWanderTarget((BlockPos)null);
/* 237 */     WanderingTrader.access$000(WanderingTrader.this).stop();
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 242 */     BlockPos wanderPosition = this.trader.getWanderTarget();
/* 243 */     return (wanderPosition != null && isTooFarAway(wanderPosition, this.stopDistance));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 248 */     BlockPos wanderPosition = this.trader.getWanderTarget();
/* 249 */     if (wanderPosition != null && WanderingTrader.access$100(WanderingTrader.this).isDone())
/* 250 */       if (isTooFarAway(wanderPosition, 10.0D)) {
/* 252 */         Vec3 dir = (new Vec3(wanderPosition.getX() - this.trader.getX(), wanderPosition.getY() - this.trader.getY(), wanderPosition.getZ() - this.trader.getZ())).normalize();
/* 253 */         Vec3 targetPos = dir.scale(10.0D).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
/* 254 */         WanderingTrader.access$200(WanderingTrader.this).moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
/*     */       } else {
/* 256 */         WanderingTrader.access$300(WanderingTrader.this).moveTo(wanderPosition.getX(), wanderPosition.getY(), wanderPosition.getZ(), this.speedModifier);
/*     */       }  
/*     */   }
/*     */   
/*     */   private boolean isTooFarAway(BlockPos pos, double distance) {
/* 262 */     return !pos.closerToCenterThan((Position)this.trader.position(), distance);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTrader$WanderToPositionGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */