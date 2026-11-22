/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ 
/*    */ public class ColumnFeatureConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<ColumnFeatureConfiguration> CODEC;
/*    */   
/*    */   private final IntProvider reach;
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)IntProviders.codec(0, 3).fieldOf("reach").forGetter(()), (App)IntProviders.codec(1, 10).fieldOf("height").forGetter(())).apply((Applicative)i, ColumnFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public ColumnFeatureConfiguration(IntProvider reach, IntProvider height) {
/* 18 */     this.reach = reach;
/* 19 */     this.height = height;
/*    */   }
/*    */   
/*    */   public IntProvider reach() {
/* 23 */     return this.reach;
/*    */   }
/*    */   
/*    */   public IntProvider height() {
/* 27 */     return this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\ColumnFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */