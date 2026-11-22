/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BlockEntityRenderDispatcher implements ResourceManagerReloadListener {
/*  30 */   private Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>> renderers = (Map<BlockEntityType<?>, BlockEntityRenderer<?, ?>>)ImmutableMap.of();
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   private final Supplier<EntityModelSet> entityModelSet;
/*     */   
/*     */   private Vec3 cameraPos;
/*     */   
/*     */   private final BlockModelResolver blockModelResolver;
/*     */   
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   private final EntityRenderDispatcher entityRenderer;
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   public BlockEntityRenderDispatcher(Font font, Supplier<EntityModelSet> entityModelSet, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, EntityRenderDispatcher entityRenderer, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache) {
/*  43 */     this.blockModelResolver = blockModelResolver;
/*  44 */     this.itemModelResolver = itemModelResolver;
/*  45 */     this.entityRenderer = entityRenderer;
/*  46 */     this.font = font;
/*  47 */     this.entityModelSet = entityModelSet;
/*  48 */     this.sprites = sprites;
/*  49 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*     */   }
/*     */   
/*     */   public <E extends net.minecraft.world.level.block.entity.BlockEntity, S extends BlockEntityRenderState> BlockEntityRenderer<E, S> getRenderer(E blockEntity) {
/*  54 */     return (BlockEntityRenderer<E, S>)this.renderers.get(blockEntity.getType());
/*     */   }
/*     */   
/*     */   public <E extends net.minecraft.world.level.block.entity.BlockEntity, S extends BlockEntityRenderState> BlockEntityRenderer<E, S> getRenderer(S state) {
/*  59 */     return (BlockEntityRenderer<E, S>)this.renderers.get(((BlockEntityRenderState)state).blockEntityType);
/*     */   }
/*     */   
/*     */   public void prepare(Vec3 cameraPos) {
/*  63 */     this.cameraPos = cameraPos;
/*     */   }
/*     */   
/*     */   public <E extends net.minecraft.world.level.block.entity.BlockEntity, S extends BlockEntityRenderState> S tryExtractRenderState(E blockEntity, float partialTicks, ModelFeatureRenderer.CrumblingOverlay breakProgress, boolean isGloballyRendered) {
/*  67 */     BlockEntityRenderer<E, S> renderer = getRenderer(blockEntity);
/*  68 */     if (renderer == null)
/*  69 */       return null; 
/*  73 */     if (!blockEntity.hasLevel() || !blockEntity.getType().isValid(blockEntity.getBlockState()))
/*  74 */       return null; 
/*  77 */     if (isGloballyRendered != renderer.shouldRenderOffScreen())
/*  78 */       return null; 
/*  81 */     if (!renderer.shouldRender(blockEntity, this.cameraPos))
/*  82 */       return null; 
/*  84 */     Vec3 cameraPosition = this.cameraPos;
/*  85 */     S state = renderer.createRenderState();
/*  86 */     renderer.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  87 */     return state;
/*     */   }
/*     */   
/*     */   public <S extends BlockEntityRenderState> void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  91 */     BlockEntityRenderer<?, S> renderer = getRenderer(state);
/*  92 */     if (renderer == null)
/*     */       return; 
/*     */     try {
/*  97 */       renderer.submit(state, poseStack, submitNodeCollector, camera);
/*  98 */     } catch (Throwable t) {
/*  99 */       CrashReport report = CrashReport.forThrowable(t, "Rendering Block Entity");
/* 100 */       CrashReportCategory category = report.addCategory("Block Entity Details");
/* 102 */       state.fillCrashReportCategory(category);
/* 104 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 110 */     BlockEntityRendererProvider.Context context = new BlockEntityRendererProvider.Context(this, this.blockModelResolver, this.itemModelResolver, this.entityRenderer, this.entityModelSet.get(), this.font, this.sprites, this.playerSkinRenderCache);
/* 111 */     this.renderers = BlockEntityRenderers.createEntityRenderers(context);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BlockEntityRenderDispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */