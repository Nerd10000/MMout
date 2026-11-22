/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CaveVinesPlantBlock extends GrowingPlantBodyBlock implements CaveVines {
/* 19 */   public static final MapCodec<CaveVinesPlantBlock> CODEC = simpleCodec(CaveVinesPlantBlock::new);
/*    */   
/*    */   public MapCodec<CaveVinesPlantBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   public CaveVinesPlantBlock(BlockBehaviour.Properties properties) {
/* 27 */     super(properties, Direction.DOWN, SHAPE, false);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BERRIES, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 33 */     return (GrowingPlantHeadBlock)Blocks.CAVE_VINES;
/*    */   }
/*    */   
/*    */   protected BlockState updateHeadAfterConvertedFromBody(BlockState bodyState, BlockState headState) {
/* 38 */     return (BlockState)headState.setValue((Property)BERRIES, bodyState.getValue((Property)BERRIES));
/*    */   }
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 43 */     return new ItemStack((ItemLike)Items.GLOW_BERRIES);
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 48 */     return CaveVines.use((Entity)player, state, level, pos);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 53 */     builder.add(new Property[] { (Property)BERRIES });
/*    */   }
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 58 */     return !((Boolean)state.getValue((Property)BERRIES)).booleanValue();
/*    */   }
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 68 */     level.setBlock(pos, (BlockState)state.setValue((Property)BERRIES, Boolean.valueOf(true)), 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CaveVinesPlantBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */