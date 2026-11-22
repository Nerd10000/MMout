/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.core.cauldron.CauldronInteractions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LayeredCauldronBlock extends AbstractCauldronBlock {
/*     */   public static final MapCodec<LayeredCauldronBlock> CODEC;
/*     */   
/*     */   public static final int MIN_FILL_LEVEL = 1;
/*     */   
/*     */   public static final int MAX_FILL_LEVEL = 3;
/*     */   
/*     */   static {
/*  29 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(()), (App)CauldronInteractions.CODEC.fieldOf("interactions").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, LayeredCauldronBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<LayeredCauldronBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  42 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
/*     */   
/*     */   private static final int BASE_CONTENT_HEIGHT = 6;
/*     */   
/*     */   private static final double HEIGHT_PER_LEVEL = 3.0D;
/*     */   
/*  47 */   private static final VoxelShape[] FILLED_SHAPES = (VoxelShape[])Util.make(() -> Block.boxes(2, ()));
/*     */   
/*     */   private final Biome.Precipitation precipitationType;
/*     */   
/*     */   public LayeredCauldronBlock(Biome.Precipitation precipitationType, CauldronInteraction.Dispatcher interactionMap, BlockBehaviour.Properties properties) {
/*  52 */     super(properties, interactionMap);
/*  53 */     this.precipitationType = precipitationType;
/*  54 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(1)));
/*     */   }
/*     */   
/*     */   public boolean isFull(BlockState state) {
/*  59 */     return (((Integer)state.getValue((Property)LEVEL)).intValue() == 3);
/*     */   }
/*     */   
/*     */   protected boolean canReceiveStalactiteDrip(Fluid fluid) {
/*  64 */     return (fluid == Fluids.WATER && this.precipitationType == Biome.Precipitation.RAIN);
/*     */   }
/*     */   
/*     */   protected double getContentHeight(BlockState state) {
/*  69 */     return getPixelContentHeight(((Integer)state.getValue((Property)LEVEL)).intValue()) / 16.0D;
/*     */   }
/*     */   
/*     */   private static double getPixelContentHeight(int level) {
/*  73 */     return 6.0D + level * 3.0D;
/*     */   }
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
/*  78 */     return FILLED_SHAPES[((Integer)state.getValue((Property)LEVEL)).intValue() - 1];
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  83 */     if (level instanceof ServerLevel) {
/*  83 */       ServerLevel serverLevel = (ServerLevel)level;
/*  84 */       BlockPos blockPos = pos.immutable();
/*  85 */       effectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, e -> {
/*     */             if (e.isOnFire() && e.mayInteract(serverLevel, blockPos))
/*     */               handleEntityOnFireInside(state, level, blockPos); 
/*     */           });
/*     */     } 
/*  91 */     effectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */   
/*     */   private void handleEntityOnFireInside(BlockState state, Level level, BlockPos pos) {
/*  95 */     if (this.precipitationType == Biome.Precipitation.SNOW) {
/*  96 */       lowerFillLevel((BlockState)Blocks.WATER_CAULDRON.defaultBlockState().setValue((Property)LEVEL, state.getValue((Property)LEVEL)), level, pos);
/*     */     } else {
/*  98 */       lowerFillLevel(state, level, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
/* 103 */     int newLevel = ((Integer)state.getValue((Property)LEVEL)).intValue() - 1;
/* 104 */     BlockState newState = (newLevel == 0) ? Blocks.CAULDRON.defaultBlockState() : (BlockState)state.setValue((Property)LEVEL, Integer.valueOf(newLevel));
/* 105 */     level.setBlockAndUpdate(pos, newState);
/* 106 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/*     */   }
/*     */   
/*     */   public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
/* 111 */     if (!CauldronBlock.shouldHandlePrecipitation(level, precipitation) || ((Integer)state.getValue((Property)LEVEL)).intValue() == 3 || precipitation != this.precipitationType)
/*     */       return; 
/* 115 */     BlockState newState = (BlockState)state.cycle((Property)LEVEL);
/* 116 */     level.setBlockAndUpdate(pos, newState);
/* 117 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 122 */     return ((Integer)state.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 127 */     builder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */   
/*     */   protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
/* 132 */     if (isFull(state))
/*     */       return; 
/* 135 */     BlockState newState = (BlockState)state.setValue((Property)LEVEL, Integer.valueOf(((Integer)state.getValue((Property)LEVEL)).intValue() + 1));
/* 136 */     level.setBlockAndUpdate(pos, newState);
/* 137 */     level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
/* 138 */     level.levelEvent(1047, pos, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\LayeredCauldronBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */