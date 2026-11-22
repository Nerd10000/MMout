/*   */ package net.minecraft.world.level.block;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*   */ 
/*   */ public abstract class MultifaceSpreadeableBlock extends MultifaceBlock {
/*   */   public MultifaceSpreadeableBlock(BlockBehaviour.Properties properties) {
/* 7 */     super(properties);
/*   */   }
/*   */   
/*   */   public abstract MapCodec<? extends MultifaceSpreadeableBlock> codec();
/*   */   
/*   */   public abstract MultifaceSpreader getSpreader();
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreadeableBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */