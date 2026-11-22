/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Transformation;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.equipment.ShieldModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.core.component.DataComponentMap;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class ShieldSpecialRenderer implements SpecialModelRenderer<DataComponentMap> {
/* 30 */   public static final Transformation DEFAULT_TRANSFORMATION = new Transformation(null, null, (Vector3fc)new Vector3f(1.0F, -1.0F, -1.0F), null);
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final ShieldModel model;
/*    */   
/*    */   public ShieldSpecialRenderer(SpriteGetter sprites, ShieldModel model) {
/* 41 */     this.sprites = sprites;
/* 42 */     this.model = model;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements SpecialModelRenderer.Unbaked<DataComponentMap> {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/ShieldSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 46 */     public static final Unbaked INSTANCE = new Unbaked();
/*    */     
/* 48 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 52 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public ShieldSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 57 */       return new ShieldSpecialRenderer(context.sprites(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)));
/*    */     }
/*    */   }
/*    */   
/*    */   public DataComponentMap extractArgument(ItemStack stack) {
/* 63 */     return stack.immutableComponents();
/*    */   }
/*    */   
/*    */   public void submit(DataComponentMap components, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 68 */     BannerPatternLayers patterns = (components != null) ? (BannerPatternLayers)components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) : BannerPatternLayers.EMPTY;
/* 69 */     DyeColor baseColor = (components != null) ? (DyeColor)components.get(DataComponents.BASE_COLOR) : null;
/* 70 */     boolean hasPatterns = (!patterns.layers().isEmpty() || baseColor != null);
/* 72 */     SpriteId base = hasPatterns ? Sheets.SHIELD_BASE : Sheets.SHIELD_BASE_NO_PATTERN;
/* 73 */     submitNodeCollector.submitModel((Model)this.model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, base, this.sprites, outlineColor, null);
/* 75 */     if (hasPatterns)
/* 76 */       BannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, (Model)this.model, Unit.INSTANCE, false, Objects.<DyeColor>requireNonNullElse(baseColor, DyeColor.WHITE), patterns, null); 
/* 79 */     if (hasFoil)
/* 80 */       submitNodeCollector.order(patterns.layers().size() + 1).submitModel((Model)this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, this.sprites.get(base), 0, null); 
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 86 */     PoseStack poseStack = new PoseStack();
/* 87 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\ShieldSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */