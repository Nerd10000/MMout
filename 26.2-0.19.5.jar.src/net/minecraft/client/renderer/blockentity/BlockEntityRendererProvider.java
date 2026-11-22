/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface BlockEntityRendererProvider<T extends net.minecraft.world.level.block.entity.BlockEntity, S extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState> {
/*    */   BlockEntityRenderer<T, S> create(Context paramContext);
/*    */   
/*    */   public static final class Context extends Record {
/*    */     private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*    */     
/*    */     private final BlockModelResolver blockModelResolver;
/*    */     
/*    */     private final ItemModelResolver itemModelResolver;
/*    */     
/*    */     private final EntityRenderDispatcher entityRenderer;
/*    */     
/*    */     private final EntityModelSet entityModelSet;
/*    */     
/*    */     private final Font font;
/*    */     
/*    */     private final SpriteGetter sprites;
/*    */     
/*    */     private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */     
/*    */     public Context(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, EntityRenderDispatcher entityRenderer, EntityModelSet entityModelSet, Font font, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache) {
/* 17 */       this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
/* 17 */       this.blockModelResolver = blockModelResolver;
/* 17 */       this.itemModelResolver = itemModelResolver;
/* 17 */       this.entityRenderer = entityRenderer;
/* 17 */       this.entityModelSet = entityModelSet;
/* 17 */       this.font = font;
/* 17 */       this.sprites = sprites;
/* 17 */       this.playerSkinRenderCache = playerSkinRenderCache;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/BlockEntityRendererProvider$Context;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public BlockEntityRenderDispatcher blockEntityRenderDispatcher() {
/* 17 */       return this.blockEntityRenderDispatcher;
/*    */     }
/*    */     
/*    */     public BlockModelResolver blockModelResolver() {
/* 17 */       return this.blockModelResolver;
/*    */     }
/*    */     
/*    */     public ItemModelResolver itemModelResolver() {
/* 17 */       return this.itemModelResolver;
/*    */     }
/*    */     
/*    */     public EntityRenderDispatcher entityRenderer() {
/* 17 */       return this.entityRenderer;
/*    */     }
/*    */     
/*    */     public EntityModelSet entityModelSet() {
/* 17 */       return this.entityModelSet;
/*    */     }
/*    */     
/*    */     public Font font() {
/* 17 */       return this.font;
/*    */     }
/*    */     
/*    */     public SpriteGetter sprites() {
/* 17 */       return this.sprites;
/*    */     }
/*    */     
/*    */     public PlayerSkinRenderCache playerSkinRenderCache() {
/* 17 */       return this.playerSkinRenderCache;
/*    */     }
/*    */     
/*    */     public ModelPart bakeLayer(ModelLayerLocation id) {
/* 28 */       return this.entityModelSet.bakeLayer(id);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BlockEntityRendererProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */