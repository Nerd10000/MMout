/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.block.FluidModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.model.BlockModel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ final class ReloadState extends Record {
/*     */   private final ModelBakery.BakingResult bakedModels;
/*     */   
/*     */   private final Object2IntMap<BlockState> modelGroups;
/*     */   
/*     */   private final Map<BlockState, BlockStateModel> blockStateModels;
/*     */   
/*     */   private final Map<BlockState, BlockModel> blockModels;
/*     */   
/*     */   private final Map<Fluid, FluidModel> fluidModels;
/*     */   
/*     */   private final EntityModelSet entityModelSet;
/*     */   
/*     */   private ReloadState(ModelBakery.BakingResult bakedModels, Object2IntMap<BlockState> modelGroups, Map<BlockState, BlockStateModel> blockStateModels, Map<BlockState, BlockModel> blockModels, Map<Fluid, FluidModel> fluidModels, EntityModelSet entityModelSet) {
/* 259 */     this.bakedModels = bakedModels;
/* 259 */     this.modelGroups = modelGroups;
/* 259 */     this.blockStateModels = blockStateModels;
/* 259 */     this.blockModels = blockModels;
/* 259 */     this.fluidModels = fluidModels;
/* 259 */     this.entityModelSet = entityModelSet;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ModelBakery.BakingResult bakedModels() {
/* 259 */     return this.bakedModels;
/*     */   }
/*     */   
/*     */   public Object2IntMap<BlockState> modelGroups() {
/* 259 */     return this.modelGroups;
/*     */   }
/*     */   
/*     */   public Map<BlockState, BlockStateModel> blockStateModels() {
/* 259 */     return this.blockStateModels;
/*     */   }
/*     */   
/*     */   public Map<BlockState, BlockModel> blockModels() {
/* 259 */     return this.blockModels;
/*     */   }
/*     */   
/*     */   public Map<Fluid, FluidModel> fluidModels() {
/* 259 */     return this.fluidModels;
/*     */   }
/*     */   
/*     */   public EntityModelSet entityModelSet() {
/* 259 */     return this.entityModelSet;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelManager$ReloadState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */