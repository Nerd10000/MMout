/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AttachedStemBlock extends VegetationBlock {
/*    */   public static final MapCodec<AttachedStemBlock> CODEC;
/*    */   
/*    */   static {
/* 28 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), (App)ResourceKey.codec(Registries.BLOCK).fieldOf("stem").forGetter(()), (App)ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), (App)TagKey.codec(Registries.BLOCK).fieldOf("support_blocks").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, AttachedStemBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<AttachedStemBlock> codec() {
/* 38 */     return CODEC;
/*    */   }
/*    */   
/* 41 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 43 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(4.0D, 0.0D, 10.0D, 0.0D, 10.0D));
/*    */   
/*    */   private final ResourceKey<Block> fruit;
/*    */   
/*    */   private final ResourceKey<Block> stem;
/*    */   
/*    */   private final ResourceKey<Item> seed;
/*    */   
/*    */   private final TagKey<Block> supportBlocks;
/*    */   
/*    */   protected AttachedStemBlock(ResourceKey<Block> stem, ResourceKey<Block> fruit, ResourceKey<Item> seed, TagKey<Block> supportBlocks, BlockBehaviour.Properties properties) {
/* 52 */     super(properties);
/* 53 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/* 54 */     this.stem = stem;
/* 55 */     this.fruit = fruit;
/* 56 */     this.seed = seed;
/* 57 */     this.supportBlocks = supportBlocks;
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 62 */     return SHAPES.get(state.getValue((Property)FACING));
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 67 */     if (!neighbourState.is(this.fruit) && directionToNeighbour == state.getValue((Property)FACING)) {
/* 68 */       Optional<Block> stem = level.registryAccess().lookupOrThrow(Registries.BLOCK).getOptional(this.stem);
/* 69 */       if (stem.isPresent())
/* 70 */         return (BlockState)((Block)stem.get()).defaultBlockState().trySetValue((Property)StemBlock.AGE, Integer.valueOf(7)); 
/*    */     } 
/* 73 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 78 */     return state.is(this.supportBlocks);
/*    */   }
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 83 */     return new ItemStack((ItemLike)DataFixUtils.orElse(level.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this));
/*    */   }
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 88 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 93 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 98 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\AttachedStemBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */