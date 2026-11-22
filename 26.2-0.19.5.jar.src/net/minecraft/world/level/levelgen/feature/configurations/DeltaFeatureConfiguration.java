/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DeltaFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<DeltaFeatureConfiguration> CODEC;
/*    */   
/*    */   private final BlockState contents;
/*    */   
/*    */   private final BlockState rim;
/*    */   
/*    */   private final IntProvider size;
/*    */   
/*    */   private final IntProvider rimSize;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("contents").forGetter(()), (App)BlockState.CODEC.fieldOf("rim").forGetter(()), (App)IntProviders.codec(0, 16).fieldOf("size").forGetter(()), (App)IntProviders.codec(0, 16).fieldOf("rim_size").forGetter(())).apply((Applicative)i, DeltaFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public DeltaFeatureConfiguration(BlockState contents, BlockState rim, IntProvider size, IntProvider rimSize) {
/* 23 */     this.contents = contents;
/* 24 */     this.rim = rim;
/* 25 */     this.size = size;
/* 26 */     this.rimSize = rimSize;
/*    */   }
/*    */   
/*    */   public BlockState contents() {
/* 30 */     return this.contents;
/*    */   }
/*    */   
/*    */   public BlockState rim() {
/* 34 */     return this.rim;
/*    */   }
/*    */   
/*    */   public IntProvider size() {
/* 38 */     return this.size;
/*    */   }
/*    */   
/*    */   public IntProvider rimSize() {
/* 42 */     return this.rimSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\DeltaFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */