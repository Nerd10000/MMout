/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ 
/*    */ public class LayerDefinition {
/*    */   private final MeshDefinition mesh;
/*    */   
/*    */   private final MaterialDefinition material;
/*    */   
/*    */   private LayerDefinition(MeshDefinition mesh, MaterialDefinition material) {
/* 10 */     this.mesh = mesh;
/* 11 */     this.material = material;
/*    */   }
/*    */   
/*    */   public LayerDefinition apply(MeshTransformer transformer) {
/* 15 */     return new LayerDefinition(transformer.apply(this.mesh), this.material);
/*    */   }
/*    */   
/*    */   public ModelPart bakeRoot() {
/* 19 */     return this.mesh.getRoot().bake(this.material.xTexSize, this.material.yTexSize);
/*    */   }
/*    */   
/*    */   public static LayerDefinition create(MeshDefinition mesh, int xTexSize, int yTexSize) {
/* 23 */     return new LayerDefinition(mesh, new MaterialDefinition(xTexSize, yTexSize));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\builders\LayerDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */