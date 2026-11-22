/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ScalableParticleOptionsBase implements ParticleOptions {
/*    */   public static final float MIN_SCALE = 0.01F;
/*    */   
/*    */   public static final float MAX_SCALE = 4.0F;
/*    */   
/*    */   protected static final Codec<Float> SCALE;
/*    */   
/*    */   private final float scale;
/*    */   
/*    */   static {
/* 12 */     SCALE = Codec.FLOAT.validate(v -> (v.floatValue() >= 0.01F && v.floatValue() <= 4.0F) ? DataResult.success(v) : DataResult.error(()));
/*    */   }
/*    */   
/*    */   public ScalableParticleOptionsBase(float scale) {
/* 20 */     this.scale = Mth.clamp(scale, 0.01F, 4.0F);
/*    */   }
/*    */   
/*    */   public float getScale() {
/* 24 */     return this.scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\particles\ScalableParticleOptionsBase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */