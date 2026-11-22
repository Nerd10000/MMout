/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ 
/*     */ public interface RuleSource extends Function<SurfaceRules.Context, SurfaceRules.SurfaceRule> {
/* 500 */   public static final Codec<RuleSource> CODEC = BuiltInRegistries.MATERIAL_RULE.byNameCodec().dispatch(RuleSource::codec, Function.identity());
/*     */   
/*     */   static MapCodec<? extends RuleSource> bootstrap(Registry<MapCodec<? extends RuleSource>> registry) {
/* 503 */     SurfaceRules.register(registry, "bandlands", (MapCodec)SurfaceRules.Bandlands.CODEC);
/* 504 */     SurfaceRules.register(registry, "block", (MapCodec)SurfaceRules.BlockRuleSource.CODEC);
/* 505 */     SurfaceRules.register(registry, "sequence", (MapCodec)SurfaceRules.SequenceRuleSource.CODEC);
/* 506 */     return SurfaceRules.register(registry, "condition", (MapCodec)SurfaceRules.TestRuleSource.CODEC);
/*     */   }
/*     */   
/*     */   MapCodec<? extends RuleSource> codec();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$RuleSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */