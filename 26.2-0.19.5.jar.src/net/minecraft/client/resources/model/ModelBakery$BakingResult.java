/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class BakingResult extends Record {
/*     */   private final ModelBakery.MissingModels missingModels;
/*     */   
/*     */   private final Map<BlockState, BlockStateModel> blockStateModels;
/*     */   
/*     */   private final Map<Identifier, ItemModel> itemStackModels;
/*     */   
/*     */   private final Map<Identifier, ClientItem.Properties> itemProperties;
/*     */   
/*     */   public BakingResult(ModelBakery.MissingModels missingModels, Map<BlockState, BlockStateModel> blockStateModels, Map<Identifier, ItemModel> itemStackModels, Map<Identifier, ClientItem.Properties> itemProperties) {
/* 254 */     this.missingModels = missingModels;
/* 254 */     this.blockStateModels = blockStateModels;
/* 254 */     this.itemStackModels = itemStackModels;
/* 254 */     this.itemProperties = itemProperties;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ModelBakery.MissingModels missingModels() {
/* 254 */     return this.missingModels;
/*     */   }
/*     */   
/*     */   public Map<BlockState, BlockStateModel> blockStateModels() {
/* 254 */     return this.blockStateModels;
/*     */   }
/*     */   
/*     */   public Map<Identifier, ItemModel> itemStackModels() {
/* 254 */     return this.itemStackModels;
/*     */   }
/*     */   
/*     */   public Map<Identifier, ClientItem.Properties> itemProperties() {
/* 254 */     return this.itemProperties;
/*     */   }
/*     */   
/*     */   public BlockStateModel getBlockStateModel(BlockState blockState) {
/* 262 */     return this.blockStateModels.getOrDefault(blockState, this.missingModels.block);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery$BakingResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */