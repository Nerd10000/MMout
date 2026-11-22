/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.PotDecorations;
/*    */ 
/*    */ public class DecoratedPotRenderState extends BlockEntityRenderState {
/*    */   public float yRot;
/*    */   
/*    */   public DecoratedPotBlockEntity.WobbleStyle wobbleStyle;
/*    */   
/*    */   public float wobbleProgress;
/*    */   
/* 12 */   public PotDecorations decorations = PotDecorations.EMPTY;
/*    */   
/* 13 */   public Direction direction = Direction.NORTH;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\DecoratedPotRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */