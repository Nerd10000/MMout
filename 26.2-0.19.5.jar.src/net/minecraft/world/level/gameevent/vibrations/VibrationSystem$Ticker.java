/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public interface Ticker {
/*     */   static void tick(Level level, VibrationSystem.Data data, VibrationSystem.User user) {
/*     */     ServerLevel serverLevel;
/* 332 */     if (level instanceof ServerLevel) {
/* 332 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 336 */     if (data.currentVibration == null)
/* 337 */       trySelectAndScheduleVibration(serverLevel, data, user); 
/* 340 */     if (data.currentVibration == null)
/*     */       return; 
/* 344 */     boolean hasChanged = (data.getTravelTimeInTicks() > 0);
/* 345 */     tryReloadVibrationParticle(serverLevel, data, user);
/* 346 */     data.decrementTravelTime();
/* 348 */     if (data.getTravelTimeInTicks() <= 0)
/* 349 */       hasChanged = receiveVibration(serverLevel, data, user, data.currentVibration); 
/* 352 */     if (hasChanged)
/* 353 */       user.onDataChanged(); 
/*     */   }
/*     */   
/*     */   private static void trySelectAndScheduleVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user) {
/* 358 */     data.getSelectionStrategy().chosenCandidate(serverLevel.getGameTime()).ifPresent(context -> {
/*     */           data.setCurrentVibration(context);
/*     */           Vec3 origin = context.pos();
/*     */           data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(context.distance()));
/*     */           serverLevel.sendParticles((ParticleOptions)new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()), origin.x, origin.y, origin.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */           user.onDataChanged();
/*     */           data.getSelectionStrategy().startOver();
/*     */         });
/*     */   }
/*     */   
/*     */   private static void tryReloadVibrationParticle(ServerLevel level, VibrationSystem.Data data, VibrationSystem.User user) {
/* 369 */     if (!data.shouldReloadVibrationParticle())
/*     */       return; 
/* 373 */     if (data.currentVibration == null) {
/* 374 */       data.setReloadVibrationParticle(false);
/*     */       return;
/*     */     } 
/* 378 */     Vec3 origin = data.currentVibration.pos();
/* 379 */     PositionSource positionSource = user.getPositionSource();
/* 380 */     Vec3 destination = positionSource.getPosition((Level)level).orElse(origin);
/* 381 */     int travelTimeInTicks = data.getTravelTimeInTicks();
/* 383 */     int initialTravelTime = user.calculateTravelTimeInTicks(data.currentVibration.distance());
/* 384 */     double alpha = 1.0D - travelTimeInTicks / initialTravelTime;
/* 386 */     double newInitialX = Mth.lerp(alpha, origin.x, destination.x);
/* 387 */     double newInitialY = Mth.lerp(alpha, origin.y, destination.y);
/* 388 */     double newInitialZ = Mth.lerp(alpha, origin.z, destination.z);
/* 390 */     boolean particleWasSent = (level.sendParticles((ParticleOptions)new VibrationParticleOption(positionSource, travelTimeInTicks), newInitialX, newInitialY, newInitialZ, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0);
/* 392 */     if (particleWasSent)
/* 393 */       data.setReloadVibrationParticle(false); 
/*     */   }
/*     */   
/*     */   private static boolean receiveVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, VibrationInfo currentVibration) {
/* 398 */     BlockPos origin = BlockPos.containing((Position)currentVibration.pos());
/* 399 */     BlockPos destination = user.getPositionSource().getPosition((Level)serverLevel).map(BlockPos::containing).orElse(origin);
/* 403 */     if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking((Level)serverLevel, destination))
/* 404 */       return false; 
/* 407 */     user.onReceiveVibration(serverLevel, origin, currentVibration
/*     */         
/* 410 */         .gameEvent(), currentVibration
/* 411 */         .getEntity(serverLevel).orElse(null), currentVibration
/* 412 */         .getProjectileOwner(serverLevel).orElse(null), 
/* 413 */         VibrationSystem.Listener.distanceBetweenInBlocks(origin, destination));
/* 417 */     data.setCurrentVibration(null);
/* 418 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areAdjacentChunksTicking(Level level, BlockPos listenerPos) {
/* 422 */     ChunkPos listenerChunkPos = ChunkPos.containing(listenerPos);
/* 424 */     for (int x = listenerChunkPos.x() - 1; x <= listenerChunkPos.x() + 1; x++) {
/* 425 */       for (int z = listenerChunkPos.z() - 1; z <= listenerChunkPos.z() + 1; z++) {
/* 426 */         if (!level.shouldTickBlocksAt(ChunkPos.pack(x, z)) || level.getChunkSource().getChunkNow(x, z) == null)
/* 427 */           return false; 
/*     */       } 
/*     */     } 
/* 432 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Ticker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */