/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.world.entity.Crackiness;
/*    */ 
/*    */ public class IronGolemRenderState extends LivingEntityRenderState {
/*    */   public float attackTicksRemaining;
/*    */   
/*    */   public int offerFlowerTick;
/*    */   
/*  9 */   public final BlockModelRenderState flowerBlock = new BlockModelRenderState();
/*    */   
/* 10 */   public Crackiness.Level crackiness = Crackiness.Level.NONE;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\IronGolemRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */