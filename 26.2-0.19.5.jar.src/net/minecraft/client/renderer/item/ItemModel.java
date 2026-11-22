/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.util.RegistryContextSwapper;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public interface ItemModel {
/*    */   void update(ItemStackRenderState paramItemStackRenderState, ItemStack paramItemStack, ItemModelResolver paramItemModelResolver, ItemDisplayContext paramItemDisplayContext, ClientLevel paramClientLevel, ItemOwner paramItemOwner, int paramInt);
/*    */   
/*    */   public static final class BakingContext extends Record implements SpecialModelRenderer.BakingContext {
/*    */     private final ModelBaker blockModelBaker;
/*    */     
/*    */     private final EntityModelSet entityModelSet;
/*    */     
/*    */     private final SpriteGetter sprites;
/*    */     
/*    */     private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */     
/*    */     private final MissingItemModel missingItemModel;
/*    */     
/*    */     private final RegistryContextSwapper contextSwapper;
/*    */     
/*    */     public BakingContext(ModelBaker blockModelBaker, EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, MissingItemModel missingItemModel, RegistryContextSwapper contextSwapper) {
/* 30 */       this.blockModelBaker = blockModelBaker;
/* 30 */       this.entityModelSet = entityModelSet;
/* 30 */       this.sprites = sprites;
/* 30 */       this.playerSkinRenderCache = playerSkinRenderCache;
/* 30 */       this.missingItemModel = missingItemModel;
/* 30 */       this.contextSwapper = contextSwapper;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/ItemModel$BakingContext;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ModelBaker blockModelBaker() {
/* 30 */       return this.blockModelBaker;
/*    */     }
/*    */     
/*    */     public EntityModelSet entityModelSet() {
/* 30 */       return this.entityModelSet;
/*    */     }
/*    */     
/*    */     public SpriteGetter sprites() {
/* 30 */       return this.sprites;
/*    */     }
/*    */     
/*    */     public PlayerSkinRenderCache playerSkinRenderCache() {
/* 30 */       return this.playerSkinRenderCache;
/*    */     }
/*    */     
/*    */     public MissingItemModel missingItemModel() {
/* 30 */       return this.missingItemModel;
/*    */     }
/*    */     
/*    */     public RegistryContextSwapper contextSwapper() {
/* 30 */       return this.contextSwapper;
/*    */     }
/*    */     
/*    */     public MissingItemModel missingItemModel(Matrix4fc transformation) {
/* 40 */       return this.missingItemModel.withTransform(transformation);
/*    */     }
/*    */   }
/*    */   
/*    */   public static interface Unbaked extends ResolvableModel {
/*    */     MapCodec<? extends Unbaked> type();
/*    */     
/*    */     ItemModel bake(ItemModel.BakingContext param1BakingContext, Matrix4fc param1Matrix4fc);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */