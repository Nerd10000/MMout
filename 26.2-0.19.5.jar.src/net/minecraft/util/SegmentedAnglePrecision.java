/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class SegmentedAnglePrecision {
/*    */   private final int mask;
/*    */   
/*    */   private final int precision;
/*    */   
/*    */   private final float degreeToAngle;
/*    */   
/*    */   private final float angleToDegree;
/*    */   
/*    */   public SegmentedAnglePrecision(int bitPrecision) {
/* 21 */     if (bitPrecision < 2)
/* 22 */       throw new IllegalArgumentException("Precision cannot be less than 2 bits"); 
/* 24 */     if (bitPrecision > 30)
/* 25 */       throw new IllegalArgumentException("Precision cannot be greater than 30 bits"); 
/* 28 */     int twoPi = 1 << bitPrecision;
/* 29 */     this.mask = twoPi - 1;
/* 30 */     this.precision = bitPrecision;
/* 31 */     this.degreeToAngle = twoPi / 360.0F;
/* 32 */     this.angleToDegree = 360.0F / twoPi;
/*    */   }
/*    */   
/*    */   public boolean isSameAxis(int binaryAngleA, int binaryAngleB) {
/* 36 */     int semicircleMask = getMask() >> 1;
/* 38 */     return ((binaryAngleA & semicircleMask) == (binaryAngleB & semicircleMask));
/*    */   }
/*    */   
/*    */   public int fromDirection(Direction direction) {
/* 42 */     if (direction.getAxis().isVertical())
/* 43 */       return 0; 
/* 45 */     int segmentedAngle2bit = direction.get2DDataValue();
/* 46 */     return segmentedAngle2bit << this.precision - 2;
/*    */   }
/*    */   
/*    */   public int fromDegreesWithTurns(float degrees) {
/* 53 */     return Math.round(degrees * this.degreeToAngle);
/*    */   }
/*    */   
/*    */   public int fromDegrees(float degrees) {
/* 57 */     return normalize(fromDegreesWithTurns(degrees));
/*    */   }
/*    */   
/*    */   public float toDegreesWithTurns(int binaryAngle) {
/* 64 */     return binaryAngle * this.angleToDegree;
/*    */   }
/*    */   
/*    */   public float toDegrees(int binaryAngle) {
/* 71 */     float degrees = toDegreesWithTurns(normalize(binaryAngle));
/* 72 */     return (degrees >= 180.0F) ? (degrees - 360.0F) : degrees;
/*    */   }
/*    */   
/*    */   public int normalize(int binaryAngle) {
/* 79 */     return binaryAngle & this.mask;
/*    */   }
/*    */   
/*    */   public int getMask() {
/* 83 */     return this.mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SegmentedAnglePrecision.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */