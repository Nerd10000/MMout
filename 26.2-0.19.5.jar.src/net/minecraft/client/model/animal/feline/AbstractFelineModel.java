/*    */ package net.minecraft.client.model.animal.feline;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.FelineRenderState;
/*    */ 
/*    */ public abstract class AbstractFelineModel<S extends FelineRenderState> extends EntityModel<S> {
/*    */   protected static final String TAIL_1 = "tail1";
/*    */   
/*    */   protected static final String TAIL_2 = "tail2";
/*    */   
/*    */   protected final ModelPart leftHindLeg;
/*    */   
/*    */   protected final ModelPart rightHindLeg;
/*    */   
/*    */   protected final ModelPart leftFrontLeg;
/*    */   
/*    */   protected final ModelPart rightFrontLeg;
/*    */   
/*    */   protected final ModelPart tail1;
/*    */   
/*    */   protected final ModelPart tail2;
/*    */   
/*    */   protected final ModelPart head;
/*    */   
/*    */   protected final ModelPart body;
/*    */   
/*    */   protected AbstractFelineModel(ModelPart root) {
/* 22 */     super(root);
/* 23 */     this.head = root.getChild("head");
/* 24 */     this.body = root.getChild("body");
/* 25 */     this.tail1 = root.getChild("tail1");
/* 26 */     this.tail2 = root.getChild("tail2");
/* 27 */     this.leftHindLeg = root.getChild("left_hind_leg");
/* 28 */     this.rightHindLeg = root.getChild("right_hind_leg");
/* 29 */     this.leftFrontLeg = root.getChild("left_front_leg");
/* 30 */     this.rightFrontLeg = root.getChild("right_front_leg");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\animal\feline\AbstractFelineModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */