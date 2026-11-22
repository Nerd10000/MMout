/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.color.block.BlockTintSource;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class BlockStateModelWrapper implements BlockModel {
/*    */   private final BlockStateModel model;
/*    */   
/*    */   private final List<BlockTintSource> tints;
/*    */   
/*    */   private final Matrix4fc transformation;
/*    */   
/*    */   public BlockStateModelWrapper(BlockStateModel model, List<BlockTintSource> tints, Matrix4fc transformation) {
/* 22 */     this.model = model;
/* 23 */     this.tints = tints;
/* 24 */     this.transformation = transformation;
/*    */   }
/*    */   
/*    */   public void update(BlockModelRenderState output, BlockState blockState, BlockDisplayContext displayContext, long seed) {
/* 29 */     List<BlockStateModelPart> partList = output.setupModel(this.transformation, this.model.hasMaterialFlag(1));
/* 30 */     this.model.collectParts(output.scratchRandomSource(seed), partList);
/* 31 */     updateTints(output, blockState);
/*    */   }
/*    */   
/*    */   private void updateTints(BlockModelRenderState renderState, BlockState blockState) {
/* 35 */     if (!this.tints.isEmpty()) {
/* 36 */       IntList tintLayers = renderState.tintLayers();
/* 37 */       for (BlockTintSource tint : this.tints)
/* 38 */         tintLayers.add(tint.color(blockState)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements BlockModel.Unbaked {
/*    */     private final BlockState model;
/*    */     
/*    */     private final List<BlockTintSource> tints;
/*    */     
/*    */     private final Optional<Transformation> transformation;
/*    */     
/*    */     public Unbaked(BlockState model, List<BlockTintSource> tints, Optional<Transformation> transformation) {
/* 43 */       this.model = model;
/* 43 */       this.tints = tints;
/* 43 */       this.transformation = transformation;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockStateModelWrapper$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public BlockState model() {
/* 43 */       return this.model;
/*    */     }
/*    */     
/*    */     public List<BlockTintSource> tints() {
/* 43 */       return this.tints;
/*    */     }
/*    */     
/*    */     public Optional<Transformation> transformation() {
/* 43 */       return this.transformation;
/*    */     }
/*    */     
/*    */     public BlockModel bake(BlockModel.BakingContext context, Matrix4fc transformation) {
/* 51 */       BlockStateModel baseModel = context.modelGetter().apply(this.model);
/* 52 */       Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
/* 53 */       return new BlockStateModelWrapper(baseModel, this.tints, modelTransform);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\model\BlockStateModelWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */