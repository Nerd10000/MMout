/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.skull.SkullModel;
/*    */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class PlayerHeadSpecialRenderer implements SpecialModelRenderer<PlayerSkinRenderCache.RenderInfo> {
/*    */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */   
/*    */   private final SkullModelBase modelBase;
/*    */   
/*    */   private PlayerHeadSpecialRenderer(PlayerSkinRenderCache playerSkinRenderCache, SkullModelBase modelBase) {
/* 25 */     this.playerSkinRenderCache = playerSkinRenderCache;
/* 26 */     this.modelBase = modelBase;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked<PlayerSkinRenderCache.RenderInfo> {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/PlayerHeadSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 30 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 34 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public PlayerHeadSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 39 */       SkullModel model = new SkullModel(context.entityModelSet().bakeLayer(ModelLayers.PLAYER_HEAD));
/* 40 */       return new PlayerHeadSpecialRenderer(context.playerSkinRenderCache(), (SkullModelBase)model);
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PlayerSkinRenderCache.RenderInfo argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 46 */     RenderType renderType = (argument != null) ? argument.renderType() : PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
/* 47 */     SkullBlockRenderer.submitSkull(0.0F, poseStack, submitNodeCollector, lightCoords, this.modelBase, renderType, outlineColor, null);
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 52 */     PoseStack poseStack = new PoseStack();
/* 53 */     this.modelBase.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */   
/*    */   public PlayerSkinRenderCache.RenderInfo extractArgument(ItemStack stack) {
/* 59 */     ResolvableProfile profile = (ResolvableProfile)stack.get(DataComponents.PROFILE);
/* 60 */     if (profile == null)
/* 61 */       return null; 
/* 63 */     return this.playerSkinRenderCache.getOrDefault(profile);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\PlayerHeadSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */