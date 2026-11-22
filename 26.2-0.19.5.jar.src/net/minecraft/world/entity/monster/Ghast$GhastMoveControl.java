/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GhastMoveControl<T extends Mob> extends MoveControl<T> {
/*     */   private int floatDuration;
/*     */   
/*     */   private final boolean careful;
/*     */   
/*     */   private final BooleanSupplier shouldBeStopped;
/*     */   
/*     */   public GhastMoveControl(T ghast, boolean careful, BooleanSupplier shouldBeStopped) {
/* 209 */     super((Mob)ghast);
/* 210 */     this.careful = careful;
/* 211 */     this.shouldBeStopped = shouldBeStopped;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 216 */     if (this.shouldBeStopped.getAsBoolean()) {
/* 217 */       this.operation = MoveControl.Operation.WAIT;
/* 218 */       this.mob.stopInPlace();
/*     */     } 
/* 221 */     if (this.operation != MoveControl.Operation.MOVE_TO)
/*     */       return; 
/* 226 */     if (this.floatDuration-- <= 0) {
/* 227 */       this.floatDuration += this.mob.getRandom().nextInt(5) + 2;
/* 232 */       Vec3 travel = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
/* 235 */       if (canReach(travel)) {
/* 236 */         this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(travel.normalize().scale(this.mob.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */       } else {
/* 238 */         this.operation = MoveControl.Operation.WAIT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canReach(Vec3 travel) {
/* 244 */     AABB aabb = this.mob.getBoundingBox();
/* 245 */     AABB aabbAtDestination = aabb.move(travel);
/* 246 */     if (this.careful)
/* 247 */       for (BlockPos pos : BlockPos.betweenClosed(aabbAtDestination.inflate(1.0D))) {
/* 248 */         if (!blockTraversalPossible((BlockGetter)this.mob.level(), (Vec3)null, (Vec3)null, pos, false, false))
/* 249 */           return false; 
/*     */       }  
/* 253 */     boolean isInWater = this.mob.isInWater();
/* 254 */     boolean isInLava = this.mob.isInLava();
/* 255 */     Vec3 start = this.mob.position();
/* 256 */     Vec3 end = start.add(travel);
/* 258 */     return BlockGetter.forEachBlockIntersectedBetween(start, end, aabbAtDestination, (blockPos, i) -> aabb.intersects(blockPos) ? true : blockTraversalPossible((BlockGetter)this.mob.level(), start, end, blockPos, isInWater, isInLava));
/*     */   }
/*     */   
/*     */   private boolean blockTraversalPossible(BlockGetter level, Vec3 start, Vec3 end, BlockPos pos, boolean canPathThroughWater, boolean canPathThroughLava) {
/* 267 */     BlockState state = level.getBlockState(pos);
/* 268 */     if (state.isAir())
/* 269 */       return true; 
/* 271 */     boolean preciseBlockCollisions = (start != null && end != null);
/* 272 */     boolean pathNoCollisions = preciseBlockCollisions ? (!this.mob.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3((Vec3i)pos)).toAabbs())) : state.getCollisionShape(level, pos).isEmpty();
/* 273 */     if (!this.careful)
/* 274 */       return pathNoCollisions; 
/* 276 */     if (state.is(BlockTags.HAPPY_GHAST_AVOIDS))
/* 277 */       return false; 
/* 279 */     FluidState fluidState = level.getFluidState(pos);
/* 280 */     if (!fluidState.isEmpty() && (!preciseBlockCollisions || this.mob.collidedWithFluid(fluidState, pos, start, end))) {
/* 281 */       if (fluidState.is(FluidTags.WATER))
/* 282 */         return canPathThroughWater; 
/* 284 */       if (fluidState.is(FluidTags.LAVA))
/* 285 */         return canPathThroughLava; 
/*     */     } 
/* 288 */     return pathNoCollisions;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ghast$GhastMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */