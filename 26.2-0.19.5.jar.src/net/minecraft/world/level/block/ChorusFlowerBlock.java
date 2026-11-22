/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChorusFlowerBlock extends Block {
/*     */   public static final MapCodec<ChorusFlowerBlock> CODEC;
/*     */   
/*     */   public static final int DEAD_AGE = 5;
/*     */   
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("plant").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ChorusFlowerBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<ChorusFlowerBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
/*     */   
/*  39 */   private static final VoxelShape SHAPE_BLOCK_SUPPORT = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   private final Block plant;
/*     */   
/*     */   protected ChorusFlowerBlock(Block plant, BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     this.plant = plant;
/*  46 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  51 */     if (!state.canSurvive((LevelReader)level, pos))
/*  52 */       level.destroyBlock(pos, true); 
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  58 */     return (((Integer)state.getValue((Property)AGE)).intValue() < 5);
/*     */   }
/*     */   
/*     */   public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  63 */     return SHAPE_BLOCK_SUPPORT;
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  69 */     BlockPos above = pos.above();
/*  70 */     if (!level.isEmptyBlock(above) || above.getY() > level.getMaxY())
/*     */       return; 
/*  74 */     int currentAge = ((Integer)state.getValue((Property)AGE)).intValue();
/*  75 */     if (currentAge >= 5)
/*     */       return; 
/*  79 */     boolean growUpwards = false;
/*  80 */     boolean pillarOnSupportBlock = false;
/*  82 */     BlockState belowState = level.getBlockState(pos.below());
/*  83 */     if (belowState.is(BlockTags.SUPPORTS_CHORUS_FLOWER)) {
/*  84 */       growUpwards = true;
/*  85 */     } else if (belowState.is(this.plant)) {
/*  86 */       int height = 1;
/*  87 */       for (int i = 0; i < 4; i++) {
/*  88 */         BlockState testState = level.getBlockState(pos.below(height + 1));
/*  89 */         if (testState.is(this.plant)) {
/*  90 */           height++;
/*     */         } else {
/*  92 */           if (testState.is(BlockTags.SUPPORTS_CHORUS_FLOWER))
/*  93 */             pillarOnSupportBlock = true; 
/*     */           break;
/*     */         } 
/*     */       } 
/*  98 */       if (height < 2 || height <= random.nextInt(pillarOnSupportBlock ? 5 : 4))
/*  99 */         growUpwards = true; 
/* 101 */     } else if (belowState.isAir()) {
/* 102 */       growUpwards = true;
/*     */     } 
/* 105 */     if (growUpwards && allNeighborsEmpty((LevelReader)level, above, (Direction)null) && level.isEmptyBlock(pos.above(2))) {
/* 106 */       level.setBlock(pos, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, pos, this.plant.defaultBlockState()), 2);
/* 107 */       placeGrownFlower((Level)level, above, currentAge);
/* 108 */     } else if (currentAge < 4) {
/* 109 */       int numBranchAttempts = random.nextInt(4);
/* 110 */       if (pillarOnSupportBlock)
/* 111 */         numBranchAttempts++; 
/* 114 */       boolean createdBranch = false;
/* 115 */       for (int i = 0; i < numBranchAttempts; i++) {
/* 116 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 117 */         BlockPos target = pos.relative(direction);
/* 118 */         if (level.isEmptyBlock(target) && level.isEmptyBlock(target.below()) && allNeighborsEmpty((LevelReader)level, target, direction.getOpposite())) {
/* 119 */           placeGrownFlower((Level)level, target, currentAge + 1);
/* 120 */           createdBranch = true;
/*     */         } 
/*     */       } 
/* 124 */       if (createdBranch) {
/* 125 */         level.setBlock(pos, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, pos, this.plant.defaultBlockState()), 2);
/*     */       } else {
/* 127 */         placeDeadFlower((Level)level, pos);
/*     */       } 
/*     */     } else {
/* 130 */       placeDeadFlower((Level)level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeGrownFlower(Level level, BlockPos pos, int age) {
/* 135 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(age)), 2);
/* 136 */     level.levelEvent(1033, pos, 0);
/*     */   }
/*     */   
/*     */   private void placeDeadFlower(Level level, BlockPos pos) {
/* 140 */     level.setBlock(pos, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2);
/* 141 */     level.levelEvent(1034, pos, 0);
/*     */   }
/*     */   
/*     */   private static boolean allNeighborsEmpty(LevelReader level, BlockPos pos, Direction ignore) {
/* 145 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 146 */       if (direction != ignore && !level.isEmptyBlock(pos.relative(direction)))
/* 147 */         return false; 
/*     */     } 
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 155 */     if (directionToNeighbour != Direction.UP && !state.canSurvive(level, pos))
/* 156 */       ticks.scheduleTick(pos, this, 1); 
/* 159 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 164 */     BlockState belowState = level.getBlockState(pos.below());
/* 165 */     if (belowState.is(this.plant) || belowState.is(BlockTags.SUPPORTS_CHORUS_FLOWER))
/* 166 */       return true; 
/* 168 */     if (!belowState.isAir())
/* 169 */       return false; 
/* 172 */     boolean oneNeighbor = false;
/* 173 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 174 */       BlockState neighbor = level.getBlockState(pos.relative(direction));
/* 175 */       if (neighbor.is(this.plant)) {
/* 176 */         if (oneNeighbor)
/* 177 */           return false; 
/* 179 */         oneNeighbor = true;
/*     */         continue;
/*     */       } 
/* 180 */       if (!neighbor.isAir())
/* 181 */         return false; 
/*     */     } 
/* 184 */     return oneNeighbor;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 189 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public static void generatePlant(LevelAccessor level, BlockPos target, RandomSource random, int maxHorizontalSpread) {
/* 193 */     level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, Blocks.CHORUS_PLANT.defaultBlockState()), 2);
/* 194 */     growTreeRecursive(level, target, random, target, maxHorizontalSpread, 0);
/*     */   }
/*     */   
/*     */   private static void growTreeRecursive(LevelAccessor level, BlockPos current, RandomSource random, BlockPos startPos, int maxHorizontalSpread, int depth) {
/* 198 */     Block chorus = Blocks.CHORUS_PLANT;
/* 200 */     int height = random.nextInt(4) + 1;
/* 201 */     if (depth == 0)
/* 202 */       height++; 
/* 205 */     for (int i = 0; i < height; i++) {
/* 206 */       BlockPos target = current.above(i + 1);
/* 207 */       if (!allNeighborsEmpty((LevelReader)level, target, (Direction)null))
/*     */         return; 
/* 211 */       level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, chorus.defaultBlockState()), 2);
/* 212 */       level.setBlock(target.below(), ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target.below(), chorus.defaultBlockState()), 2);
/*     */     } 
/* 215 */     boolean placedStem = false;
/* 216 */     if (depth < 4) {
/* 217 */       int stems = random.nextInt(4);
/* 218 */       if (depth == 0)
/* 219 */         stems++; 
/* 221 */       for (int j = 0; j < stems; j++) {
/* 222 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 223 */         BlockPos target = current.above(height).relative(direction);
/* 224 */         if (Math.abs(target.getX() - startPos.getX()) < maxHorizontalSpread && Math.abs(target.getZ() - startPos.getZ()) < maxHorizontalSpread)
/* 227 */           if (level.isEmptyBlock(target) && level.isEmptyBlock(target.below()) && allNeighborsEmpty((LevelReader)level, target, direction.getOpposite())) {
/* 228 */             placedStem = true;
/* 229 */             level.setBlock(target, ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target, chorus.defaultBlockState()), 2);
/* 230 */             level.setBlock(target.relative(direction.getOpposite()), ChorusPlantBlock.getStateWithConnections((BlockGetter)level, target.relative(direction.getOpposite()), chorus.defaultBlockState()), 2);
/* 231 */             growTreeRecursive(level, target, random, startPos, maxHorizontalSpread, depth + 1);
/*     */           }  
/*     */       } 
/*     */     } 
/* 236 */     if (!placedStem)
/* 237 */       level.setBlock(current.above(height), (BlockState)Blocks.CHORUS_FLOWER.defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2); 
/*     */   }
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 243 */     BlockPos pos = blockHit.getBlockPos();
/* 244 */     if (level instanceof ServerLevel) {
/* 244 */       ServerLevel serverLevel = (ServerLevel)level;
/* 244 */       if (projectile.mayInteract(serverLevel, pos) && projectile.mayBreak(serverLevel))
/* 245 */         level.destroyBlock(pos, true, (Entity)projectile); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChorusFlowerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */