/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public abstract class Model<S> {
/*    */   protected final ModelPart root;
/*    */   
/*    */   protected final Function<Identifier, RenderType> renderType;
/*    */   
/*    */   private final List<ModelPart> allParts;
/*    */   
/*    */   public Model(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 20 */     this.root = root;
/* 21 */     this.renderType = renderType;
/* 22 */     this.allParts = root.getAllParts();
/*    */   }
/*    */   
/*    */   public final Function<Identifier, RenderType> renderType() {
/* 26 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public final RenderType renderType(Identifier texture) {
/* 30 */     return renderType().apply(texture);
/*    */   }
/*    */   
/*    */   public final void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int lightCoords, int overlayCoords, int color) {
/* 34 */     root().render(poseStack, buffer, lightCoords, overlayCoords, color);
/*    */   }
/*    */   
/*    */   public final ModelPart root() {
/* 38 */     return this.root;
/*    */   }
/*    */   
/*    */   public final List<ModelPart> allParts() {
/* 42 */     return this.allParts;
/*    */   }
/*    */   
/*    */   public void setupAnim(S state) {
/* 46 */     resetPose();
/*    */   }
/*    */   
/*    */   public final void resetPose() {
/* 50 */     for (ModelPart part : this.allParts)
/* 51 */       part.resetPose(); 
/*    */   }
/*    */   
/*    */   public static class Simple extends Model<Unit> {
/*    */     public Simple(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 57 */       super(root, renderType);
/*    */     }
/*    */     
/*    */     public void setupAnim(Unit state) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\Model.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */