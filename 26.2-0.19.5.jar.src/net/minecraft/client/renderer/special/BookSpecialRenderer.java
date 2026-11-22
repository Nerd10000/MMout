/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class BookSpecialRenderer implements NoDataSpecialModelRenderer {
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final BookModel model;
/*    */   
/*    */   private final BookModel.State state;
/*    */   
/*    */   public BookSpecialRenderer(SpriteGetter sprites, BookModel model, BookModel.State state) {
/* 24 */     this.sprites = sprites;
/* 25 */     this.model = model;
/* 26 */     this.state = state;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */     private final float openAngle;
/*    */     
/*    */     private final float page1;
/*    */     
/*    */     private final float page2;
/*    */     
/*    */     public static final MapCodec<Unbaked> MAP_CODEC;
/*    */     
/*    */     public Unbaked(float openAngle, float page1, float page2) {
/* 29 */       this.openAngle = openAngle;
/* 29 */       this.page1 = page1;
/* 29 */       this.page2 = page2;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/special/BookSpecialRenderer$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float openAngle() {
/* 29 */       return this.openAngle;
/*    */     }
/*    */     
/*    */     public float page1() {
/* 29 */       return this.page1;
/*    */     }
/*    */     
/*    */     public float page2() {
/* 29 */       return this.page2;
/*    */     }
/*    */     
/*    */     static {
/* 34 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("open_angle").forGetter(Unbaked::openAngle), (App)Codec.FLOAT.fieldOf("page1").forGetter(Unbaked::page1), (App)Codec.FLOAT.fieldOf("page2").forGetter(Unbaked::page2)).apply((Applicative)i, Unbaked::new));
/*    */     }
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 42 */       return MAP_CODEC;
/*    */     }
/*    */     
/*    */     public BookSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 47 */       return new BookSpecialRenderer(context
/* 48 */           .sprites(), new BookModel(context
/* 49 */             .entityModelSet().bakeLayer(ModelLayers.BOOK)), new BookModel.State(this.openAngle * 0.017453292F, this.page1, this.page2));
/*    */     }
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
/* 57 */     submitNodeCollector.submitModel((Model)this.model, this.state, poseStack, lightCoords, overlayCoords, -1, EnchantTableRenderer.BOOK_TEXTURE, this.sprites, outlineColor, null);
/*    */   }
/*    */   
/*    */   public void getExtents(Consumer<Vector3fc> output) {
/* 62 */     PoseStack poseStack = new PoseStack();
/* 63 */     this.model.setupAnim(this.state);
/* 64 */     this.model.root().getExtentsForGui(poseStack, output);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\BookSpecialRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */