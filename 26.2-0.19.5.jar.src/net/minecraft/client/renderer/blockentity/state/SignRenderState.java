/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class SignRenderState extends BlockEntityRenderState {
/*    */   public SignText frontText;
/*    */   
/*    */   public SignText backText;
/*    */   
/*    */   public int textLineHeight;
/*    */   
/*    */   public int maxTextLineWidth;
/*    */   
/*    */   public boolean isTextFilteringEnabled;
/*    */   
/*    */   public boolean drawOutline;
/*    */   
/* 14 */   public SignTransformations transformations = SignTransformations.IDENTITY;
/*    */   
/*    */   public static final class SignTransformations extends Record {
/*    */     private final Transformation frontText;
/*    */     
/*    */     private final Transformation backText;
/*    */     
/*    */     public SignTransformations(Transformation frontText, Transformation backText) {
/* 16 */       this.frontText = frontText;
/* 16 */       this.backText = backText;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/state/SignRenderState$SignTransformations;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Transformation frontText() {
/* 16 */       return this.frontText;
/*    */     }
/*    */     
/*    */     public Transformation backText() {
/* 16 */       return this.backText;
/*    */     }
/*    */     
/* 20 */     public static final SignTransformations IDENTITY = new SignTransformations(Transformation.IDENTITY, Transformation.IDENTITY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\SignRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */