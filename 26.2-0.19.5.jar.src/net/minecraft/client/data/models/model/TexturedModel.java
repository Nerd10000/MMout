/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class TexturedModel {
/*  12 */   public static final Provider CUBE = createDefault(TextureMapping::cube, ModelTemplates.CUBE_ALL);
/*     */   
/*  13 */   public static final Provider CUBE_INNER_FACES = createDefault(TextureMapping::cube, ModelTemplates.CUBE_ALL_INNER_FACES);
/*     */   
/*  14 */   public static final Provider CUBE_MIRRORED = createDefault(TextureMapping::cube, ModelTemplates.CUBE_MIRRORED_ALL);
/*     */   
/*  15 */   public static final Provider COLUMN = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN);
/*     */   
/*  16 */   public static final Provider COLUMN_HORIZONTAL = createDefault(TextureMapping::column, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/*     */   
/*  17 */   public static final Provider CUBE_TOP_BOTTOM = createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP);
/*     */   
/*  18 */   public static final Provider CUBE_TOP = createDefault(TextureMapping::cubeTop, ModelTemplates.CUBE_TOP);
/*     */   
/*  20 */   public static final Provider ORIENTABLE_ONLY_TOP = createDefault(TextureMapping::orientableCubeOnlyTop, ModelTemplates.CUBE_ORIENTABLE);
/*     */   
/*  21 */   public static final Provider ORIENTABLE = createDefault(TextureMapping::orientableCube, ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM);
/*     */   
/*  23 */   public static final Provider CARPET = createDefault(TextureMapping::wool, ModelTemplates.CARPET);
/*     */   
/*  24 */   public static final Provider MOSSY_CARPET_SIDE = createDefault(TextureMapping::side, ModelTemplates.MOSSY_CARPET_SIDE);
/*     */   
/*  26 */   public static final Provider FLOWERBED_1 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_1);
/*     */   
/*  27 */   public static final Provider FLOWERBED_2 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_2);
/*     */   
/*  28 */   public static final Provider FLOWERBED_3 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_3);
/*     */   
/*  29 */   public static final Provider FLOWERBED_4 = createDefault(TextureMapping::flowerbed, ModelTemplates.FLOWERBED_4);
/*     */   
/*  31 */   public static final Provider LEAF_LITTER_1 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_1);
/*     */   
/*  32 */   public static final Provider LEAF_LITTER_2 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_2);
/*     */   
/*  33 */   public static final Provider LEAF_LITTER_3 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_3);
/*     */   
/*  34 */   public static final Provider LEAF_LITTER_4 = createDefault(TextureMapping::defaultTexture, ModelTemplates.LEAF_LITTER_4);
/*     */   
/*  36 */   public static final Provider GLAZED_TERRACOTTA = createDefault(TextureMapping::pattern, ModelTemplates.GLAZED_TERRACOTTA);
/*     */   
/*  37 */   public static final Provider CORAL_FAN = createDefault(TextureMapping::fan, ModelTemplates.CORAL_FAN);
/*     */   
/*  38 */   public static final Provider ANVIL = createDefault(TextureMapping::top, ModelTemplates.ANVIL);
/*     */   
/*  39 */   public static final Provider LEAVES = createDefault(TextureMapping::cube, ModelTemplates.LEAVES);
/*     */   
/*  40 */   public static final Provider LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.LANTERN);
/*     */   
/*  41 */   public static final Provider HANGING_LANTERN = createDefault(TextureMapping::lantern, ModelTemplates.HANGING_LANTERN);
/*     */   
/*  42 */   public static final Provider CHAIN = createDefault(TextureMapping::defaultTexture, ModelTemplates.CHAIN);
/*     */   
/*  43 */   public static final Provider SEAGRASS = createDefault(TextureMapping::defaultTexture, ModelTemplates.SEAGRASS);
/*     */   
/*  48 */   public static final Provider COLUMN_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN);
/*     */   
/*  49 */   public static final Provider COLUMN_HORIZONTAL_ALT = createDefault(TextureMapping::logColumn, ModelTemplates.CUBE_COLUMN_HORIZONTAL);
/*     */   
/*  52 */   public static final Provider TOP_BOTTOM_WITH_WALL = createDefault(TextureMapping::cubeBottomTopWithWall, ModelTemplates.CUBE_BOTTOM_TOP);
/*     */   
/*  55 */   public static final Provider COLUMN_WITH_WALL = createDefault(TextureMapping::columnWithWall, ModelTemplates.CUBE_COLUMN);
/*     */   
/*     */   private final TextureMapping mapping;
/*     */   
/*     */   private final ModelTemplate template;
/*     */   
/*     */   private TexturedModel(TextureMapping mapping, ModelTemplate template) {
/*  61 */     this.mapping = mapping;
/*  62 */     this.template = template;
/*     */   }
/*     */   
/*     */   public ModelTemplate getTemplate() {
/*  66 */     return this.template;
/*     */   }
/*     */   
/*     */   public TextureMapping getMapping() {
/*  70 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public TexturedModel updateTextures(Consumer<TextureMapping> mutator) {
/*  74 */     mutator.accept(this.mapping);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public Identifier create(Block block, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  79 */     return this.template.create(block, this.mapping, modelOutput);
/*     */   }
/*     */   
/*     */   public Identifier createWithSuffix(Block block, String extraSuffix, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  83 */     return this.template.createWithSuffix(block, extraSuffix, this.mapping, modelOutput);
/*     */   }
/*     */   
/*     */   private static Provider createDefault(Function<Block, TextureMapping> mapping, ModelTemplate template) {
/*  87 */     return block -> new TexturedModel(mapping.apply(block), template);
/*     */   }
/*     */   
/*     */   public static TexturedModel createAllSame(Material material) {
/*  91 */     return new TexturedModel(TextureMapping.cube(material), ModelTemplates.CUBE_ALL);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Provider {
/*     */     TexturedModel get(Block param1Block);
/*     */     
/*     */     default Identifier create(Block block, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  99 */       return get(block).create(block, modelOutput);
/*     */     }
/*     */     
/*     */     default Identifier createWithSuffix(Block block, String suffix, BiConsumer<Identifier, ModelInstance> modelOutput) {
/* 103 */       return get(block).createWithSuffix(block, suffix, modelOutput);
/*     */     }
/*     */     
/*     */     default Provider updateTexture(Consumer<TextureMapping> mutator) {
/* 107 */       return block -> get(block).updateTextures(mutator);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\model\TexturedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */