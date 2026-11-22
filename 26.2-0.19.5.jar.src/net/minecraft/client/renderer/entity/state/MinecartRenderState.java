/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartRenderState extends EntityRenderState {
/*    */   public float xRot;
/*    */   
/*    */   public float yRot;
/*    */   
/*    */   public long offsetSeed;
/*    */   
/*    */   public int hurtDir;
/*    */   
/*    */   public float hurtTime;
/*    */   
/*    */   public float damageTime;
/*    */   
/*    */   public int displayOffset;
/*    */   
/* 15 */   public BlockModelRenderState displayBlockModel = new BlockModelRenderState();
/*    */   
/*    */   public boolean isNewRender;
/*    */   
/*    */   public Vec3 renderPos;
/*    */   
/*    */   public Vec3 posOnRail;
/*    */   
/*    */   public Vec3 frontPos;
/*    */   
/*    */   public Vec3 backPos;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\MinecartRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */