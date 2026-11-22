/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class EndCubeSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final RenderType renderType;
/*    */   
/*    */   public EndCubeSpecialRenderer(RenderType renderType) {
/* 20 */     this.renderType = renderType;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */     private final EndCubeSpecialRenderer.Type effect;
/*    */     
/*    */     public static final MapCodec<Unbaked> MAP_CODEC;
/*    */     
/*    */     public Unbaked(EndCubeSpecialRenderer.Type effect) {
/* 23 */       this.effect = effect;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/EndCubeSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public EndCubeSpecialRenderer.Type effect() {
/* 23 */       return this.effect;
/*    */     }
/*    */     
/*    */     static {
/* 24 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EndCubeSpecialRenderer.Type.CODEC.fieldOf("effect").forGetter(Unbaked::effect)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */     
/*    */     public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
/* 30 */       switch (this.effect.ordinal()) {
/*    */         default:
/* 30 */           throw new MatchException(null, null);
/*    */         case 0:
/*    */         
/*    */         case 1:
/*    */           break;
/*    */       } 
/* 30 */       return new EndCubeSpecialRenderer(
/*    */           
/* 32 */           RenderTypes.endGateway());
/*    */     }
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 38 */       return MAP_CODEC;
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 44 */     AbstractEndPortalRenderer.submitSpecial(this.renderType, poseStack, submitNodeCollector);
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 49 */     AbstractEndPortalRenderer.getExtents(output);
/*    */   }
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 53 */     PORTAL("portal"),
/* 54 */     GATEWAY("gateway");
/*    */     
/* 57 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Type(String name) {
/* 62 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 67 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\EndCubeSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */