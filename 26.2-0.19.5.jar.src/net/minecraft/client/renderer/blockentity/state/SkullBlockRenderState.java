/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ 
/*    */ public class SkullBlockRenderState extends BlockEntityRenderState {
/*    */   public float animationProgress;
/*    */   
/*  9 */   public Transformation transformation = Transformation.IDENTITY;
/*    */   
/* 10 */   public SkullBlock.Type skullType = (SkullBlock.Type)SkullBlock.Types.ZOMBIE;
/*    */   
/*    */   public RenderType renderType;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\SkullBlockRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */