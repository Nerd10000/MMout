/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.ServerExplosion;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CreakingHeartBlock extends BaseEntityBlock {
/*  39 */   public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);
/*     */   
/*     */   public MapCodec<CreakingHeartBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*  46 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
/*     */   
/*  47 */   public static final EnumProperty<CreakingHeartState> STATE = BlockStateProperties.CREAKING_HEART_STATE;
/*     */   
/*  48 */   public static final BooleanProperty NATURAL = BlockStateProperties.NATURAL;
/*     */   
/*     */   protected CreakingHeartBlock(BlockBehaviour.Properties properties) {
/*  51 */     super(properties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)Direction.Axis.Y)).setValue((Property)STATE, (Comparable)CreakingHeartState.UPROOTED)).setValue((Property)NATURAL, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  57 */     return (BlockEntity)new CreakingHeartBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  62 */     if (level.isClientSide())
/*  63 */       return null; 
/*  65 */     if (blockState.getValue((Property)STATE) != CreakingHeartState.UPROOTED)
/*  66 */       return createTickerHelper(type, BlockEntityTypes.CREAKING_HEART, CreakingHeartBlockEntity::serverTick); 
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  73 */     if (!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue())
/*     */       return; 
/*  76 */     if (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED)
/*     */       return; 
/*  79 */     if (random.nextInt(16) == 0 && isSurroundedByLogs((LevelAccessor)level, pos))
/*  80 */       level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CREAKING_HEART_IDLE, SoundSource.BLOCKS, 1.0F, 1.0F, false); 
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  86 */     ticks.scheduleTick(pos, this, 1);
/*  87 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  92 */     BlockState newState = updateState(state, (Level)level, pos);
/*  93 */     if (newState != state)
/*  94 */       level.setBlock(pos, newState, 3); 
/*     */   }
/*     */   
/*     */   private static BlockState updateState(BlockState state, Level level, BlockPos pos) {
/*  99 */     boolean hasLogs = hasRequiredLogs(state, (LevelReader)level, pos);
/* 100 */     boolean disabled = (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED);
/* 101 */     if (hasLogs && disabled)
/* 102 */       return (BlockState)state.setValue((Property)STATE, ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue() ? (Comparable)CreakingHeartState.AWAKE : (Comparable)CreakingHeartState.DORMANT); 
/* 104 */     return state;
/*     */   }
/*     */   
/*     */   public static boolean hasRequiredLogs(BlockState state, LevelReader level, BlockPos pos) {
/* 108 */     Direction.Axis axis = (Direction.Axis)state.getValue((Property)AXIS);
/* 109 */     for (Direction dir : axis.getDirections()) {
/* 110 */       BlockState neigbour = level.getBlockState(pos.relative(dir));
/* 111 */       if (!neigbour.is(BlockTags.PALE_OAK_LOGS) || neigbour.getValue((Property)AXIS) != axis)
/* 112 */         return false; 
/*     */     } 
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSurroundedByLogs(LevelAccessor level, BlockPos pos) {
/* 119 */     for (Direction dir : Direction.values()) {
/* 120 */       BlockPos neighbourPos = pos.relative(dir);
/* 121 */       BlockState neighbourState = level.getBlockState(neighbourPos);
/* 122 */       if (!neighbourState.is(BlockTags.PALE_OAK_LOGS))
/* 123 */         return false; 
/*     */     } 
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 131 */     return updateState((BlockState)defaultBlockState().setValue((Property)AXIS, (Comparable)context.getClickedFace().getAxis()), context.getLevel(), context.getClickedPos());
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 136 */     return RotatedPillarBlock.rotatePillar(state, rotation);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 141 */     builder.add(new Property[] { (Property)AXIS, (Property)STATE, (Property)NATURAL });
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 146 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 151 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 151 */     if (blockEntity instanceof CreakingHeartBlockEntity) {
/* 151 */       CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 151 */       if (explosion instanceof ServerExplosion) {
/* 151 */         ServerExplosion serverExplosion = (ServerExplosion)explosion;
/* 151 */         if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
/* 152 */           creakingHeartBlockEntity.removeProtector(serverExplosion.getDamageSource());
/* 153 */           LivingEntity livingEntity = explosion.getIndirectSourceEntity();
/* 153 */           if (livingEntity instanceof Player) {
/* 153 */             Player player = (Player)livingEntity;
/* 153 */             if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities())
/* 154 */               tryAwardExperience(player, state, (Level)level, pos); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 162 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 162 */     if (blockEntity instanceof CreakingHeartBlockEntity) {
/* 162 */       CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 163 */       creakingHeartBlockEntity.removeProtector(player.damageSources().playerAttack(player));
/* 164 */       tryAwardExperience(player, state, level, pos);
/*     */     } 
/* 166 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   private void tryAwardExperience(Player player, BlockState state, Level level, BlockPos pos) {
/* 170 */     if (!player.preventsBlockDrops() && !player.isSpectator() && ((Boolean)state.getValue((Property)NATURAL)).booleanValue() && level instanceof ServerLevel) {
/* 170 */       ServerLevel serverLevel = (ServerLevel)level;
/* 171 */       popExperience(serverLevel, pos, level.getRandom().nextIntBetweenInclusive(20, 24));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 177 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*     */     CreakingHeartBlockEntity creakingHeartBlockEntity;
/* 182 */     if (state.getValue((Property)STATE) == CreakingHeartState.UPROOTED)
/* 183 */       return 0; 
/* 185 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 185 */     if (blockEntity instanceof CreakingHeartBlockEntity) {
/* 185 */       creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/*     */     } else {
/* 186 */       return 0;
/*     */     } 
/* 188 */     return creakingHeartBlockEntity.getAnalogOutputSignal();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CreakingHeartBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */