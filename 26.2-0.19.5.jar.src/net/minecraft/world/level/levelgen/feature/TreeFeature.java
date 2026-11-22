/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.LevelWriter;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeavesBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ 
/*     */ public class TreeFeature extends Feature<TreeConfiguration> {
/*     */   private static final int BLOCK_UPDATE_FLAGS = 19;
/*     */   
/*     */   public TreeFeature(Codec<TreeConfiguration> codec) {
/*  38 */     super(codec);
/*     */   }
/*     */   
/*     */   public static boolean isVine(LevelSimulatedReader level, BlockPos pos) {
/*  42 */     return level.isStateAtPosition(pos, state -> state.is(Blocks.VINE));
/*     */   }
/*     */   
/*     */   public static boolean isAirOrLeaves(LevelSimulatedReader level, BlockPos pos) {
/*  46 */     return level.isStateAtPosition(pos, state -> (state.isAir() || state.is(BlockTags.LEAVES)));
/*     */   }
/*     */   
/*     */   private static void setBlockKnownShape(LevelWriter level, BlockPos pos, BlockState blockState) {
/*  50 */     level.setBlock(pos, blockState, 19);
/*     */   }
/*     */   
/*     */   public static boolean validTreePos(LevelSimulatedReader level, BlockPos pos) {
/*  54 */     return level.isStateAtPosition(pos, state -> (state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES)));
/*     */   }
/*     */   
/*     */   private boolean doPlace(WorldGenLevel level, RandomSource random, BlockPos origin, BiConsumer<BlockPos, BlockState> rootSetter, BiConsumer<BlockPos, BlockState> trunkSetter, FoliagePlacer.FoliageSetter foliageSetter, TreeConfiguration config) {
/*  58 */     int treeHeight = config.trunkPlacer.getTreeHeight(random);
/*  59 */     int foliageHeight = config.foliagePlacer.foliageHeight(random, treeHeight, config);
/*  60 */     int trunkHeight = treeHeight - foliageHeight;
/*  62 */     int leafRadius = config.foliagePlacer.foliageRadius(random, trunkHeight);
/*  64 */     BlockPos trunkOrigin = config.rootPlacer.map(rootPlacer -> rootPlacer.getTrunkOrigin(origin, random)).orElse(origin);
/*  66 */     int minY = Math.min(origin.getY(), trunkOrigin.getY());
/*  67 */     int maxY = Math.max(origin.getY(), trunkOrigin.getY()) + treeHeight + 1;
/*  68 */     if (minY < level.getMinY() + 1 || maxY > level.getMaxY() + 1)
/*  69 */       return false; 
/*  72 */     OptionalInt minClippedHeight = config.minimumSize.minClippedHeight();
/*  74 */     int clippedTreeHeight = getMaxFreeTreeHeight(level, treeHeight, trunkOrigin, config);
/*  75 */     if (clippedTreeHeight < treeHeight && (minClippedHeight.isEmpty() || clippedTreeHeight < minClippedHeight.getAsInt()))
/*  76 */       return false; 
/*  79 */     if (config.rootPlacer.isPresent() && 
/*  80 */       !((RootPlacer)config.rootPlacer.get()).placeRoots(level, rootSetter, random, origin, trunkOrigin, config))
/*  81 */       return false; 
/*  85 */     List<FoliagePlacer.FoliageAttachment> foliageAttachments = config.trunkPlacer.placeTrunk(level, trunkSetter, random, clippedTreeHeight, trunkOrigin, config);
/*  86 */     foliageAttachments.forEach(foliageAttachment -> config.foliagePlacer.createFoliage(level, foliageSetter, random, config, clippedTreeHeight, foliageAttachment, foliageHeight, leafRadius));
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   private int getMaxFreeTreeHeight(WorldGenLevel level, int maxTreeHeight, BlockPos treePos, TreeConfiguration config) {
/*  93 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  95 */     for (int y = 0; y <= maxTreeHeight + 1; y++) {
/*  96 */       int r = config.minimumSize.getSizeAtHeight(maxTreeHeight, y);
/*  97 */       for (int x = -r; x <= r; x++) {
/*  98 */         for (int z = -r; z <= r; z++) {
/*  99 */           blockPos.setWithOffset((Vec3i)treePos, x, y, z);
/* 100 */           if (!config.trunkPlacer.isFree(level, (BlockPos)blockPos) || (!config.ignoreVines && isVine((LevelSimulatedReader)level, (BlockPos)blockPos)))
/* 101 */             return y - 2; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 107 */     return maxTreeHeight;
/*     */   }
/*     */   
/*     */   protected void setBlock(LevelWriter level, BlockPos pos, BlockState blockState) {
/* 112 */     setBlockKnownShape(level, pos, blockState);
/*     */   }
/*     */   
/*     */   public final boolean place(FeaturePlaceContext<TreeConfiguration> context) {
/* 117 */     final WorldGenLevel level = context.level();
/* 118 */     RandomSource random = context.random();
/* 119 */     BlockPos origin = context.origin();
/* 120 */     TreeConfiguration config = context.config();
/* 122 */     Set<BlockPos> rootPositions = Sets.newHashSet();
/* 123 */     Set<BlockPos> trunks = Sets.newHashSet();
/* 124 */     final Set<BlockPos> foliage = Sets.newHashSet();
/* 125 */     Set<BlockPos> decorations = Sets.newHashSet();
/* 127 */     BiConsumer<BlockPos, BlockState> rootSetter = (pos, state) -> {
/*     */         rootPositions.add(pos.immutable());
/*     */         level.setBlock(pos, state, 19);
/*     */       };
/* 131 */     BiConsumer<BlockPos, BlockState> trunkSetter = (pos, state) -> {
/*     */         trunks.add(pos.immutable());
/*     */         level.setBlock(pos, state, 19);
/*     */       };
/* 135 */     FoliagePlacer.FoliageSetter foliageSetter = new FoliagePlacer.FoliageSetter(this) {
/*     */         public void set(BlockPos pos, BlockState state) {
/* 138 */           foliage.add(pos.immutable());
/* 139 */           level.setBlock(pos, state, 19);
/*     */         }
/*     */         
/*     */         public boolean isSet(BlockPos pos) {
/* 144 */           return foliage.contains(pos);
/*     */         }
/*     */       };
/* 147 */     BiConsumer<BlockPos, BlockState> decorationSetter = (pos, state) -> {
/*     */         decorations.add(pos.immutable());
/*     */         level.setBlock(pos, state, 19);
/*     */       };
/* 152 */     boolean result = doPlace(level, random, origin, rootSetter, trunkSetter, foliageSetter, config);
/* 153 */     if (!result || (trunks.isEmpty() && foliage.isEmpty()))
/* 154 */       return false; 
/* 157 */     if (!config.decorators.isEmpty()) {
/* 158 */       TreeDecorator.Context decoratorContext = new TreeDecorator.Context(level, decorationSetter, random, trunks, foliage, rootPositions);
/* 159 */       config.decorators.forEach(decorator -> decorator.place(decoratorContext));
/*     */     } 
/* 162 */     return ((Boolean)BoundingBox.encapsulatingPositions(Iterables.concat(rootPositions, trunks, foliage, decorations)).map(bounds -> {
/*     */           DiscreteVoxelShape shape = updateLeaves((LevelAccessor)level, bounds, trunks, decorations, rootPositions);
/*     */           StructureTemplate.updateShapeAtEdge((LevelAccessor)level, 3, shape, bounds.minX(), bounds.minY(), bounds.minZ());
/*     */           return Boolean.valueOf(true);
/* 167 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   private static DiscreteVoxelShape updateLeaves(LevelAccessor level, BoundingBox bounds, Set<BlockPos> logs, Set<BlockPos> decorationSet, Set<BlockPos> rootPositions) {
/* 173 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(bounds.getXSpan(), bounds.getYSpan(), bounds.getZSpan());
/* 174 */     int maxDistance = 7;
/* 177 */     List<Set<BlockPos>> toCheck = Lists.newArrayList();
/* 178 */     for (int i = 0; i < 7; i++)
/* 179 */       toCheck.add(Sets.newHashSet()); 
/* 182 */     for (BlockPos pos : Lists.newArrayList((Iterable)Sets.union(decorationSet, rootPositions))) {
/* 183 */       if (bounds.isInside((Vec3i)pos))
/* 184 */         bitSetDiscreteVoxelShape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ()); 
/*     */     } 
/* 188 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 189 */     int smallestDistance = 0;
/* 190 */     ((Set<BlockPos>)toCheck.get(0)).addAll(logs);
/*     */     while (true) {
/* 193 */       if (smallestDistance < 7 && ((Set)toCheck.get(smallestDistance)).isEmpty()) {
/* 194 */         smallestDistance++;
/*     */         continue;
/*     */       } 
/* 196 */       if (smallestDistance >= 7)
/*     */         break; 
/* 199 */       Iterator<BlockPos> iterator = ((Set<BlockPos>)toCheck.get(smallestDistance)).iterator();
/* 200 */       BlockPos pos = iterator.next();
/* 201 */       iterator.remove();
/* 203 */       if (!bounds.isInside((Vec3i)pos))
/*     */         continue; 
/* 206 */       if (smallestDistance != 0) {
/* 207 */         BlockState state = level.getBlockState(pos);
/* 208 */         setBlockKnownShape((LevelWriter)level, pos, (BlockState)state.setValue((Property)BlockStateProperties.DISTANCE, Integer.valueOf(smallestDistance)));
/*     */       } 
/* 210 */       bitSetDiscreteVoxelShape.fill(pos.getX() - bounds.minX(), pos.getY() - bounds.minY(), pos.getZ() - bounds.minZ());
/* 212 */       for (Direction direction : Direction.values()) {
/* 213 */         neighborPos.setWithOffset((Vec3i)pos, direction);
/* 214 */         if (bounds.isInside((Vec3i)neighborPos)) {
/* 217 */           int xInShape = neighborPos.getX() - bounds.minX();
/* 218 */           int yInShape = neighborPos.getY() - bounds.minY();
/* 219 */           int zinShape = neighborPos.getZ() - bounds.minZ();
/* 220 */           if (!bitSetDiscreteVoxelShape.isFull(xInShape, yInShape, zinShape)) {
/* 224 */             BlockState currentState = level.getBlockState((BlockPos)neighborPos);
/* 225 */             OptionalInt distance = LeavesBlock.getOptionalDistanceAt(currentState);
/* 227 */             if (!distance.isEmpty()) {
/* 231 */               int newDistance = Math.min(distance.getAsInt(), smallestDistance + 1);
/* 233 */               if (newDistance < 7) {
/* 234 */                 ((Set<BlockPos>)toCheck.get(newDistance)).add(neighborPos.immutable());
/* 235 */                 smallestDistance = Math.min(smallestDistance, newDistance);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 239 */     return (DiscreteVoxelShape)bitSetDiscreteVoxelShape;
/*     */   }
/*     */   
/*     */   public static List<BlockPos> getLowestTrunkOrRootOfTree(TreeDecorator.Context context) {
/* 243 */     List<BlockPos> blockPositions = Lists.newArrayList();
/* 244 */     ObjectArrayList<BlockPos> objectArrayList1 = context.roots();
/* 245 */     ObjectArrayList<BlockPos> objectArrayList2 = context.logs();
/* 247 */     if (objectArrayList1.isEmpty()) {
/* 248 */       blockPositions.addAll((Collection<? extends BlockPos>)objectArrayList2);
/* 249 */     } else if (!objectArrayList2.isEmpty() && ((BlockPos)objectArrayList1.get(0)).getY() == ((BlockPos)objectArrayList2.get(0)).getY()) {
/* 250 */       blockPositions.addAll((Collection<? extends BlockPos>)objectArrayList2);
/* 251 */       blockPositions.addAll((Collection<? extends BlockPos>)objectArrayList1);
/*     */     } else {
/* 253 */       blockPositions.addAll((Collection<? extends BlockPos>)objectArrayList1);
/*     */     } 
/* 255 */     return blockPositions;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\TreeFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */