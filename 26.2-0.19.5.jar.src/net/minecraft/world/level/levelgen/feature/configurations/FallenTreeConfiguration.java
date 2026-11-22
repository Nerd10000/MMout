/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ public class FallenTreeConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<FallenTreeConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider trunkProvider;
/*    */   
/*    */   public final IntProvider logLength;
/*    */   
/*    */   public final List<TreeDecorator> stumpDecorators;
/*    */   
/*    */   public final List<TreeDecorator> logDecorators;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)IntProviders.codec(0, 16).fieldOf("log_length").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(())).apply((Applicative)i, FallenTreeConfiguration::new));
/*    */   }
/*    */   
/*    */   protected FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) {
/* 27 */     this.trunkProvider = trunkProvider;
/* 28 */     this.logLength = logLength;
/* 29 */     this.stumpDecorators = stumpDecorators;
/* 30 */     this.logDecorators = logDecorators;
/*    */   }
/*    */   
/*    */   public static class FallenTreeConfigurationBuilder {
/*    */     private final BlockStateProvider trunkProvider;
/*    */     
/*    */     private final IntProvider logLength;
/*    */     
/* 36 */     private List<TreeDecorator> stumpDecorators = new ArrayList<>();
/*    */     
/* 37 */     private List<TreeDecorator> logDecorators = new ArrayList<>();
/*    */     
/*    */     public FallenTreeConfigurationBuilder(BlockStateProvider trunkProvider, IntProvider logLength) {
/* 40 */       this.trunkProvider = trunkProvider;
/* 41 */       this.logLength = logLength;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> stumpDecorators) {
/* 45 */       this.stumpDecorators = stumpDecorators;
/* 46 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> logDecorators) {
/* 50 */       this.logDecorators = logDecorators;
/* 51 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfiguration build() {
/* 55 */       return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\FallenTreeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */