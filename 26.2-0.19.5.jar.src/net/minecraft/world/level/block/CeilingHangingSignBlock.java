/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CeilingHangingSignBlock extends SignBlock implements HangingSignBlock {
/*     */   public static final MapCodec<CeilingHangingSignBlock> CODEC;
/*     */   
/*     */   static {
/*  45 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WoodType.CODEC.fieldOf("wood_type").forGetter(SignBlock::type), (App)propertiesCodec()).apply((Applicative)i, CeilingHangingSignBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<CeilingHangingSignBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*     */   
/*  56 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*  58 */   private static final VoxelShape SHAPE_DEFAULT = Block.column(10.0D, 0.0D, 16.0D);
/*     */   
/*     */   private static final Map<Integer, VoxelShape> SHAPES;
/*     */   
/*     */   static {
/*  59 */     SHAPES = (Map<Integer, VoxelShape>)Shapes.rotateHorizontal(Block.column(14.0D, 2.0D, 0.0D, 10.0D)).entrySet().stream().collect(Collectors.toMap(e -> Integer.valueOf(RotationSegment.convertToSegment((Direction)e.getKey())), Map.Entry::getValue));
/*     */   }
/*     */   
/*     */   public CeilingHangingSignBlock(WoodType type, BlockBehaviour.Properties properties) {
/*  65 */     super(type, properties.sound(type.hangingSignSoundType()));
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(8))).setValue((Property)ATTACHED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  71 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  71 */     if (blockEntity instanceof SignBlockEntity) {
/*  71 */       SignBlockEntity signEntity = (SignBlockEntity)blockEntity;
/*  72 */       if (shouldTryToChainAnotherHangingSign(player, hitResult, signEntity, itemStack))
/*  73 */         return (InteractionResult)InteractionResult.PASS; 
/*     */     } 
/*  76 */     return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */   }
/*     */   
/*     */   private boolean shouldTryToChainAnotherHangingSign(Player player, BlockHitResult hitResult, SignBlockEntity signEntity, ItemStack itemStack) {
/*  80 */     return (!signEntity.canExecuteClickCommands(signEntity.isFacingFrontText(player), player) && itemStack
/*  81 */       .getItem() instanceof net.minecraft.world.item.HangingSignItem && hitResult.getDirection().equals(Direction.DOWN));
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  86 */     return level.getBlockState(pos.above()).isFaceSturdy((BlockGetter)level, pos.above(), Direction.DOWN, SupportType.CENTER);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  91 */     Level level = context.getLevel();
/*  92 */     FluidState replacedFluidState = level.getFluidState(context.getClickedPos());
/*  93 */     BlockPos above = context.getClickedPos().above();
/*  94 */     BlockState stateAbove = level.getBlockState(above);
/*  95 */     boolean isBelowHangingSign = stateAbove.is(BlockTags.ALL_HANGING_SIGNS);
/*  96 */     Direction direction = Direction.fromYRot(context.getRotation());
/*  97 */     boolean attachedToMiddle = (!Block.isFaceFull(stateAbove.getCollisionShape((BlockGetter)level, above), Direction.DOWN) || context.isSecondaryUseActive());
/*  99 */     if (isBelowHangingSign && !context.isSecondaryUseActive())
/* 100 */       if (stateAbove.hasProperty((Property)WallHangingSignBlock.FACING)) {
/* 101 */         Direction aboveDirection = (Direction)stateAbove.getValue((Property)WallHangingSignBlock.FACING);
/* 102 */         if (aboveDirection.getAxis().test(direction))
/* 103 */           attachedToMiddle = false; 
/* 105 */       } else if (stateAbove.hasProperty((Property)ROTATION)) {
/* 106 */         Optional<Direction> aboveDirection = RotationSegment.convertToDirection(((Integer)stateAbove.getValue((Property)ROTATION)).intValue());
/* 107 */         if (aboveDirection.isPresent() && ((Direction)aboveDirection.get()).getAxis().test(direction))
/* 108 */           attachedToMiddle = false; 
/*     */       }  
/* 113 */     int rotationSegment = !attachedToMiddle ? RotationSegment.convertToSegment(direction.getOpposite()) : RotationSegment.convertToSegment(context.getRotation() + 180.0F);
/* 114 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)ATTACHED, Boolean.valueOf(attachedToMiddle))).setValue((Property)ROTATION, Integer.valueOf(rotationSegment))).setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 119 */     return SHAPES.getOrDefault(state.getValue((Property)ROTATION), SHAPE_DEFAULT);
/*     */   }
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 124 */     return getShape(state, level, pos, CollisionContext.empty());
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 129 */     if (directionToNeighbour == Direction.UP && !canSurvive(state, level, pos))
/* 130 */       return Blocks.AIR.defaultBlockState(); 
/* 132 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public float getYRotationDegrees(BlockState state) {
/* 137 */     return RotationSegment.convertToDegrees(((Integer)state.getValue((Property)ROTATION)).intValue());
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 142 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 147 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 152 */     builder.add(new Property[] { (Property)ROTATION, (Property)ATTACHED, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 157 */     return (BlockEntity)new HangingSignBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 162 */     return createTickerHelper(type, BlockEntityTypes.HANGING_SIGN, SignBlockEntity::tick);
/*     */   }
/*     */   
/*     */   public HangingSignBlock.Attachment attachmentPoint(BlockState state) {
/* 167 */     return getAttachmentPoint(((Boolean)state.getValue((Property)BlockStateProperties.ATTACHED)).booleanValue());
/*     */   }
/*     */   
/*     */   public static HangingSignBlock.Attachment getAttachmentPoint(boolean isAttached) {
/* 171 */     return isAttached ? HangingSignBlock.Attachment.CEILING_MIDDLE : HangingSignBlock.Attachment.CEILING;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CeilingHangingSignBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */