/*     */ package net.minecraft.server.network.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.server.network.CommonListenerCookie;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class Ready implements PrepareSpawnTask.State {
/*     */   private final ServerLevel spawnLevel;
/*     */   
/*     */   private final Vec3 spawnPosition;
/*     */   
/*     */   private final Vec2 spawnAngle;
/*     */   
/*     */   private Ready(ServerLevel spawnLevel, Vec3 spawnPosition, Vec2 spawnAngle) {
/* 171 */     this.spawnLevel = spawnLevel;
/* 172 */     this.spawnPosition = spawnPosition;
/* 173 */     this.spawnAngle = spawnAngle;
/*     */   }
/*     */   
/*     */   public void keepAlive() {
/* 177 */     this.spawnLevel.getChunkSource().addTicketWithRadius(TicketType.PLAYER_SPAWN, ChunkPos.containing(BlockPos.containing((Position)this.spawnPosition)), 3);
/*     */   }
/*     */   
/*     */   public ServerPlayer spawn(Connection connection, CommonListenerCookie cookie) {
/* 183 */     ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing((Position)this.spawnPosition));
/* 184 */     this.spawnLevel.waitForEntities(spawnChunk, 3);
/* 186 */     ServerPlayer player = new ServerPlayer(PrepareSpawnTask.this.server, this.spawnLevel, cookie.gameProfile(), cookie.clientInformation());
/* 188 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(player.problemPath(), PrepareSpawnTask.LOGGER);
/*     */     try {
/* 193 */       Optional<ValueInput> input = PrepareSpawnTask.this.server.getPlayerList().loadPlayerData(PrepareSpawnTask.this.nameAndId).map(tag -> TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)PrepareSpawnTask.this.server.registryAccess(), tag));
/* 194 */       Objects.requireNonNull(player);
/* 194 */       input.ifPresent(player::load);
/* 196 */       player.snapTo(this.spawnPosition, this.spawnAngle.x, this.spawnAngle.y);
/* 198 */       PrepareSpawnTask.this.server.getPlayerList().placeNewPlayer(connection, player, cookie);
/* 200 */       input.ifPresent(tag -> {
/*     */             player.loadAndSpawnEnderPearls(tag);
/*     */             player.loadAndSpawnParentVehicle(tag);
/*     */           });
/* 205 */       ServerPlayer serverPlayer = player;
/* 206 */       reporter.close();
/*     */       return serverPlayer;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\config\PrepareSpawnTask$Ready.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */