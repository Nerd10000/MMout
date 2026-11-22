/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class SinglePoolElement extends StructurePoolElement {
/*  42 */   private static final Comparator<StructureTemplate.JigsawBlockInfo> HIGHEST_SELECTION_PRIORITY_FIRST = Comparator.<StructureTemplate.JigsawBlockInfo>comparingInt(StructureTemplate.JigsawBlockInfo::selectionPriority).reversed();
/*     */   
/*     */   private static <T> DataResult<T> encodeTemplate(Either<Identifier, StructureTemplate> template, DynamicOps<T> ops, T prefix) {
/*  45 */     Optional<Identifier> location = template.left();
/*  46 */     if (location.isEmpty())
/*  47 */       return DataResult.error(() -> "Can not serialize a runtime pool element"); 
/*  49 */     return Identifier.CODEC.encode(location.get(), ops, prefix);
/*     */   }
/*     */   
/*  52 */   private static final Codec<Either<Identifier, StructureTemplate>> TEMPLATE_CODEC = Codec.of(SinglePoolElement::encodeTemplate, Identifier.CODEC
/*     */       
/*  54 */       .map(Either::left));
/*     */   
/*     */   public static final MapCodec<SinglePoolElement> CODEC;
/*     */   
/*     */   protected final Either<Identifier, StructureTemplate> template;
/*     */   
/*     */   protected final Holder<StructureProcessorList> processors;
/*     */   
/*     */   protected final Optional<LiquidSettings> overrideLiquidSettings;
/*     */   
/*     */   static {
/*  57 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec(), (App)overrideLiquidSettingsCodec()).apply((Applicative)i, SinglePoolElement::new));
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Holder<StructureProcessorList>> processorsCodec() {
/*  65 */     return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(t -> t.processors);
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Optional<LiquidSettings>> overrideLiquidSettingsCodec() {
/*  69 */     return LiquidSettings.CODEC.optionalFieldOf("override_liquid_settings").forGetter(t -> t.overrideLiquidSettings);
/*     */   }
/*     */   
/*     */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Either<Identifier, StructureTemplate>> templateCodec() {
/*  73 */     return TEMPLATE_CODEC.fieldOf("location").forGetter(t -> t.template);
/*     */   }
/*     */   
/*     */   protected SinglePoolElement(Either<Identifier, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> overrideLiquidSettings) {
/*  81 */     super(projection);
/*  82 */     this.template = template;
/*  83 */     this.processors = processors;
/*  84 */     this.overrideLiquidSettings = overrideLiquidSettings;
/*     */   }
/*     */   
/*     */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
/*  89 */     StructureTemplate template = getTemplate(structureTemplateManager);
/*  90 */     return template.getSize(rotation);
/*     */   }
/*     */   
/*     */   private StructureTemplate getTemplate(StructureTemplateManager structureTemplateManager) {
/*  94 */     Objects.requireNonNull(structureTemplateManager);
/*  94 */     return (StructureTemplate)this.template.map(structureTemplateManager::getOrCreate, Function.identity());
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, boolean absolute) {
/*  98 */     StructureTemplate template = getTemplate(structureTemplateManager);
/*  99 */     ObjectArrayList objectArrayList = template.filterBlocks(position, (new StructurePlaceSettings()).setRotation(rotation), Blocks.STRUCTURE_BLOCK, absolute);
/* 100 */     List<StructureTemplate.StructureBlockInfo> dataMarkers = Lists.newArrayList();
/* 101 */     for (StructureTemplate.StructureBlockInfo info : objectArrayList) {
/* 102 */       CompoundTag nbt = info.nbt();
/* 103 */       if (nbt == null)
/*     */         continue; 
/* 107 */       StructureMode mode = nbt.read("mode", StructureMode.LEGACY_CODEC).orElseThrow();
/* 108 */       if (mode != StructureMode.DATA)
/*     */         continue; 
/* 112 */       dataMarkers.add(info);
/*     */     } 
/* 115 */     return dataMarkers;
/*     */   }
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
/* 120 */     List<StructureTemplate.JigsawBlockInfo> jigsaws = getTemplate(structureTemplateManager).getJigsaws(position, rotation);
/* 121 */     Util.shuffle(jigsaws, random);
/* 122 */     sortBySelectionPriority(jigsaws);
/* 123 */     return jigsaws;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void sortBySelectionPriority(List<StructureTemplate.JigsawBlockInfo> blocks) {
/* 129 */     blocks.sort(HIGHEST_SELECTION_PRIORITY_FIRST);
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
/* 134 */     StructureTemplate template = getTemplate(structureTemplateManager);
/* 135 */     return template.getBoundingBox((new StructurePlaceSettings()).setRotation(rotation), position);
/*     */   }
/*     */   
/*     */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 140 */     StructureTemplate template = getTemplate(structureTemplateManager);
/* 141 */     StructurePlaceSettings settings = getSettings(rotation, chunkBB, liquidSettings, keepJigsaws);
/* 143 */     if (template.placeInWorld((ServerLevelAccessor)level, position, referencePos, settings, random, 18)) {
/* 144 */       List<StructureTemplate.StructureBlockInfo> dataMarkers = StructureTemplate.processBlockInfos((ServerLevelAccessor)level, position, referencePos, settings, getDataMarkers(structureTemplateManager, position, rotation, false));
/* 145 */       for (StructureTemplate.StructureBlockInfo dataMarker : dataMarkers)
/* 146 */         handleDataMarker((LevelAccessor)level, dataMarker, position, rotation, random, chunkBB); 
/* 149 */       return true;
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox chunkBB, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 155 */     StructurePlaceSettings settings = new StructurePlaceSettings();
/* 156 */     settings.setBoundingBox(chunkBB);
/* 157 */     settings.setRotation(rotation);
/* 158 */     settings.setKnownShape(true);
/* 159 */     settings.setIgnoreEntities(false);
/* 160 */     settings.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 161 */     settings.setFinalizeEntities(true);
/* 162 */     settings.setLiquidSettings(this.overrideLiquidSettings.orElse(liquidSettings));
/* 163 */     if (!keepJigsaws)
/* 164 */       settings.addProcessor((StructureProcessor)JigsawReplacementProcessor.INSTANCE); 
/* 166 */     Objects.requireNonNull(settings);
/* 166 */     ((StructureProcessorList)this.processors.value()).list().forEach(settings::addProcessor);
/* 167 */     Objects.requireNonNull(settings);
/* 167 */     getProjection().getProcessors().forEach(settings::addProcessor);
/* 168 */     return settings;
/*     */   }
/*     */   
/*     */   public StructurePoolElementType<?> getType() {
/* 173 */     return StructurePoolElementType.SINGLE;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 178 */     return "Single[" + String.valueOf(this.template) + "]";
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Identifier getTemplateLocation() {
/* 183 */     return (Identifier)this.template.orThrow();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\SinglePoolElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */