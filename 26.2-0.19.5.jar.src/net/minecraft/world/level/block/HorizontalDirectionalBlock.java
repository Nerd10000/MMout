/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class HorizontalDirectionalBlock extends Block {
/* 10 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   protected HorizontalDirectionalBlock(BlockBehaviour.Properties properties) {
/* 13 */     super(properties);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends HorizontalDirectionalBlock> codec();
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 21 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 26 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HorizontalDirectionalBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */