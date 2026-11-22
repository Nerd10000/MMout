/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.ConduitRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ConduitSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final ModelPart model;
/*    */   
/*    */   public ConduitSpecialRenderer(SpriteGetter sprites, ModelPart model) {
/* 21 */     this.sprites = sprites;
/* 22 */     this.model = model;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ConduitSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 26 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 30 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public ConduitSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 35 */       return new ConduitSpecialRenderer(context.sprites(), context.entityModelSet().bakeLayer(ModelLayers.CONDUIT_SHELL));
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 41 */     submitNodeCollector.submitModelPart(this.model, poseStack, ConduitRenderer.SHELL_TEXTURE.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(ConduitRenderer.SHELL_TEXTURE), -1, null, outlineColor);
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 46 */     PoseStack poseStack = new PoseStack();
/* 47 */     this.model.getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\ConduitSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */