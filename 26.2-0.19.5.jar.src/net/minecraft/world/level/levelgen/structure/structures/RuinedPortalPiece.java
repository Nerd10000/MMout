/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeavesBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlackstoneReplaceProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockAgeProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LavaSubmergedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class RuinedPortalPiece extends TemplateStructurePiece {
/*     */   private static final float PROBABILITY_OF_GOLD_GONE = 0.3F;
/*     */   
/*     */   private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_NETHERRACK = 0.07F;
/*     */   
/*     */   private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_LAVA = 0.2F;
/*     */   
/*     */   private final VerticalPlacement verticalPlacement;
/*     */   
/*     */   private final Properties properties;
/*     */   
/*     */   public static final class Properties extends Record {
/*     */     private final boolean cold;
/*     */     
/*     */     private final float mossiness;
/*     */     
/*     */     private final boolean airPocket;
/*     */     
/*     */     private final boolean overgrown;
/*     */     
/*     */     private final boolean vines;
/*     */     
/*     */     private final boolean replaceWithBlackstone;
/*     */     
/*     */     public static final Codec<Properties> CODEC;
/*     */     
/*     */     public Properties(boolean cold, float mossiness, boolean airPocket, boolean overgrown, boolean vines, boolean replaceWithBlackstone) {
/*  63 */       this.cold = cold;
/*  63 */       this.mossiness = mossiness;
/*  63 */       this.airPocket = airPocket;
/*  63 */       this.overgrown = overgrown;
/*  63 */       this.vines = vines;
/*  63 */       this.replaceWithBlackstone = replaceWithBlackstone;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #63	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public boolean cold() {
/*  63 */       return this.cold;
/*     */     }
/*     */     
/*     */     public float mossiness() {
/*  63 */       return this.mossiness;
/*     */     }
/*     */     
/*     */     public boolean airPocket() {
/*  63 */       return this.airPocket;
/*     */     }
/*     */     
/*     */     public boolean overgrown() {
/*  63 */       return this.overgrown;
/*     */     }
/*     */     
/*     */     public boolean vines() {
/*  63 */       return this.vines;
/*     */     }
/*     */     
/*     */     public boolean replaceWithBlackstone() {
/*  63 */       return this.replaceWithBlackstone;
/*     */     }
/*     */     
/*     */     static {
/*  71 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("cold").forGetter(Properties::cold), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(Properties::mossiness), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(Properties::airPocket), (App)Codec.BOOL.fieldOf("overgrown").forGetter(Properties::overgrown), (App)Codec.BOOL.fieldOf("vines").forGetter(Properties::vines), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(Properties::replaceWithBlackstone)).apply((Applicative)i, Properties::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public RuinedPortalPiece(HolderLookup.Provider registries, StructureTemplateManager structureTemplateManager, BlockPos templatePosition, VerticalPlacement verticalPlacement, Properties properties, Identifier templateLocation, StructureTemplate template, Rotation rotation, Mirror mirror, BlockPos pivot) {
/*  82 */     super(StructurePieceType.RUINED_PORTAL, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(registries, mirror, rotation, verticalPlacement, pivot, properties), templatePosition);
/*  84 */     this.verticalPlacement = verticalPlacement;
/*  85 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public RuinedPortalPiece(StructurePieceSerializationContext context, CompoundTag tag) {
/*  89 */     super(StructurePieceType.RUINED_PORTAL, tag, context.structureTemplateManager(), location -> makeSettings((HolderLookup.Provider)context.registryAccess(), context.structureTemplateManager(), tag, location));
/*  91 */     this.verticalPlacement = tag.read("VerticalPlacement", VerticalPlacement.CODEC).orElseThrow();
/*  92 */     this.properties = tag.read("Properties", Properties.CODEC).orElseThrow();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  97 */     super.addAdditionalSaveData(context, tag);
/*  98 */     tag.store("Rotation", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*  99 */     tag.store("Mirror", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/* 100 */     tag.store("VerticalPlacement", VerticalPlacement.CODEC, this.verticalPlacement);
/* 101 */     tag.store("Properties", Properties.CODEC, this.properties);
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(HolderLookup.Provider registries, StructureTemplateManager structureTemplateManager, CompoundTag tag, Identifier location) {
/* 105 */     StructureTemplate template = structureTemplateManager.getOrCreate(location);
/* 107 */     BlockPos pivot = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
/* 108 */     return makeSettings(registries, tag
/* 109 */         .read("Mirror", Mirror.LEGACY_CODEC).orElseThrow(), tag
/* 110 */         .read("Rotation", Rotation.LEGACY_CODEC).orElseThrow(), tag
/* 111 */         .read("VerticalPlacement", VerticalPlacement.CODEC).orElseThrow(), pivot, (Properties)Properties.CODEC
/*     */         
/* 113 */         .parse(new Dynamic((DynamicOps)NbtOps.INSTANCE, tag.get("Properties"))).getPartialOrThrow());
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(HolderLookup.Provider registries, Mirror mirror, Rotation rotation, VerticalPlacement verticalPlacement, BlockPos pivot, Properties properties) {
/* 118 */     HolderLookup.RegistryLookup<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);
/* 119 */     BlockIgnoreProcessor ignoreProcessor = properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
/* 121 */     List<ProcessorRule> rules = Lists.newArrayList();
/* 122 */     rules.add(getBlockReplaceRule(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
/* 123 */     rules.add(getLavaProcessorRule(verticalPlacement, properties));
/* 124 */     if (!properties.cold)
/* 125 */       rules.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07F, Blocks.MAGMA_BLOCK)); 
/* 136 */     StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation).setMirror(mirror).setRotationPivot(pivot).addProcessor((StructureProcessor)ignoreProcessor).addProcessor((StructureProcessor)new RuleProcessor(rules)).addProcessor((StructureProcessor)new BlockAgeProcessor(properties.mossiness)).addProcessor((StructureProcessor)new ProtectedBlockProcessor((HolderSet)blocks.getOrThrow(BlockTags.FEATURES_CANNOT_REPLACE))).addProcessor((StructureProcessor)new LavaSubmergedBlockProcessor());
/* 138 */     if (properties.replaceWithBlackstone)
/* 139 */       settings.addProcessor((StructureProcessor)BlackstoneReplaceProcessor.INSTANCE); 
/* 141 */     return settings;
/*     */   }
/*     */   
/*     */   private static ProcessorRule getLavaProcessorRule(VerticalPlacement verticalPlacement, Properties properties) {
/* 145 */     if (verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR)
/* 146 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.MAGMA_BLOCK); 
/* 147 */     if (properties.cold)
/* 148 */       return getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK); 
/* 150 */     return getBlockReplaceRule(Blocks.LAVA, 0.2F, Blocks.MAGMA_BLOCK);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 157 */     BoundingBox boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 158 */     if (!chunkBB.isInside((Vec3i)boundingBox.getCenter()))
/*     */       return; 
/* 162 */     chunkBB.encapsulate(boundingBox);
/* 164 */     super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/* 166 */     spreadNetherrack(random, (LevelAccessor)level);
/* 167 */     addNetherrackDripColumnsBelowPortal(random, (LevelAccessor)level);
/* 169 */     if (this.properties.vines || this.properties.overgrown)
/* 170 */       BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
/*     */             if (this.properties.vines)
/*     */               maybeAddVines(random, (LevelAccessor)level, pos); 
/*     */             if (this.properties.overgrown)
/*     */               maybeAddLeavesAbove(random, (LevelAccessor)level, pos); 
/*     */           }); 
/*     */   }
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {}
/*     */   
/*     */   private void maybeAddVines(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 186 */     BlockState state = level.getBlockState(pos);
/* 187 */     if (state.isAir() || state.is(Blocks.VINE))
/*     */       return; 
/* 191 */     Direction direction = getRandomHorizontalDirection(random);
/* 192 */     BlockPos neighbourPos = pos.relative(direction);
/* 193 */     BlockState neighourState = level.getBlockState(neighbourPos);
/* 194 */     if (!neighourState.isAir())
/*     */       return; 
/* 197 */     if (!Block.isFaceFull(state.getCollisionShape((BlockGetter)level, pos), direction))
/*     */       return; 
/* 200 */     BooleanProperty vineDir = VineBlock.getPropertyForFace(direction.getOpposite());
/* 201 */     level.setBlock(neighbourPos, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)vineDir, Boolean.valueOf(true)), 3);
/*     */   }
/*     */   
/*     */   private void maybeAddLeavesAbove(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 205 */     if (random.nextFloat() < 0.5F && level.getBlockState(pos).is(Blocks.NETHERRACK) && level.getBlockState(pos.above()).isAir())
/* 206 */       level.setBlock(pos.above(), (BlockState)Blocks.JUNGLE_LEAVES.defaultBlockState().setValue((Property)LeavesBlock.PERSISTENT, Boolean.valueOf(true)), 3); 
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumnsBelowPortal(RandomSource random, LevelAccessor level) {
/* 211 */     for (int x = this.boundingBox.minX() + 1; x < this.boundingBox.maxX(); x++) {
/* 212 */       for (int z = this.boundingBox.minZ() + 1; z < this.boundingBox.maxZ(); z++) {
/* 213 */         BlockPos pos = new BlockPos(x, this.boundingBox.minY(), z);
/* 214 */         if (level.getBlockState(pos).is(Blocks.NETHERRACK))
/* 215 */           addNetherrackDripColumn(random, level, pos.below()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNetherrackDripColumn(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 222 */     BlockPos.MutableBlockPos currentPos = pos.mutable();
/* 223 */     placeNetherrackOrMagma(random, level, (BlockPos)currentPos);
/* 224 */     int remainingCap = 8;
/* 225 */     while (remainingCap > 0 && random.nextFloat() < 0.5F) {
/* 226 */       currentPos.move(Direction.DOWN);
/* 227 */       remainingCap--;
/* 228 */       placeNetherrackOrMagma(random, level, (BlockPos)currentPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spreadNetherrack(RandomSource random, LevelAccessor level) {
/* 233 */     boolean followGroundSurface = (this.verticalPlacement == VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR);
/* 235 */     BlockPos center = this.boundingBox.getCenter();
/* 236 */     int centerX = center.getX();
/* 237 */     int centerZ = center.getZ();
/* 239 */     float[] netherrackProbabilityByDistance = { 
/* 239 */         1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 
/* 239 */         0.7F, 0.6F, 0.4F, 0.2F };
/* 240 */     int maxDistance = netherrackProbabilityByDistance.length;
/* 241 */     int averageWidth = (this.boundingBox.getXSpan() + this.boundingBox.getZSpan()) / 2;
/* 242 */     int distanceAdjustment = random.nextInt(Math.max(1, 8 - averageWidth / 2));
/* 243 */     int maxYDiff = 3;
/* 244 */     BlockPos.MutableBlockPos pos = BlockPos.ZERO.mutable();
/* 245 */     for (int x = centerX - maxDistance; x <= centerX + maxDistance; x++) {
/* 246 */       for (int z = centerZ - maxDistance; z <= centerZ + maxDistance; z++) {
/* 247 */         int distance = Math.abs(x - centerX) + Math.abs(z - centerZ);
/* 248 */         int adjustedDistance = Math.max(0, distance + distanceAdjustment);
/* 249 */         if (adjustedDistance < maxDistance) {
/* 252 */           float probabilityOfNetherrack = netherrackProbabilityByDistance[adjustedDistance];
/* 253 */           if (random.nextDouble() < probabilityOfNetherrack) {
/* 254 */             int surfaceY = getSurfaceY(level, x, z, this.verticalPlacement);
/* 255 */             int y = followGroundSurface ? surfaceY : Math.min(this.boundingBox.minY(), surfaceY);
/* 256 */             pos.set(x, y, z);
/* 257 */             if (Math.abs(y - this.boundingBox.minY()) <= 3 && canBlockBeReplacedByNetherrackOrMagma(level, (BlockPos)pos)) {
/* 258 */               placeNetherrackOrMagma(random, level, (BlockPos)pos);
/* 259 */               if (this.properties.overgrown)
/* 260 */                 maybeAddLeavesAbove(random, level, (BlockPos)pos); 
/* 262 */               addNetherrackDripColumn(random, level, pos.below());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canBlockBeReplacedByNetherrackOrMagma(LevelAccessor level, BlockPos pos) {
/* 270 */     BlockState state = level.getBlockState(pos);
/* 271 */     return (!state.is(Blocks.AIR) && 
/* 272 */       !state.is(Blocks.OBSIDIAN) && 
/* 273 */       !state.is(BlockTags.FEATURES_CANNOT_REPLACE) && (this.verticalPlacement == VerticalPlacement.IN_NETHER || 
/* 274 */       !state.is(Blocks.LAVA)));
/*     */   }
/*     */   
/*     */   private void placeNetherrackOrMagma(RandomSource random, LevelAccessor level, BlockPos pos) {
/* 278 */     if (!this.properties.cold && random.nextFloat() < 0.07F) {
/* 279 */       level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
/*     */     } else {
/* 281 */       level.setBlock(pos, Blocks.NETHERRACK.defaultBlockState(), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getSurfaceY(LevelAccessor level, int x, int z, VerticalPlacement verticalPlacement) {
/* 286 */     return level.getHeight(getHeightMapType(verticalPlacement), x, z) - 1;
/*     */   }
/*     */   
/*     */   public static Heightmap.Types getHeightMapType(VerticalPlacement verticalPlacement) {
/* 290 */     return (verticalPlacement == VerticalPlacement.ON_OCEAN_FLOOR) ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block source, float probability, Block target) {
/* 294 */     return new ProcessorRule((RuleTest)new RandomBlockMatchTest(source, probability), (RuleTest)AlwaysTrueTest.INSTANCE, target.defaultBlockState());
/*     */   }
/*     */   
/*     */   private static ProcessorRule getBlockReplaceRule(Block source, Block target) {
/* 298 */     return new ProcessorRule((RuleTest)new BlockMatchTest(source), (RuleTest)AlwaysTrueTest.INSTANCE, target.defaultBlockState());
/*     */   }
/*     */   
/*     */   public enum VerticalPlacement implements StringRepresentable {
/* 302 */     ON_LAND_SURFACE("on_land_surface"),
/* 303 */     PARTLY_BURIED("partly_buried"),
/* 304 */     ON_OCEAN_FLOOR("on_ocean_floor"),
/* 305 */     IN_MOUNTAIN("in_mountain"),
/* 306 */     UNDERGROUND("underground"),
/* 307 */     IN_NETHER("in_nether");
/*     */     
/* 310 */     public static final Codec<VerticalPlacement> CODEC = (Codec<VerticalPlacement>)StringRepresentable.fromEnum(VerticalPlacement::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     VerticalPlacement(String name) {
/* 315 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 319 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 324 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */