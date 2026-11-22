/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.bell.BellModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.BellRenderer;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class BellSpecialRenderer implements NoDataSpecialModelRenderer {
/* 16 */   private static final BellModel.State STATE = new BellModel.State(0.0F, null);
/*    */   
/*    */   private final BellModel model;
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   public BellSpecialRenderer(SpriteGetter sprites, BellModel model) {
/* 21 */     this.sprites = sprites;
/* 22 */     this.model = model;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/BellSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 26 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 30 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public BellSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 35 */       return new BellSpecialRenderer(context
/* 36 */           .sprites(), new BellModel(context
/* 37 */             .entityModelSet().bakeLayer(ModelLayers.BELL)));
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 44 */     submitNodeCollector.submitModel((Model)this.model, STATE, poseStack, lightCoords, overlayCoords, -1, BellRenderer.BELL_TEXTURE, this.sprites, outlineColor, null);
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 49 */     PoseStack poseStack = new PoseStack();
/* 50 */     this.model.setupAnim(STATE);
/* 51 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\BellSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */