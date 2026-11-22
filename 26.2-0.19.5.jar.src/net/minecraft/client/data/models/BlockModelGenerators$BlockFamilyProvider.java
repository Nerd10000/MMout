/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
/*      */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.data.models.model.TextureSlot;
/*      */ import net.minecraft.client.data.models.model.TexturedModel;
/*      */ import net.minecraft.client.renderer.block.dispatch.Variant;
/*      */ import net.minecraft.data.BlockFamilies;
/*      */ import net.minecraft.data.BlockFamily;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ 
/*      */ class BlockFamilyProvider {
/*      */   private final TextureMapping mapping;
/*      */   
/*  884 */   private final Map<ModelTemplate, Identifier> models = new HashMap<>();
/*      */   
/*      */   private BlockFamily family;
/*      */   
/*      */   private Variant fullBlock;
/*      */   
/*  888 */   private final Set<Block> skipGeneratingModelsFor = new HashSet<>();
/*      */   
/*      */   public BlockFamilyProvider(TextureMapping mapping) {
/*  891 */     this.mapping = mapping;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider fullBlock(Block block, ModelTemplate template) {
/*  895 */     this.fullBlock = BlockModelGenerators.plainModel(template.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  896 */     if (BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.containsKey(block)) {
/*  897 */       BlockModelGenerators.this.blockStateOutput.accept(((BlockModelGenerators.BlockStateGeneratorSupplier)BlockModelGenerators.FULL_BLOCK_MODEL_CUSTOM_GENERATORS.get(block)).create(block, this.fullBlock, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */     } else {
/*  899 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.variant(this.fullBlock)));
/*      */     } 
/*  901 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider donateModelTo(Block donor, Block copyTo) {
/*  905 */     Identifier donorModelLocation = ModelLocationUtils.getModelLocation(donor);
/*  906 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(copyTo, BlockModelGenerators.plainVariant(donorModelLocation)));
/*  907 */     BlockModelGenerators.this.itemModelOutput.copy(donor.asItem(), copyTo.asItem());
/*  908 */     this.skipGeneratingModelsFor.add(copyTo);
/*  909 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider button(Block block) {
/*  913 */     MultiVariant normal = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  914 */     MultiVariant pressed = BlockModelGenerators.plainVariant(ModelTemplates.BUTTON_PRESSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  915 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createButton(block, normal, pressed));
/*  917 */     Identifier inventory = ModelTemplates.BUTTON_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  918 */     BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  919 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider wall(Block block) {
/*  923 */     MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  924 */     MultiVariant low = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  925 */     MultiVariant high = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  926 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createWall(block, post, low, high));
/*  928 */     Identifier inventory = ModelTemplates.WALL_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  929 */     BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  930 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider customFence(Block block) {
/*  934 */     TextureMapping mapping = TextureMapping.customParticle(block);
/*  936 */     MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_POST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  937 */     MultiVariant north = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_NORTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  938 */     MultiVariant east = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_EAST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  939 */     MultiVariant south = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_SOUTH.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  940 */     MultiVariant west = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_SIDE_WEST.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  941 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createCustomFence(block, post, north, east, south, west));
/*  943 */     Identifier inventory = ModelTemplates.CUSTOM_FENCE_INVENTORY.create(block, mapping, BlockModelGenerators.this.modelOutput);
/*  944 */     BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  945 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider fence(Block block) {
/*  949 */     MultiVariant post = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_POST.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  950 */     MultiVariant side = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_SIDE.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  951 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFence(block, post, side));
/*  953 */     Identifier inventory = ModelTemplates.FENCE_INVENTORY.create(block, this.mapping, BlockModelGenerators.this.modelOutput);
/*  954 */     BlockModelGenerators.this.registerSimpleItemModel(block, inventory);
/*  955 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider customFenceGate(Block block) {
/*  959 */     TextureMapping mapping = TextureMapping.customParticle(block);
/*  961 */     MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  962 */     MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  963 */     MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_OPEN.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  964 */     MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.CUSTOM_FENCE_GATE_WALL_CLOSED.create(block, mapping, BlockModelGenerators.this.modelOutput));
/*  965 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, false));
/*  966 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider fenceGate(Block block) {
/*  970 */     MultiVariant open = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  971 */     MultiVariant closed = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  972 */     MultiVariant openWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_OPEN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  973 */     MultiVariant closedWall = BlockModelGenerators.plainVariant(ModelTemplates.FENCE_GATE_WALL_CLOSED.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  974 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(block, open, closed, openWall, closedWall, true));
/*  975 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider pressurePlate(Block block) {
/*  979 */     MultiVariant off = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_UP.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  980 */     MultiVariant on = BlockModelGenerators.plainVariant(ModelTemplates.PRESSURE_PLATE_DOWN.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*  981 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(block, off, on));
/*  982 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider sign(Block sign) {
/*  986 */     if (this.family == null)
/*  987 */       throw new IllegalStateException("Family not defined"); 
/*  991 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(sign)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(this.family.getBaseBlock()));
/*  993 */     MultiVariant standingRot0 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(sign, "_rot_0"), mapping, BlockModelGenerators.this.modelOutput));
/*  994 */     MultiVariant standingRot1 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(sign, "_rot_1"), mapping, BlockModelGenerators.this.modelOutput));
/*  995 */     MultiVariant standingRot2 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(sign, "_rot_2"), mapping, BlockModelGenerators.this.modelOutput));
/*  996 */     MultiVariant standingRot3 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(sign, "_rot_3"), mapping, BlockModelGenerators.this.modelOutput));
/*  997 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSign(sign, standingRot0, standingRot1, standingRot2, standingRot3));
/*  999 */     Block wallSign = (Block)this.family.getVariants().get(BlockFamily.Variant.WALL_SIGN);
/* 1000 */     MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_SIGN.create(wallSign, mapping, BlockModelGenerators.this.modelOutput));
/* 1001 */     BlockModelGenerators.this.blockStateOutput.accept(
/* 1002 */         MultiVariantGenerator.dispatch(wallSign, wallModel)
/* 1003 */         .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
/* 1006 */     BlockModelGenerators.this.registerSimpleFlatItemModel(sign.asItem());
/* 1007 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider customHangingSign(Block hangingSign) {
/* 1011 */     return hangingSign(hangingSign, this.family.getBaseBlock(), BlockFamily.Variant.CUSTOM_WALL_HANGING_SIGN);
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider hangingSign(Block hangingSign) {
/* 1015 */     return hangingSign(hangingSign, this.family.get(BlockFamily.Variant.STRIPPED_LOG), BlockFamily.Variant.WALL_HANGING_SIGN);
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider hangingSign(Block hangingSign, Block particleBlock, BlockFamily.Variant wallVarient) {
/* 1021 */     TextureMapping mapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(hangingSign)).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(particleBlock));
/* 1023 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createHangingSign(hangingSign, 
/*      */           
/* 1025 */           BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_0"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1026 */           BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_1"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1027 */           BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_2"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1028 */           BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(hangingSign, "_rot_3"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1029 */           BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_0"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1030 */           BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_1"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1031 */           BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_2"), mapping, BlockModelGenerators.this.modelOutput)), 
/* 1032 */           BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(hangingSign, "_attached_rot_3"), mapping, BlockModelGenerators.this.modelOutput))));
/* 1035 */     Block wallSign = this.family.get(wallVarient);
/* 1036 */     MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_HANGING_SIGN.create(wallSign, mapping, BlockModelGenerators.this.modelOutput));
/* 1037 */     BlockModelGenerators.this.blockStateOutput.accept(
/* 1038 */         MultiVariantGenerator.dispatch(wallSign, wallModel)
/* 1039 */         .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
/* 1042 */     BlockModelGenerators.this.registerSimpleFlatItemModel(hangingSign.asItem());
/* 1043 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider slab(Block slab) {
/* 1047 */     if (this.fullBlock == null)
/* 1048 */       throw new IllegalStateException("Full block not generated yet"); 
/* 1050 */     Identifier bottom = getOrCreateModel(ModelTemplates.SLAB_BOTTOM, slab);
/* 1051 */     MultiVariant top = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.SLAB_TOP, slab));
/* 1053 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSlab(slab, BlockModelGenerators.plainVariant(bottom), top, BlockModelGenerators.variant(this.fullBlock)));
/* 1054 */     BlockModelGenerators.this.registerSimpleItemModel(slab, bottom);
/* 1055 */     return this;
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider stairs(Block stairs) {
/* 1059 */     MultiVariant inner = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_INNER, stairs));
/* 1060 */     Identifier straight = getOrCreateModel(ModelTemplates.STAIRS_STRAIGHT, stairs);
/* 1061 */     MultiVariant outer = BlockModelGenerators.plainVariant(getOrCreateModel(ModelTemplates.STAIRS_OUTER, stairs));
/* 1063 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createStairs(stairs, inner, BlockModelGenerators.plainVariant(straight), outer));
/* 1064 */     BlockModelGenerators.this.registerSimpleItemModel(stairs, straight);
/* 1065 */     return this;
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider fullBlockVariant(Block variant) {
/* 1069 */     TexturedModel model = BlockModelGenerators.TEXTURED_MODELS.getOrDefault(variant, TexturedModel.CUBE.get(variant));
/* 1070 */     MultiVariant variantModel = BlockModelGenerators.plainVariant(model.create(variant, BlockModelGenerators.this.modelOutput));
/* 1071 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(variant, variantModel));
/* 1072 */     return this;
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider pillar(Block variant) {
/* 1076 */     MultiVariant verticalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN.create(variant, BlockModelGenerators.this.modelOutput));
/* 1077 */     MultiVariant horizontalModel = BlockModelGenerators.plainVariant(TexturedModel.COLUMN_HORIZONTAL.create(variant, BlockModelGenerators.this.modelOutput));
/* 1078 */     BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(variant, verticalModel, horizontalModel));
/* 1079 */     return this;
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider door(Block door) {
/* 1083 */     BlockModelGenerators.this.createDoor(door);
/* 1084 */     return this;
/*      */   }
/*      */   
/*      */   private void trapdoor(Block result) {
/* 1088 */     if (BlockModelGenerators.NON_ORIENTABLE_TRAPDOOR.contains(result)) {
/* 1089 */       BlockModelGenerators.this.createTrapdoor(result);
/*      */     } else {
/* 1091 */       BlockModelGenerators.this.createOrientableTrapdoor(result);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Identifier getOrCreateModel(ModelTemplate modelTemplate, Block block) {
/* 1096 */     return this.models.computeIfAbsent(modelTemplate, template -> template.create(block, this.mapping, BlockModelGenerators.this.modelOutput));
/*      */   }
/*      */   
/*      */   public BlockFamilyProvider generateFor(BlockFamily family) {
/* 1100 */     this.family = family;
/* 1101 */     family.getVariants().forEach((variant, result) -> {
/*      */           boolean modelAlreadyRegisteredAsAnotherFamilyBase = BlockFamilies.getAllFamilies().anyMatch(());
/*      */           if (this.skipGeneratingModelsFor.contains(result) || modelAlreadyRegisteredAsAnotherFamilyBase)
/*      */             return; 
/*      */           BiConsumer<BlockFamilyProvider, Block> consumer = BlockModelGenerators.SHAPE_CONSUMERS.get(variant);
/*      */           if (consumer != null)
/*      */             consumer.accept(this, result); 
/*      */         });
/* 1111 */     return this;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators$BlockFamilyProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */