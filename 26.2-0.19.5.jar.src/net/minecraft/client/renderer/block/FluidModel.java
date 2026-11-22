/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Transparency;
/*    */ import net.minecraft.client.color.block.BlockTintSource;
/*    */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*    */ import net.minecraft.client.resources.model.ModelDebugName;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*    */ 
/*    */ public final class FluidModel extends Record {
/*    */   private final ChunkSectionLayer layer;
/*    */   
/*    */   private final Material.Baked stillMaterial;
/*    */   
/*    */   private final Material.Baked flowingMaterial;
/*    */   
/*    */   private final Material.Baked overlayMaterial;
/*    */   
/*    */   private final BlockTintSource tintSource;
/*    */   
/*    */   public FluidModel(ChunkSectionLayer layer, Material.Baked stillMaterial, Material.Baked flowingMaterial, Material.Baked overlayMaterial, BlockTintSource tintSource) {
/* 11 */     this.layer = layer;
/* 11 */     this.stillMaterial = stillMaterial;
/* 11 */     this.flowingMaterial = flowingMaterial;
/* 11 */     this.overlayMaterial = overlayMaterial;
/* 11 */     this.tintSource = tintSource;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/FluidModel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/FluidModel;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/FluidModel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/FluidModel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/FluidModel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/FluidModel;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ChunkSectionLayer layer() {
/* 11 */     return this.layer;
/*    */   }
/*    */   
/*    */   public Material.Baked stillMaterial() {
/* 11 */     return this.stillMaterial;
/*    */   }
/*    */   
/*    */   public Material.Baked flowingMaterial() {
/* 11 */     return this.flowingMaterial;
/*    */   }
/*    */   
/*    */   public Material.Baked overlayMaterial() {
/* 11 */     return this.overlayMaterial;
/*    */   }
/*    */   
/*    */   public BlockTintSource tintSource() {
/* 11 */     return this.tintSource;
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record {
/*    */     private final Material stillMaterial;
/*    */     
/*    */     private final Material flowingMaterial;
/*    */     
/*    */     private final Material overlayMaterial;
/*    */     
/*    */     private final BlockTintSource tintSource;
/*    */     
/*    */     public Unbaked(Material stillMaterial, Material flowingMaterial, Material overlayMaterial, BlockTintSource tintSource) {
/* 18 */       this.stillMaterial = stillMaterial;
/* 18 */       this.flowingMaterial = flowingMaterial;
/* 18 */       this.overlayMaterial = overlayMaterial;
/* 18 */       this.tintSource = tintSource;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Material stillMaterial() {
/* 18 */       return this.stillMaterial;
/*    */     }
/*    */     
/*    */     public Material flowingMaterial() {
/* 18 */       return this.flowingMaterial;
/*    */     }
/*    */     
/*    */     public Material overlayMaterial() {
/* 18 */       return this.overlayMaterial;
/*    */     }
/*    */     
/*    */     public BlockTintSource tintSource() {
/* 18 */       return this.tintSource;
/*    */     }
/*    */     
/*    */     public FluidModel bake(MaterialBaker materials, ModelDebugName modelName) {
/* 25 */       Material.Baked stillMaterial = materials.get(this.stillMaterial, modelName);
/* 26 */       Material.Baked flowingMaterial = materials.get(this.flowingMaterial, modelName);
/* 27 */       Material.Baked overlayMaterial = (this.overlayMaterial != null) ? materials.get(this.overlayMaterial, modelName) : null;
/* 28 */       Transparency transparency = getTransparency(stillMaterial).or(getTransparency(flowingMaterial));
/* 29 */       if (overlayMaterial != null)
/* 30 */         transparency = transparency.or(getTransparency(overlayMaterial)); 
/* 32 */       return new FluidModel(
/* 33 */           ChunkSectionLayer.byTransparency(transparency), stillMaterial, flowingMaterial, overlayMaterial, this.tintSource);
/*    */     }
/*    */     
/*    */     private static Transparency getTransparency(Material.Baked material) {
/* 42 */       if (material.forceTranslucent())
/* 43 */         return Transparency.TRANSLUCENT; 
/* 45 */       return material.sprite().transparency();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\FluidModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */