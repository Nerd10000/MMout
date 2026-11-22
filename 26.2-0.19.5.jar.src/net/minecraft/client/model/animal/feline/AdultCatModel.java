/*    */ package net.minecraft.client.model.animal.feline;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ 
/*    */ public class AdultCatModel extends AdultFelineModel<CatRenderState> {
/*  9 */   public static final MeshTransformer CAT_TRANSFORMER = MeshTransformer.scaling(0.8F);
/*    */   
/* 10 */   public static final CubeDeformation COLLAR_DEFORMATION = new CubeDeformation(0.01F);
/*    */   
/*    */   public AdultCatModel(ModelPart root) {
/* 13 */     super(root);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\feline\AdultCatModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */