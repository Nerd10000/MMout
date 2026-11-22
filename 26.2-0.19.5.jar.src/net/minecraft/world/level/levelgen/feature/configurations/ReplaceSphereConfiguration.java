/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceSphereConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<ReplaceSphereConfiguration> CODEC;
/*    */   
/*    */   public final BlockState targetState;
/*    */   
/*    */   public final BlockState replaceState;
/*    */   
/*    */   private final IntProvider radius;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)IntProviders.codec(0, 12).fieldOf("radius").forGetter(())).apply((Applicative)i, ReplaceSphereConfiguration::new));
/*    */   }
/*    */   
/*    */   public ReplaceSphereConfiguration(BlockState targetState, BlockState replaceState, IntProvider radius) {
/* 22 */     this.targetState = targetState;
/* 23 */     this.replaceState = replaceState;
/* 24 */     this.radius = radius;
/*    */   }
/*    */   
/*    */   public IntProvider radius() {
/* 28 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceSphereConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */