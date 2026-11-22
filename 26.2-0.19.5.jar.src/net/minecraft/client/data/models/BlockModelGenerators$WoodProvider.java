/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.data.models.model.TextureSlot;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ 
/*      */ class WoodProvider {
/*      */   private final TextureMapping logMapping;
/*      */   
/*      */   public WoodProvider(TextureMapping logMapping) {
/* 1200 */     this.logMapping = logMapping;
/*      */   }
/*      */   
/*      */   public WoodProvider wood(Block block) {
/* 1204 */     TextureMapping woodMapping = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
/* 1205 */     Identifier model = ModelTemplates.CUBE_COLUMN.create(block, woodMapping, BlockModelGenerators.this.modelOutput);
/* 1206 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1207 */     BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1208 */     return this;
/*      */   }
/*      */   
/*      */   public WoodProvider log(Block block) {
/* 1212 */     Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1213 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(block, BlockModelGenerators.plainVariant(model)));
/* 1214 */     BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1215 */     return this;
/*      */   }
/*      */   
/*      */   public WoodProvider logWithHorizontal(Block block) {
/* 1219 */     Identifier model = ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput);
/* 1220 */     MultiVariant horizontalModel = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1221 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, BlockModelGenerators.plainVariant(model), horizontalModel));
/* 1222 */     BlockModelGenerators.this.registerSimpleItemModel(block, model);
/* 1223 */     return this;
/*      */   }
/*      */   
/*      */   public WoodProvider logUVLocked(Block block) {
/* 1227 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPillarBlockUVLocked(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1228 */     BlockModelGenerators.this.registerSimpleItemModel(block, ModelTemplates.CUBE_COLUMN.create(block, this.logMapping, BlockModelGenerators.this.modelOutput));
/* 1229 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators$WoodProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */