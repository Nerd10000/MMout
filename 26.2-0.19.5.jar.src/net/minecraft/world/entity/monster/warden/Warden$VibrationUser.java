/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ 
/*     */ class VibrationUser implements VibrationSystem.User {
/*     */   private static final int GAME_EVENT_LISTENER_RANGE = 16;
/*     */   
/* 633 */   private final PositionSource positionSource = (PositionSource)new EntityPositionSource((Entity)Warden.this, Warden.this.getEyeHeight());
/*     */   
/*     */   public int getListenerRadius() {
/* 637 */     return 16;
/*     */   }
/*     */   
/*     */   public PositionSource getPositionSource() {
/* 642 */     return this.positionSource;
/*     */   }
/*     */   
/*     */   public TagKey<GameEvent> getListenableEvents() {
/* 647 */     return GameEventTags.WARDEN_CAN_LISTEN;
/*     */   }
/*     */   
/*     */   public boolean canTriggerAvoidVibration() {
/* 652 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 657 */     if (Warden.this.isNoAi() || Warden.this
/* 658 */       .isDeadOrDying() || Warden.this
/* 659 */       .getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) || Warden.this
/* 660 */       .isDiggingOrEmerging() || 
/* 661 */       !level.getWorldBorder().isWithinBounds(pos))
/* 663 */       return false; 
/* 666 */     Entity entity = context.sourceEntity();
/* 666 */     if (entity instanceof LivingEntity) {
/* 666 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 666 */       if (Warden.this.canTargetEntity((Entity)livingEntity));
/* 666 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 671 */     if (Warden.this.isDeadOrDying())
/*     */       return; 
/* 675 */     Warden.access$000(Warden.this).setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
/* 677 */     level.broadcastEntityEvent((Entity)Warden.this, (byte)61);
/* 678 */     Warden.this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, Warden.this.getVoicePitch());
/* 680 */     BlockPos suspiciousPos = pos;
/* 683 */     if (projectileOwner != null) {
/* 684 */       if (Warden.this.closerThan(projectileOwner, 30.0D))
/* 685 */         if (Warden.this.getBrain().hasMemoryValue(MemoryModuleType.RECENT_PROJECTILE)) {
/* 686 */           if (Warden.this.canTargetEntity(projectileOwner))
/* 687 */             suspiciousPos = projectileOwner.blockPosition(); 
/* 689 */           Warden.this.increaseAngerAt(projectileOwner);
/*     */         } else {
/* 691 */           Warden.this.increaseAngerAt(projectileOwner, 10, true);
/*     */         }  
/* 694 */       Warden.this.getBrain().setMemoryWithExpiry(MemoryModuleType.RECENT_PROJECTILE, Unit.INSTANCE, 100L);
/*     */     } else {
/* 696 */       Warden.this.increaseAngerAt(sourceEntity);
/*     */     } 
/* 699 */     if (!Warden.this.getAngerLevel().isAngry()) {
/* 701 */       Optional<LivingEntity> activeEntity = Warden.this.angerManagement.getActiveEntity();
/* 702 */       if (projectileOwner != null || activeEntity.isEmpty() || activeEntity.get() == sourceEntity)
/* 703 */         WardenAi.setDisturbanceLocation(Warden.this, suspiciousPos); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\Warden$VibrationUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */