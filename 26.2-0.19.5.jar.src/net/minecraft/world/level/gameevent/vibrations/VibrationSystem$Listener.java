/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ClipBlockStateContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Listener implements GameEventListener {
/*     */   private final VibrationSystem system;
/*     */   
/*     */   public Listener(VibrationSystem system) {
/* 225 */     this.system = system;
/*     */   }
/*     */   
/*     */   public PositionSource getListenerSource() {
/* 230 */     return this.system.getVibrationUser().getPositionSource();
/*     */   }
/*     */   
/*     */   public int getListenerRadius() {
/* 235 */     return this.system.getVibrationUser().getListenerRadius();
/*     */   }
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 240 */     VibrationSystem.Data data = this.system.getVibrationData();
/* 241 */     VibrationSystem.User user = this.system.getVibrationUser();
/* 244 */     if (data.getCurrentVibration() != null)
/* 245 */       return false; 
/* 248 */     if (!user.isValidVibration(event, context))
/* 249 */       return false; 
/* 252 */     Optional<Vec3> listenerSourcePos = user.getPositionSource().getPosition((Level)level);
/* 254 */     if (listenerSourcePos.isEmpty())
/* 255 */       return false; 
/* 258 */     Vec3 destination = listenerSourcePos.get();
/* 261 */     if (!user.canReceiveVibration(level, BlockPos.containing((Position)sourcePosition), event, context))
/* 262 */       return false; 
/* 265 */     if (isOccluded((Level)level, sourcePosition, destination))
/* 266 */       return false; 
/* 269 */     scheduleVibration(level, data, event, context, sourcePosition, destination);
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public void forceScheduleVibration(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin) {
/* 275 */     this.system.getVibrationUser().getPositionSource().getPosition((Level)level).ifPresent(p -> scheduleVibration(level, this.system.getVibrationData(), event, context, origin, p));
/*     */   }
/*     */   
/*     */   private void scheduleVibration(ServerLevel level, VibrationSystem.Data data, Holder<GameEvent> event, GameEvent.Context context, Vec3 origin, Vec3 dest) {
/* 279 */     data.selectionStrategy.addCandidate(new VibrationInfo(event, (float)origin.distanceTo(dest), origin, context.sourceEntity()), level.getGameTime());
/*     */   }
/*     */   
/*     */   public static float distanceBetweenInBlocks(BlockPos origin, BlockPos dest) {
/* 296 */     return (float)Math.sqrt(origin.distSqr((Vec3i)dest));
/*     */   }
/*     */   
/*     */   private static boolean isOccluded(Level level, Vec3 origin, Vec3 dest) {
/* 305 */     Vec3 from = new Vec3(Mth.floor(origin.x) + 0.5D, Mth.floor(origin.y) + 0.5D, Mth.floor(origin.z) + 0.5D);
/* 310 */     Vec3 to = new Vec3(Mth.floor(dest.x) + 0.5D, Mth.floor(dest.y) + 0.5D, Mth.floor(dest.z) + 0.5D);
/* 313 */     for (Direction direction : Direction.values()) {
/* 314 */       Vec3 nudgedSource = from.relative(direction, 9.999999747378752E-6D);
/* 315 */       if (level.isBlockInLine(new ClipBlockStateContext(nudgedSource, to, state -> state.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType() != HitResult.Type.BLOCK)
/* 316 */         return false; 
/*     */     } 
/* 319 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Listener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */