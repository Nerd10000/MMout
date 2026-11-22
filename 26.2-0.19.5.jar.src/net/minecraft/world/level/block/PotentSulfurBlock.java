/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PotentSulfurState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class PotentSulfurBlock extends BaseEntityBlock {
/*     */   public static final int ALLOWED_WATER_BLOCKS_ABOVE = 4;
/*     */   
/*  33 */   public static final MapCodec<PotentSulfurBlock> CODEC = simpleCodec(PotentSulfurBlock::new);
/*     */   
/*  34 */   public static final EnumProperty<PotentSulfurState> STATE = BlockStateProperties.POTENT_SULFUR_STATE;
/*     */   
/*     */   public MapCodec<PotentSulfurBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*     */   public PotentSulfurBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)STATE, (Comparable)PotentSulfurState.DRY));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  48 */     builder.add(new Property[] { (Property)STATE });
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  53 */     return (BlockEntity)new PotentSulfurBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  58 */     return validBlockState(state, level, pos);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  63 */     return validBlockState(defaultBlockState(), (LevelReader)context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   private static BlockState validBlockState(BlockState state, LevelReader level, BlockPos pos) {
/*  67 */     if (!level.getFluidState(pos.above()).isSourceOfType((Fluid)Fluids.WATER))
/*  68 */       return (BlockState)state.setValue((Property)STATE, (Comparable)PotentSulfurState.DRY); 
/*  71 */     BlockState belowState = level.getBlockState(pos.below());
/*  72 */     if (belowState.is(BlockTags.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS) && isSourceIfFluid(belowState))
/*  73 */       return (BlockState)state.setValue((Property)STATE, (Comparable)PotentSulfurState.CONTINUOUS); 
/*  76 */     if (belowState.is(BlockTags.CAUSES_PERIODIC_GEYSER_ERUPTIONS) && isSourceIfFluid(belowState)) {
/*  78 */       boolean isGeyser = (state.getValue((Property)STATE) == PotentSulfurState.ERUPTING || state.getValue((Property)STATE) == PotentSulfurState.DORMANT);
/*  79 */       if (!isGeyser) {
/*  81 */         BlockEntity blockEntity = level.getBlockEntity(pos);
/*  81 */         if (blockEntity instanceof PotentSulfurBlockEntity) {
/*  81 */           PotentSulfurBlockEntity potentSulfurEntity = (PotentSulfurBlockEntity)blockEntity;
/*  82 */           potentSulfurEntity.resetCountdown();
/*     */         } 
/*     */       } 
/*  85 */       if (state.getValue((Property)STATE) == PotentSulfurState.ERUPTING)
/*  86 */         return state; 
/*  88 */       return (BlockState)state.setValue((Property)STATE, (Comparable)PotentSulfurState.DORMANT);
/*     */     } 
/*  90 */     return (BlockState)state.setValue((Property)STATE, (Comparable)PotentSulfurState.WET);
/*     */   }
/*     */   
/*     */   private static boolean isSourceIfFluid(BlockState belowState) {
/*  94 */     FluidState fluidState = belowState.getFluidState();
/*  95 */     return (fluidState.isEmpty() || fluidState.isSource());
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 100 */     super.onPlace(state, level, pos, oldState, movedByPiston);
/* 101 */     if (state.getValue((Property)STATE) == PotentSulfurState.ERUPTING || state.getValue((Property)STATE) == PotentSulfurState.CONTINUOUS) {
/* 103 */       level.blockEvent(pos, this, 0, 0);
/* 106 */       level.playSound(null, pos, (state.getValue((Property)STATE) == PotentSulfurState.CONTINUOUS) ? SoundEvents.GEYSER_CONTINUOUS_START : SoundEvents.GEYSER_ERUPTION_START, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 107 */       level.gameEvent((Holder)GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(state));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 113 */     if (state.getValue((Property)STATE) == PotentSulfurState.DRY)
/*     */       return; 
/* 117 */     if (!level.getFluidState(pos.above()).isSourceOfType((Fluid)Fluids.WATER))
/*     */       return; 
/* 122 */     spawnBubbleParticlesAt(level, random, pos.getX(), (pos.getY() + 1), pos.getZ());
/* 123 */     spawnBubbleParticlesAt(level, random, pos.getX(), (pos.getY() + 1), pos.getZ());
/* 125 */     if (random.nextInt(10) == 0)
/* 126 */       level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.NOXIOUS_GAS, SoundSource.AMBIENT, 1.0F, 1.0F, false); 
/*     */   }
/*     */   
/*     */   private static void spawnBubbleParticlesAt(Level level, RandomSource random, double x, double y, double z) {
/* 131 */     level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.SULFUR_BUBBLES, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/* 136 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 136 */     if (blockEntity instanceof PotentSulfurBlockEntity) {
/* 136 */       PotentSulfurBlockEntity entity = (PotentSulfurBlockEntity)blockEntity;
/* 137 */       entity.eruptionTick = level.getGameTime();
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 144 */     boolean client = level.isClientSide();
/* 145 */     switch ((PotentSulfurState)blockState.getValue((Property)STATE)) {
/*     */       default:
/* 145 */         throw new MatchException(null, null);
/*     */       case DRY:
/*     */       
/*     */       case WET:
/* 145 */         return createTickerHelper(type, BlockEntityTypes.POTENT_SULFUR, 
/*     */             
/* 148 */             client ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER : PotentSulfurBlockEntity.SERVER_NAUSEA_EFFECT_TICKER);
/*     */       case DORMANT:
/* 150 */         return createTickerHelper(type, BlockEntityTypes.POTENT_SULFUR, client ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER : PotentSulfurBlockEntity.SERVER_WAITING_COUNTDOWN_TICKER.andThen(PotentSulfurBlockEntity.SERVER_NAUSEA_EFFECT_TICKER));
/*     */       case ERUPTING:
/* 152 */         return createTickerHelper(type, BlockEntityTypes.POTENT_SULFUR, client ? ((BlockEntityTicker)PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER.apply(SoundEvents.GEYSER_ERUPTION_ACTIVE)).andThen(PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER) : PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER.andThen(PotentSulfurBlockEntity.SERVER_WAITING_COUNTDOWN_TICKER));
/*     */       case CONTINUOUS:
/*     */         break;
/*     */     } 
/* 154 */     return createTickerHelper(type, BlockEntityTypes.POTENT_SULFUR, client ? ((BlockEntityTicker)PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER.apply(SoundEvents.GEYSER_CONTINUOUS_ACTIVE)).andThen(PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER) : PotentSulfurBlockEntity.LAUNCH_ENTITY_TICKER);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PotentSulfurBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */