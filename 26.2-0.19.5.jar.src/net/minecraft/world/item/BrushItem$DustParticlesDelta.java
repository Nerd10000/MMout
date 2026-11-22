/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ final class DustParticlesDelta extends Record {
/*     */   private final double xd;
/*     */   
/*     */   private final double yd;
/*     */   
/*     */   private final double zd;
/*     */   
/*     */   private static final double ALONG_SIDE_DELTA = 1.0D;
/*     */   
/*     */   private static final double OUT_FROM_SIDE_DELTA = 0.1D;
/*     */   
/*     */   private DustParticlesDelta(double xd, double yd, double zd) {
/* 138 */     this.xd = xd;
/* 138 */     this.yd = yd;
/* 138 */     this.zd = zd;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public double xd() {
/* 138 */     return this.xd;
/*     */   }
/*     */   
/*     */   public double yd() {
/* 138 */     return this.yd;
/*     */   }
/*     */   
/*     */   public double zd() {
/* 138 */     return this.zd;
/*     */   }
/*     */   
/*     */   public static DustParticlesDelta fromDirection(Vec3 viewVector, Direction hitDirection) {
/* 144 */     double yd = 0.0D;
/* 145 */     switch (BrushItem.null.$SwitchMap$net$minecraft$core$Direction[hitDirection.ordinal()]) {
/*     */       default:
/* 145 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */       
/*     */       case 6:
/*     */         break;
/*     */     } 
/* 145 */     return 
/*     */       
/* 150 */       new DustParticlesDelta(0.1D, 0.0D, 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BrushItem$DustParticlesDelta.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */