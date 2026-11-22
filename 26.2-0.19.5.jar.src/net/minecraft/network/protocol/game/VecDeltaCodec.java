/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class VecDeltaCodec {
/*    */   private static final double TRUNCATION_STEPS = 4096.0D;
/*    */   
/* 10 */   private Vec3 base = Vec3.ZERO;
/*    */   
/*    */   @VisibleForTesting
/*    */   static long encode(double input) {
/* 15 */     return Math.round(input * 4096.0D);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static double decode(long v) {
/* 21 */     return v / 4096.0D;
/*    */   }
/*    */   
/*    */   public Vec3 decode(long xa, long ya, long za) {
/* 25 */     if (xa == 0L && ya == 0L && za == 0L)
/* 26 */       return this.base; 
/* 28 */     double x = (xa == 0L) ? this.base.x : decode(encode(this.base.x) + xa);
/* 29 */     double y = (ya == 0L) ? this.base.y : decode(encode(this.base.y) + ya);
/* 30 */     double z = (za == 0L) ? this.base.z : decode(encode(this.base.z) + za);
/* 31 */     return new Vec3(x, y, z);
/*    */   }
/*    */   
/*    */   public long encodeX(Vec3 pos) {
/* 35 */     return encode(pos.x) - encode(this.base.x);
/*    */   }
/*    */   
/*    */   public long encodeY(Vec3 pos) {
/* 39 */     return encode(pos.y) - encode(this.base.y);
/*    */   }
/*    */   
/*    */   public long encodeZ(Vec3 pos) {
/* 43 */     return encode(pos.z) - encode(this.base.z);
/*    */   }
/*    */   
/*    */   public Vec3 delta(Vec3 pos) {
/* 47 */     return pos.subtract(this.base);
/*    */   }
/*    */   
/*    */   public void setBase(Vec3 base) {
/* 51 */     this.base = base;
/*    */   }
/*    */   
/*    */   public Vec3 getBase() {
/* 55 */     return this.base;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\VecDeltaCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */