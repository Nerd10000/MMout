/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ 
/*     */ public interface ConditionSource extends Function<SurfaceRules.Context, SurfaceRules.Condition> {
/* 479 */   public static final Codec<ConditionSource> CODEC = BuiltInRegistries.MATERIAL_CONDITION.byNameCodec().dispatch(ConditionSource::codec, Function.identity());
/*     */   
/*     */   static MapCodec<? extends ConditionSource> bootstrap(Registry<MapCodec<? extends ConditionSource>> registry) {
/* 482 */     SurfaceRules.register(registry, "biome", (MapCodec)SurfaceRules.BiomeConditionSource.CODEC);
/* 483 */     SurfaceRules.register(registry, "noise_threshold", (MapCodec)SurfaceRules.NoiseThresholdConditionSource.CODEC);
/* 484 */     SurfaceRules.register(registry, "vertical_gradient", (MapCodec)SurfaceRules.VerticalGradientConditionSource.CODEC);
/* 485 */     SurfaceRules.register(registry, "y_above", (MapCodec)SurfaceRules.YConditionSource.CODEC);
/* 486 */     SurfaceRules.register(registry, "water", (MapCodec)SurfaceRules.WaterConditionSource.CODEC);
/* 487 */     SurfaceRules.register(registry, "temperature", (MapCodec)SurfaceRules.Temperature.CODEC);
/* 488 */     SurfaceRules.register(registry, "steep", (MapCodec)SurfaceRules.Steep.CODEC);
/* 489 */     SurfaceRules.register(registry, "not", (MapCodec)SurfaceRules.NotConditionSource.CODEC);
/* 490 */     SurfaceRules.register(registry, "hole", (MapCodec)SurfaceRules.Hole.CODEC);
/* 491 */     SurfaceRules.register(registry, "above_preliminary_surface", (MapCodec)SurfaceRules.AbovePreliminarySurface.CODEC);
/* 492 */     return SurfaceRules.register(registry, "stone_depth", (MapCodec)SurfaceRules.StoneDepthCheck.CODEC);
/*     */   }
/*     */   
/*     */   MapCodec<? extends ConditionSource> codec();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$ConditionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */