/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class BakingContext extends Record implements SpecialModelRenderer.BakingContext {
/*    */   private final EntityModelSet entityModelSet;
/*    */   
/*    */   private final SpriteGetter sprites;
/*    */   
/*    */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */   
/*    */   private final Function<BlockState, BlockStateModel> modelGetter;
/*    */   
/*    */   private final BlockModel missingBlockModel;
/*    */   
/*    */   public BakingContext(EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, Function<BlockState, BlockStateModel> modelGetter, BlockModel missingBlockModel) {
/* 21 */     this.entityModelSet = entityModelSet;
/* 21 */     this.sprites = sprites;
/* 21 */     this.playerSkinRenderCache = playerSkinRenderCache;
/* 21 */     this.modelGetter = modelGetter;
/* 21 */     this.missingBlockModel = missingBlockModel;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModel$BakingContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public EntityModelSet entityModelSet() {
/* 21 */     return this.entityModelSet;
/*    */   }
/*    */   
/*    */   public SpriteGetter sprites() {
/* 21 */     return this.sprites;
/*    */   }
/*    */   
/*    */   public PlayerSkinRenderCache playerSkinRenderCache() {
/* 21 */     return this.playerSkinRenderCache;
/*    */   }
/*    */   
/*    */   public Function<BlockState, BlockStateModel> modelGetter() {
/* 21 */     return this.modelGetter;
/*    */   }
/*    */   
/*    */   public BlockModel missingBlockModel() {
/* 21 */     return this.missingBlockModel;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\BlockModel$BakingContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */