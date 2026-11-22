/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class ScoreboardNameProviders {
/*    */   private static final Codec<ScoreboardNameProvider> TYPED_CODEC;
/*    */   
/*    */   static {
/* 10 */     TYPED_CODEC = BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.byNameCodec().dispatch(ScoreboardNameProvider::codec, c -> c);
/*    */   }
/*    */   
/* 12 */   public static final Codec<ScoreboardNameProvider> CODEC = Codec.lazyInitialized(() -> Codec.either(ContextScoreboardNameProvider.INLINE_CODEC, TYPED_CODEC).xmap(Either::unwrap, ()));
/*    */   
/*    */   public static MapCodec<? extends ScoreboardNameProvider> bootstrap(Registry<MapCodec<? extends ScoreboardNameProvider>> registry) {
/* 20 */     Registry.register(registry, "fixed", FixedScoreboardNameProvider.MAP_CODEC);
/* 21 */     return (MapCodec<? extends ScoreboardNameProvider>)Registry.register(registry, "context", ContextScoreboardNameProvider.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\score\ScoreboardNameProviders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */