/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public enum Extent {
/* 45 */   MIN_X, MIN_Y, MIN_Z, MAX_X, MAX_Y, MAX_Z;
/*    */   
/*    */   public float select(Vector3fc min, Vector3fc max) {
/* 55 */     switch (ordinal()) {
/*    */       default:
/* 55 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */       
/*    */       case 5:
/*    */         break;
/*    */     } 
/* 55 */     return 
/*    */       
/* 62 */       max.z();
/*    */   }
/*    */   
/*    */   public float select(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
/* 67 */     switch (ordinal()) {
/*    */       default:
/* 67 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */       
/*    */       case 5:
/*    */         break;
/*    */     } 
/* 67 */     return 
/*    */       
/* 74 */       maxZ;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\FaceInfo$Extent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */