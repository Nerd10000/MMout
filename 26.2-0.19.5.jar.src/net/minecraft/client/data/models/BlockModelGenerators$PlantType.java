/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ 
/*      */ enum PlantType {
/* 1246 */   TINTED(ModelTemplates.TINTED_CROSS, ModelTemplates.TINTED_FLOWER_POT_CROSS, false),
/* 1247 */   NOT_TINTED(ModelTemplates.CROSS, ModelTemplates.FLOWER_POT_CROSS, false),
/* 1248 */   EMISSIVE_NOT_TINTED(ModelTemplates.CROSS_EMISSIVE, ModelTemplates.FLOWER_POT_CROSS_EMISSIVE, true);
/*      */   
/*      */   private final ModelTemplate blockTemplate;
/*      */   
/*      */   private final ModelTemplate flowerPotTemplate;
/*      */   
/*      */   private final boolean isEmissive;
/*      */   
/*      */   PlantType(ModelTemplate blockTemplate, ModelTemplate flowerPotTemplate, boolean isEmissive) {
/* 1256 */     this.blockTemplate = blockTemplate;
/* 1257 */     this.flowerPotTemplate = flowerPotTemplate;
/* 1258 */     this.isEmissive = isEmissive;
/*      */   }
/*      */   
/*      */   public ModelTemplate getCross() {
/* 1262 */     return this.blockTemplate;
/*      */   }
/*      */   
/*      */   public ModelTemplate getCrossPot() {
/* 1266 */     return this.flowerPotTemplate;
/*      */   }
/*      */   
/*      */   public Identifier createItemModel(BlockModelGenerators generator, Block block) {
/* 1270 */     Item blockItem = block.asItem();
/* 1271 */     if (this.isEmissive)
/* 1272 */       return generator.createFlatItemModelWithBlockTextureAndOverlay(blockItem, block, "_emissive"); 
/* 1274 */     return generator.createFlatItemModelWithBlockTexture(blockItem, block);
/*      */   }
/*      */   
/*      */   public TextureMapping getTextureMapping(Block block) {
/* 1279 */     return this.isEmissive ? TextureMapping.crossEmissive(block) : TextureMapping.cross(block);
/*      */   }
/*      */   
/*      */   public TextureMapping getPlantTextureMapping(Block standAlone) {
/* 1283 */     return this.isEmissive ? TextureMapping.plantEmissive(standAlone) : TextureMapping.plant(standAlone);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators$PlantType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */