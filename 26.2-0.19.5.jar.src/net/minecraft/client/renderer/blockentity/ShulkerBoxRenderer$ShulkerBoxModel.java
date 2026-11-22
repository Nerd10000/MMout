/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ 
/*     */ class ShulkerBoxModel extends Model<Float> {
/*     */   private final ModelPart lid;
/*     */   
/*     */   public ShulkerBoxModel(ModelPart root) {
/* 117 */     super(root, RenderTypes::entityCutout);
/* 118 */     this.lid = root.getChild("lid");
/*     */   }
/*     */   
/*     */   public void setupAnim(Float progress) {
/* 123 */     super.setupAnim(progress);
/* 124 */     this.lid.setPos(0.0F, 24.0F - progress.floatValue() * 0.5F * 16.0F, 0.0F);
/* 125 */     this.lid.yRot = 270.0F * progress.floatValue() * 0.017453292F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\ShulkerBoxRenderer$ShulkerBoxModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */