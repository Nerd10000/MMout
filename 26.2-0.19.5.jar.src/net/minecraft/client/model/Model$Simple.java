/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class Simple extends Model<Unit> {
/*    */   public Simple(ModelPart root, Function<Identifier, RenderType> renderType) {
/* 57 */     super(root, renderType);
/*    */   }
/*    */   
/*    */   public void setupAnim(Unit state) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\Model$Simple.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */