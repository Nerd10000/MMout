/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoAndGiveItemsToTarget<E extends LivingEntity & InventoryCarrier> extends Behavior<E> {
/*    */   private static final int CLOSE_ENOUGH_DISTANCE_TO_TARGET = 3;
/*    */   
/*    */   private static final int ITEM_PICKUP_COOLDOWN_AFTER_THROWING = 60;
/*    */   
/*    */   private final Vec3 throwVelocity;
/*    */   
/*    */   private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;
/*    */   
/*    */   private final float speedModifier;
/*    */   
/*    */   private final ItemThrower<E> itemThrower;
/*    */   
/*    */   public GoAndGiveItemsToTarget(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, float speedModifier, int timeoutDuration, ItemThrower<E> itemThrower) {
/* 27 */     super(Map.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.REGISTERED), timeoutDuration);
/* 32 */     this.targetPositionGetter = targetPositionGetter;
/* 33 */     this.speedModifier = speedModifier;
/* 34 */     this.itemThrower = itemThrower;
/* 35 */     this.throwVelocity = new Vec3(0.20000000298023224D, 0.30000001192092896D, 0.20000000298023224D);
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/* 40 */     return canThrowItemToTarget(body);
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
/* 45 */     return canThrowItemToTarget(body);
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, E body, long timestamp) {
/* 50 */     ((Optional)this.targetPositionGetter.apply((LivingEntity)body)).ifPresent(positionTracker -> BehaviorUtils.setWalkAndLookTargetMemories(body, positionTracker, this.speedModifier, 3));
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, E body, long timestamp) {
/* 57 */     Optional<PositionTracker> targetPosition = this.targetPositionGetter.apply((LivingEntity)body);
/* 58 */     if (targetPosition.isEmpty())
/*    */       return; 
/* 62 */     PositionTracker depositTarget = targetPosition.get();
/* 63 */     Vec3 depositPosition = depositTarget.currentPosition();
/* 64 */     double distanceToTarget = depositPosition.distanceTo(body.getEyePosition());
/* 65 */     if (distanceToTarget < 3.0D) {
/* 66 */       ItemStack item = ((InventoryCarrier)body).getInventory().removeItem(0, 1);
/* 67 */       if (!item.isEmpty()) {
/* 68 */         BehaviorUtils.throwItem((LivingEntity)body, item, depositPosition.add(0.0D, 1.0D, 0.0D), this.throwVelocity, 0.2F);
/* 69 */         this.itemThrower.onItemThrown(level, body, item, depositTarget.currentBlockPosition());
/* 70 */         body.getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, Integer.valueOf(60));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean canThrowItemToTarget(E body) {
/* 76 */     if (((InventoryCarrier)body).getInventory().isEmpty())
/* 77 */       return false; 
/* 79 */     Optional<PositionTracker> positionTracker = this.targetPositionGetter.apply((LivingEntity)body);
/* 80 */     return positionTracker.isPresent();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ItemThrower<E> {
/*    */     void onItemThrown(ServerLevel param1ServerLevel, E param1E, ItemStack param1ItemStack, BlockPos param1BlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GoAndGiveItemsToTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */