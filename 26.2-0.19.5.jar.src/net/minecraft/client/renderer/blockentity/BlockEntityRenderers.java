/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ 
/*    */ public class BlockEntityRenderers {
/* 14 */   private static final Map<BlockEntityType<?>, BlockEntityRendererProvider<?, ?>> PROVIDERS = Maps.newHashMap();
/*    */   
/*    */   static {
/* 17 */     register(BlockEntityTypes.SIGN, StandingSignRenderer::new);
/* 18 */     register(BlockEntityTypes.HANGING_SIGN, HangingSignRenderer::new);
/* 19 */     register(BlockEntityTypes.MOB_SPAWNER, SpawnerRenderer::new);
/* 20 */     register(BlockEntityTypes.PISTON, paramContext -> new PistonHeadRenderer());
/* 21 */     register(BlockEntityTypes.CHEST, ChestRenderer::new);
/* 22 */     register(BlockEntityTypes.ENDER_CHEST, ChestRenderer::new);
/* 23 */     register(BlockEntityTypes.TRAPPED_CHEST, ChestRenderer::new);
/* 24 */     register(BlockEntityTypes.ENCHANTING_TABLE, EnchantTableRenderer::new);
/* 25 */     register(BlockEntityTypes.LECTERN, LecternRenderer::new);
/* 26 */     register(BlockEntityTypes.END_PORTAL, paramContext -> new TheEndPortalRenderer());
/* 27 */     register(BlockEntityTypes.END_GATEWAY, paramContext -> new TheEndGatewayRenderer());
/* 28 */     register(BlockEntityTypes.BEACON, paramContext -> new BeaconRenderer<>());
/* 29 */     register(BlockEntityTypes.SKULL, SkullBlockRenderer::new);
/* 30 */     register(BlockEntityTypes.BANNER, BannerRenderer::new);
/* 31 */     register(BlockEntityTypes.STRUCTURE_BLOCK, paramContext -> new BlockEntityWithBoundingBoxRenderer<>());
/* 32 */     register(BlockEntityTypes.TEST_INSTANCE_BLOCK, paramContext -> new TestInstanceRenderer());
/* 33 */     register(BlockEntityTypes.SHULKER_BOX, ShulkerBoxRenderer::new);
/* 34 */     register(BlockEntityTypes.CONDUIT, ConduitRenderer::new);
/* 35 */     register(BlockEntityTypes.BELL, BellRenderer::new);
/* 36 */     register(BlockEntityTypes.CAMPFIRE, CampfireRenderer::new);
/* 37 */     register(BlockEntityTypes.BRUSHABLE_BLOCK, BrushableBlockRenderer::new);
/* 38 */     register(BlockEntityTypes.DECORATED_POT, DecoratedPotRenderer::new);
/* 39 */     register(BlockEntityTypes.TRIAL_SPAWNER, TrialSpawnerRenderer::new);
/* 40 */     register(BlockEntityTypes.VAULT, VaultRenderer::new);
/* 41 */     register(BlockEntityTypes.COPPER_GOLEM_STATUE, CopperGolemStatueBlockRenderer::new);
/* 42 */     register(BlockEntityTypes.SHELF, ShelfRenderer::new);
/*    */   }
/*    */   
/*    */   private static <T extends BlockEntity, S extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState> void register(BlockEntityType<? extends T> type, BlockEntityRendererProvider<T, S> renderer) {
/* 46 */     PROVIDERS.put(type, renderer);
/*    */   }
/*    */   
/*    */   public static Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> createEntityRenderers(BlockEntityRendererProvider.Context context) {
/* 50 */     ImmutableMap.Builder<BlockEntityType<?>, BlockEntityRenderer<?, ?>> result = ImmutableMap.builder();
/* 51 */     PROVIDERS.forEach((type, provider) -> {
/*    */           try {
/*    */             result.put(type, provider.create(context));
/* 54 */           } catch (Exception e) {
/*    */             throw new IllegalStateException("Failed to create model for " + String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type)), e);
/*    */           } 
/*    */         });
/* 58 */     return (Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>>)result.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BlockEntityRenderers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */