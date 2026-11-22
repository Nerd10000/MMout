/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ public final class UVPair extends Record {
/*    */   private final float u;
/*    */   
/*    */   private final float v;
/*    */   
/*    */   public UVPair(float u, float v) {
/*  3 */     this.u = u;
/*  3 */     this.v = v;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/builders/UVPair;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/geom/builders/UVPair;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/builders/UVPair;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/geom/builders/UVPair;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float u() {
/*  3 */     return this.u;
/*    */   }
/*    */   
/*    */   public float v() {
/*  3 */     return this.v;
/*    */   }
/*    */   
/*    */   public String toString() {
/*  6 */     return "(" + this.u + "," + this.v + ")";
/*    */   }
/*    */   
/*    */   public static long pack(float u, float v) {
/* 11 */     long high = Float.floatToIntBits(u) & 0xFFFFFFFFL;
/* 12 */     long low = Float.floatToIntBits(v) & 0xFFFFFFFFL;
/* 13 */     return high << 32L | low;
/*    */   }
/*    */   
/*    */   public static float unpackU(long packedUV) {
/* 17 */     int bits = (int)(packedUV >> 32L);
/* 18 */     return Float.intBitsToFloat(bits);
/*    */   }
/*    */   
/*    */   public static float unpackV(long packedUV) {
/* 22 */     return Float.intBitsToFloat((int)packedUV);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\builders\UVPair.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */