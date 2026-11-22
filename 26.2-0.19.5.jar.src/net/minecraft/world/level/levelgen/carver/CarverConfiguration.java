/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CarverConfiguration extends ProbabilityFeatureConfiguration {
/*    */   public static final MapCodec<CarverConfiguration> CODEC;
/*    */   
/*    */   public final HeightProvider y;
/*    */   
/*    */   public final FloatProvider yScale;
/*    */   
/*    */   public final VerticalAnchor lavaLevel;
/*    */   
/*    */   public final CarverDebugSettings debugSettings;
/*    */   
/*    */   public final HolderSet<Block> replaceable;
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)HeightProvider.CODEC.fieldOf("y").forGetter(()), (App)FloatProviders.CODEC.fieldOf("yScale").forGetter(()), (App)VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(()), (App)CarverDebugSettings.CODEC.optionalFieldOf("debug_settings", CarverDebugSettings.DEFAULT).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(())).apply((Applicative)i, CarverConfiguration::new));
/*    */   }
/*    */   
/*    */   public CarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable) {
/* 34 */     super(probability);
/* 35 */     this.y = y;
/* 36 */     this.yScale = yScale;
/* 37 */     this.lavaLevel = lavaLevel;
/* 38 */     this.debugSettings = debugSettings;
/* 39 */     this.replaceable = replaceable;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\carver\CarverConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */