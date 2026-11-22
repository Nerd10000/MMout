/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.google.common.base.Suppliers;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class MissingItemModel implements ItemModel {
/*    */   private final List<BakedQuad> quads;
/*    */   
/*    */   private final Supplier<Vector3fc[]> extents;
/*    */   
/*    */   private final ModelRenderProperties properties;
/*    */   
/*    */   private final Matrix4fc transform;
/*    */   
/*    */   public MissingItemModel(List<BakedQuad> quads, ModelRenderProperties properties) {
/* 24 */     this(quads, 
/*    */         
/* 26 */         (Supplier<Vector3fc[]>)Suppliers.memoize(() -> CuboidItemModelWrapper.computeExtents(quads)), properties, (Matrix4fc)new Matrix4f());
/*    */   }
/*    */   
/*    */   private MissingItemModel(List<BakedQuad> quads, Supplier<Vector3fc[]> extents, ModelRenderProperties properties, Matrix4fc transform) {
/* 33 */     this.quads = quads;
/* 34 */     this.extents = extents;
/* 35 */     this.properties = properties;
/* 36 */     this.transform = transform;
/*    */   }
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/* 41 */     output.appendModelIdentityElement(this);
/* 42 */     ItemStackRenderState.LayerRenderState layer = output.newLayer();
/* 43 */     this.properties.applyToLayer(layer, displayContext);
/* 44 */     layer.setExtents(this.extents);
/* 45 */     layer.setLocalTransform(this.transform);
/* 46 */     layer.prepareQuadList().addAll(this.quads);
/*    */   }
/*    */   
/*    */   public MissingItemModel withTransform(Matrix4fc transform) {
/* 50 */     if (transform.equals(this.transform))
/* 51 */       return this; 
/* 53 */     return new MissingItemModel(this.quads, this.extents, this.properties, transform);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\MissingItemModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */