/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public final class Submit extends Record implements TranslucentSubmit {
/*    */   private final Matrix4fc pose;
/*    */   
/*    */   private final float x;
/*    */   
/*    */   private final float y;
/*    */   
/*    */   private final Component text;
/*    */   
/*    */   private final int lightCoords;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final int backgroundColor;
/*    */   
/*    */   private final Font.DisplayMode displayMode;
/*    */   
/*    */   public Submit(Matrix4fc pose, float x, float y, Component text, int lightCoords, int color, int backgroundColor, Font.DisplayMode displayMode) {
/* 49 */     this.pose = pose;
/* 49 */     this.x = x;
/* 49 */     this.y = y;
/* 49 */     this.text = text;
/* 49 */     this.lightCoords = lightCoords;
/* 49 */     this.color = color;
/* 49 */     this.backgroundColor = backgroundColor;
/* 49 */     this.displayMode = displayMode;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #49	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/NameTagFeatureRenderer$Submit;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Matrix4fc pose() {
/* 49 */     return this.pose;
/*    */   }
/*    */   
/*    */   public float x() {
/* 49 */     return this.x;
/*    */   }
/*    */   
/*    */   public float y() {
/* 49 */     return this.y;
/*    */   }
/*    */   
/*    */   public Component text() {
/* 49 */     return this.text;
/*    */   }
/*    */   
/*    */   public int lightCoords() {
/* 49 */     return this.lightCoords;
/*    */   }
/*    */   
/*    */   public int color() {
/* 49 */     return this.color;
/*    */   }
/*    */   
/*    */   public int backgroundColor() {
/* 49 */     return this.backgroundColor;
/*    */   }
/*    */   
/*    */   public Font.DisplayMode displayMode() {
/* 49 */     return this.displayMode;
/*    */   }
/*    */   
/*    */   public float distanceToCameraSq() {
/* 61 */     return TranslucentSubmit.computeDistanceToCameraSq(this.pose);
/*    */   }
/*    */   
/*    */   public FeatureRendererType<Submit> featureType() {
/* 66 */     return NameTagFeatureRenderer.TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\NameTagFeatureRenderer$Submit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */