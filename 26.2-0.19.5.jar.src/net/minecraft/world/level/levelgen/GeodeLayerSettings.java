/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public class GeodeLayerSettings {
/*  7 */   private static final Codec<Double> LAYER_RANGE = Codec.doubleRange(0.01D, 50.0D);
/*    */   
/*    */   public static final Codec<GeodeLayerSettings> CODEC;
/*    */   
/*    */   public final double filling;
/*    */   
/*    */   public final double innerLayer;
/*    */   
/*    */   public final double middleLayer;
/*    */   
/*    */   public final double outerLayer;
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LAYER_RANGE.optionalFieldOf("filling", Double.valueOf(1.7D)).forGetter(()), (App)LAYER_RANGE.optionalFieldOf("inner_layer", Double.valueOf(2.2D)).forGetter(()), (App)LAYER_RANGE.optionalFieldOf("middle_layer", Double.valueOf(3.2D)).forGetter(()), (App)LAYER_RANGE.optionalFieldOf("outer_layer", Double.valueOf(4.2D)).forGetter(())).apply((Applicative)i, GeodeLayerSettings::new));
/*    */   }
/*    */   
/*    */   public GeodeLayerSettings(double filling, double innerLayer, double middleLayer, double outerLayer) {
/* 21 */     this.filling = filling;
/* 22 */     this.innerLayer = innerLayer;
/* 23 */     this.middleLayer = middleLayer;
/* 24 */     this.outerLayer = outerLayer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\GeodeLayerSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */