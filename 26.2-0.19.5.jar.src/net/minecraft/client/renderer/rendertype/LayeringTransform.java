/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.util.function.Consumer;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public class LayeringTransform {
/*    */   private final String name;
/*    */   
/*    */   private final Consumer<Matrix4f> modifier;
/*    */   
/*    */   public LayeringTransform(String name, Consumer<Matrix4f> modifier) {
/* 14 */     this.name = name;
/* 15 */     this.modifier = modifier;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 20 */     return "LayeringTransform[" + this.name + "]";
/*    */   }
/*    */   
/*    */   public Consumer<Matrix4f> getModifier() {
/* 24 */     return this.modifier;
/*    */   }
/*    */   
/* 27 */   public static final LayeringTransform NO_LAYERING = new LayeringTransform("no_layering", null);
/*    */   
/*    */   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING;
/*    */   
/*    */   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING_FORWARD;
/*    */   
/*    */   static {
/* 29 */     VIEW_OFFSET_Z_LAYERING = new LayeringTransform("view_offset_z_layering", modelViewMatrix -> RenderSystem.getProjectionType().applyLayeringTransform(modelViewMatrix, 1.0F));
/* 33 */     VIEW_OFFSET_Z_LAYERING_FORWARD = new LayeringTransform("view_offset_z_layering_forward", modelViewMatrix -> RenderSystem.getProjectionType().applyLayeringTransform(modelViewMatrix, -1.0F));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\LayeringTransform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */