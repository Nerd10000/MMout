/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public interface FeatureConfiguration {
/*  9 */   public static final NoneFeatureConfiguration NONE = NoneFeatureConfiguration.INSTANCE;
/*    */   
/*    */   default Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
/* 12 */     return Stream.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\FeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */