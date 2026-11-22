/*    */ package net.minecraft.client.model.object.statue;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class CopperGolemStatueModel extends Model<Unit> {
/*    */   public CopperGolemStatueModel(ModelPart root) {
/* 11 */     super(root, RenderTypes::entityCutout);
/*    */   }
/*    */   
/*    */   public void setupAnim(Unit ignored) {
/* 16 */     this.root.y = 0.0F;
/* 17 */     this.root.zRot = 3.1415927F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\statue\CopperGolemStatueModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */