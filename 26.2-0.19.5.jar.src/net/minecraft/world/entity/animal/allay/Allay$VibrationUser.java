/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ 
/*     */ class VibrationUser implements VibrationSystem.User {
/*     */   private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
/*     */   
/* 605 */   private final PositionSource positionSource = (PositionSource)new EntityPositionSource((Entity)Allay.this, Allay.this.getEyeHeight());
/*     */   
/*     */   public int getListenerRadius() {
/* 609 */     return 16;
/*     */   }
/*     */   
/*     */   public PositionSource getPositionSource() {
/* 614 */     return this.positionSource;
/*     */   }
/*     */   
/*     */   public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 619 */     if (Allay.this.isNoAi())
/* 620 */       return false; 
/* 623 */     Optional<GlobalPos> maybeGlobalPos = Allay.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 624 */     if (maybeGlobalPos.isEmpty())
/* 625 */       return true; 
/* 627 */     GlobalPos globalPos = maybeGlobalPos.get();
/* 628 */     return (globalPos.isCloseEnough(level.dimension(), Allay.this.blockPosition(), 1024) && globalPos.pos().equals(pos));
/*     */   }
/*     */   
/*     */   public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 633 */     if (event.is((Holder)GameEvent.NOTE_BLOCK_PLAY))
/* 634 */       AllayAi.hearNoteblock((LivingEntity)Allay.this, new BlockPos((Vec3i)pos)); 
/*     */   }
/*     */   
/*     */   public TagKey<GameEvent> getListenableEvents() {
/* 640 */     return GameEventTags.ALLAY_CAN_LISTEN;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\allay\Allay$VibrationUser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */