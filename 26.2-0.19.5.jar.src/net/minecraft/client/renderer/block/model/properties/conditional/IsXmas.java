/*   */ package net.minecraft.client.renderer.block.model.properties.conditional;
/*   */ 
/*   */ import net.minecraft.client.renderer.blockentity.ChestRenderer;
/*   */ import net.minecraft.world.level.block.state.BlockState;
/*   */ 
/*   */ public class IsXmas implements ConditionalBlockModelProperty {
/*   */   public boolean get(BlockState blockState) {
/* 9 */     return ChestRenderer.xmasTextures();
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\properties\conditional\IsXmas.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */