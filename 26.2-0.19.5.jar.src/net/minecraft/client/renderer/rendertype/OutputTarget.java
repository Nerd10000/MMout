/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class OutputTarget {
/*    */   private final String name;
/*    */   
/*    */   private final Supplier<RenderTarget> renderTargetSupplier;
/*    */   
/*    */   public OutputTarget(String name, Supplier<RenderTarget> renderTargetSupplier) {
/* 14 */     this.name = name;
/* 15 */     this.renderTargetSupplier = renderTargetSupplier;
/*    */   }
/*    */   
/*    */   public RenderTarget getRenderTarget() {
/* 19 */     RenderTarget preferredTarget = this.renderTargetSupplier.get();
/* 20 */     return (preferredTarget != null) ? preferredTarget : (Minecraft.getInstance()).gameRenderer.mainRenderTarget();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return "OutputTarget[" + this.name + "]";
/*    */   }
/*    */   
/* 28 */   public static final OutputTarget MAIN_TARGET = new OutputTarget("main_target", () -> (Minecraft.getInstance()).gameRenderer.mainRenderTarget());
/*    */   
/* 30 */   public static final OutputTarget OUTLINE_TARGET = new OutputTarget("outline_target", () -> (Minecraft.getInstance()).levelRenderer.entityOutlineTarget());
/*    */   
/* 32 */   public static final OutputTarget WEATHER_TARGET = new OutputTarget("weather_target", () -> (Minecraft.getInstance()).levelRenderer.weatherTarget());
/*    */   
/* 34 */   public static final OutputTarget ITEM_ENTITY_TARGET = new OutputTarget("item_entity_target", () -> (Minecraft.getInstance()).levelRenderer.itemEntityTarget());
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\OutputTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */