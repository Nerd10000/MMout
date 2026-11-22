/*     */ package net.minecraft.server.chase;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ final class PlayerPosition extends Record {
/*     */   private final String dimensionName;
/*     */   
/*     */   private final double x;
/*     */   
/*     */   private final double y;
/*     */   
/*     */   private final double z;
/*     */   
/*     */   private final float yRot;
/*     */   
/*     */   private final float xRot;
/*     */   
/*     */   private PlayerPosition(String dimensionName, double x, double y, double z, float yRot, float xRot) {
/* 142 */     this.dimensionName = dimensionName;
/* 142 */     this.x = x;
/* 142 */     this.y = y;
/* 142 */     this.z = z;
/* 142 */     this.yRot = yRot;
/* 142 */     this.xRot = xRot;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/chase/ChaseServer$PlayerPosition;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String dimensionName() {
/* 142 */     return this.dimensionName;
/*     */   }
/*     */   
/*     */   public double x() {
/* 142 */     return this.x;
/*     */   }
/*     */   
/*     */   public double y() {
/* 142 */     return this.y;
/*     */   }
/*     */   
/*     */   public double z() {
/* 142 */     return this.z;
/*     */   }
/*     */   
/*     */   public float yRot() {
/* 142 */     return this.yRot;
/*     */   }
/*     */   
/*     */   public float xRot() {
/* 142 */     return this.xRot;
/*     */   }
/*     */   
/*     */   private String format() {
/* 144 */     return String.format(Locale.ROOT, "t %s %.2f %.2f %.2f %.2f %.2f\n", new Object[] { this.dimensionName, Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), Float.valueOf(this.yRot), Float.valueOf(this.xRot) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\chase\ChaseServer$PlayerPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */