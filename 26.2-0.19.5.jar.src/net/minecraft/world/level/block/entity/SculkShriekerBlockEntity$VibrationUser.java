/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ 
/*     */ class VibrationUser implements VibrationSystem.User {
/*     */   private static final int LISTENER_RADIUS = 8;
/*     */   
/*     */   private final PositionSource positionSource;
/*     */   
/*     */   public VibrationUser() {
/* 209 */     this.positionSource = (PositionSource)new BlockPositionSource(paramSculkShriekerBlockEntity.worldPosition);
/*     */   }
/*     */   
/*     */   public int getListenerRadius() {
/* 214 */     return 8;
/*     */   }
/*     */   
/*     */   public PositionSource getPositionSource() {
/* 219 */     return this.positionSource;
/*     */   }
/*     */   
/*     */   public TagKey<GameEvent> getListenableEvents() {
/* 224 */     return GameEventTags.SHRIEKER_CAN_LISTEN;
/*     */   }
/*     */   
/*     */   public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 229 */     return (!((Boolean)SculkShriekerBlockEntity.this.getBlockState().getValue((Property)SculkShriekerBlock.SHRIEKING)).booleanValue() && SculkShriekerBlockEntity.tryGetPlayer(context.sourceEntity()) != null);
/*     */   }
/*     */   
/*     */   public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 234 */     SculkShriekerBlockEntity.this.tryShriek(level, SculkShriekerBlockEntity.tryGetPlayer((projectileOwner != null) ? projectileOwner : sourceEntity));
/*     */   }
/*     */   
/*     */   public void onDataChanged() {
/* 239 */     SculkShriekerBlockEntity.this.setChanged();
/*     */   }
/*     */   
/*     */   public boolean requiresAdjacentChunksToBeTicking() {
/* 244 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\SculkShriekerBlockEntity$VibrationUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */