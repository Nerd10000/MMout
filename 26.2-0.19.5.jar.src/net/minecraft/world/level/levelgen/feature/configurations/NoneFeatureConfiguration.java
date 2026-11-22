/*   */ package net.minecraft.world.level.levelgen.feature.configurations;
/*   */ 
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.MapCodec;
/*   */ 
/*   */ public class NoneFeatureConfiguration implements FeatureConfiguration {
/* 7 */   public static final NoneFeatureConfiguration INSTANCE = new NoneFeatureConfiguration();
/*   */   
/* 9 */   public static final Codec<NoneFeatureConfiguration> CODEC = MapCodec.unitCodec(INSTANCE);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\NoneFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */