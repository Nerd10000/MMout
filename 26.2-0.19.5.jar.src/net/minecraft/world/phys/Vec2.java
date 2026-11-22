/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Vec2 {
/* 12 */   public static final Vec2 ZERO = new Vec2(0.0F, 0.0F);
/*    */   
/* 13 */   public static final Vec2 ONE = new Vec2(1.0F, 1.0F);
/*    */   
/* 14 */   public static final Vec2 UNIT_X = new Vec2(1.0F, 0.0F);
/*    */   
/* 15 */   public static final Vec2 NEG_UNIT_X = new Vec2(-1.0F, 0.0F);
/*    */   
/* 16 */   public static final Vec2 UNIT_Y = new Vec2(0.0F, 1.0F);
/*    */   
/* 17 */   public static final Vec2 NEG_UNIT_Y = new Vec2(0.0F, -1.0F);
/*    */   
/* 18 */   public static final Vec2 MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
/*    */   
/* 19 */   public static final Vec2 MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);
/*    */   
/*    */   public static final Codec<Vec2> CODEC;
/*    */   
/*    */   public final float x;
/*    */   
/*    */   public final float y;
/*    */   
/*    */   static {
/* 21 */     CODEC = Codec.FLOAT.listOf().comapFlatMap(input -> Util.fixedSize(input, 2).map(()), vec -> List.of(Float.valueOf(vec.x), Float.valueOf(vec.y)));
/*    */   }
/*    */   
/*    */   public Vec2(float x, float y) {
/* 30 */     this.x = x;
/* 31 */     this.y = y;
/*    */   }
/*    */   
/*    */   public Vec2 scale(float s) {
/* 35 */     return new Vec2(this.x * s, this.y * s);
/*    */   }
/*    */   
/*    */   public float dot(Vec2 v) {
/* 39 */     return this.x * v.x + this.y * v.y;
/*    */   }
/*    */   
/*    */   public Vec2 add(Vec2 rhs) {
/* 43 */     return new Vec2(this.x + rhs.x, this.y + rhs.y);
/*    */   }
/*    */   
/*    */   public Vec2 add(float v) {
/* 47 */     return new Vec2(this.x + v, this.y + v);
/*    */   }
/*    */   
/*    */   public boolean equals(Object rhs) {
/*    */     Vec2 other;
/* 52 */     if (this == rhs)
/* 53 */       return true; 
/* 55 */     if (rhs instanceof Vec2) {
/* 55 */       other = (Vec2)rhs;
/*    */     } else {
/* 56 */       return false;
/*    */     } 
/* 58 */     return (this.x == other.x && this.y == other.y);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hash(new Object[] { Float.valueOf(this.x), Float.valueOf(this.y) });
/*    */   }
/*    */   
/*    */   public Vec2 normalized() {
/* 67 */     float dist = Mth.sqrt(this.x * this.x + this.y * this.y);
/* 68 */     return (dist < 1.0E-4F) ? ZERO : new Vec2(this.x / dist, this.y / dist);
/*    */   }
/*    */   
/*    */   public float length() {
/* 72 */     return Mth.sqrt(this.x * this.x + this.y * this.y);
/*    */   }
/*    */   
/*    */   public float lengthSquared() {
/* 76 */     return this.x * this.x + this.y * this.y;
/*    */   }
/*    */   
/*    */   public float distanceToSqr(Vec2 p) {
/* 80 */     float xd = p.x - this.x;
/* 81 */     float yd = p.y - this.y;
/* 82 */     return xd * xd + yd * yd;
/*    */   }
/*    */   
/*    */   public Vec2 negated() {
/* 86 */     return new Vec2(-this.x, -this.y);
/*    */   }
/*    */   
/*    */   public Vec2 rotate(double angleRadians) {
/* 90 */     float cosine = Mth.cos(angleRadians);
/* 91 */     float sine = Mth.sin(angleRadians);
/* 92 */     return new Vec2(this.x * cosine - this.y * sine, this.y * cosine + this.x * sine);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\Vec2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */