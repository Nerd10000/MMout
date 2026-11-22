/*    */ package net.minecraft.client.resources.model.geometry;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ModelDebugName;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface UnbakedGeometry {
/*    */   public static final UnbakedGeometry EMPTY = (paramTextureSlots, paramModelBaker, paramModelState, paramModelDebugName) -> QuadCollection.EMPTY;
/*    */   
/*    */   QuadCollection bake(TextureSlots paramTextureSlots, ModelBaker paramModelBaker, ModelState paramModelState, ModelDebugName paramModelDebugName);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\geometry\UnbakedGeometry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */