/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.grower.TreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SaplingBlock extends VegetationBlock implements BonemealableBlock {
/*    */   public static final MapCodec<SaplingBlock> CODEC;
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, SaplingBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<? extends SaplingBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*    */   
/* 34 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */   
/*    */   protected final TreeGrower treeGrower;
/*    */   
/*    */   protected SaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/* 39 */     super(properties);
/* 40 */     this.treeGrower = treeGrower;
/* 41 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STAGE, Integer.valueOf(0)));
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 46 */     return SHAPE;
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 51 */     if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && 
/* 52 */       random.nextInt(7) == 0)
/* 53 */       advanceTree(level, pos, state, random); 
/*    */   }
/*    */   
/*    */   public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/* 59 */     if (((Integer)state.getValue((Property)STAGE)).intValue() == 0) {
/* 60 */       level.setBlock(pos, (BlockState)state.cycle((Property)STAGE), 260);
/*    */     } else {
/* 62 */       this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*    */     ServerLevel serverLevel;
/* 68 */     if (level instanceof ServerLevel) {
/* 68 */       serverLevel = (ServerLevel)level;
/*    */     } else {
/* 69 */       return false;
/*    */     } 
/* 72 */     int heightOffset = this.treeGrower.getMinimumHeight(serverLevel).orElse(0);
/* 73 */     return level.isInsideBuildHeight(pos.above(heightOffset));
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 78 */     return (level.getRandom().nextFloat() < 0.45D);
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 83 */     advanceTree(level, pos, state, random);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 88 */     builder.add(new Property[] { (Property)STAGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SaplingBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */