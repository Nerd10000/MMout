/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class VillageSectionSynchronizer extends TrackingDebugSynchronizer<Unit> {
/*     */   public VillageSectionSynchronizer() {
/* 269 */     super(DebugSubscriptions.VILLAGE_SECTIONS);
/*     */   }
/*     */   
/*     */   protected void sendInitialChunk(ServerPlayer player, ChunkPos chunkPos) {
/* 274 */     ServerLevel level = player.level();
/* 275 */     PoiManager poiManager = level.getPoiManager();
/* 276 */     poiManager.getInChunk(t -> true, chunkPos, PoiManager.Occupancy.ANY).forEach(record -> {
/*     */           SectionPos centerSection = SectionPos.of(record.getPos());
/*     */           forEachVillageSectionUpdate(level, centerSection, ());
/*     */         });
/*     */   }
/*     */   
/*     */   public void onPoiAdded(ServerLevel level, PoiRecord record) {
/* 288 */     sendVillageSectionsPacket(level, record.getPos());
/*     */   }
/*     */   
/*     */   public void onPoiRemoved(ServerLevel level, BlockPos poiPos) {
/* 292 */     sendVillageSectionsPacket(level, poiPos);
/*     */   }
/*     */   
/*     */   private void sendVillageSectionsPacket(ServerLevel level, BlockPos poiPos) {
/* 296 */     forEachVillageSectionUpdate(level, SectionPos.of(poiPos), (sectionPos, isVillage) -> {
/*     */           BlockPos sectionBlockPos = sectionPos.center();
/*     */           if (isVillage.booleanValue()) {
/*     */             sendToPlayersTrackingChunk(level, ChunkPos.containing(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.packUpdate(Unit.INSTANCE)));
/*     */           } else {
/*     */             sendToPlayersTrackingChunk(level, ChunkPos.containing(sectionBlockPos), (Packet<? super ClientGamePacketListener>)new ClientboundDebugBlockValuePacket(sectionBlockPos, this.subscription.emptyUpdate()));
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void forEachVillageSectionUpdate(ServerLevel level, SectionPos centerSection, BiConsumer<SectionPos, Boolean> consumer) {
/* 307 */     for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 308 */       for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 309 */         for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 310 */           SectionPos sectionPos = centerSection.offset(offsetX, offsetY, offsetZ);
/* 311 */           if (level.isVillage(sectionPos.center())) {
/* 312 */             consumer.accept(sectionPos, Boolean.valueOf(true));
/*     */           } else {
/* 314 */             consumer.accept(sectionPos, Boolean.valueOf(false));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$VillageSectionSynchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */