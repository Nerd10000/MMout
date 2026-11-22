/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ 
/*    */ public abstract class DirectionalBlock extends Block {
/*  9 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*    */   
/*    */   protected DirectionalBlock(BlockBehaviour.Properties properties) {
/* 12 */     super(properties);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends DirectionalBlock> codec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DirectionalBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */