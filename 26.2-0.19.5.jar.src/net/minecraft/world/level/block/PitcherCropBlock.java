/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PitcherCropBlock extends DoublePlantBlock implements BonemealableBlock {
/*  34 */   public static final MapCodec<PitcherCropBlock> CODEC = simpleCodec(PitcherCropBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*     */   
/*     */   public MapCodec<PitcherCropBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  42 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
/*     */   
/*  43 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*     */   
/*     */   private static final int DOUBLE_PLANT_AGE_INTERSECTION = 3;
/*     */   
/*     */   private static final int BONEMEAL_INCREASE = 1;
/*     */   
/*  48 */   private static final VoxelShape SHAPE_BULB = Block.column(6.0D, -1.0D, 3.0D);
/*     */   
/*  49 */   private static final VoxelShape SHAPE_CROP = Block.column(10.0D, -1.0D, 5.0D);
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   public PitcherCropBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  56 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  60 */     int[] plantHeights = { 0, 9, 11, 22, 26 };
/*  62 */     return getShapeForEachState(state -> {
/*     */           int height = ((((Integer)state.getValue((Property)AGE)).intValue() == 0) ? 4 : 6) + plantHeights[((Integer)state.getValue((Property)AGE)).intValue()];
/*     */           int width = (((Integer)state.getValue((Property)AGE)).intValue() == 0) ? 6 : 10;
/*     */           switch ((DoubleBlockHalf)state.getValue((Property)HALF)) {
/*     */             default:
/*     */               throw new MatchException(null, null);
/*     */             case LOWER:
/*     */             
/*     */             case UPPER:
/*     */               break;
/*     */           } 
/*     */           return Block.column(width, 0.0D, Math.max(0, -1 + height - 16));
/*     */         });
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  74 */     return defaultBlockState();
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  79 */     return this.shapes.apply(state);
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  84 */     if (state.getValue((Property)HALF) == DoubleBlockHalf.LOWER)
/*  85 */       return (((Integer)state.getValue((Property)AGE)).intValue() == 0) ? SHAPE_BULB : SHAPE_CROP; 
/*  87 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  92 */     if (isDouble(((Integer)state.getValue((Property)AGE)).intValue()))
/*  93 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random); 
/*  95 */     return state.canSurvive(level, pos) ? state : Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 101 */     if (isLower(state) && !sufficientLight(level, pos))
/* 102 */       return false; 
/* 104 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 109 */     return state.is(BlockTags.SUPPORTS_CROPS);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 114 */     builder.add(new Property[] { (Property)AGE });
/* 115 */     super.createBlockStateDefinition(builder);
/*     */   }
/*     */   
/*     */   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 120 */     if (level instanceof ServerLevel) {
/* 120 */       ServerLevel serverLevel = (ServerLevel)level;
/* 120 */       if (entity instanceof net.minecraft.world.entity.monster.Ravager && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 121 */         serverLevel.destroyBlock(pos, true, entity); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {}
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState state) {
/* 138 */     return (state.getValue((Property)HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state));
/*     */   }
/*     */   
/*     */   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 143 */     float growthSpeed = CropBlock.getGrowthSpeed(this, (BlockGetter)level, pos);
/* 144 */     boolean shouldProgressGrowth = (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0);
/* 146 */     if (shouldProgressGrowth)
/* 147 */       grow(level, state, pos, 1); 
/*     */   }
/*     */   
/*     */   private void grow(ServerLevel level, BlockState lowerState, BlockPos lowerPos, int increase) {
/* 152 */     int updatedAge = Math.min(((Integer)lowerState.getValue((Property)AGE)).intValue() + increase, 4);
/* 153 */     if (!canGrow((LevelReader)level, lowerPos, lowerState, updatedAge))
/*     */       return; 
/* 157 */     BlockState newLowerState = (BlockState)lowerState.setValue((Property)AGE, Integer.valueOf(updatedAge));
/* 158 */     level.setBlock(lowerPos, newLowerState, 2);
/* 160 */     if (isDouble(updatedAge))
/* 161 */       level.setBlock(lowerPos.above(), (BlockState)newLowerState.setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), 3); 
/*     */   }
/*     */   
/*     */   private static boolean canGrowInto(LevelReader level, BlockPos pos) {
/* 166 */     BlockState state = level.getBlockState(pos);
/* 167 */     return (state.isAir() || state.is(Blocks.PITCHER_CROP));
/*     */   }
/*     */   
/*     */   private static boolean sufficientLight(LevelReader level, BlockPos pos) {
/* 171 */     return CropBlock.hasSufficientLight(level, pos);
/*     */   }
/*     */   
/*     */   private static boolean isLower(BlockState state) {
/* 175 */     return (state.is(Blocks.PITCHER_CROP) && state.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */   }
/*     */   
/*     */   private static boolean isDouble(int age) {
/* 179 */     return (age >= 3);
/*     */   }
/*     */   
/*     */   private boolean canGrow(LevelReader level, BlockPos lowerPos, BlockState lowerState, int newAge) {
/* 183 */     return (!isMaxAge(lowerState) && 
/* 184 */       sufficientLight(level, lowerPos) && level
/* 185 */       .isInsideBuildHeight(lowerPos.above()) && (
/* 186 */       !isDouble(newAge) || canGrowInto(level, lowerPos.above())));
/*     */   }
/*     */   
/*     */   private boolean isMaxAge(BlockState state) {
/* 190 */     return (((Integer)state.getValue((Property)AGE)).intValue() >= 4);
/*     */   }
/*     */   
/*     */   private static final class PosAndState extends Record {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final BlockState state;
/*     */     
/*     */     private PosAndState(BlockPos pos, BlockState state) {
/* 193 */       this.pos = pos;
/* 193 */       this.state = state;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #193	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #193	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #193	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/PitcherCropBlock$PosAndState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 193 */       return this.pos;
/*     */     }
/*     */     
/*     */     public BlockState state() {
/* 193 */       return this.state;
/*     */     }
/*     */   }
/*     */   
/*     */   private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
/* 196 */     if (isLower(state))
/* 197 */       return new PosAndState(pos, state); 
/* 199 */     BlockPos lowerPos = pos.below();
/* 200 */     BlockState lowerState = level.getBlockState(lowerPos);
/* 201 */     if (isLower(lowerState))
/* 202 */       return new PosAndState(lowerPos, lowerState); 
/* 204 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 209 */     PosAndState lowerHalf = getLowerHalf(level, pos, state);
/* 210 */     if (lowerHalf == null)
/* 211 */       return false; 
/* 213 */     return canGrow(level, lowerHalf.pos, lowerHalf.state, ((Integer)lowerHalf.state.getValue((Property)AGE)).intValue() + 1);
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 218 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 223 */     PosAndState lowerHalf = getLowerHalf((LevelReader)level, pos, state);
/* 224 */     if (lowerHalf == null)
/*     */       return; 
/* 227 */     grow(level, lowerHalf.state, lowerHalf.pos, 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PitcherCropBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */