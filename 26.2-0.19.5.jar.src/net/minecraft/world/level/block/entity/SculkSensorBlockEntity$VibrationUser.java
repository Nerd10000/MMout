/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SculkSensorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ 
/*     */ public class VibrationUser implements VibrationSystem.User {
/*     */   public static final int LISTENER_RANGE = 8;
/*     */   
/*     */   protected final BlockPos blockPos;
/*     */   
/*     */   private final PositionSource positionSource;
/*     */   
/*     */   public VibrationUser(BlockPos blockPos) {
/*  87 */     this.blockPos = blockPos;
/*  88 */     this.positionSource = (PositionSource)new BlockPositionSource(blockPos);
/*     */   }
/*     */   
/*     */   public int getListenerRadius() {
/*  93 */     return 8;
/*     */   }
/*     */   
/*     */   public PositionSource getPositionSource() {
/*  98 */     return this.positionSource;
/*     */   }
/*     */   
/*     */   public boolean canTriggerAvoidVibration() {
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 112 */     if (pos.equals(this.blockPos) && (event.is((Holder)GameEvent.BLOCK_DESTROY) || event.is((Holder)GameEvent.BLOCK_PLACE)))
/* 113 */       return false; 
/* 116 */     if (VibrationSystem.getGameEventFrequency(event) == 0)
/* 117 */       return false; 
/* 120 */     return SculkSensorBlock.canActivate(SculkSensorBlockEntity.this.getBlockState());
/*     */   }
/*     */   
/*     */   public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 125 */     BlockState state = SculkSensorBlockEntity.this.getBlockState();
/* 126 */     if (SculkSensorBlock.canActivate(state)) {
/* 127 */       int eventFrequency = VibrationSystem.getGameEventFrequency(event);
/* 128 */       SculkSensorBlockEntity.this.setLastVibrationFrequency(eventFrequency);
/* 129 */       int calculatedPower = VibrationSystem.getRedstoneStrengthForDistance(receivingDistance, getListenerRadius());
/* 130 */       Block block = state.getBlock();
/* 130 */       if (block instanceof SculkSensorBlock) {
/* 130 */         SculkSensorBlock sculkSensorBlock = (SculkSensorBlock)block;
/* 131 */         sculkSensorBlock.activate(sourceEntity, (Level)level, this.blockPos, state, calculatedPower, eventFrequency);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDataChanged() {
/* 138 */     SculkSensorBlockEntity.this.setChanged();
/*     */   }
/*     */   
/*     */   public boolean requiresAdjacentChunksToBeTicking() {
/* 143 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\SculkSensorBlockEntity$VibrationUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */