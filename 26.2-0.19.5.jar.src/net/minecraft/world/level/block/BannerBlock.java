/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BannerBlock extends AbstractBannerBlock {
/*    */   public static final MapCodec<BannerBlock> CODEC;
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, BannerBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<BannerBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */   
/* 34 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/* 36 */   private static final VoxelShape SHAPE = Block.column(8.0D, 0.0D, 16.0D);
/*    */   
/*    */   public BannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 39 */     super(color, properties);
/* 40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(8)));
/*    */   }
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 45 */     return level.getBlockState(pos.below()).isSolid();
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 50 */     return SHAPE;
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 55 */     return (BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(RotationSegment.convertToSegment(context.getRotation() + 180.0F)));
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 60 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos))
/* 61 */       return Blocks.AIR.defaultBlockState(); 
/* 64 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 69 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(rotation.rotate(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 74 */     return (BlockState)state.setValue((Property)ROTATION, Integer.valueOf(mirror.mirror(((Integer)state.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 79 */     builder.add(new Property[] { (Property)ROTATION });
/*    */   }
/*    */   
/*    */   public enum AttachmentType implements StringRepresentable {
/* 83 */     WALL("wall"),
/* 84 */     GROUND("ground");
/*    */     
/* 87 */     public static final Codec<AttachmentType> CODEC = (Codec<AttachmentType>)StringRepresentable.fromEnum(AttachmentType::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     AttachmentType(String name) {
/* 92 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 97 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BannerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */