/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SavedDataStorage implements AutoCloseable {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  45 */   private final Map<SavedDataType<?>, Optional<SavedData>> cache = new HashMap<>();
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   private final Path dataFolder;
/*     */   
/*  50 */   private CompletableFuture<?> pendingWriteFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   public SavedDataStorage(Path dataFolder, DataFixer fixerUpper, HolderLookup.Provider registries) {
/*  54 */     this.fixerUpper = fixerUpper;
/*  55 */     this.dataFolder = dataFolder;
/*  56 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   private Path getDataFile(Identifier id) {
/*  60 */     Path path = id.withSuffix(".dat").resolveAgainst(this.dataFolder);
/*  61 */     if (!path.toAbsolutePath().startsWith(this.dataFolder.toAbsolutePath()))
/*  62 */       throw new IllegalArgumentException("SavedDataStorage attempted file access outside of data directory: {}" + String.valueOf(path)); 
/*  64 */     return path;
/*     */   }
/*     */   
/*     */   public <T extends SavedData> T computeIfAbsent(SavedDataType<T> type) {
/*  68 */     T data = get(type);
/*  69 */     if (data != null)
/*  70 */       return data; 
/*  72 */     SavedData savedData = type.constructor().get();
/*  73 */     set(type, (T)savedData);
/*  74 */     return (T)savedData;
/*     */   }
/*     */   
/*     */   public <T extends SavedData> T get(SavedDataType<T> type) {
/*  79 */     Optional<SavedData> data = this.cache.get(type);
/*  80 */     if (data == null) {
/*  81 */       data = Optional.ofNullable(readSavedData(type));
/*  82 */       this.cache.put(type, data);
/*     */     } 
/*  84 */     return (T)data.orElse(null);
/*     */   }
/*     */   
/*     */   private <T extends SavedData> T readSavedData(SavedDataType<T> type) {
/*     */     try {
/*  89 */       Path file = getDataFile(type.id());
/*  90 */       if (Files.exists(file, new java.nio.file.LinkOption[0])) {
/*  91 */         CompoundTag tag = readTagFromDisk(file, type.dataFixType(), SharedConstants.getCurrentVersion().dataVersion().version());
/*  92 */         RegistryOps<Tag> ops = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  93 */         return (T)type.codec().parse((DynamicOps)ops, tag.get("data"))
/*  94 */           .resultOrPartial(error -> LOGGER.error("Failed to parse saved data for '{}': {}", type, error))
/*  95 */           .orElse(null);
/*     */       } 
/*  97 */     } catch (Exception e) {
/*  98 */       LOGGER.error("Error loading saved data: {}", type, e);
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends SavedData> void set(SavedDataType<T> type, T data) {
/* 104 */     this.cache.put(type, Optional.of((SavedData)data));
/* 105 */     data.setDirty();
/*     */   }
/*     */   
/*     */   public CompoundTag readTagFromDisk(Path dataFile, DataFixTypes type, int newVersion) throws IOException {
/* 110 */     InputStream in = Files.newInputStream(dataFile, new java.nio.file.OpenOption[0]);
/*     */     try {
/* 111 */       PushbackInputStream inputStream = new PushbackInputStream((InputStream)new FastBufferedInputStream(in), 2);
/*     */       try {
/*     */         CompoundTag tag;
/* 114 */         if (isGzip(inputStream)) {
/* 115 */           tag = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
/*     */         } else {
/* 117 */           DataInputStream dis = new DataInputStream(inputStream);
/*     */           try {
/* 118 */             tag = NbtIo.read(dis);
/* 119 */             dis.close();
/*     */           } catch (Throwable throwable) {
/*     */             try {
/*     */               dis.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             } 
/*     */             throw throwable;
/*     */           } 
/*     */         } 
/* 122 */         int version = NbtUtils.getDataVersion(tag, 1343);
/* 123 */         CompoundTag compoundTag1 = type.update(this.fixerUpper, tag, version, newVersion);
/* 124 */         inputStream.close();
/* 124 */         if (in != null)
/* 124 */           in.close(); 
/*     */         return compoundTag1;
/*     */       } catch (Throwable tag) {
/*     */         try {
/*     */           inputStream.close();
/*     */         } catch (Throwable throwable) {
/*     */           tag.addSuppressed(throwable);
/*     */         } 
/*     */         throw tag;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (in != null)
/*     */         try {
/*     */           in.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isGzip(PushbackInputStream inputStream) throws IOException {
/* 128 */     byte[] header = new byte[2];
/* 129 */     boolean gzip = false;
/* 130 */     int read = inputStream.read(header, 0, 2);
/* 131 */     if (read == 2) {
/* 132 */       int fullHeader = (header[1] & 0xFF) << 8 | header[0] & 0xFF;
/* 133 */       if (fullHeader == 35615)
/* 134 */         gzip = true; 
/*     */     } 
/* 137 */     if (read != 0)
/* 138 */       inputStream.unread(header, 0, read); 
/* 140 */     return gzip;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> scheduleSave() {
/* 144 */     if (this.closed)
/* 145 */       throw new IllegalStateException("Trying to schedule save when SavedDataStorage is already closed"); 
/* 148 */     Map<SavedDataType<?>, CompoundTag> tagsToSave = collectDirtyTagsToSave();
/* 149 */     if (tagsToSave.isEmpty())
/* 150 */       return CompletableFuture.completedFuture(null); 
/* 153 */     int threads = Util.maxAllowedExecutorThreads();
/* 154 */     int taskCount = tagsToSave.size();
/* 157 */     if (taskCount > threads) {
/* 159 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(ignored -> {
/*     */             List<CompletableFuture<?>> tasks = new ArrayList<>(threads);
/*     */             int bucketSize = Mth.positiveCeilDiv(taskCount, threads);
/*     */             for (List<Map.Entry<SavedDataType<?>, CompoundTag>> entries : (Iterable<List<Map.Entry<SavedDataType<?>, CompoundTag>>>)Iterables.partition(tagsToSave.entrySet(), bucketSize))
/*     */               tasks.add(CompletableFuture.runAsync((), (Executor)Util.ioPool())); 
/*     */             return CompletableFuture.allOf((CompletableFuture<?>[])tasks.toArray(()));
/*     */           });
/*     */     } else {
/* 172 */       this.pendingWriteFuture = this.pendingWriteFuture.thenCompose(ignored -> CompletableFuture.allOf((CompletableFuture<?>[])tagsToSave.entrySet().stream().map(()).toArray(())));
/*     */     } 
/* 179 */     return this.pendingWriteFuture;
/*     */   }
/*     */   
/*     */   private Map<SavedDataType<?>, CompoundTag> collectDirtyTagsToSave() {
/* 183 */     Object2ObjectArrayMap object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 184 */     RegistryOps<Tag> ops = this.registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 185 */     this.cache.forEach((type, optional) -> optional.filter(SavedData::isDirty).ifPresent(()));
/* 191 */     return (Map<SavedDataType<?>, CompoundTag>)object2ObjectArrayMap;
/*     */   }
/*     */   
/*     */   private <T extends SavedData> CompoundTag encodeUnchecked(SavedDataType<T> type, SavedData data, RegistryOps<Tag> ops) {
/* 196 */     Codec<T> codec = type.codec();
/* 197 */     CompoundTag tag = new CompoundTag();
/* 198 */     tag.put("data", (Tag)codec.encodeStart((DynamicOps)ops, data).getOrThrow());
/* 199 */     NbtUtils.addCurrentDataVersion(tag);
/* 200 */     return tag;
/*     */   }
/*     */   
/*     */   private void tryWrite(SavedDataType<?> type, CompoundTag tag) {
/* 204 */     Path path = getDataFile(type.id());
/*     */     try {
/* 206 */       FileUtil.createDirectoriesSafe(path.getParent());
/* 207 */       NbtIo.writeCompressed(tag, path);
/* 208 */     } catch (IOException e) {
/* 209 */       LOGGER.error("Could not save data to {}", path.getFileName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveAndJoin() {
/* 214 */     scheduleSave().join();
/*     */   }
/*     */   
/*     */   public void close() {
/* 219 */     if (this.closed)
/* 220 */       throw new IllegalStateException("Trying to close SavedDataStorage when it is already closed"); 
/* 223 */     saveAndJoin();
/* 224 */     this.closed = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\SavedDataStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */