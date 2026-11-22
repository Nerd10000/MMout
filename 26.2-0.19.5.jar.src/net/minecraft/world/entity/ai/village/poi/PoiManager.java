/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.SectionTracker;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.debug.DebugPoiInfo;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SectionStorage;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ 
/*     */ public class PoiManager extends SectionStorage<PoiSection, PoiSection.Packed> {
/*     */   public static final int MAX_VILLAGE_DISTANCE = 6;
/*     */   
/*     */   public static final int VILLAGE_SECTION_SIZE = 1;
/*     */   
/*     */   private final DistanceTracker distanceTracker;
/*     */   
/*  50 */   private final LongSet loadedChunks = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   public PoiManager(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean sync, RegistryAccess registryAccess, ChunkIOErrorReporter errorReporter, LevelHeightAccessor levelHeightAccessor) {
/*  53 */     super(new SimpleRegionStorage(info, folder, fixerUpper, sync, DataFixTypes.POI_CHUNK), PoiSection.Packed.CODEC, PoiSection::pack, PoiSection.Packed::unpack, PoiSection::new, registryAccess, errorReporter, levelHeightAccessor);
/*  54 */     this.distanceTracker = new DistanceTracker();
/*     */   }
/*     */   
/*     */   public PoiRecord add(BlockPos pos, Holder<PoiType> type) {
/*  58 */     return ((PoiSection)getOrCreate(SectionPos.asLong(pos))).add(pos, type);
/*     */   }
/*     */   
/*     */   public void remove(BlockPos pos) {
/*  62 */     getOrLoad(SectionPos.asLong(pos)).ifPresent(poiSection -> poiSection.remove(pos));
/*     */   }
/*     */   
/*     */   public long getCountInRange(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/*  66 */     return getInRange(predicate, center, radius, occupancy).count();
/*     */   }
/*     */   
/*     */   public boolean existsAtPosition(ResourceKey<PoiType> poiType, BlockPos blockPos) {
/*  70 */     return exists(blockPos, p -> p.is(poiType));
/*     */   }
/*     */   
/*     */   public Stream<PoiRecord> getInSquare(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/*  77 */     int chunkRadius = Math.floorDiv(radius, 16) + 1;
/*  79 */     return ChunkPos.rangeClosed(ChunkPos.containing(center), chunkRadius).flatMap(pos -> getInChunk(predicate, pos, occupancy))
/*  80 */       .filter(record -> {
/*     */           BlockPos pos = record.getPos();
/*  82 */           return (Math.abs(pos.getX() - center.getX()) <= radius && Math.abs(pos.getZ() - center.getZ()) <= radius);
/*     */         });
/*     */   }
/*     */   
/*     */   public Stream<PoiRecord> getInRange(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/*  88 */     int radiusSqr = radius * radius;
/*  89 */     return getInSquare(predicate, center, radius, occupancy).filter(r -> (r.getPos().distSqr((Vec3i)center) <= radiusSqr));
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public Stream<PoiRecord> getInChunk(Predicate<Holder<PoiType>> predicate, ChunkPos chunkPos, Occupancy occupancy) {
/*  94 */     return IntStream.rangeClosed(this.levelHeightAccessor.getMinSectionY(), this.levelHeightAccessor.getMaxSectionY()).boxed()
/*  95 */       .map(sectionY -> getOrLoad(SectionPos.of(chunkPos, sectionY.intValue()).asLong()))
/*  96 */       .filter(Optional::isPresent)
/*  97 */       .flatMap(poiSection -> ((PoiSection)poiSection.get()).getRecords(predicate, occupancy));
/*     */   }
/*     */   
/*     */   public Stream<BlockPos> findAll(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 101 */     return getInRange(predicate, center, radius, occupancy)
/* 102 */       .<BlockPos>map(PoiRecord::getPos)
/* 103 */       .filter(filter);
/*     */   }
/*     */   
/*     */   public Stream<Pair<Holder<PoiType>, BlockPos>> findAllWithType(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 107 */     return getInRange(predicate, center, radius, occupancy)
/* 108 */       .filter(p -> filter.test(p.getPos()))
/* 109 */       .map(p -> Pair.of(p.getPoiType(), p.getPos()));
/*     */   }
/*     */   
/*     */   public Stream<Pair<Holder<PoiType>, BlockPos>> findAllClosestFirstWithType(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 113 */     return findAllWithType(predicate, filter, center, radius, occupancy)
/* 114 */       .sorted(Comparator.comparingDouble(p -> ((BlockPos)p.getSecond()).distSqr((Vec3i)center)));
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> find(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 118 */     return findAll(predicate, filter, center, radius, occupancy).findFirst();
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/* 122 */     return getInRange(predicate, center, radius, occupancy)
/* 123 */       .<BlockPos>map(PoiRecord::getPos)
/* 124 */       .min(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)center)));
/*     */   }
/*     */   
/*     */   public Optional<Pair<Holder<PoiType>, BlockPos>> findClosestWithType(Predicate<Holder<PoiType>> predicate, BlockPos center, int radius, Occupancy occupancy) {
/* 128 */     return getInRange(predicate, center, radius, occupancy)
/* 129 */       .min(Comparator.comparingDouble(r -> r.getPos().distSqr((Vec3i)center)))
/* 130 */       .map(p -> Pair.of(p.getPoiType(), p.getPos()));
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, BlockPos center, int radius, Occupancy occupancy) {
/* 134 */     return getInRange(predicate, center, radius, occupancy)
/* 135 */       .<BlockPos>map(PoiRecord::getPos)
/* 136 */       .filter(filter)
/* 137 */       .min(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)center)));
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> take(Predicate<Holder<PoiType>> predicate, BiPredicate<Holder<PoiType>, BlockPos> filter, BlockPos center, int radius) {
/* 141 */     return getInRange(predicate, center, radius, Occupancy.HAS_SPACE)
/* 142 */       .filter(poi -> filter.test(poi.getPoiType(), poi.getPos()))
/* 143 */       .findFirst()
/* 144 */       .map(r -> {
/*     */           r.acquireTicket();
/*     */           return r.getPos();
/*     */         });
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> getRandom(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> filter, Occupancy occupancy, BlockPos center, int radius, RandomSource random) {
/* 151 */     List<PoiRecord> collect = Util.toShuffledList(getInRange(predicate, center, radius, occupancy), random);
/* 152 */     return collect.stream().filter(poi -> filter.test(poi.getPos())).findFirst().map(PoiRecord::getPos);
/*     */   }
/*     */   
/*     */   public boolean release(BlockPos pos) {
/* 156 */     return ((Boolean)getOrLoad(SectionPos.asLong(pos))
/* 157 */       .map(section -> Boolean.valueOf(section.release(pos)))
/* 158 */       .orElseThrow(() -> (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI never registered at " + String.valueOf(pos))))).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean exists(BlockPos pos, Predicate<Holder<PoiType>> predicate) {
/* 162 */     return ((Boolean)getOrLoad(SectionPos.asLong(pos)).map(s -> Boolean.valueOf(s.exists(pos, predicate))).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public Optional<Holder<PoiType>> getType(BlockPos pos) {
/* 166 */     return getOrLoad(SectionPos.asLong(pos)).flatMap(section -> section.getType(pos));
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public DebugPoiInfo getDebugPoiInfo(BlockPos pos) {
/* 171 */     return getOrLoad(SectionPos.asLong(pos)).flatMap(section -> section.getDebugPoiInfo(pos)).orElse(null);
/*     */   }
/*     */   
/*     */   public int sectionsToVillage(SectionPos sectionPos) {
/* 178 */     this.distanceTracker.runAllUpdates();
/* 179 */     return this.distanceTracker.getLevel(sectionPos.asLong());
/*     */   }
/*     */   
/*     */   private boolean isVillageCenter(long sectionPos) {
/* 183 */     Optional<PoiSection> section = get(sectionPos);
/* 184 */     if (section == null)
/* 185 */       return false; 
/* 188 */     return ((Boolean)section.<Boolean>map(s -> Boolean.valueOf(s.getRecords((), Occupancy.IS_OCCUPIED).findAny().isPresent())).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public void tick(BooleanSupplier haveTime) {
/* 193 */     super.tick(haveTime);
/* 194 */     this.distanceTracker.runAllUpdates();
/*     */   }
/*     */   
/*     */   protected void setDirty(long sectionPos) {
/* 199 */     super.setDirty(sectionPos);
/* 200 */     this.distanceTracker.update(sectionPos, this.distanceTracker.getLevelFromSource(sectionPos), false);
/*     */   }
/*     */   
/*     */   protected void onSectionLoad(long sectionPos) {
/* 205 */     this.distanceTracker.update(sectionPos, this.distanceTracker.getLevelFromSource(sectionPos), false);
/*     */   }
/*     */   
/*     */   public void checkConsistencyWithBlocks(SectionPos sectionPos, LevelChunkSection blockSection) {
/* 209 */     Util.ifElse(getOrLoad(sectionPos.asLong()), section -> section.refresh(()), () -> {
/*     */           if (mayHavePoi(blockSection)) {
/*     */             PoiSection newSection = (PoiSection)getOrCreate(sectionPos.asLong());
/*     */             Objects.requireNonNull(newSection);
/*     */             updateFromSection(blockSection, sectionPos, newSection::add);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean mayHavePoi(LevelChunkSection blockSection) {
/* 227 */     return blockSection.maybeHas(PoiTypes::hasPoi);
/*     */   }
/*     */   
/*     */   private void updateFromSection(LevelChunkSection blockSection, SectionPos pos, BiConsumer<BlockPos, Holder<PoiType>> output) {
/* 231 */     pos.blocksInside().forEach(blockPos -> {
/*     */           BlockState state = blockSection.getBlockState(SectionPos.sectionRelative(blockPos.getX()), SectionPos.sectionRelative(blockPos.getY()), SectionPos.sectionRelative(blockPos.getZ()));
/*     */           PoiTypes.forState(state).ifPresent(());
/*     */         });
/*     */   }
/*     */   
/*     */   public void ensureLoadedAndValid(LevelReader reader, BlockPos center, int radius) {
/* 242 */     SectionPos.aroundChunk(ChunkPos.containing(center), Math.floorDiv(radius, 16), this.levelHeightAccessor.getMinSectionY(), this.levelHeightAccessor.getMaxSectionY())
/* 243 */       .map(pos -> Pair.of(pos, getOrLoad(pos.asLong())))
/* 244 */       .filter(poiSection -> !((Boolean)((Optional)poiSection.getSecond()).map(PoiSection::isValid).orElse(Boolean.valueOf(false))).booleanValue())
/* 245 */       .map(p -> ((SectionPos)p.getFirst()).chunk())
/* 246 */       .filter(pos -> this.loadedChunks.add(pos.pack()))
/* 247 */       .forEach(pos -> reader.getChunk(pos.x(), pos.z(), ChunkStatus.EMPTY));
/*     */   }
/*     */   
/*     */   public enum Occupancy {
/* 251 */     HAS_SPACE((String)PoiRecord::hasSpace),
/* 252 */     IS_OCCUPIED((String)PoiRecord::isOccupied),
/* 253 */     ANY((String)(poiRecord -> true));
/*     */     
/*     */     private final Predicate<? super PoiRecord> test;
/*     */     
/*     */     Occupancy(Predicate<? super PoiRecord> test) {
/* 258 */       this.test = test;
/*     */     }
/*     */     
/*     */     public Predicate<? super PoiRecord> getTest() {
/* 262 */       return this.test;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class DistanceTracker extends SectionTracker {
/*     */     private final Long2ByteMap levels;
/*     */     
/*     */     DistanceTracker() {
/* 271 */       super(7, 16, 256);
/* 272 */       this.levels = (Long2ByteMap)new Long2ByteOpenHashMap();
/* 273 */       this.levels.defaultReturnValue((byte)7);
/*     */     }
/*     */     
/*     */     protected int getLevelFromSource(long to) {
/* 278 */       return PoiManager.this.isVillageCenter(to) ? 0 : 7;
/*     */     }
/*     */     
/*     */     protected int getLevel(long node) {
/* 283 */       return this.levels.get(node);
/*     */     }
/*     */     
/*     */     protected void setLevel(long node, int level) {
/* 288 */       if (level > 6) {
/* 289 */         this.levels.remove(node);
/*     */       } else {
/* 291 */         this.levels.put(node, (byte)level);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void runAllUpdates() {
/* 296 */       runUpdates(2147483647);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\village\poi\PoiManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */