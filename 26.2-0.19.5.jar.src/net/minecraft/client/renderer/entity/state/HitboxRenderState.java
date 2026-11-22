/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ public final class HitboxRenderState extends Record {
/*    */   private final double x0;
/*    */   
/*    */   private final double y0;
/*    */   
/*    */   private final double z0;
/*    */   
/*    */   private final double x1;
/*    */   
/*    */   private final double y1;
/*    */   
/*    */   private final double z1;
/*    */   
/*    */   private final float offsetX;
/*    */   
/*    */   private final float offsetY;
/*    */   
/*    */   private final float offsetZ;
/*    */   
/*    */   private final float red;
/*    */   
/*    */   private final float green;
/*    */   
/*    */   private final float blue;
/*    */   
/*    */   public float blue() {
/*  3 */     return this.blue;
/*    */   }
/*    */   
/*    */   public float green() {
/*  3 */     return this.green;
/*    */   }
/*    */   
/*    */   public float red() {
/*  3 */     return this.red;
/*    */   }
/*    */   
/*    */   public float offsetZ() {
/*  3 */     return this.offsetZ;
/*    */   }
/*    */   
/*    */   public float offsetY() {
/*  3 */     return this.offsetY;
/*    */   }
/*    */   
/*    */   public float offsetX() {
/*  3 */     return this.offsetX;
/*    */   }
/*    */   
/*    */   public double z1() {
/*  3 */     return this.z1;
/*    */   }
/*    */   
/*    */   public double y1() {
/*  3 */     return this.y1;
/*    */   }
/*    */   
/*    */   public double x1() {
/*  3 */     return this.x1;
/*    */   }
/*    */   
/*    */   public double z0() {
/*  3 */     return this.z0;
/*    */   }
/*    */   
/*    */   public double y0() {
/*  3 */     return this.y0;
/*    */   }
/*    */   
/*    */   public double x0() {
/*  3 */     return this.x0;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/HitboxRenderState;
/*    */   }
/*    */   
/*    */   public HitboxRenderState(double x0, double y0, double z0, double x1, double y1, double z1, float offsetX, float offsetY, float offsetZ, float red, float green, float blue) {
/*  3 */     this.x0 = x0;
/*  3 */     this.y0 = y0;
/*  3 */     this.z0 = z0;
/*  3 */     this.x1 = x1;
/*  3 */     this.y1 = y1;
/*  3 */     this.z1 = z1;
/*  3 */     this.offsetX = offsetX;
/*  3 */     this.offsetY = offsetY;
/*  3 */     this.offsetZ = offsetZ;
/*  3 */     this.red = red;
/*  3 */     this.green = green;
/*  3 */     this.blue = blue;
/*    */   }
/*    */   
/*    */   public HitboxRenderState(double x0, double y0, double z0, double x1, double y1, double z1, float red, float green, float blue) {
/* 18 */     this(x0, y0, z0, x1, y1, z1, 0.0F, 0.0F, 0.0F, red, green, blue);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\HitboxRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */