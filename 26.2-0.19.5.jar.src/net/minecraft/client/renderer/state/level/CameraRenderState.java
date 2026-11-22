/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public class CameraRenderState {
/* 12 */   public BlockPos blockPos = BlockPos.ZERO;
/*    */   
/* 13 */   public Vec3 pos = new Vec3(0.0D, 0.0D, 0.0D);
/*    */   
/*    */   public float xRot;
/*    */   
/*    */   public float yRot;
/*    */   
/*    */   public boolean initialized;
/*    */   
/*    */   public boolean isPanoramicMode;
/*    */   
/*    */   public boolean isFrustumCaptured;
/*    */   
/*    */   public boolean smartCull;
/*    */   
/* 20 */   public Quaternionf orientation = new Quaternionf();
/*    */   
/* 22 */   public Frustum cullFrustum = new Frustum((Matrix4fc)new Matrix4f(), new Matrix4f());
/*    */   
/* 24 */   public FogType fogType = FogType.NONE;
/*    */   
/* 25 */   public FogData fogData = new FogData();
/*    */   
/*    */   public float hudFov;
/*    */   
/*    */   public float depthFar;
/*    */   
/* 29 */   public Matrix4f projectionMatrix = new Matrix4f();
/*    */   
/* 30 */   public Matrix4f viewRotationMatrix = new Matrix4f();
/*    */   
/* 32 */   public CameraEntityRenderState entityRenderState = new CameraEntityRenderState();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\CameraRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */