/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import net.minecraft.client.renderer.block.FluidModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.renderer.block.dispatch.SingleVariant;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*     */ import net.minecraft.client.renderer.item.MissingItemModel;
/*     */ import net.minecraft.client.renderer.item.ModelRenderProperties;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.MaterialBaker;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class MissingModels extends Record {
/*     */   private final BlockStateModelPart blockPart;
/*     */   
/*     */   private final BlockStateModel block;
/*     */   
/*     */   private final MissingItemModel item;
/*     */   
/*     */   private final FluidModel fluid;
/*     */   
/*     */   public MissingModels(BlockStateModelPart blockPart, BlockStateModel block, MissingItemModel item, FluidModel fluid) {
/*  85 */     this.blockPart = blockPart;
/*  85 */     this.block = block;
/*  85 */     this.item = item;
/*  85 */     this.fluid = fluid;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockStateModelPart blockPart() {
/*  85 */     return this.blockPart;
/*     */   }
/*     */   
/*     */   public BlockStateModel block() {
/*  85 */     return this.block;
/*     */   }
/*     */   
/*     */   public MissingItemModel item() {
/*  85 */     return this.item;
/*     */   }
/*     */   
/*     */   public FluidModel fluid() {
/*  85 */     return this.fluid;
/*     */   }
/*     */   
/*     */   public static MissingModels bake(ResolvedModel unbaked, final MaterialBaker materials, final ModelBaker.Interner interner) {
/*  92 */     ModelBaker missingModelBakery = new ModelBaker() {
/*     */         public ResolvedModel getModel(Identifier location) {
/*  95 */           throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location));
/*     */         }
/*     */         
/*     */         public BlockStateModelPart missingBlockModelPart() {
/* 101 */           throw new IllegalStateException();
/*     */         }
/*     */         
/*     */         public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 107 */           return key.compute(this);
/*     */         }
/*     */         
/*     */         public MaterialBaker materials() {
/* 112 */           return materials;
/*     */         }
/*     */         
/*     */         public ModelBaker.Interner interner() {
/* 117 */           return interner;
/*     */         }
/*     */       };
/* 121 */     TextureSlots textureSlots = unbaked.getTopTextureSlots();
/* 122 */     boolean hasAmbientOcclusion = unbaked.getTopAmbientOcclusion();
/* 123 */     boolean usesBlockLight = unbaked.getTopGuiLight().lightLikeBlock();
/* 124 */     ItemTransforms transforms = unbaked.getTopTransforms();
/* 125 */     QuadCollection geometry = unbaked.bakeTopGeometry(textureSlots, missingModelBakery, (ModelState)BlockModelRotation.IDENTITY);
/* 127 */     Multimap<Identifier, Identifier> forbiddenSprites = SimpleModelWrapper.findNonBlockSprites(geometry);
/* 128 */     if (forbiddenSprites != null)
/* 130 */       throw new IllegalStateException("Missing block contains sprites from outside of block atlas: " + String.valueOf(forbiddenSprites)); 
/* 133 */     Material.Baked particleMaterial = unbaked.resolveParticleMaterial(textureSlots, missingModelBakery);
/* 135 */     SimpleModelWrapper missingModelPart = new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleMaterial);
/* 140 */     SingleVariant singleVariant = new SingleVariant(missingModelPart);
/* 143 */     MissingItemModel bakedItemModel = new MissingItemModel(geometry.getAll(), new ModelRenderProperties(usesBlockLight, particleMaterial, transforms));
/* 151 */     FluidModel bakedFluidModel = new FluidModel(ChunkSectionLayer.SOLID, particleMaterial, particleMaterial, null, null);
/* 159 */     return new MissingModels(missingModelPart, (BlockStateModel)singleVariant, bakedItemModel, bakedFluidModel);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelBakery$MissingModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */