/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SequenceFeature extends Feature<CompositeFeatureConfiguration> {
/*    */   public SequenceFeature(Codec<CompositeFeatureConfiguration> codec) {
/* 10 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<CompositeFeatureConfiguration> context) {
/* 15 */     for (Holder<PlacedFeature> feature : (Iterable<Holder<PlacedFeature>>)((CompositeFeatureConfiguration)context.config()).features()) {
/* 16 */       if (!((PlacedFeature)feature.value()).place(context.level(), context.chunkGenerator(), context.random(), context.origin()))
/* 17 */         return false; 
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SequenceFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */