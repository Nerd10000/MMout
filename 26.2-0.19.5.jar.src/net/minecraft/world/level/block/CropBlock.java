/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CropBlock extends VegetationBlock implements BonemealableBlock {
/*  27 */   public static final MapCodec<CropBlock> CODEC = simpleCodec(CropBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*     */   public MapCodec<? extends CropBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  35 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
/*     */   
/*     */   private static final VoxelShape[] SHAPES;
/*     */   
/*     */   static {
/*  37 */     SHAPES = Block.boxes(7, age -> Block.column(16.0D, 0.0D, (2 + age * 2)));
/*     */   }
/*     */   
/*     */   protected CropBlock(BlockBehaviour.Properties properties) {
/*  40 */     super(properties);
/*  41 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)getAgeProperty(), Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  46 */     return SHAPES[getAge(state)];
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/*  51 */     return state.is(BlockTags.SUPPORTS_CROPS);
/*     */   }
/*     */   
/*     */   protected IntegerProperty getAgeProperty() {
/*  55 */     return AGE;
/*     */   }
/*     */   
/*     */   public int getMaxAge() {
/*  59 */     return 7;
/*     */   }
/*     */   
/*     */   public int getAge(BlockState state) {
/*  63 */     return ((Integer)state.getValue((Property)getAgeProperty())).intValue();
/*     */   }
/*     */   
/*     */   public BlockState getStateForAge(int age) {
/*  67 */     return (BlockState)defaultBlockState().setValue((Property)getAgeProperty(), Integer.valueOf(age));
/*     */   }
/*     */   
/*     */   public final boolean isMaxAge(BlockState state) {
/*  71 */     return (getAge(state) >= getMaxAge());
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/*  76 */     return !isMaxAge(state);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  81 */     if (level.getRawBrightness(pos, 0) >= 9) {
/*  82 */       int age = getAge(state);
/*  83 */       if (age < getMaxAge()) {
/*  84 */         float growthSpeed = getGrowthSpeed(this, (BlockGetter)level, pos);
/*  86 */         if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)
/*  87 */           level.setBlock(pos, getStateForAge(age + 1), 2); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void growCrops(Level level, BlockPos pos, BlockState state) {
/*  94 */     int age = Math.min(getMaxAge(), getAge(state) + getBonemealAgeIncrease(level));
/*  95 */     level.setBlock(pos, getStateForAge(age), 2);
/*     */   }
/*     */   
/*     */   protected int getBonemealAgeIncrease(Level level) {
/*  99 */     return Mth.nextInt(level.getRandom(), 2, 5);
/*     */   }
/*     */   
/*     */   protected static float getGrowthSpeed(Block type, BlockGetter level, BlockPos pos) {
/* 103 */     float speed = 1.0F;
/* 105 */     BlockPos below = pos.below();
/* 106 */     for (int xx = -1; xx <= 1; xx++) {
/* 107 */       for (int zz = -1; zz <= 1; zz++) {
/* 108 */         float blockSpeed = 0.0F;
/* 110 */         BlockState blockState = level.getBlockState(below.offset(xx, 0, zz));
/* 111 */         if (blockState.is(BlockTags.GROWS_CROPS)) {
/* 112 */           blockSpeed = 1.0F;
/* 113 */           if (((Integer)blockState.getValueOrElse((Property)FarmlandBlock.MOISTURE, Integer.valueOf(0))).intValue() > 0)
/* 114 */             blockSpeed = 3.0F; 
/*     */         } 
/* 118 */         if (xx != 0 || zz != 0)
/* 119 */           blockSpeed /= 4.0F; 
/* 122 */         speed += blockSpeed;
/*     */       } 
/*     */     } 
/* 126 */     BlockPos north = pos.north();
/* 127 */     BlockPos south = pos.south();
/* 128 */     BlockPos west = pos.west();
/* 129 */     BlockPos east = pos.east();
/* 131 */     boolean horizontal = (level.getBlockState(west).is(type) || level.getBlockState(east).is(type));
/* 132 */     boolean vertical = (level.getBlockState(north).is(type) || level.getBlockState(south).is(type));
/* 134 */     if (horizontal && vertical) {
/* 135 */       speed /= 2.0F;
/*     */     } else {
/* 141 */       boolean diagonal = (level.getBlockState(west.north()).is(type) || level.getBlockState(east.north()).is(type) || level.getBlockState(east.south()).is(type) || level.getBlockState(west.south()).is(type));
/* 143 */       if (diagonal)
/* 144 */         speed /= 2.0F; 
/*     */     } 
/* 148 */     return speed;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 153 */     return (hasSufficientLight(level, pos) && super.canSurvive(state, level, pos));
/*     */   }
/*     */   
/*     */   protected static boolean hasSufficientLight(LevelReader level, BlockPos pos) {
/* 157 */     return (level.getRawBrightness(pos, 0) >= 8);
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 162 */     if (level instanceof ServerLevel) {
/* 162 */       ServerLevel serverLevel = (ServerLevel)level;
/* 162 */       if (entity instanceof net.minecraft.world.entity.monster.Ravager && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 163 */         serverLevel.destroyBlock(pos, true, entity); 
/*     */     } 
/* 165 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */   
/*     */   protected ItemLike getBaseSeedId() {
/* 170 */     return (ItemLike)Items.WHEAT_SEEDS;
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 175 */     return new ItemStack(getBaseSeedId());
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 180 */     return !isMaxAge(state);
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 185 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 190 */     growCrops((Level)level, pos, state);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 195 */     builder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CropBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */