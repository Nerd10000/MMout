/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NoOpProfiler implements JvmProfiler {
/*  52 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final ProfiledDuration NO_OP_COMMIT = ignored -> {
/*     */     
/*     */     };
/*     */   
/*     */   public boolean start(Environment environment) {
/*  57 */     LOGGER.warn("Attempted to start Flight Recorder, but it's not supported on this JVM");
/*  58 */     return false;
/*     */   }
/*     */   
/*     */   public Path stop() {
/*  63 */     throw new IllegalStateException("Attempted to stop Flight Recorder, but it's not supported on this JVM");
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAvailable() {
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public void onPacketReceived(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int readableBytes) {}
/*     */   
/*     */   public void onPacketSent(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int writtenBytes) {}
/*     */   
/*     */   public void onRegionFileRead(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int readBytes) {}
/*     */   
/*     */   public void onRegionFileWrite(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int writtenBytes) {}
/*     */   
/*     */   public void onServerTick(float averageTickTime) {}
/*     */   
/*     */   public void onClientTick(int fps) {}
/*     */   
/*     */   public ProfiledDuration onWorldLoadedStarted() {
/* 108 */     return NO_OP_COMMIT;
/*     */   }
/*     */   
/*     */   public ProfiledDuration onChunkGenerate(ChunkPos pos, ResourceKey<Level> dimension, String name) {
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public ProfiledDuration onStructureGenerate(ChunkPos sourceChunkPos, ResourceKey<Level> dimension, Holder<Structure> structure) {
/* 118 */     return NO_OP_COMMIT;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\JvmProfiler$NoOpProfiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */