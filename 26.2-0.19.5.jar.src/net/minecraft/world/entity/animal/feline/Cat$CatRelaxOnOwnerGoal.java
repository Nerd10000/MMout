/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class CatRelaxOnOwnerGoal extends Goal {
/*     */   private final Cat cat;
/*     */   
/*     */   private Player ownerPlayer;
/*     */   
/*     */   private BlockPos goalPos;
/*     */   
/*     */   private int onBedTicks;
/*     */   
/*     */   public CatRelaxOnOwnerGoal(Cat cat) {
/* 584 */     this.cat = cat;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 589 */     if (!this.cat.isTame())
/* 590 */       return false; 
/* 593 */     if (this.cat.isOrderedToSit())
/* 594 */       return false; 
/* 597 */     LivingEntity owner = this.cat.getOwner();
/* 598 */     if (owner instanceof Player) {
/* 598 */       Player playerOwner = (Player)owner;
/* 599 */       this.ownerPlayer = playerOwner;
/* 601 */       if (!owner.isSleeping())
/* 602 */         return false; 
/* 605 */       if (this.cat.distanceToSqr((Entity)this.ownerPlayer) > 100.0D)
/* 606 */         return false; 
/* 609 */       BlockPos ownerPos = this.ownerPlayer.blockPosition();
/* 610 */       BlockState ownerPosState = this.cat.level().getBlockState(ownerPos);
/* 611 */       if (ownerPosState.is(BlockTags.BEDS)) {
/* 612 */         this.goalPos = ownerPosState.getOptionalValue((Property)BedBlock.FACING).map(bedDir -> ownerPos.relative(bedDir.getOpposite())).orElseGet(() -> new BlockPos((Vec3i)ownerPos));
/* 613 */         return !spaceIsOccupied();
/*     */       } 
/*     */     } 
/* 616 */     return false;
/*     */   }
/*     */   
/*     */   private boolean spaceIsOccupied() {
/* 620 */     List<Cat> cats = this.cat.level().getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 621 */     for (Cat otherCat : cats) {
/* 622 */       if (otherCat != this.cat && (otherCat.isLying() || otherCat.isRelaxStateOne()))
/* 623 */         return true; 
/*     */     } 
/* 626 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 631 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied());
/*     */   }
/*     */   
/*     */   public void start() {
/* 636 */     if (this.goalPos != null) {
/* 637 */       this.cat.setInSittingPose(false);
/* 638 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 644 */     this.cat.setLying(false);
/* 646 */     if (this.ownerPlayer.getSleepTimer() >= 100 && this.cat
/* 647 */       .level().getRandom().nextFloat() < ((Float)this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())).floatValue())
/* 648 */       giveMorningGift(); 
/* 651 */     this.onBedTicks = 0;
/* 652 */     this.cat.setRelaxStateOne(false);
/* 653 */     this.cat.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private void giveMorningGift() {
/* 657 */     RandomSource random = this.cat.getRandom();
/* 658 */     BlockPos.MutableBlockPos catPos = new BlockPos.MutableBlockPos();
/* 659 */     catPos.set(this.cat.isLeashed() ? (Vec3i)this.cat.getLeashHolder().blockPosition() : (Vec3i)this.cat.blockPosition());
/* 660 */     this.cat.randomTeleport((catPos.getX() + random.nextInt(11) - 5), (catPos.getY() + random.nextInt(5) - 2), (catPos.getZ() + random.nextInt(11) - 5), false);
/* 662 */     catPos.set((Vec3i)this.cat.blockPosition());
/* 663 */     this.cat.dropFromGiftLootTable(getServerLevel((Entity)this.cat), BuiltInLootTables.CAT_MORNING_GIFT, (level, itemStack) -> level.addFreshEntity((Entity)new ItemEntity((Level)level, catPos.getX() - Mth.sin((this.cat.yBodyRot * 0.017453292F)), catPos.getY(), catPos.getZ() + Mth.cos((this.cat.yBodyRot * 0.017453292F)), itemStack)));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 670 */     if (this.ownerPlayer != null && this.goalPos != null) {
/* 671 */       this.cat.setInSittingPose(false);
/* 672 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 673 */       if (this.cat.distanceToSqr((Entity)this.ownerPlayer) < 2.5D) {
/* 674 */         this.onBedTicks++;
/* 675 */         if (this.onBedTicks > adjustedTickDelay(16)) {
/* 676 */           this.cat.setLying(true);
/* 677 */           this.cat.setRelaxStateOne(false);
/*     */         } else {
/* 679 */           this.cat.lookAt((Entity)this.ownerPlayer, 45.0F, 45.0F);
/* 680 */           this.cat.setRelaxStateOne(true);
/*     */         } 
/*     */       } else {
/* 683 */         this.cat.setLying(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Cat$CatRelaxOnOwnerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */