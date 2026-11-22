/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.MapRenderer;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.resources.model.EquipmentAssetManager;
/*    */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.data.AtlasIds;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface EntityRendererProvider<T extends net.minecraft.world.entity.Entity> {
/*    */   EntityRenderer<T, ?> create(Context paramContext);
/*    */   
/*    */   public static class Context {
/*    */     private final EntityRenderDispatcher entityRenderDispatcher;
/*    */     
/*    */     private final BlockModelResolver blockModelResolver;
/*    */     
/*    */     private final ItemModelResolver itemModelResolver;
/*    */     
/*    */     private final MapRenderer mapRenderer;
/*    */     
/*    */     private final ResourceManager resourceManager;
/*    */     
/*    */     private final EntityModelSet modelSet;
/*    */     
/*    */     private final EquipmentAssetManager equipmentAssets;
/*    */     
/*    */     private final Font font;
/*    */     
/*    */     private final EquipmentLayerRenderer equipmentRenderer;
/*    */     
/*    */     private final AtlasManager atlasManager;
/*    */     
/*    */     private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */     
/*    */     public Context(EntityRenderDispatcher entityRenderDispatcher, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, MapRenderer mapRenderer, ResourceManager resourceManager, EntityModelSet modelSet, EquipmentAssetManager equipmentAssets, AtlasManager atlasManager, Font font, PlayerSkinRenderCache playerSkinRenderCache) {
/* 37 */       this.entityRenderDispatcher = entityRenderDispatcher;
/* 38 */       this.blockModelResolver = blockModelResolver;
/* 39 */       this.itemModelResolver = itemModelResolver;
/* 40 */       this.mapRenderer = mapRenderer;
/* 41 */       this.resourceManager = resourceManager;
/* 42 */       this.modelSet = modelSet;
/* 43 */       this.equipmentAssets = equipmentAssets;
/* 44 */       this.font = font;
/* 45 */       this.atlasManager = atlasManager;
/* 46 */       this.playerSkinRenderCache = playerSkinRenderCache;
/* 47 */       this.equipmentRenderer = new EquipmentLayerRenderer(equipmentAssets, atlasManager.getAtlasOrThrow(AtlasIds.ARMOR_TRIMS));
/*    */     }
/*    */     
/*    */     public EntityRenderDispatcher getEntityRenderDispatcher() {
/* 51 */       return this.entityRenderDispatcher;
/*    */     }
/*    */     
/*    */     public BlockModelResolver getBlockModelResolver() {
/* 55 */       return this.blockModelResolver;
/*    */     }
/*    */     
/*    */     public ItemModelResolver getItemModelResolver() {
/* 59 */       return this.itemModelResolver;
/*    */     }
/*    */     
/*    */     public MapRenderer getMapRenderer() {
/* 63 */       return this.mapRenderer;
/*    */     }
/*    */     
/*    */     public ResourceManager getResourceManager() {
/* 67 */       return this.resourceManager;
/*    */     }
/*    */     
/*    */     public EntityModelSet getModelSet() {
/* 71 */       return this.modelSet;
/*    */     }
/*    */     
/*    */     public EquipmentAssetManager getEquipmentAssets() {
/* 75 */       return this.equipmentAssets;
/*    */     }
/*    */     
/*    */     public EquipmentLayerRenderer getEquipmentRenderer() {
/* 79 */       return this.equipmentRenderer;
/*    */     }
/*    */     
/*    */     public SpriteGetter getSprites() {
/* 83 */       return (SpriteGetter)this.atlasManager;
/*    */     }
/*    */     
/*    */     public TextureAtlas getAtlas(Identifier sheet) {
/* 87 */       return this.atlasManager.getAtlasOrThrow(sheet);
/*    */     }
/*    */     
/*    */     public ModelPart bakeLayer(ModelLayerLocation id) {
/* 91 */       return this.modelSet.bakeLayer(id);
/*    */     }
/*    */     
/*    */     public Font getFont() {
/* 95 */       return this.font;
/*    */     }
/*    */     
/*    */     public PlayerSkinRenderCache getPlayerSkinRenderCache() {
/* 99 */       return this.playerSkinRenderCache;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EntityRendererProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */