/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Transformation;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.TridentModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class TridentSpecialRenderer implements NoDataSpecialModelRenderer {
/* 17 */   public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, (Vector3fc)new Vector3f(1.0F, -1.0F, -1.0F), null);
/*    */   
/*    */   private final TridentModel model;
/*    */   
/*    */   public TridentSpecialRenderer(TridentModel model) {
/* 27 */     this.model = model;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/TridentSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 31 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 35 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public TridentSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 40 */       return new TridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 46 */     submitNodeCollector.order(0).submitModel((Model)this.model, Unit.INSTANCE, poseStack, TridentModel.TEXTURE, lightCoords, overlayCoords, outlineColor, null);
/* 47 */     if (hasFoil)
/* 48 */       submitNodeCollector.order(1).submitModel((Model)this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, outlineColor, null); 
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 54 */     PoseStack poseStack = new PoseStack();
/* 55 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\TridentSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */