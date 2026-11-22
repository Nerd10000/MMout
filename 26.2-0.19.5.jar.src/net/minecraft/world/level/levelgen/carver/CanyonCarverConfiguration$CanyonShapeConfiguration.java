/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ 
/*    */ public class CanyonShapeConfiguration {
/*    */   public static final Codec<CanyonShapeConfiguration> CODEC;
/*    */   
/*    */   public final FloatProvider distanceFactor;
/*    */   
/*    */   public final FloatProvider thickness;
/*    */   
/*    */   public final int widthSmoothness;
/*    */   
/*    */   public final FloatProvider horizontalRadiusFactor;
/*    */   
/*    */   public final float verticalRadiusDefaultFactor;
/*    */   
/*    */   public final float verticalRadiusCenterFactor;
/*    */   
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)FloatProviders.CODEC.fieldOf("distance_factor").forGetter(()), (App)FloatProviders.CODEC.fieldOf("thickness").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width_smoothness").forGetter(()), (App)FloatProviders.CODEC.fieldOf("horizontal_radius_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_default_factor").forGetter(()), (App)Codec.FLOAT.fieldOf("vertical_radius_center_factor").forGetter(())).apply((Applicative)i, CanyonShapeConfiguration::new));
/*    */   }
/*    */   
/*    */   public CanyonShapeConfiguration(FloatProvider distanceFactor, FloatProvider thickness, int widthSmoothness, FloatProvider horizontalRadiusFactor, float verticalRadiusDefaultFactor, float verticalRadiusCenterFactor) {
/* 32 */     this.widthSmoothness = widthSmoothness;
/* 33 */     this.horizontalRadiusFactor = horizontalRadiusFactor;
/* 34 */     this.verticalRadiusDefaultFactor = verticalRadiusDefaultFactor;
/* 35 */     this.verticalRadiusCenterFactor = verticalRadiusCenterFactor;
/* 36 */     this.distanceFactor = distanceFactor;
/* 37 */     this.thickness = thickness;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\carver\CanyonCarverConfiguration$CanyonShapeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */