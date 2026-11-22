/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class NoOpFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public NoOpFeature(Codec<NoneFeatureConfiguration> codec) {
/*  8 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 13 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\NoOpFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */