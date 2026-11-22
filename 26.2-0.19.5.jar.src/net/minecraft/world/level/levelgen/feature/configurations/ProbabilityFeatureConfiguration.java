/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ProbabilityFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<ProbabilityFeatureConfiguration> CODEC;
/*    */   
/*    */   public final float probability;
/*    */   
/*    */   static {
/*  7 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(())).apply((Applicative)i, ProbabilityFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public ProbabilityFeatureConfiguration(float probability) {
/* 14 */     this.probability = probability;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\ProbabilityFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */