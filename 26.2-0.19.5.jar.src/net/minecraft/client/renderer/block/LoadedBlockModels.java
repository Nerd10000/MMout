/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class LoadedBlockModels {
/* 21 */   private static final Matrix4fc IDENTITY = (Matrix4fc)new Matrix4f();
/*    */   
/*    */   private final Map<BlockState, BlockModel.Unbaked> unbakedModels;
/*    */   
/*    */   private final EntityModelSet entityModelSet;
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */   
/*    */   public LoadedBlockModels(Map<BlockState, BlockModel.Unbaked> unbakedModels, EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache) {
/* 29 */     this.unbakedModels = unbakedModels;
/* 30 */     this.entityModelSet = entityModelSet;
/* 31 */     this.sprites = sprites;
/* 32 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*    */   }
/*    */   
/*    */   public CompletableFuture<Map<BlockState, BlockModel>> bake(Function<BlockState, BlockStateModel> bakedBlockStateModels, BlockStateModel missingModel, Executor executor) {
/* 37 */     return CompletableFuture.supplyAsync(() -> {
/*    */           BlockStateModelWrapper blockStateModelWrapper = new BlockStateModelWrapper(missingModel, List.of(), IDENTITY);
/*    */           BlockModel.BakingContext context = new BlockModel.BakingContext(this.entityModelSet, this.sprites, this.playerSkinRenderCache, bakedBlockStateModels, (BlockModel)blockStateModelWrapper);
/*    */           ImmutableMap.Builder<BlockState, BlockModel> result = ImmutableMap.builder();
/*    */           this.unbakedModels.forEach(());
/*    */           return (Map)result.build();
/* 37 */         }executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\LoadedBlockModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */