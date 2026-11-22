/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ 
/*    */ public class CountConfiguration implements FeatureConfiguration {
/*  9 */   public static final Codec<CountConfiguration> CODEC = IntProviders.codec(0, 256).fieldOf("count")
/* 10 */     .xmap(CountConfiguration::new, CountConfiguration::count).codec();
/*    */   
/*    */   private final IntProvider count;
/*    */   
/*    */   public CountConfiguration(int count) {
/* 15 */     this.count = (IntProvider)ConstantInt.of(count);
/*    */   }
/*    */   
/*    */   public CountConfiguration(IntProvider count) {
/* 19 */     this.count = count;
/*    */   }
/*    */   
/*    */   public IntProvider count() {
/* 23 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\CountConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */