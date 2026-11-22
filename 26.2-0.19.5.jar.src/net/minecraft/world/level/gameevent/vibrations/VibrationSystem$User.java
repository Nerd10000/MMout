/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ 
/*     */ public interface User {
/*     */   int getListenerRadius();
/*     */   
/*     */   PositionSource getPositionSource();
/*     */   
/*     */   boolean canReceiveVibration(ServerLevel paramServerLevel, BlockPos paramBlockPos, Holder<GameEvent> paramHolder, GameEvent.Context paramContext);
/*     */   
/*     */   void onReceiveVibration(ServerLevel paramServerLevel, BlockPos paramBlockPos, Holder<GameEvent> paramHolder, Entity paramEntity1, Entity paramEntity2, float paramFloat);
/*     */   
/*     */   default TagKey<GameEvent> getListenableEvents() {
/* 460 */     return GameEventTags.VIBRATIONS;
/*     */   }
/*     */   
/*     */   default boolean canTriggerAvoidVibration() {
/* 464 */     return false;
/*     */   }
/*     */   
/*     */   default boolean requiresAdjacentChunksToBeTicking() {
/* 468 */     return false;
/*     */   }
/*     */   
/*     */   default int calculateTravelTimeInTicks(float distanceToDestination) {
/* 472 */     return Mth.floor(distanceToDestination);
/*     */   }
/*     */   
/*     */   default boolean isValidVibration(Holder<GameEvent> event, GameEvent.Context context) {
/* 487 */     if (!event.is(getListenableEvents()))
/* 488 */       return false; 
/* 491 */     Entity sourceEntity = context.sourceEntity();
/* 493 */     if (sourceEntity != null) {
/* 494 */       if (sourceEntity.isSpectator())
/* 495 */         return false; 
/* 498 */       if (sourceEntity.isSteppingCarefully() && event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
/* 499 */         if (canTriggerAvoidVibration() && sourceEntity instanceof ServerPlayer) {
/* 499 */           ServerPlayer player = (ServerPlayer)sourceEntity;
/* 500 */           CriteriaTriggers.AVOID_VIBRATION.trigger(player);
/*     */         } 
/* 503 */         return false;
/*     */       } 
/* 506 */       if (sourceEntity.dampensVibrations())
/* 507 */         return false; 
/*     */     } 
/* 511 */     if (context.affectedState() != null)
/* 512 */       return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS); 
/* 515 */     return true;
/*     */   }
/*     */   
/*     */   default void onDataChanged() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$User.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */