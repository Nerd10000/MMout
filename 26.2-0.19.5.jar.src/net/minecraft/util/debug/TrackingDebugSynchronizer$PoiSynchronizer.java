/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class PoiSynchronizer extends TrackingDebugSynchronizer<DebugPoiInfo> {
/*     */   public PoiSynchronizer() {
/* 240 */     super(DebugSubscriptions.POIS);
/*     */   }
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 245 */     ServerLevel level = player.level();
/* 246 */     PoiManager poiManager = level.getPoiManager();
/* 247 */     poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> player.connection.send((Packet)new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record)))));
/*     */   }
/*     */   
/*     */   public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 255 */     sendToPlayersTrackingChunk(level, ChunkPos.containing(record.getPos()), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(record.getPos(), this.subscription.packUpdate(new DebugPoiInfo(record))));
/*     */   }
/*     */   
/*     */   public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 259 */     sendToPlayersTrackingChunk(level, ChunkPos.containing(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.emptyUpdate()));
/*     */   }
/*     */   
/*     */   public void onPoiTicketCountChanged(ServerLevel level, BlockPos poiPos) {
/* 263 */     sendToPlayersTrackingChunk(level, ChunkPos.containing(poiPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(poiPos, this.subscription.packUpdate(level.getPoiManager().getDebugPoiInfo(poiPos))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$PoiSynchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */