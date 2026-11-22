/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ 
/*    */ public class CaveCarverConfiguration extends CarverConfiguration {
/*    */   public static final Codec<CaveCarverConfiguration> CODEC;
/*    */   
/*    */   public final FloatProvider horizontalRadiusMultiplier;
/*    */   
/*    */   public final FloatProvider verticalRadiusMultiplier;
/*    */   
/*    */   public final FloatProvider floorLevel;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)CarverConfiguration.CODEC.forGetter(()), (App)FloatProviders.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(()), (App)FloatProviders.CODEC.fieldOf("vertical_radius_multiplier").forGetter(()), (App)FloatProviders.codec(-1.0F, 1.0F).fieldOf("floor_level").forGetter(())).apply((Applicative)i, CaveCarverConfiguration::new));
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, CarverDebugSettings debugSettings, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 27 */     super(probability, y, yScale, lavaLevel, debugSettings, replaceable);
/* 28 */     this.horizontalRadiusMultiplier = horizontalRadiusMultiplier;
/* 29 */     this.verticalRadiusMultiplier = verticalRadiusMultiplier;
/* 30 */     this.floorLevel = floorLevel;
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(float probability, HeightProvider y, FloatProvider yScale, VerticalAnchor lavaLevel, HolderSet<Block> replaceable, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 34 */     this(probability, y, yScale, lavaLevel, CarverDebugSettings.DEFAULT, replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel);
/*    */   }
/*    */   
/*    */   public CaveCarverConfiguration(CarverConfiguration carver, FloatProvider horizontalRadiusMultiplier, FloatProvider verticalRadiusMultiplier, FloatProvider floorLevel) {
/* 38 */     this(carver.probability, carver.y, carver.yScale, carver.lavaLevel, carver.debugSettings, carver.replaceable, horizontalRadiusMultiplier, verticalRadiusMultiplier, floorLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\carver\CaveCarverConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */