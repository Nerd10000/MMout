/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface MeshTransformer {
/*    */   public static final MeshTransformer IDENTITY;
/*    */   
/*    */   static {
/*  7 */     IDENTITY = (mesh -> mesh);
/*    */   }
/*    */   
/*    */   static MeshTransformer scaling(float factor) {
/* 11 */     float yOffset = 24.016F * (1.0F - factor);
/* 12 */     return mesh -> mesh.transformed(());
/*    */   }
/*    */   
/*    */   MeshDefinition apply(MeshDefinition paramMeshDefinition);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\builders\MeshTransformer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */