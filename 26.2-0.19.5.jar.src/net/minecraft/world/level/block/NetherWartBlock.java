/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class NetherWartBlock extends VegetationBlock {
/* 20 */   public static final MapCodec<NetherWartBlock> CODEC = simpleCodec(NetherWartBlock::new);
/*    */   
/*    */   public static final int MAX_AGE = 3;
/*    */   
/*    */   public MapCodec<NetherWartBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*    */   
/*    */   private static final VoxelShape[] SHAPES;
/*    */   
/*    */   static {
/* 30 */     SHAPES = Block.boxes(3, age -> Block.column(16.0D, 0.0D, (5 + age * 3)));
/*    */   }
/*    */   
/*    */   protected NetherWartBlock(BlockBehaviour.Properties properties) {
/* 33 */     super(properties);
/* 34 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 39 */     return SHAPES[((Integer)state.getValue((Property)AGE)).intValue()];
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 44 */     return state.is(BlockTags.SUPPORTS_NETHER_WART);
/*    */   }
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState state) {
/* 49 */     return (((Integer)state.getValue((Property)AGE)).intValue() < 3);
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 54 */     int age = ((Integer)state.getValue((Property)AGE)).intValue();
/* 55 */     if (age < 3 && random.nextInt(10) == 0) {
/* 56 */       state = (BlockState)state.setValue((Property)AGE, Integer.valueOf(age + 1));
/* 57 */       level.setBlock(pos, state, 2);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 63 */     return new ItemStack((ItemLike)Items.NETHER_WART);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 68 */     builder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\NetherWartBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */