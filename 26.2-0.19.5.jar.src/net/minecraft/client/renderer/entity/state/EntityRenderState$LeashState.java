/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LeashState {
/* 47 */   public Vec3 offset = Vec3.ZERO;
/*    */   
/* 48 */   public Vec3 start = Vec3.ZERO;
/*    */   
/* 49 */   public Vec3 end = Vec3.ZERO;
/*    */   
/* 50 */   public int startBlockLight = 0;
/*    */   
/* 51 */   public int endBlockLight = 0;
/*    */   
/* 52 */   public int startSkyLight = 15;
/*    */   
/* 53 */   public int endSkyLight = 15;
/*    */   
/*    */   public boolean slack = true;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\EntityRenderState$LeashState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */