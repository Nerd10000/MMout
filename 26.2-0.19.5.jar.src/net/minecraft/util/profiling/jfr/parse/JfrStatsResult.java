/*    */ package net.minecraft.util.profiling.jfr.parse;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.profiling.jfr.serialize.JfrResultJsonSerializer;
/*    */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*    */ import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.FpsStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*    */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*    */ import net.minecraft.util.profiling.jfr.stats.StructureGenStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
/*    */ import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public final class JfrStatsResult extends Record {
/*    */   private final Instant recordingStarted;
/*    */   
/*    */   private final Instant recordingEnded;
/*    */   
/*    */   private final Duration recordingDuration;
/*    */   
/*    */   private final Duration worldCreationDuration;
/*    */   
/*    */   private final List<FpsStat> fps;
/*    */   
/*    */   private final List<TickTimeStat> serverTickTimes;
/*    */   
/*    */   private final List<CpuLoadStat> cpuLoadStats;
/*    */   
/*    */   private final GcHeapStat.Summary heapSummary;
/*    */   
/*    */   private final ThreadAllocationStat.Summary threadAllocationSummary;
/*    */   
/*    */   private final IoSummary<PacketIdentification> receivedPacketsSummary;
/*    */   
/*    */   private final IoSummary<PacketIdentification> sentPacketsSummary;
/*    */   
/*    */   private final IoSummary<ChunkIdentification> writtenChunks;
/*    */   
/*    */   private final IoSummary<ChunkIdentification> readChunks;
/*    */   
/*    */   private final FileIOStat.Summary fileWrites;
/*    */   
/*    */   private final FileIOStat.Summary fileReads;
/*    */   
/*    */   private final List<ChunkGenStat> chunkGenStats;
/*    */   
/*    */   private final List<StructureGenStat> structureGenStats;
/*    */   
/*    */   public JfrStatsResult(Instant recordingStarted, Instant recordingEnded, Duration recordingDuration, Duration worldCreationDuration, List<FpsStat> fps, List<TickTimeStat> serverTickTimes, List<CpuLoadStat> cpuLoadStats, GcHeapStat.Summary heapSummary, ThreadAllocationStat.Summary threadAllocationSummary, IoSummary<PacketIdentification> receivedPacketsSummary, IoSummary<PacketIdentification> sentPacketsSummary, IoSummary<ChunkIdentification> writtenChunks, IoSummary<ChunkIdentification> readChunks, FileIOStat.Summary fileWrites, FileIOStat.Summary fileReads, List<ChunkGenStat> chunkGenStats, List<StructureGenStat> structureGenStats) {
/* 27 */     this.recordingStarted = recordingStarted;
/* 27 */     this.recordingEnded = recordingEnded;
/* 27 */     this.recordingDuration = recordingDuration;
/* 27 */     this.worldCreationDuration = worldCreationDuration;
/* 27 */     this.fps = fps;
/* 27 */     this.serverTickTimes = serverTickTimes;
/* 27 */     this.cpuLoadStats = cpuLoadStats;
/* 27 */     this.heapSummary = heapSummary;
/* 27 */     this.threadAllocationSummary = threadAllocationSummary;
/* 27 */     this.receivedPacketsSummary = receivedPacketsSummary;
/* 27 */     this.sentPacketsSummary = sentPacketsSummary;
/* 27 */     this.writtenChunks = writtenChunks;
/* 27 */     this.readChunks = readChunks;
/* 27 */     this.fileWrites = fileWrites;
/* 27 */     this.fileReads = fileReads;
/* 27 */     this.chunkGenStats = chunkGenStats;
/* 27 */     this.structureGenStats = structureGenStats;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/parse/JfrStatsResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Instant recordingStarted() {
/* 27 */     return this.recordingStarted;
/*    */   }
/*    */   
/*    */   public Instant recordingEnded() {
/* 27 */     return this.recordingEnded;
/*    */   }
/*    */   
/*    */   public Duration recordingDuration() {
/* 27 */     return this.recordingDuration;
/*    */   }
/*    */   
/*    */   public Duration worldCreationDuration() {
/* 27 */     return this.worldCreationDuration;
/*    */   }
/*    */   
/*    */   public List<FpsStat> fps() {
/* 27 */     return this.fps;
/*    */   }
/*    */   
/*    */   public List<TickTimeStat> serverTickTimes() {
/* 27 */     return this.serverTickTimes;
/*    */   }
/*    */   
/*    */   public List<CpuLoadStat> cpuLoadStats() {
/* 27 */     return this.cpuLoadStats;
/*    */   }
/*    */   
/*    */   public GcHeapStat.Summary heapSummary() {
/* 27 */     return this.heapSummary;
/*    */   }
/*    */   
/*    */   public ThreadAllocationStat.Summary threadAllocationSummary() {
/* 27 */     return this.threadAllocationSummary;
/*    */   }
/*    */   
/*    */   public IoSummary<PacketIdentification> receivedPacketsSummary() {
/* 27 */     return this.receivedPacketsSummary;
/*    */   }
/*    */   
/*    */   public IoSummary<PacketIdentification> sentPacketsSummary() {
/* 27 */     return this.sentPacketsSummary;
/*    */   }
/*    */   
/*    */   public IoSummary<ChunkIdentification> writtenChunks() {
/* 27 */     return this.writtenChunks;
/*    */   }
/*    */   
/*    */   public IoSummary<ChunkIdentification> readChunks() {
/* 27 */     return this.readChunks;
/*    */   }
/*    */   
/*    */   public FileIOStat.Summary fileWrites() {
/* 27 */     return this.fileWrites;
/*    */   }
/*    */   
/*    */   public FileIOStat.Summary fileReads() {
/* 27 */     return this.fileReads;
/*    */   }
/*    */   
/*    */   public List<ChunkGenStat> chunkGenStats() {
/* 27 */     return this.chunkGenStats;
/*    */   }
/*    */   
/*    */   public List<StructureGenStat> structureGenStats() {
/* 27 */     return this.structureGenStats;
/*    */   }
/*    */   
/*    */   public List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary() {
/* 47 */     Map<ChunkStatus, List<ChunkGenStat>> byStatus = (Map<ChunkStatus, List<ChunkGenStat>>)this.chunkGenStats.stream().collect(Collectors.groupingBy(ChunkGenStat::status));
/* 48 */     return byStatus.entrySet().stream()
/* 49 */       .map(e -> Pair.of(e.getKey(), TimedStatSummary.summary((List)e.getValue())))
/* 50 */       .filter(pair -> ((Optional)pair.getSecond()).isPresent())
/* 51 */       .map(e -> Pair.of(e.getFirst(), ((Optional<TimedStatSummary>)e.getSecond()).get()))
/* 52 */       .sorted(Comparator.<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>, Comparable>comparing(pair -> ((TimedStatSummary)pair.getSecond()).totalDuration()).reversed())
/* 53 */       .toList();
/*    */   }
/*    */   
/*    */   public String asJson() {
/* 57 */     return (new JfrResultJsonSerializer()).format(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\parse\JfrStatsResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */