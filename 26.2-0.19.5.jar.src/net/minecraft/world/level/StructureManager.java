/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.chunk.StructureAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheck;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ 
/*     */ public class StructureManager {
/*     */   private final LevelAccessor level;
/*     */   
/*     */   private final WorldOptions worldOptions;
/*     */   
/*     */   private final StructureCheck structureCheck;
/*     */   
/*     */   public StructureManager(LevelAccessor level, WorldOptions worldOptions, StructureCheck structureCheck) {
/*  36 */     this.level = level;
/*  37 */     this.worldOptions = worldOptions;
/*  38 */     this.structureCheck = structureCheck;
/*     */   }
/*     */   
/*     */   public StructureManager forWorldGenRegion(WorldGenRegion region) {
/*  43 */     if (region.getLevel() != this.level)
/*  44 */       throw new IllegalStateException("Using invalid structure manager (source level: " + String.valueOf(region.getLevel()) + ", region: " + String.valueOf(region)); 
/*  46 */     return new StructureManager((LevelAccessor)region, this.worldOptions, this.structureCheck);
/*     */   }
/*     */   
/*     */   public List<StructureStart> startsForStructure(ChunkPos pos, Predicate<Structure> matcher) {
/*  50 */     Map<Structure, LongSet> allReferences = this.level.getChunk(pos.x(), pos.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*  51 */     ImmutableList.Builder<StructureStart> result = ImmutableList.builder();
/*  53 */     for (Map.Entry<Structure, LongSet> entry : allReferences.entrySet()) {
/*  54 */       Structure structure = entry.getKey();
/*  55 */       if (matcher.test(structure)) {
/*  56 */         Objects.requireNonNull(result);
/*  56 */         fillStartsForStructure(structure, entry.getValue(), result::add);
/*     */       } 
/*     */     } 
/*  60 */     return (List<StructureStart>)result.build();
/*     */   }
/*     */   
/*     */   public List<StructureStart> startsForStructure(SectionPos pos, Structure structure) {
/*  66 */     LongSet referencesForStructure = this.level.getChunk(pos.x(), pos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
/*  67 */     ImmutableList.Builder<StructureStart> result = ImmutableList.builder();
/*  68 */     Objects.requireNonNull(result);
/*  68 */     fillStartsForStructure(structure, referencesForStructure, result::add);
/*  69 */     return (List<StructureStart>)result.build();
/*     */   }
/*     */   
/*     */   public void fillStartsForStructure(Structure structure, LongSet referencesForStructure, Consumer<StructureStart> consumer) {
/*  73 */     for (LongIterator<Long> longIterator = referencesForStructure.iterator(); longIterator.hasNext(); ) {
/*  73 */       long key = ((Long)longIterator.next()).longValue();
/*  74 */       SectionPos sectionPos = SectionPos.of(ChunkPos.unpack(key), this.level.getMinSectionY());
/*  75 */       StructureStart start = getStartForStructure(sectionPos, structure, (StructureAccess)this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS));
/*  76 */       if (start != null && start.isValid())
/*  77 */         consumer.accept(start); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public StructureStart getStartForStructure(SectionPos pos, Structure structure, StructureAccess chunk) {
/*  83 */     return chunk.getStartForStructure(structure);
/*     */   }
/*     */   
/*     */   public void setStartForStructure(SectionPos pos, Structure structure, StructureStart start, StructureAccess chunk) {
/*  87 */     chunk.setStartForStructure(structure, start);
/*     */   }
/*     */   
/*     */   public void addReferenceForStructure(SectionPos pos, Structure structure, long reference, StructureAccess chunk) {
/*  91 */     chunk.addReferenceForStructure(structure, reference);
/*     */   }
/*     */   
/*     */   public boolean shouldGenerateStructures() {
/*  95 */     return this.worldOptions.generateStructures();
/*     */   }
/*     */   
/*     */   public StructureStart getStructureAt(BlockPos blockPos, Structure structure) {
/* 100 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(blockPos), structure)) {
/* 101 */       if (structureStart.getBoundingBox().isInside((Vec3i)blockPos))
/* 102 */         return structureStart; 
/*     */     } 
/* 105 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, TagKey<Structure> structureTag) {
/* 109 */     return getStructureWithPieceAt(blockPos, structure -> structure.is(structureTag));
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, HolderSet<Structure> structures) {
/* 113 */     Objects.requireNonNull(structures);
/* 113 */     return getStructureWithPieceAt(blockPos, structures::contains);
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, Predicate<Holder<Structure>> predicate) {
/* 118 */     Registry<Structure> structures = registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 119 */     for (StructureStart structureStart : startsForStructure(ChunkPos.containing(blockPos), s -> {
/*     */           Objects.requireNonNull(predicate);
/*     */           return ((Boolean)structures.get(structures.getId(s)).map(predicate::test).orElse(Boolean.valueOf(false))).booleanValue();
/*     */         })) {
/* 120 */       if (structureHasPieceAt(blockPos, structureStart))
/* 121 */         return structureStart; 
/*     */     } 
/* 124 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, Structure structure) {
/* 129 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(blockPos), structure)) {
/* 130 */       if (structureHasPieceAt(blockPos, structureStart))
/* 131 */         return structureStart; 
/*     */     } 
/* 134 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public boolean structureHasPieceAt(BlockPos blockPos, StructureStart structureStart) {
/* 138 */     for (StructurePiece piece : structureStart.getPieces()) {
/* 139 */       if (piece.getBoundingBox().isInside((Vec3i)blockPos))
/* 140 */         return true; 
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasAnyStructureAt(BlockPos pos) {
/* 147 */     SectionPos sectionPos = SectionPos.of(pos);
/* 148 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).hasAnyStructureReferences();
/*     */   }
/*     */   
/*     */   public Map<Structure, LongSet> getAllStructuresAt(BlockPos pos) {
/* 152 */     SectionPos sectionPos = SectionPos.of(pos);
/* 153 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*     */   }
/*     */   
/*     */   public StructureCheckResult checkStructurePresence(ChunkPos pos, Structure structure, StructurePlacement placement, boolean createReference) {
/* 157 */     return this.structureCheck.checkStart(pos, structure, placement, createReference);
/*     */   }
/*     */   
/*     */   public void addReference(StructureStart start) {
/* 161 */     start.addReference();
/* 162 */     this.structureCheck.incrementReference(start.getChunkPos(), start.getStructure());
/*     */   }
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 166 */     return this.level.registryAccess();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\StructureManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */