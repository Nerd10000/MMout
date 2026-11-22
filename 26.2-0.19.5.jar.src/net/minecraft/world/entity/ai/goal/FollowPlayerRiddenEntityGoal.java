/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class FollowPlayerRiddenEntityGoal extends Goal {
/*     */   private int timeToRecalcPath;
/*     */   
/*     */   private final PathfinderMob mob;
/*     */   
/*     */   private final Class<? extends Entity> entityTypeToFollow;
/*     */   
/*     */   private Player following;
/*     */   
/*     */   private FollowEntityGoal currentGoal;
/*     */   
/*     */   public FollowPlayerRiddenEntityGoal(PathfinderMob mob, Class<? extends Entity> entityTypeToFollow) {
/*  23 */     this.mob = mob;
/*  24 */     this.entityTypeToFollow = entityTypeToFollow;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/*  29 */     if (this.following != null && this.following.hasMovedHorizontallyRecently())
/*  30 */       return true; 
/*  32 */     List<? extends Entity> entities = this.mob.level().getEntitiesOfClass(this.entityTypeToFollow, this.mob.getBoundingBox().inflate(5.0D));
/*  33 */     for (Entity entity : entities) {
/*  34 */       LivingEntity livingEntity = entity.getControllingPassenger();
/*  34 */       if (livingEntity instanceof Player) {
/*  34 */         Player controllingPlayer = (Player)livingEntity;
/*  34 */         if (controllingPlayer.hasMovedHorizontallyRecently())
/*  35 */           return true; 
/*     */       } 
/*     */     } 
/*  38 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInterruptable() {
/*  43 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/*  48 */     return (this.following != null && this.following.isPassenger() && this.following.hasMovedHorizontallyRecently());
/*     */   }
/*     */   
/*     */   public void start() {
/*  53 */     List<? extends Entity> entities = this.mob.level().getEntitiesOfClass(this.entityTypeToFollow, this.mob.getBoundingBox().inflate(5.0D));
/*  54 */     for (Entity entity : entities) {
/*  55 */       LivingEntity livingEntity = entity.getControllingPassenger();
/*  55 */       if (livingEntity instanceof Player) {
/*  55 */         Player player = (Player)livingEntity;
/*  56 */         this.following = player;
/*     */         break;
/*     */       } 
/*     */     } 
/*  61 */     this.timeToRecalcPath = 0;
/*  62 */     this.currentGoal = FollowEntityGoal.GO_TO_ENTITY;
/*     */   }
/*     */   
/*     */   public void stop() {
/*  67 */     this.following = null;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  72 */     float speed = (this.currentGoal == FollowEntityGoal.GO_IN_ENTITY_DIRECTION) ? 0.01F : 0.015F;
/*  73 */     this.mob.moveRelative(speed, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/*  74 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*  76 */     if (--this.timeToRecalcPath > 0)
/*     */       return; 
/*  79 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*  81 */     if (this.currentGoal == FollowEntityGoal.GO_TO_ENTITY) {
/*  82 */       BlockPos behindEntityPos = this.following.blockPosition().relative(this.following.getDirection().getOpposite());
/*  83 */       behindEntityPos = behindEntityPos.offset(0, -1, 0);
/*  84 */       this.mob.getNavigation().moveTo(behindEntityPos.getX(), behindEntityPos.getY(), behindEntityPos.getZ(), 1.0D);
/*  86 */       if (this.mob.distanceTo((Entity)this.following) < 4.0F) {
/*  87 */         this.timeToRecalcPath = 0;
/*  88 */         this.currentGoal = FollowEntityGoal.GO_IN_ENTITY_DIRECTION;
/*     */       } 
/*  90 */     } else if (this.currentGoal == FollowEntityGoal.GO_IN_ENTITY_DIRECTION) {
/*  92 */       Direction direction = this.following.getMotionDirection();
/*  93 */       BlockPos goTo = this.following.blockPosition().relative(direction, 10);
/*  96 */       this.mob.getNavigation().moveTo(goTo.getX(), (goTo.getY() - 1), goTo.getZ(), 1.0D);
/*  98 */       if (this.mob.distanceTo((Entity)this.following) > 12.0F) {
/*  99 */         this.timeToRecalcPath = 0;
/* 100 */         this.currentGoal = FollowEntityGoal.GO_TO_ENTITY;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum FollowEntityGoal {
/* 106 */     GO_TO_ENTITY, GO_IN_ENTITY_DIRECTION;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\FollowPlayerRiddenEntityGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */