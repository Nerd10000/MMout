/*     */ package net.minecraft.util.profiling.jfr.serialize;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.LongSerializationPolicy;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.time.Duration;
/*     */ import java.util.DoubleSummaryStatistics;
/*     */ import java.util.IntSummaryStatistics;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.jfr.Percentiles;
/*     */ import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FpsStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*     */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.StructureGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class JfrResultJsonSerializer {
/*     */   private static final String BYTES_PER_SECOND = "bytesPerSecond";
/*     */   
/*     */   private static final String COUNT = "count";
/*     */   
/*     */   private static final String DURATION_NANOS_TOTAL = "durationNanosTotal";
/*     */   
/*     */   private static final String TOTAL_BYTES = "totalBytes";
/*     */   
/*     */   private static final String COUNT_PER_SECOND = "countPerSecond";
/*     */   
/*  49 */   private final Gson gson = (new GsonBuilder())
/*  50 */     .setPrettyPrinting()
/*  51 */     .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
/*  52 */     .create();
/*     */   
/*     */   private static void serializePacketId(PacketIdentification identifier, JsonObject output) {
/*  55 */     output.addProperty("protocolId", identifier.protocolId());
/*  56 */     output.addProperty("packetId", identifier.packetId());
/*     */   }
/*     */   
/*     */   private static void serializeChunkId(ChunkIdentification identifier, JsonObject output) {
/*  60 */     output.addProperty("level", identifier.level());
/*  61 */     output.addProperty("dimension", identifier.dimension());
/*  62 */     output.addProperty("x", Integer.valueOf(identifier.x()));
/*  63 */     output.addProperty("z", Integer.valueOf(identifier.z()));
/*     */   }
/*     */   
/*     */   public String format(JfrStatsResult jfrStats) {
/*  67 */     JsonObject root = new JsonObject();
/*  69 */     root.addProperty("startedEpoch", Long.valueOf(jfrStats.recordingStarted().toEpochMilli()));
/*  70 */     root.addProperty("endedEpoch", Long.valueOf(jfrStats.recordingEnded().toEpochMilli()));
/*  71 */     root.addProperty("durationMs", Long.valueOf(jfrStats.recordingDuration().toMillis()));
/*  72 */     Duration worldCreationDuration = jfrStats.worldCreationDuration();
/*  73 */     if (worldCreationDuration != null)
/*  74 */       root.addProperty("worldGenDurationMs", Long.valueOf(worldCreationDuration.toMillis())); 
/*  76 */     root.add("heap", heap(jfrStats.heapSummary()));
/*  77 */     root.add("cpuPercent", cpu(jfrStats.cpuLoadStats()));
/*  78 */     root.add("network", network(jfrStats));
/*  79 */     root.add("fileIO", fileIO(jfrStats));
/*  80 */     root.add("fps", fps(jfrStats.fps()));
/*  81 */     root.add("serverTick", serverTicks(jfrStats.serverTickTimes()));
/*  82 */     root.add("threadAllocation", threadAllocations(jfrStats.threadAllocationSummary()));
/*  83 */     root.add("chunkGen", chunkGen(jfrStats.chunkGenSummary()));
/*  84 */     root.add("structureGen", structureGen(jfrStats.structureGenStats()));
/*  86 */     return this.gson.toJson((JsonElement)root);
/*     */   }
/*     */   
/*     */   private JsonElement heap(GcHeapStat.Summary heapSummary) {
/*  90 */     JsonObject json = new JsonObject();
/*  91 */     json.addProperty("allocationRateBytesPerSecond", Double.valueOf(heapSummary.allocationRateBytesPerSecond()));
/*  92 */     json.addProperty("gcCount", Integer.valueOf(heapSummary.totalGCs()));
/*  93 */     json.addProperty("gcOverHeadPercent", Float.valueOf(heapSummary.gcOverHead()));
/*  94 */     json.addProperty("gcTotalDurationMs", Long.valueOf(heapSummary.gcTotalDuration().toMillis()));
/*  95 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement structureGen(List<StructureGenStat> structureGenStats) {
/*  99 */     JsonObject root = new JsonObject();
/* 100 */     Optional<TimedStatSummary<StructureGenStat>> optionalSummary = TimedStatSummary.summary(structureGenStats);
/* 101 */     if (optionalSummary.isEmpty())
/* 102 */       return (JsonElement)root; 
/* 104 */     TimedStatSummary<StructureGenStat> summary = optionalSummary.get();
/* 106 */     JsonArray structureJsonArray = new JsonArray();
/* 107 */     root.add("structure", (JsonElement)structureJsonArray);
/* 109 */     ((Map)structureGenStats.stream()
/* 110 */       .collect(Collectors.groupingBy(StructureGenStat::structureName)))
/* 111 */       .forEach((structureName, timedStat) -> {
/*     */           Optional<TimedStatSummary<StructureGenStat>> optionalStatSummary = TimedStatSummary.summary(timedStat);
/*     */           if (optionalStatSummary.isEmpty())
/*     */             return; 
/*     */           TimedStatSummary<StructureGenStat> statSummary = optionalStatSummary.get();
/*     */           JsonObject structureJson = new JsonObject();
/*     */           structureJsonArray.add((JsonElement)structureJson);
/*     */           structureJson.addProperty("name", structureName);
/*     */           structureJson.addProperty("count", Integer.valueOf(statSummary.count()));
/*     */           structureJson.addProperty("durationNanosTotal", Long.valueOf(statSummary.totalDuration().toNanos()));
/*     */           structureJson.addProperty("durationNanosAvg", Long.valueOf(statSummary.totalDuration().toNanos() / statSummary.count()));
/*     */           JsonObject percentiles = (JsonObject)Util.make(new JsonObject(), ());
/*     */           statSummary.percentilesNanos().forEach(());
/*     */           Function<StructureGenStat, JsonElement> structureGenStatJsonGenerator = ();
/*     */           root.add("fastest", structureGenStatJsonGenerator.apply((StructureGenStat)summary.fastest()));
/*     */           root.add("slowest", structureGenStatJsonGenerator.apply((StructureGenStat)summary.slowest()));
/*     */           root.add("secondSlowest", (summary.secondSlowest() != null) ? structureGenStatJsonGenerator.apply((StructureGenStat)summary.secondSlowest()) : (JsonElement)JsonNull.INSTANCE);
/*     */         });
/* 146 */     return (JsonElement)root;
/*     */   }
/*     */   
/*     */   private JsonElement chunkGen(List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary) {
/* 150 */     JsonObject json = new JsonObject();
/* 151 */     if (chunkGenSummary.isEmpty())
/* 152 */       return (JsonElement)json; 
/* 154 */     json.addProperty("durationNanosTotal", Double.valueOf(chunkGenSummary.stream().mapToDouble(it -> ((TimedStatSummary)it.getSecond()).totalDuration().toNanos()).sum()));
/* 155 */     JsonArray chunkJsonArray = (JsonArray)Util.make(new JsonArray(), self -> json.add("status", (JsonElement)self));
/* 157 */     for (Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>> summaryByStatus : chunkGenSummary) {
/* 158 */       TimedStatSummary<ChunkGenStat> chunkStat = (TimedStatSummary<ChunkGenStat>)summaryByStatus.getSecond();
/* 159 */       Objects.requireNonNull(chunkJsonArray);
/* 159 */       JsonObject chunkStatusJson = (JsonObject)Util.make(new JsonObject(), chunkJsonArray::add);
/* 160 */       chunkStatusJson.addProperty("state", ((ChunkStatus)summaryByStatus.getFirst()).toString());
/* 161 */       chunkStatusJson.addProperty("count", Integer.valueOf(chunkStat.count()));
/* 162 */       chunkStatusJson.addProperty("durationNanosTotal", Long.valueOf(chunkStat.totalDuration().toNanos()));
/* 163 */       chunkStatusJson.addProperty("durationNanosAvg", Long.valueOf(chunkStat.totalDuration().toNanos() / chunkStat.count()));
/* 164 */       JsonObject percentiles = (JsonObject)Util.make(new JsonObject(), self -> chunkStatusJson.add("durationNanosPercentiles", (JsonElement)self));
/* 165 */       chunkStat.percentilesNanos().forEach((percentile, value) -> percentiles.addProperty("p" + percentile, value));
/* 167 */       Function<ChunkGenStat, JsonElement> chunkGenStatJsonGenerator = chunk -> {
/*     */           JsonObject chunkGenStatJson = new JsonObject();
/*     */           chunkGenStatJson.addProperty("durationNanos", Long.valueOf(chunk.duration().toNanos()));
/*     */           chunkGenStatJson.addProperty("level", chunk.level());
/*     */           chunkGenStatJson.addProperty("chunkPosX", Integer.valueOf(chunk.chunkPos().x()));
/*     */           chunkGenStatJson.addProperty("chunkPosZ", Integer.valueOf(chunk.chunkPos().z()));
/*     */           chunkGenStatJson.addProperty("worldPosX", Integer.valueOf(chunk.worldPos().x()));
/*     */           chunkGenStatJson.addProperty("worldPosZ", Integer.valueOf(chunk.worldPos().z()));
/*     */           return (JsonElement)chunkGenStatJson;
/*     */         };
/* 177 */       chunkStatusJson.add("fastest", chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.fastest()));
/* 178 */       chunkStatusJson.add("slowest", chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.slowest()));
/* 179 */       chunkStatusJson.add("secondSlowest", (chunkStat.secondSlowest() != null) ? 
/* 180 */           chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.secondSlowest()) : 
/* 181 */           (JsonElement)JsonNull.INSTANCE);
/*     */     } 
/* 184 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement threadAllocations(ThreadAllocationStat.Summary threadAllocationSummary) {
/* 188 */     JsonArray threads = new JsonArray();
/* 189 */     threadAllocationSummary.allocationsPerSecondByThread().forEach((threadName, bytesPerSecond) -> threads.add((JsonElement)Util.make(new JsonObject(), ())));
/* 195 */     return (JsonElement)threads;
/*     */   }
/*     */   
/*     */   private JsonElement serverTicks(List<TickTimeStat> tickTimeStats) {
/* 199 */     if (tickTimeStats.isEmpty())
/* 200 */       return (JsonElement)JsonNull.INSTANCE; 
/* 202 */     JsonObject json = new JsonObject();
/* 203 */     double[] tickTimesMs = tickTimeStats.stream().mapToDouble(tickTimeStat -> tickTimeStat.currentAverage().toNanos() / 1000000.0D).toArray();
/* 205 */     DoubleSummaryStatistics summary = DoubleStream.of(tickTimesMs).summaryStatistics();
/* 206 */     json.addProperty("minMs", Double.valueOf(summary.getMin()));
/* 207 */     json.addProperty("averageMs", Double.valueOf(summary.getAverage()));
/* 208 */     json.addProperty("maxMs", Double.valueOf(summary.getMax()));
/* 209 */     Map<Integer, Double> percentiles = Percentiles.evaluate(tickTimesMs);
/* 210 */     percentiles.forEach((percentile, value) -> json.addProperty("p" + percentile, value));
/* 211 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fps(List<FpsStat> fpsStats) {
/* 215 */     if (fpsStats.isEmpty())
/* 216 */       return (JsonElement)JsonNull.INSTANCE; 
/* 218 */     JsonObject json = new JsonObject();
/* 219 */     int[] fps = fpsStats.stream().mapToInt(FpsStat::fps).toArray();
/* 220 */     IntSummaryStatistics summary = IntStream.of(fps).summaryStatistics();
/* 221 */     json.addProperty("minFPS", Integer.valueOf(summary.getMin()));
/* 222 */     json.addProperty("averageFPS", Double.valueOf(summary.getAverage()));
/* 223 */     json.addProperty("maxFPS", Integer.valueOf(summary.getMax()));
/* 224 */     Map<Integer, Double> percentiles = Percentiles.evaluate(fps);
/* 225 */     percentiles.forEach((percentile, value) -> json.addProperty("p" + percentile, value));
/* 226 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fileIO(JfrStatsResult jfrStats) {
/* 230 */     JsonObject json = new JsonObject();
/* 231 */     json.add("write", fileIoSummary(jfrStats.fileWrites()));
/* 232 */     json.add("read", fileIoSummary(jfrStats.fileReads()));
/* 233 */     json.add("chunksRead", ioSummary(jfrStats.readChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 234 */     json.add("chunksWritten", ioSummary(jfrStats.writtenChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 235 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fileIoSummary(FileIOStat.Summary io) {
/* 239 */     JsonObject json = new JsonObject();
/* 240 */     json.addProperty("totalBytes", Long.valueOf(io.totalBytes()));
/* 241 */     json.addProperty("count", Long.valueOf(io.counts()));
/* 242 */     json.addProperty("bytesPerSecond", Double.valueOf(io.bytesPerSecond()));
/* 243 */     json.addProperty("countPerSecond", Double.valueOf(io.countsPerSecond()));
/* 244 */     JsonArray topContributors = new JsonArray();
/* 245 */     json.add("topContributors", (JsonElement)topContributors);
/* 246 */     io.topTenContributorsByTotalBytes().forEach(contributor -> {
/*     */           JsonObject contributorJson = new JsonObject();
/*     */           topContributors.add((JsonElement)contributorJson);
/*     */           contributorJson.addProperty("path", (String)contributor.getFirst());
/*     */           contributorJson.addProperty("totalBytes", (Number)contributor.getSecond());
/*     */         });
/* 252 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement network(JfrStatsResult jfrStats) {
/* 256 */     JsonObject json = new JsonObject();
/* 257 */     json.add("sent", ioSummary(jfrStats.sentPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 258 */     json.add("received", ioSummary(jfrStats.receivedPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 259 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private <T> JsonElement ioSummary(IoSummary<T> summary, BiConsumer<T, JsonObject> elementWriter) {
/* 263 */     JsonObject json = new JsonObject();
/* 264 */     json.addProperty("totalBytes", Long.valueOf(summary.getTotalSize()));
/* 265 */     json.addProperty("count", Long.valueOf(summary.getTotalCount()));
/* 266 */     json.addProperty("bytesPerSecond", Double.valueOf(summary.getSizePerSecond()));
/* 267 */     json.addProperty("countPerSecond", Double.valueOf(summary.getCountsPerSecond()));
/* 268 */     JsonArray topContributors = new JsonArray();
/* 269 */     json.add("topContributors", (JsonElement)topContributors);
/* 270 */     summary.largestSizeContributors().forEach(contributor -> {
/*     */           JsonObject contributorJson = new JsonObject();
/*     */           topContributors.add((JsonElement)contributorJson);
/*     */           T identifier = (T)contributor.getFirst();
/*     */           IoSummary.CountAndSize countAndSize = (IoSummary.CountAndSize)contributor.getSecond();
/*     */           elementWriter.accept(identifier, contributorJson);
/*     */           contributorJson.addProperty("totalBytes", Long.valueOf(countAndSize.totalSize()));
/*     */           contributorJson.addProperty("count", Long.valueOf(countAndSize.totalCount()));
/*     */           contributorJson.addProperty("averageSize", Float.valueOf(countAndSize.averageSize()));
/*     */         });
/* 280 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement cpu(List<CpuLoadStat> cpuStats) {
/* 284 */     JsonObject json = new JsonObject();
/* 285 */     BiFunction<List<CpuLoadStat>, ToDoubleFunction<CpuLoadStat>, JsonObject> transformer = (cpuLoadStats, extractor) -> {
/*     */         JsonObject jsonGroup = new JsonObject();
/*     */         DoubleSummaryStatistics stats = cpuLoadStats.stream().mapToDouble(extractor).summaryStatistics();
/*     */         jsonGroup.addProperty("min", Double.valueOf(stats.getMin()));
/*     */         jsonGroup.addProperty("average", Double.valueOf(stats.getAverage()));
/*     */         jsonGroup.addProperty("max", Double.valueOf(stats.getMax()));
/*     */         return jsonGroup;
/*     */       };
/* 294 */     json.add("jvm", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::jvm));
/* 295 */     json.add("userJvm", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::userJvm));
/* 296 */     json.add("system", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::system));
/* 298 */     return (JsonElement)json;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\serialize\JfrResultJsonSerializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */