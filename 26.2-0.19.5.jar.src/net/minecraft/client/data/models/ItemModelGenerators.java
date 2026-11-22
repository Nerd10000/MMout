/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.function.BiConsumer;
/*      */ import net.minecraft.client.color.item.Dye;
/*      */ import net.minecraft.client.color.item.Firework;
/*      */ import net.minecraft.client.color.item.ItemTintSource;
/*      */ import net.minecraft.client.color.item.MapColor;
/*      */ import net.minecraft.client.color.item.Potion;
/*      */ import net.minecraft.client.data.models.model.ItemModelUtils;
/*      */ import net.minecraft.client.data.models.model.ModelInstance;
/*      */ import net.minecraft.client.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ import net.minecraft.client.data.models.model.ModelTemplates;
/*      */ import net.minecraft.client.data.models.model.TextureMapping;
/*      */ import net.minecraft.client.renderer.item.BundleSelectedItemSpecialRenderer;
/*      */ import net.minecraft.client.renderer.item.ClientItem;
/*      */ import net.minecraft.client.renderer.item.ItemModel;
/*      */ import net.minecraft.client.renderer.item.RangeSelectItemModel;
/*      */ import net.minecraft.client.renderer.item.SelectItemModel;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.Broken;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.BundleHasSelectedItem;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.Time;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.UseCycle;
/*      */ import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
/*      */ import net.minecraft.client.renderer.item.properties.select.Charge;
/*      */ import net.minecraft.client.renderer.item.properties.select.DisplayContext;
/*      */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*      */ import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
/*      */ import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
/*      */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*      */ import net.minecraft.client.renderer.special.TridentSpecialRenderer;
/*      */ import net.minecraft.client.resources.model.sprite.Material;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.item.CrossbowItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemDisplayContext;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*      */ import net.minecraft.world.item.equipment.EquipmentAssets;
/*      */ import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
/*      */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*      */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*      */ 
/*      */ public class ItemModelGenerators {
/*   72 */   private static final ItemTintSource BLANK_LAYER = ItemModelUtils.constantTint(-1);
/*      */   
/*   74 */   public static final Identifier TRIM_PREFIX_HELMET = prefixForSlotTrim("helmet");
/*      */   
/*   75 */   public static final Identifier TRIM_PREFIX_CHESTPLATE = prefixForSlotTrim("chestplate");
/*      */   
/*   76 */   public static final Identifier TRIM_PREFIX_LEGGINGS = prefixForSlotTrim("leggings");
/*      */   
/*   77 */   public static final Identifier TRIM_PREFIX_BOOTS = prefixForSlotTrim("boots");
/*      */   
/*      */   public static Identifier prefixForSlotTrim(String slotName) {
/*   80 */     return Identifier.withDefaultNamespace("trims/items/" + slotName + "_trim");
/*      */   }
/*      */   
/*      */   public static final class TrimMaterialData extends Record {
/*      */     private final MaterialAssetGroup assets;
/*      */     
/*      */     private final ResourceKey<TrimMaterial> materialKey;
/*      */     
/*      */     public TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) {
/*   83 */       this.assets = assets;
/*   83 */       this.materialKey = materialKey;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #83	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #83	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #83	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public MaterialAssetGroup assets() {
/*   83 */       return this.assets;
/*      */     }
/*      */     
/*      */     public ResourceKey<TrimMaterial> materialKey() {
/*   83 */       return this.materialKey;
/*      */     }
/*      */   }
/*      */   
/*   86 */   public static final List<TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(new TrimMaterialData[] { 
/*   86 */         new TrimMaterialData(MaterialAssetGroup.QUARTZ, TrimMaterials.QUARTZ), new TrimMaterialData(MaterialAssetGroup.IRON, TrimMaterials.IRON), new TrimMaterialData(MaterialAssetGroup.NETHERITE, TrimMaterials.NETHERITE), new TrimMaterialData(MaterialAssetGroup.REDSTONE, TrimMaterials.REDSTONE), new TrimMaterialData(MaterialAssetGroup.COPPER, TrimMaterials.COPPER), new TrimMaterialData(MaterialAssetGroup.GOLD, TrimMaterials.GOLD), new TrimMaterialData(MaterialAssetGroup.EMERALD, TrimMaterials.EMERALD), new TrimMaterialData(MaterialAssetGroup.DIAMOND, TrimMaterials.DIAMOND), new TrimMaterialData(MaterialAssetGroup.LAPIS, TrimMaterials.LAPIS), new TrimMaterialData(MaterialAssetGroup.AMETHYST, TrimMaterials.AMETHYST), 
/*   86 */         new TrimMaterialData(MaterialAssetGroup.RESIN, TrimMaterials.RESIN) });
/*      */   
/*      */   private final ItemModelOutput itemModelOutput;
/*      */   
/*      */   private final BiConsumer<Identifier, ModelInstance> modelOutput;
/*      */   
/*      */   public ItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
/*  104 */     this.itemModelOutput = itemModelOutput;
/*  105 */     this.modelOutput = modelOutput;
/*      */   }
/*      */   
/*      */   private void declareCustomModelItem(Item item) {
/*  109 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, ModelTemplate template) {
/*  113 */     return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateFlatItem(Item item, ModelTemplate template) {
/*  117 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(createFlatItemModel(item, template)));
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, String suffix, ModelTemplate template) {
/*  121 */     return template.create(ModelLocationUtils.getModelLocation(item, suffix), TextureMapping.layer0(TextureMapping.getItemTexture(item, suffix)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier createFlatItemModel(Item item, Item textureDonor, ModelTemplate template) {
/*  125 */     return template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(textureDonor), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateFlatItem(Item item, Item textureDonor, ModelTemplate template) {
/*  129 */     this.itemModelOutput.accept(item, ItemModelUtils.plainModel(createFlatItemModel(item, textureDonor, template)));
/*      */   }
/*      */   
/*      */   private void generateItemWithTintedOverlay(Item item, ItemTintSource overlayTint) {
/*  133 */     generateItemWithTintedOverlay(item, "_overlay", overlayTint);
/*      */   }
/*      */   
/*      */   private void generateItemWithTintedOverlay(Item item, String overlaySuffix, ItemTintSource overlayTint) {
/*  137 */     Identifier model = generateLayeredItem(item, 
/*      */         
/*  139 */         TextureMapping.getItemTexture(item), 
/*  140 */         TextureMapping.getItemTexture(item, overlaySuffix));
/*  143 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { BLANK_LAYER, overlayTint }));
/*      */   }
/*      */   
/*      */   private void generateItemWithTintedBaseLayer(Item item, int defaultColor) {
/*  149 */     Material tintedLayer = TextureMapping.getItemTexture(item);
/*  150 */     Material untintedLayer = TextureMapping.getItemTexture(item, "_overlay");
/*  152 */     Identifier model = ModelLocationUtils.getModelLocation(item);
/*  153 */     ModelTemplates.TWO_LAYERED_ITEM.create(model, TextureMapping.layered(tintedLayer, untintedLayer), this.modelOutput);
/*  155 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Dye(defaultColor) }));
/*      */   }
/*      */   
/*      */   private List<RangeSelectItemModel.Entry> createCompassModels(Item compass) {
/*  159 */     List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
/*  160 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(compass, "_16", ModelTemplates.FLAT_ITEM));
/*  161 */     overrides.add(ItemModelUtils.override(base, 0.0F));
/*  163 */     for (int i = 1; i < 32; i++) {
/*  164 */       int textureIndex = Mth.positiveModulo(i - 16, 32);
/*  165 */       ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(createFlatItemModel(compass, String.format(Locale.ROOT, "_%02d", new Object[] { Integer.valueOf(textureIndex) }), ModelTemplates.FLAT_ITEM));
/*  166 */       overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
/*      */     } 
/*  169 */     overrides.add(ItemModelUtils.override(base, 31.5F));
/*  170 */     return overrides;
/*      */   }
/*      */   
/*      */   private void generateStandardCompassItem(Item compass) {
/*  174 */     List<RangeSelectItemModel.Entry> overrides = createCompassModels(compass);
/*  176 */     this.itemModelOutput.accept(compass, 
/*  177 */         ItemModelUtils.conditional(
/*  178 */           ItemModelUtils.hasComponent(DataComponents.LODESTONE_TRACKER), 
/*  179 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.LODESTONE), 32.0F, overrides), 
/*  180 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.SPAWN), 32.0F, overrides)));
/*      */   }
/*      */   
/*      */   private void generateRecoveryCompassItem(Item compass) {
/*  186 */     this.itemModelOutput.accept(compass, 
/*  187 */         ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CompassAngle(true, CompassAngleState.CompassTarget.RECOVERY), 32.0F, 
/*      */           
/*  190 */           createCompassModels(compass)));
/*      */   }
/*      */   
/*      */   private void generateClockItem(Item clock) {
/*  196 */     List<RangeSelectItemModel.Entry> overrides = new ArrayList<>();
/*  197 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(createFlatItemModel(clock, "_00", ModelTemplates.FLAT_ITEM));
/*  198 */     overrides.add(ItemModelUtils.override(base, 0.0F));
/*  200 */     for (int i = 1; i < 64; i++) {
/*  201 */       ItemModel.Unbaked overrideModel = ItemModelUtils.plainModel(createFlatItemModel(clock, String.format(Locale.ROOT, "_%02d", new Object[] { Integer.valueOf(i) }), ModelTemplates.FLAT_ITEM));
/*  202 */       overrides.add(ItemModelUtils.override(overrideModel, i - 0.5F));
/*      */     } 
/*  205 */     overrides.add(ItemModelUtils.override(base, 63.5F));
/*  206 */     this.itemModelOutput.accept(clock, 
/*  207 */         ItemModelUtils.inOverworld(
/*  208 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new Time(true, Time.TimeSource.DAYTIME), 64.0F, overrides), 
/*  209 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new Time(true, Time.TimeSource.RANDOM), 64.0F, overrides)));
/*      */   }
/*      */   
/*      */   private Identifier generateLayeredItem(Item target, Material layer0, Material layer1) {
/*  215 */     return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private Identifier generateLayeredItem(Identifier target, Material layer0, Material layer1) {
/*  219 */     return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateLayeredItem(Identifier target, Material layer0, Material layer1, Material layer2) {
/*  223 */     ModelTemplates.THREE_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1, layer2), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateTrimmableItem(Item armor, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
/*      */     ItemModel.Unbaked untrimmedModel;
/*  227 */     Identifier modelLocation = ModelLocationUtils.getModelLocation(armor);
/*  228 */     Material itemTexture = TextureMapping.getItemTexture(armor);
/*  229 */     Material overlayTexture = TextureMapping.getItemTexture(armor, "_overlay");
/*  231 */     List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> cases = new ArrayList<>(TRIM_MATERIAL_MODELS.size());
/*  233 */     for (TrimMaterialData material : TRIM_MATERIAL_MODELS) {
/*      */       ItemModel.Unbaked trimModel;
/*  235 */       Identifier trimModelLocation = modelLocation.withSuffix("_" + material.assets().base().suffix() + "_trim");
/*  236 */       Material trimOverlayTexture = new Material(slotTrimPrefix.withSuffix("_" + material.assets().assetId(equipmentAssetId).suffix()));
/*  239 */       if (hasDyedLayer) {
/*  240 */         generateLayeredItem(trimModelLocation, itemTexture, overlayTexture, trimOverlayTexture);
/*  241 */         trimModel = ItemModelUtils.tintedModel(trimModelLocation, new ItemTintSource[] { (ItemTintSource)new Dye(-6265536) });
/*      */       } else {
/*  243 */         generateLayeredItem(trimModelLocation, itemTexture, trimOverlayTexture);
/*  244 */         trimModel = ItemModelUtils.plainModel(trimModelLocation);
/*      */       } 
/*  247 */       cases.add(ItemModelUtils.when(material.materialKey, trimModel));
/*      */     } 
/*  251 */     if (hasDyedLayer) {
/*  252 */       ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(itemTexture, overlayTexture), this.modelOutput);
/*  253 */       untrimmedModel = ItemModelUtils.tintedModel(modelLocation, new ItemTintSource[] { (ItemTintSource)new Dye(-6265536) });
/*      */     } else {
/*  255 */       ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
/*  256 */       untrimmedModel = ItemModelUtils.plainModel(modelLocation);
/*      */     } 
/*  259 */     this.itemModelOutput.accept(armor, 
/*  260 */         ItemModelUtils.select((SelectItemModelProperty)new TrimMaterialProperty(), untrimmedModel, cases));
/*      */   }
/*      */   
/*      */   private void generateBundleModels(Item bundle) {
/*  269 */     ItemModel.Unbaked closedModel = ItemModelUtils.plainModel(createFlatItemModel(bundle, ModelTemplates.FLAT_ITEM));
/*  271 */     Identifier openBackCover = generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_BACK_INVENTORY, "_open_back");
/*  272 */     Identifier openFrontCover = generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_FRONT_INVENTORY, "_open_front");
/*  274 */     ItemModel.Unbaked openModel = ItemModelUtils.composite(new ItemModel.Unbaked[] { ItemModelUtils.plainModel(openBackCover), (ItemModel.Unbaked)new BundleSelectedItemSpecialRenderer.Unbaked(), 
/*      */           
/*  277 */           ItemModelUtils.plainModel(openFrontCover) });
/*  280 */     ItemModel.Unbaked inGuiModel = ItemModelUtils.conditional((ConditionalItemModelProperty)new BundleHasSelectedItem(), openModel, closedModel);
/*  286 */     this.itemModelOutput.accept(bundle, ItemModelUtils.select((SelectItemModelProperty)new DisplayContext(), closedModel, new SelectItemModel.SwitchCase[] { ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel) }));
/*      */   }
/*      */   
/*      */   private Identifier generateBundleCoverModel(Item item, ModelTemplate template, String suffix) {
/*  294 */     Material texture = TextureMapping.getItemTexture(item, suffix);
/*  295 */     return template.create(item, TextureMapping.layer0(texture), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void generateBow(Item item) {
/*  299 */     ItemModel.Unbaked bowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  300 */     ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_0", ModelTemplates.BOW));
/*  301 */     ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_1", ModelTemplates.BOW));
/*  302 */     ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_2", ModelTemplates.BOW));
/*  304 */     this.itemModelOutput.accept(item, 
/*  305 */         ItemModelUtils.conditional(
/*  306 */           ItemModelUtils.isUsingItem(), 
/*  307 */           ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new UseDuration(false), 0.05F, pulling0, new RangeSelectItemModel.Entry[] { ItemModelUtils.override(pulling1, 0.65F), 
/*  312 */               ItemModelUtils.override(pulling2, 0.9F) }), bowModel));
/*      */   }
/*      */   
/*      */   private void generateCrossbow(Item item) {
/*  320 */     ItemModel.Unbaked crossbowModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  321 */     ItemModel.Unbaked pulling0 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_0", ModelTemplates.CROSSBOW));
/*  322 */     ItemModel.Unbaked pulling1 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_1", ModelTemplates.CROSSBOW));
/*  323 */     ItemModel.Unbaked pulling2 = ItemModelUtils.plainModel(createFlatItemModel(item, "_pulling_2", ModelTemplates.CROSSBOW));
/*  324 */     ItemModel.Unbaked loadedArrow = ItemModelUtils.plainModel(createFlatItemModel(item, "_arrow", ModelTemplates.CROSSBOW));
/*  325 */     ItemModel.Unbaked loadedFirework = ItemModelUtils.plainModel(createFlatItemModel(item, "_firework", ModelTemplates.CROSSBOW));
/*  327 */     this.itemModelOutput.accept(item, 
/*  328 */         ItemModelUtils.select((SelectItemModelProperty)new Charge(), 
/*      */           
/*  330 */           ItemModelUtils.conditional(
/*  331 */             ItemModelUtils.isUsingItem(), 
/*  332 */             ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new CrossbowPull(), pulling0, new RangeSelectItemModel.Entry[] { ItemModelUtils.override(pulling1, 0.58F), 
/*  336 */                 ItemModelUtils.override(pulling2, 1.0F) }), crossbowModel), new SelectItemModel.SwitchCase[] { ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, loadedArrow), 
/*  341 */             ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, loadedFirework) }));
/*      */   }
/*      */   
/*      */   private void generateBooleanDispatch(Item item, ConditionalItemModelProperty property, ItemModel.Unbaked modelOnTrue, ItemModel.Unbaked modelOnFalse) {
/*  347 */     this.itemModelOutput.accept(item, 
/*  348 */         ItemModelUtils.conditional(property, modelOnTrue, modelOnFalse));
/*      */   }
/*      */   
/*      */   private void generateElytra(Item item) {
/*  357 */     ItemModel.Unbaked normalElytra = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  358 */     ItemModel.Unbaked brokenElytra = ItemModelUtils.plainModel(createFlatItemModel(item, "_broken", ModelTemplates.FLAT_ITEM));
/*  360 */     generateBooleanDispatch(item, (ConditionalItemModelProperty)new Broken(), brokenElytra, normalElytra);
/*      */   }
/*      */   
/*      */   private void generateBrush(Item item) {
/*  364 */     ItemModel.Unbaked base = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  365 */     ItemModel.Unbaked brushing0 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_0"));
/*  366 */     ItemModel.Unbaked brushing1 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_1"));
/*  367 */     ItemModel.Unbaked brushing2 = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_brushing_2"));
/*  368 */     this.itemModelOutput.accept(item, ItemModelUtils.rangeSelect((RangeSelectItemModelProperty)new UseCycle(10.0F), 0.1F, base, new RangeSelectItemModel.Entry[] { ItemModelUtils.override(brushing0, 0.25F), 
/*  373 */             ItemModelUtils.override(brushing1, 0.5F), 
/*  374 */             ItemModelUtils.override(brushing2, 0.75F) }));
/*      */   }
/*      */   
/*      */   private void generateFishingRod(Item item) {
/*  379 */     ItemModel.Unbaked normal = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
/*  380 */     ItemModel.Unbaked cast = ItemModelUtils.plainModel(createFlatItemModel(item, "_cast", ModelTemplates.FLAT_HANDHELD_ROD_ITEM));
/*  382 */     generateBooleanDispatch(item, (ConditionalItemModelProperty)new FishingRodCast(), cast, normal);
/*      */   }
/*      */   
/*      */   private void generateGoatHorn(Item item) {
/*  386 */     ItemModel.Unbaked normal = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item));
/*  387 */     ItemModel.Unbaked tooting = ItemModelUtils.plainModel(ModelLocationUtils.decorateItemModelLocation("tooting_goat_horn"));
/*  389 */     generateBooleanDispatch(item, ItemModelUtils.isUsingItem(), tooting, normal);
/*      */   }
/*      */   
/*      */   private void generateShield(Item item) {
/*  393 */     ItemModel.Unbaked normal = ItemModelUtils.specialModel(
/*  394 */         ModelLocationUtils.getModelLocation(item), (SpecialModelRenderer.Unbaked)new ShieldSpecialRenderer.Unbaked());
/*  398 */     ItemModel.Unbaked blocking = ItemModelUtils.specialModel(
/*  399 */         ModelLocationUtils.getModelLocation(item, "_blocking"), (SpecialModelRenderer.Unbaked)new ShieldSpecialRenderer.Unbaked());
/*  403 */     this.itemModelOutput.accept(item, 
/*  404 */         ItemModelUtils.conditional(ShieldSpecialRenderer.DEFAULT_TRANSFORMATION, 
/*      */           
/*  406 */           ItemModelUtils.isUsingItem(), blocking, normal));
/*      */   }
/*      */   
/*      */   private static ItemModel.Unbaked createFlatModelDispatch(ItemModel.Unbaked flatModel, ItemModel.Unbaked inHandModel) {
/*  414 */     return ItemModelUtils.select((SelectItemModelProperty)new DisplayContext(), inHandModel, new SelectItemModel.SwitchCase[] { ItemModelUtils.when(List.of(ItemDisplayContext.GUI, ItemDisplayContext.GROUND, ItemDisplayContext.FIXED, ItemDisplayContext.ON_SHELF), flatModel) });
/*      */   }
/*      */   
/*      */   private void generateSpyglass(Item item) {
/*  422 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  424 */     ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_in_hand"));
/*  425 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
/*      */   }
/*      */   
/*      */   private void generateTrident(Item item) {
/*  429 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  431 */     ItemModel.Unbaked inHandNormalModel = ItemModelUtils.specialModel(
/*  432 */         ModelLocationUtils.getModelLocation(item, "_in_hand"), (SpecialModelRenderer.Unbaked)new TridentSpecialRenderer.Unbaked());
/*  435 */     ItemModel.Unbaked inHandThrowingModel = ItemModelUtils.specialModel(
/*  436 */         ModelLocationUtils.getModelLocation(item, "_throwing"), (SpecialModelRenderer.Unbaked)new TridentSpecialRenderer.Unbaked());
/*  440 */     ItemModel.Unbaked inHandModel = ItemModelUtils.conditional(TridentSpecialRenderer.DEFAULT_TRANSFORMATION, 
/*      */         
/*  442 */         ItemModelUtils.isUsingItem(), inHandThrowingModel, inHandNormalModel);
/*  447 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel));
/*      */   }
/*      */   
/*      */   private void generateSpear(Item item) {
/*  451 */     ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
/*  452 */     ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(ModelTemplates.SPEAR_IN_HAND
/*  453 */         .create(item, TextureMapping.layer0(TextureMapping.getItemTexture(item, "_in_hand")), this.modelOutput));
/*  455 */     this.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel), new ClientItem.Properties(true, false, 1.95F));
/*      */   }
/*      */   
/*      */   private void addPotionTint(Item item, Identifier model) {
/*  459 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Potion() }));
/*      */   }
/*      */   
/*      */   private void generatePotion(Item item) {
/*  463 */     Identifier model = generateLayeredItem(item, new Material(
/*      */           
/*  465 */           Identifier.withDefaultNamespace("item/potion_overlay")), 
/*  466 */         TextureMapping.getItemTexture(item));
/*  468 */     addPotionTint(item, model);
/*      */   }
/*      */   
/*      */   private void generateTippedArrow(Item item) {
/*  472 */     Identifier model = generateLayeredItem(item, 
/*      */         
/*  474 */         TextureMapping.getItemTexture(item, "_head"), 
/*  475 */         TextureMapping.getItemTexture(item, "_base"));
/*  477 */     addPotionTint(item, model);
/*      */   }
/*      */   
/*      */   private void generateDyedItem(Item item, int defaultColor) {
/*  481 */     Identifier model = createFlatItemModel(item, ModelTemplates.FLAT_ITEM);
/*  482 */     this.itemModelOutput.accept(item, ItemModelUtils.tintedModel(model, new ItemTintSource[] { (ItemTintSource)new Dye(defaultColor) }));
/*      */   }
/*      */   
/*      */   private void generateTwoLayerDyedItem(Item item) {
/*  486 */     Material baseLayer = TextureMapping.getItemTexture(item);
/*  487 */     Material tintedLayer = TextureMapping.getItemTexture(item, "_overlay");
/*  489 */     Identifier plainModel = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(baseLayer), this.modelOutput);
/*  491 */     Identifier dyedModel = ModelLocationUtils.getModelLocation(item, "_dyed");
/*  492 */     ModelTemplates.TWO_LAYERED_ITEM.create(dyedModel, TextureMapping.layered(baseLayer, tintedLayer), this.modelOutput);
/*  494 */     this.itemModelOutput.accept(item, 
/*  495 */         ItemModelUtils.conditional(
/*  496 */           ItemModelUtils.hasComponent(DataComponents.DYED_COLOR), 
/*  497 */           ItemModelUtils.tintedModel(dyedModel, new ItemTintSource[] { BLANK_LAYER, (ItemTintSource)new Dye(0) }), ItemModelUtils.plainModel(plainModel)));
/*      */   }
/*      */   
/*      */   public void run() {
/*  504 */     generateFlatItem(Items.ACACIA_BOAT, ModelTemplates.FLAT_ITEM);
/*  505 */     generateFlatItem(Items.CHERRY_BOAT, ModelTemplates.FLAT_ITEM);
/*  506 */     generateFlatItem(Items.ACACIA_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  507 */     generateFlatItem(Items.CHERRY_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  508 */     generateFlatItem(Items.AMETHYST_SHARD, ModelTemplates.FLAT_ITEM);
/*  509 */     generateFlatItem(Items.APPLE, ModelTemplates.FLAT_ITEM);
/*  510 */     generateFlatItem(Items.ARMADILLO_SCUTE, ModelTemplates.FLAT_ITEM);
/*  511 */     generateFlatItem(Items.ARMOR_STAND, ModelTemplates.FLAT_ITEM);
/*  512 */     generateFlatItem(Items.ARROW, ModelTemplates.FLAT_ITEM);
/*  513 */     generateFlatItem(Items.BAKED_POTATO, ModelTemplates.FLAT_ITEM);
/*  514 */     generateFlatItem(Items.BAMBOO, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  515 */     generateFlatItem(Items.BEEF, ModelTemplates.FLAT_ITEM);
/*  516 */     generateFlatItem(Items.BEETROOT, ModelTemplates.FLAT_ITEM);
/*  517 */     generateFlatItem(Items.BEETROOT_SOUP, ModelTemplates.FLAT_ITEM);
/*  518 */     generateFlatItem(Items.BIRCH_BOAT, ModelTemplates.FLAT_ITEM);
/*  519 */     generateFlatItem(Items.BIRCH_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  520 */     generateFlatItem(Items.BLAZE_POWDER, ModelTemplates.FLAT_ITEM);
/*  521 */     generateFlatItem(Items.BLAZE_ROD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  522 */     generateFlatItem(Items.BONE_MEAL, ModelTemplates.FLAT_ITEM);
/*  523 */     generateFlatItem(Items.BORDURE_INDENTED_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  524 */     generateFlatItem(Items.BOOK, ModelTemplates.FLAT_ITEM);
/*  525 */     generateFlatItem(Items.BOWL, ModelTemplates.FLAT_ITEM);
/*  526 */     generateFlatItem(Items.BREAD, ModelTemplates.FLAT_ITEM);
/*  527 */     generateFlatItem(Items.BRICK, ModelTemplates.FLAT_ITEM);
/*  528 */     generateFlatItem(Items.BREEZE_ROD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  529 */     generateFlatItem(Items.BUCKET, ModelTemplates.FLAT_ITEM);
/*  530 */     generateFlatItem(Items.CARROT_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  531 */     generateFlatItem(Items.WARPED_FUNGUS_ON_A_STICK, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
/*  532 */     generateFlatItem(Items.CHARCOAL, ModelTemplates.FLAT_ITEM);
/*  533 */     generateFlatItem(Items.CHEST_MINECART, ModelTemplates.FLAT_ITEM);
/*  534 */     generateFlatItem(Items.CHICKEN, ModelTemplates.FLAT_ITEM);
/*  535 */     generateFlatItem(Items.CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/*  536 */     generateFlatItem(Items.CLAY_BALL, ModelTemplates.FLAT_ITEM);
/*  538 */     generateClockItem(Items.CLOCK);
/*  540 */     generateFlatItem(Items.COAL, ModelTemplates.FLAT_ITEM);
/*  541 */     generateFlatItem(Items.COD_BUCKET, ModelTemplates.FLAT_ITEM);
/*  542 */     generateFlatItem(Items.COMMAND_BLOCK_MINECART, ModelTemplates.FLAT_ITEM);
/*  544 */     generateStandardCompassItem(Items.COMPASS);
/*  545 */     generateRecoveryCompassItem(Items.RECOVERY_COMPASS);
/*  547 */     generateFlatItem(Items.COOKED_BEEF, ModelTemplates.FLAT_ITEM);
/*  548 */     generateFlatItem(Items.COOKED_CHICKEN, ModelTemplates.FLAT_ITEM);
/*  549 */     generateFlatItem(Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
/*  550 */     generateFlatItem(Items.COOKED_MUTTON, ModelTemplates.FLAT_ITEM);
/*  551 */     generateFlatItem(Items.COOKED_PORKCHOP, ModelTemplates.FLAT_ITEM);
/*  552 */     generateFlatItem(Items.COOKED_RABBIT, ModelTemplates.FLAT_ITEM);
/*  553 */     generateFlatItem(Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
/*  554 */     generateFlatItem(Items.COOKIE, ModelTemplates.FLAT_ITEM);
/*  555 */     generateFlatItem(Items.RAW_COPPER, ModelTemplates.FLAT_ITEM);
/*  556 */     generateFlatItem(Items.COPPER_NUGGET, ModelTemplates.FLAT_ITEM);
/*  557 */     generateFlatItem(Items.COPPER_INGOT, ModelTemplates.FLAT_ITEM);
/*  558 */     generateFlatItem(Items.COPPER_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  559 */     generateFlatItem(Items.COPPER_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  560 */     generateFlatItem(Items.COPPER_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  561 */     generateFlatItem(Items.COPPER_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  562 */     generateFlatItem(Items.COPPER_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  563 */     generateFlatItem(Items.COPPER_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  564 */     generateFlatItem(Items.COPPER_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  565 */     generateFlatItem(Items.CREEPER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  566 */     generateFlatItem(Items.DARK_OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  567 */     generateFlatItem(Items.DARK_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  568 */     generateFlatItem(Items.DIAMOND, ModelTemplates.FLAT_ITEM);
/*  569 */     generateFlatItem(Items.DIAMOND_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  570 */     generateFlatItem(Items.DIAMOND_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  571 */     generateFlatItem(Items.DIAMOND_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  572 */     generateFlatItem(Items.DIAMOND_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  573 */     generateFlatItem(Items.DIAMOND_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  574 */     generateFlatItem(Items.DIAMOND_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  575 */     generateFlatItem(Items.DIAMOND_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  576 */     generateFlatItem(Items.DRAGON_BREATH, ModelTemplates.FLAT_ITEM);
/*  577 */     generateFlatItem(Items.DRIED_KELP, ModelTemplates.FLAT_ITEM);
/*  578 */     Items.DYE.forEach(item -> generateFlatItem(item, ModelTemplates.FLAT_ITEM));
/*  579 */     generateFlatItem(Items.EGG, ModelTemplates.FLAT_ITEM);
/*  580 */     generateFlatItem(Items.BLUE_EGG, ModelTemplates.FLAT_ITEM);
/*  581 */     generateFlatItem(Items.BROWN_EGG, ModelTemplates.FLAT_ITEM);
/*  582 */     generateFlatItem(Items.EMERALD, ModelTemplates.FLAT_ITEM);
/*  583 */     generateFlatItem(Items.ENCHANTED_BOOK, ModelTemplates.FLAT_ITEM);
/*  584 */     generateFlatItem(Items.ENDER_EYE, ModelTemplates.FLAT_ITEM);
/*  585 */     generateFlatItem(Items.ENDER_PEARL, ModelTemplates.FLAT_ITEM);
/*  586 */     generateFlatItem(Items.END_CRYSTAL, ModelTemplates.FLAT_ITEM);
/*  587 */     generateFlatItem(Items.EXPERIENCE_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  588 */     generateFlatItem(Items.FERMENTED_SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/*  589 */     generateFlatItem(Items.FIELD_MASONED_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  590 */     generateFlatItem(Items.FIREWORK_ROCKET, ModelTemplates.FLAT_ITEM);
/*  591 */     generateFlatItem(Items.FIRE_CHARGE, ModelTemplates.FLAT_ITEM);
/*  592 */     generateFlatItem(Items.FLINT, ModelTemplates.FLAT_ITEM);
/*  593 */     generateFlatItem(Items.FLINT_AND_STEEL, ModelTemplates.FLAT_ITEM);
/*  594 */     generateFlatItem(Items.FLOW_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  595 */     generateFlatItem(Items.FLOWER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  596 */     generateFlatItem(Items.FURNACE_MINECART, ModelTemplates.FLAT_ITEM);
/*  597 */     generateFlatItem(Items.GHAST_TEAR, ModelTemplates.FLAT_ITEM);
/*  598 */     generateFlatItem(Items.GLASS_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  599 */     generateFlatItem(Items.GLISTERING_MELON_SLICE, ModelTemplates.FLAT_ITEM);
/*  600 */     generateFlatItem(Items.GLOBE_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  601 */     generateFlatItem(Items.GLOW_BERRIES, ModelTemplates.FLAT_ITEM);
/*  602 */     generateFlatItem(Items.GLOWSTONE_DUST, ModelTemplates.FLAT_ITEM);
/*  603 */     generateFlatItem(Items.GLOW_INK_SAC, ModelTemplates.FLAT_ITEM);
/*  604 */     generateFlatItem(Items.GLOW_ITEM_FRAME, ModelTemplates.FLAT_ITEM);
/*  605 */     generateFlatItem(Items.RAW_GOLD, ModelTemplates.FLAT_ITEM);
/*  606 */     generateFlatItem(Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/*  607 */     generateFlatItem(Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  608 */     generateFlatItem(Items.GOLDEN_CARROT, ModelTemplates.FLAT_ITEM);
/*  609 */     generateFlatItem(Items.GOLDEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  610 */     generateFlatItem(Items.GOLDEN_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  611 */     generateFlatItem(Items.GOLDEN_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  612 */     generateFlatItem(Items.GOLDEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  613 */     generateFlatItem(Items.GOLDEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  614 */     generateFlatItem(Items.GOLDEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  615 */     generateFlatItem(Items.GOLD_INGOT, ModelTemplates.FLAT_ITEM);
/*  616 */     generateFlatItem(Items.GOLD_NUGGET, ModelTemplates.FLAT_ITEM);
/*  617 */     generateFlatItem(Items.GUNPOWDER, ModelTemplates.FLAT_ITEM);
/*  618 */     generateFlatItem(Items.GUSTER_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  619 */     generateFlatItem(Items.HEART_OF_THE_SEA, ModelTemplates.FLAT_ITEM);
/*  620 */     generateFlatItem(Items.HONEYCOMB, ModelTemplates.FLAT_ITEM);
/*  621 */     generateFlatItem(Items.HONEY_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  622 */     generateFlatItem(Items.HOPPER_MINECART, ModelTemplates.FLAT_ITEM);
/*  623 */     generateFlatItem(Items.INK_SAC, ModelTemplates.FLAT_ITEM);
/*  624 */     generateFlatItem(Items.RAW_IRON, ModelTemplates.FLAT_ITEM);
/*  625 */     generateFlatItem(Items.IRON_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  626 */     generateFlatItem(Items.IRON_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  627 */     generateFlatItem(Items.IRON_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  628 */     generateFlatItem(Items.IRON_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  629 */     generateFlatItem(Items.IRON_INGOT, ModelTemplates.FLAT_ITEM);
/*  630 */     generateFlatItem(Items.IRON_NUGGET, ModelTemplates.FLAT_ITEM);
/*  631 */     generateFlatItem(Items.IRON_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  632 */     generateFlatItem(Items.IRON_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  633 */     generateFlatItem(Items.IRON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  634 */     generateFlatItem(Items.ITEM_FRAME, ModelTemplates.FLAT_ITEM);
/*  635 */     generateFlatItem(Items.JUNGLE_BOAT, ModelTemplates.FLAT_ITEM);
/*  636 */     generateFlatItem(Items.JUNGLE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  637 */     generateFlatItem(Items.KNOWLEDGE_BOOK, ModelTemplates.FLAT_ITEM);
/*  638 */     generateFlatItem(Items.LAPIS_LAZULI, ModelTemplates.FLAT_ITEM);
/*  639 */     generateFlatItem(Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
/*  640 */     generateFlatItem(Items.LEATHER, ModelTemplates.FLAT_ITEM);
/*  641 */     generateFlatItem(Items.MAGMA_CREAM, ModelTemplates.FLAT_ITEM);
/*  642 */     generateFlatItem(Items.MANGROVE_BOAT, ModelTemplates.FLAT_ITEM);
/*  643 */     generateFlatItem(Items.MANGROVE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  644 */     generateFlatItem(Items.BAMBOO_RAFT, ModelTemplates.FLAT_ITEM);
/*  645 */     generateFlatItem(Items.BAMBOO_CHEST_RAFT, ModelTemplates.FLAT_ITEM);
/*  646 */     generateFlatItem(Items.MAP, ModelTemplates.FLAT_ITEM);
/*  647 */     generateFlatItem(Items.MELON_SLICE, ModelTemplates.FLAT_ITEM);
/*  648 */     generateFlatItem(Items.MILK_BUCKET, ModelTemplates.FLAT_ITEM);
/*  649 */     generateFlatItem(Items.MINECART, ModelTemplates.FLAT_ITEM);
/*  650 */     generateFlatItem(Items.MOJANG_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  651 */     generateFlatItem(Items.MUSHROOM_STEW, ModelTemplates.FLAT_ITEM);
/*  652 */     generateFlatItem(Items.DISC_FRAGMENT_5, ModelTemplates.FLAT_ITEM);
/*  653 */     generateFlatItem(Items.MUSIC_DISC_11, ModelTemplates.MUSIC_DISC);
/*  654 */     generateFlatItem(Items.MUSIC_DISC_13, ModelTemplates.MUSIC_DISC);
/*  655 */     generateFlatItem(Items.MUSIC_DISC_BLOCKS, ModelTemplates.MUSIC_DISC);
/*  656 */     generateFlatItem(Items.MUSIC_DISC_BOUNCE, ModelTemplates.MUSIC_DISC);
/*  657 */     generateFlatItem(Items.MUSIC_DISC_CAT, ModelTemplates.MUSIC_DISC);
/*  658 */     generateFlatItem(Items.MUSIC_DISC_CHIRP, ModelTemplates.MUSIC_DISC);
/*  659 */     generateFlatItem(Items.MUSIC_DISC_CREATOR, ModelTemplates.MUSIC_DISC);
/*  660 */     generateFlatItem(Items.MUSIC_DISC_CREATOR_MUSIC_BOX, ModelTemplates.MUSIC_DISC);
/*  661 */     generateFlatItem(Items.MUSIC_DISC_FAR, ModelTemplates.MUSIC_DISC);
/*  662 */     generateFlatItem(Items.MUSIC_DISC_LAVA_CHICKEN, ModelTemplates.MUSIC_DISC);
/*  663 */     generateFlatItem(Items.MUSIC_DISC_MALL, ModelTemplates.MUSIC_DISC);
/*  664 */     generateFlatItem(Items.MUSIC_DISC_MELLOHI, ModelTemplates.MUSIC_DISC);
/*  665 */     generateFlatItem(Items.MUSIC_DISC_PIGSTEP, ModelTemplates.MUSIC_DISC);
/*  666 */     generateFlatItem(Items.MUSIC_DISC_PRECIPICE, ModelTemplates.MUSIC_DISC);
/*  667 */     generateFlatItem(Items.MUSIC_DISC_STAL, ModelTemplates.MUSIC_DISC);
/*  668 */     generateFlatItem(Items.MUSIC_DISC_STRAD, ModelTemplates.MUSIC_DISC);
/*  669 */     generateFlatItem(Items.MUSIC_DISC_WAIT, ModelTemplates.MUSIC_DISC);
/*  670 */     generateFlatItem(Items.MUSIC_DISC_WARD, ModelTemplates.MUSIC_DISC);
/*  671 */     generateFlatItem(Items.MUSIC_DISC_OTHERSIDE, ModelTemplates.MUSIC_DISC);
/*  672 */     generateFlatItem(Items.MUSIC_DISC_RELIC, ModelTemplates.MUSIC_DISC);
/*  673 */     generateFlatItem(Items.MUSIC_DISC_5, ModelTemplates.MUSIC_DISC);
/*  674 */     generateFlatItem(Items.MUSIC_DISC_TEARS, ModelTemplates.MUSIC_DISC);
/*  675 */     generateFlatItem(Items.MUTTON, ModelTemplates.FLAT_ITEM);
/*  676 */     generateFlatItem(Items.NAME_TAG, ModelTemplates.FLAT_ITEM);
/*  677 */     generateFlatItem(Items.NAUTILUS_SHELL, ModelTemplates.FLAT_ITEM);
/*  678 */     generateFlatItem(Items.NETHERITE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  679 */     generateFlatItem(Items.NETHERITE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  680 */     generateFlatItem(Items.NETHERITE_INGOT, ModelTemplates.FLAT_ITEM);
/*  681 */     generateFlatItem(Items.NETHERITE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  682 */     generateFlatItem(Items.NETHERITE_SCRAP, ModelTemplates.FLAT_ITEM);
/*  683 */     generateFlatItem(Items.NETHERITE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  684 */     generateFlatItem(Items.NETHERITE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  685 */     generateFlatItem(Items.NETHERITE_NAUTILUS_ARMOR, ModelTemplates.FLAT_ITEM);
/*  686 */     generateFlatItem(Items.NETHERITE_HORSE_ARMOR, ModelTemplates.FLAT_ITEM);
/*  687 */     generateFlatItem(Items.NETHER_BRICK, ModelTemplates.FLAT_ITEM);
/*  688 */     generateFlatItem(Items.RESIN_BRICK, ModelTemplates.FLAT_ITEM);
/*  689 */     generateFlatItem(Items.NETHER_STAR, ModelTemplates.FLAT_ITEM);
/*  690 */     generateFlatItem(Items.OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  691 */     generateFlatItem(Items.OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  692 */     generateFlatItem(Items.PAINTING, ModelTemplates.FLAT_ITEM);
/*  693 */     generateFlatItem(Items.PALE_OAK_BOAT, ModelTemplates.FLAT_ITEM);
/*  694 */     generateFlatItem(Items.PALE_OAK_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  695 */     generateFlatItem(Items.PAPER, ModelTemplates.FLAT_ITEM);
/*  696 */     generateFlatItem(Items.PHANTOM_MEMBRANE, ModelTemplates.FLAT_ITEM);
/*  697 */     generateFlatItem(Items.PIGLIN_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  698 */     generateFlatItem(Items.POISONOUS_POTATO, ModelTemplates.FLAT_ITEM);
/*  699 */     generateFlatItem(Items.POPPED_CHORUS_FRUIT, ModelTemplates.FLAT_ITEM);
/*  700 */     generateFlatItem(Items.PORKCHOP, ModelTemplates.FLAT_ITEM);
/*  701 */     generateFlatItem(Items.POWDER_SNOW_BUCKET, ModelTemplates.FLAT_ITEM);
/*  702 */     generateFlatItem(Items.PRISMARINE_CRYSTALS, ModelTemplates.FLAT_ITEM);
/*  703 */     generateFlatItem(Items.PRISMARINE_SHARD, ModelTemplates.FLAT_ITEM);
/*  704 */     generateFlatItem(Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
/*  705 */     generateFlatItem(Items.PUFFERFISH_BUCKET, ModelTemplates.FLAT_ITEM);
/*  706 */     generateFlatItem(Items.PUMPKIN_PIE, ModelTemplates.FLAT_ITEM);
/*  707 */     generateFlatItem(Items.QUARTZ, ModelTemplates.FLAT_ITEM);
/*  708 */     generateFlatItem(Items.RABBIT, ModelTemplates.FLAT_ITEM);
/*  709 */     generateFlatItem(Items.RABBIT_FOOT, ModelTemplates.FLAT_ITEM);
/*  710 */     generateFlatItem(Items.RABBIT_HIDE, ModelTemplates.FLAT_ITEM);
/*  711 */     generateFlatItem(Items.RABBIT_STEW, ModelTemplates.FLAT_ITEM);
/*  712 */     generateFlatItem(Items.ROTTEN_FLESH, ModelTemplates.FLAT_ITEM);
/*  713 */     generateFlatItem(Items.SADDLE, ModelTemplates.FLAT_ITEM);
/*  714 */     generateFlatItem(Items.SALMON, ModelTemplates.FLAT_ITEM);
/*  715 */     generateFlatItem(Items.SALMON_BUCKET, ModelTemplates.FLAT_ITEM);
/*  716 */     generateFlatItem(Items.TURTLE_SCUTE, ModelTemplates.FLAT_ITEM);
/*  717 */     generateFlatItem(Items.SHEARS, ModelTemplates.FLAT_ITEM);
/*  718 */     generateFlatItem(Items.SHULKER_SHELL, ModelTemplates.FLAT_ITEM);
/*  719 */     generateFlatItem(Items.SKULL_BANNER_PATTERN, ModelTemplates.FLAT_ITEM);
/*  720 */     generateFlatItem(Items.SLIME_BALL, ModelTemplates.FLAT_ITEM);
/*  721 */     generateFlatItem(Items.SNOWBALL, ModelTemplates.FLAT_ITEM);
/*  722 */     generateFlatItem(Items.ECHO_SHARD, ModelTemplates.FLAT_ITEM);
/*  723 */     generateFlatItem(Items.SPECTRAL_ARROW, ModelTemplates.FLAT_ITEM);
/*  724 */     generateFlatItem(Items.SPIDER_EYE, ModelTemplates.FLAT_ITEM);
/*  725 */     generateFlatItem(Items.SPRUCE_BOAT, ModelTemplates.FLAT_ITEM);
/*  726 */     generateFlatItem(Items.SPRUCE_CHEST_BOAT, ModelTemplates.FLAT_ITEM);
/*  727 */     generateFlatItem(Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  728 */     generateFlatItem(Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  729 */     generateFlatItem(Items.STONE_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  730 */     generateFlatItem(Items.STONE_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  731 */     generateFlatItem(Items.STONE_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  732 */     generateFlatItem(Items.STONE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  733 */     generateFlatItem(Items.SUGAR, ModelTemplates.FLAT_ITEM);
/*  734 */     generateFlatItem(Items.SUSPICIOUS_STEW, ModelTemplates.FLAT_ITEM);
/*  735 */     generateFlatItem(Items.TNT_MINECART, ModelTemplates.FLAT_ITEM);
/*  736 */     generateFlatItem(Items.TOTEM_OF_UNDYING, ModelTemplates.FLAT_ITEM);
/*  737 */     generateFlatItem(Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
/*  738 */     generateFlatItem(Items.TROPICAL_FISH_BUCKET, ModelTemplates.FLAT_ITEM);
/*  739 */     generateFlatItem(Items.AXOLOTL_BUCKET, ModelTemplates.FLAT_ITEM);
/*  740 */     generateFlatItem(Items.TADPOLE_BUCKET, ModelTemplates.FLAT_ITEM);
/*  741 */     generateFlatItem(Items.SULFUR_CUBE_BUCKET, ModelTemplates.FLAT_ITEM);
/*  742 */     generateFlatItem(Items.WATER_BUCKET, ModelTemplates.FLAT_ITEM);
/*  743 */     generateFlatItem(Items.WHEAT, ModelTemplates.FLAT_ITEM);
/*  744 */     generateFlatItem(Items.WIND_CHARGE, ModelTemplates.FLAT_ITEM);
/*  745 */     generateFlatItem(Items.MACE, ModelTemplates.FLAT_HANDHELD_MACE_ITEM);
/*  746 */     generateFlatItem(Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  747 */     generateFlatItem(Items.WOODEN_HOE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  748 */     generateFlatItem(Items.WOODEN_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  749 */     generateFlatItem(Items.WOODEN_SHOVEL, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  750 */     generateFlatItem(Items.WOODEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  751 */     generateFlatItem(Items.WRITABLE_BOOK, ModelTemplates.FLAT_ITEM);
/*  752 */     generateFlatItem(Items.WRITTEN_BOOK, ModelTemplates.FLAT_ITEM);
/*  754 */     generateFlatItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  755 */     generateFlatItem(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  756 */     generateFlatItem(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  757 */     generateFlatItem(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  758 */     generateFlatItem(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  759 */     generateFlatItem(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  760 */     generateFlatItem(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  761 */     generateFlatItem(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  762 */     generateFlatItem(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  763 */     generateFlatItem(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  764 */     generateFlatItem(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  765 */     generateFlatItem(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  766 */     generateFlatItem(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  767 */     generateFlatItem(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  768 */     generateFlatItem(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  769 */     generateFlatItem(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  770 */     generateFlatItem(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  771 */     generateFlatItem(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  772 */     generateFlatItem(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
/*  774 */     generateFlatItem(Items.DEBUG_STICK, Items.STICK, ModelTemplates.FLAT_HANDHELD_ITEM);
/*  775 */     generateFlatItem(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, ModelTemplates.FLAT_ITEM);
/*  777 */     generateTrimmableItem(Items.TURTLE_HELMET, EquipmentAssets.TURTLE_SCUTE, TRIM_PREFIX_HELMET, false);
/*  779 */     generateTrimmableItem(Items.LEATHER_HELMET, EquipmentAssets.LEATHER, TRIM_PREFIX_HELMET, true);
/*  780 */     generateTrimmableItem(Items.LEATHER_CHESTPLATE, EquipmentAssets.LEATHER, TRIM_PREFIX_CHESTPLATE, true);
/*  781 */     generateTrimmableItem(Items.LEATHER_LEGGINGS, EquipmentAssets.LEATHER, TRIM_PREFIX_LEGGINGS, true);
/*  782 */     generateTrimmableItem(Items.LEATHER_BOOTS, EquipmentAssets.LEATHER, TRIM_PREFIX_BOOTS, true);
/*  784 */     generateTrimmableItem(Items.COPPER_HELMET, EquipmentAssets.COPPER, TRIM_PREFIX_HELMET, false);
/*  785 */     generateTrimmableItem(Items.COPPER_CHESTPLATE, EquipmentAssets.COPPER, TRIM_PREFIX_CHESTPLATE, false);
/*  786 */     generateTrimmableItem(Items.COPPER_LEGGINGS, EquipmentAssets.COPPER, TRIM_PREFIX_LEGGINGS, false);
/*  787 */     generateTrimmableItem(Items.COPPER_BOOTS, EquipmentAssets.COPPER, TRIM_PREFIX_BOOTS, false);
/*  789 */     generateTrimmableItem(Items.CHAINMAIL_HELMET, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_HELMET, false);
/*  790 */     generateTrimmableItem(Items.CHAINMAIL_CHESTPLATE, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_CHESTPLATE, false);
/*  791 */     generateTrimmableItem(Items.CHAINMAIL_LEGGINGS, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_LEGGINGS, false);
/*  792 */     generateTrimmableItem(Items.CHAINMAIL_BOOTS, EquipmentAssets.CHAINMAIL, TRIM_PREFIX_BOOTS, false);
/*  794 */     generateTrimmableItem(Items.IRON_HELMET, EquipmentAssets.IRON, TRIM_PREFIX_HELMET, false);
/*  795 */     generateTrimmableItem(Items.IRON_CHESTPLATE, EquipmentAssets.IRON, TRIM_PREFIX_CHESTPLATE, false);
/*  796 */     generateTrimmableItem(Items.IRON_LEGGINGS, EquipmentAssets.IRON, TRIM_PREFIX_LEGGINGS, false);
/*  797 */     generateTrimmableItem(Items.IRON_BOOTS, EquipmentAssets.IRON, TRIM_PREFIX_BOOTS, false);
/*  799 */     generateTrimmableItem(Items.DIAMOND_HELMET, EquipmentAssets.DIAMOND, TRIM_PREFIX_HELMET, false);
/*  800 */     generateTrimmableItem(Items.DIAMOND_CHESTPLATE, EquipmentAssets.DIAMOND, TRIM_PREFIX_CHESTPLATE, false);
/*  801 */     generateTrimmableItem(Items.DIAMOND_LEGGINGS, EquipmentAssets.DIAMOND, TRIM_PREFIX_LEGGINGS, false);
/*  802 */     generateTrimmableItem(Items.DIAMOND_BOOTS, EquipmentAssets.DIAMOND, TRIM_PREFIX_BOOTS, false);
/*  804 */     generateTrimmableItem(Items.GOLDEN_HELMET, EquipmentAssets.GOLD, TRIM_PREFIX_HELMET, false);
/*  805 */     generateTrimmableItem(Items.GOLDEN_CHESTPLATE, EquipmentAssets.GOLD, TRIM_PREFIX_CHESTPLATE, false);
/*  806 */     generateTrimmableItem(Items.GOLDEN_LEGGINGS, EquipmentAssets.GOLD, TRIM_PREFIX_LEGGINGS, false);
/*  807 */     generateTrimmableItem(Items.GOLDEN_BOOTS, EquipmentAssets.GOLD, TRIM_PREFIX_BOOTS, false);
/*  809 */     generateTrimmableItem(Items.NETHERITE_HELMET, EquipmentAssets.NETHERITE, TRIM_PREFIX_HELMET, false);
/*  810 */     generateTrimmableItem(Items.NETHERITE_CHESTPLATE, EquipmentAssets.NETHERITE, TRIM_PREFIX_CHESTPLATE, false);
/*  811 */     generateTrimmableItem(Items.NETHERITE_LEGGINGS, EquipmentAssets.NETHERITE, TRIM_PREFIX_LEGGINGS, false);
/*  812 */     generateTrimmableItem(Items.NETHERITE_BOOTS, EquipmentAssets.NETHERITE, TRIM_PREFIX_BOOTS, false);
/*  814 */     generateItemWithTintedBaseLayer(Items.LEATHER_HORSE_ARMOR, -6265536);
/*  816 */     generateFlatItem(Items.ANGLER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  817 */     generateFlatItem(Items.ARCHER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  818 */     generateFlatItem(Items.ARMS_UP_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  819 */     generateFlatItem(Items.BLADE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  820 */     generateFlatItem(Items.BREWER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  821 */     generateFlatItem(Items.BURN_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  822 */     generateFlatItem(Items.DANGER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  823 */     generateFlatItem(Items.EXPLORER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  824 */     generateFlatItem(Items.FLOW_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  825 */     generateFlatItem(Items.FRIEND_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  826 */     generateFlatItem(Items.GUSTER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  827 */     generateFlatItem(Items.HEART_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  828 */     generateFlatItem(Items.HEARTBREAK_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  829 */     generateFlatItem(Items.HOWL_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  830 */     generateFlatItem(Items.MINER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  831 */     generateFlatItem(Items.MOURNER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  832 */     generateFlatItem(Items.PLENTY_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  833 */     generateFlatItem(Items.PRIZE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  834 */     generateFlatItem(Items.SCRAPE_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  835 */     generateFlatItem(Items.SHEAF_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  836 */     generateFlatItem(Items.SHELTER_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  837 */     generateFlatItem(Items.SKULL_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  838 */     generateFlatItem(Items.SNORT_POTTERY_SHERD, ModelTemplates.FLAT_ITEM);
/*  839 */     generateFlatItem(Items.TRIAL_KEY, ModelTemplates.FLAT_ITEM);
/*  840 */     generateFlatItem(Items.OMINOUS_TRIAL_KEY, ModelTemplates.FLAT_ITEM);
/*  841 */     generateFlatItem(Items.OMINOUS_BOTTLE, ModelTemplates.FLAT_ITEM);
/*  843 */     generateItemWithTintedOverlay(Items.FIREWORK_STAR, (ItemTintSource)new Firework());
/*  844 */     generateItemWithTintedOverlay(Items.FILLED_MAP, "_markings", (ItemTintSource)new MapColor());
/*  846 */     generateBundleModels(Items.BUNDLE);
/*  847 */     Items.DYED_BUNDLE.forEach(this::generateBundleModels);
/*  849 */     generateSpyglass(Items.SPYGLASS);
/*  850 */     generateTrident(Items.TRIDENT);
/*  851 */     generateTwoLayerDyedItem(Items.WOLF_ARMOR);
/*  853 */     Items.HARNESS.forEach(item -> generateFlatItem(item, ModelTemplates.FLAT_ITEM));
/*  855 */     generateBow(Items.BOW);
/*  856 */     generateCrossbow(Items.CROSSBOW);
/*  857 */     generateElytra(Items.ELYTRA);
/*  858 */     generateBrush(Items.BRUSH);
/*  859 */     generateFishingRod(Items.FISHING_ROD);
/*  860 */     generateGoatHorn(Items.GOAT_HORN);
/*  861 */     generateShield(Items.SHIELD);
/*  863 */     generateSpear(Items.WOODEN_SPEAR);
/*  864 */     generateSpear(Items.STONE_SPEAR);
/*  865 */     generateSpear(Items.COPPER_SPEAR);
/*  866 */     generateSpear(Items.GOLDEN_SPEAR);
/*  867 */     generateSpear(Items.IRON_SPEAR);
/*  868 */     generateSpear(Items.DIAMOND_SPEAR);
/*  869 */     generateSpear(Items.NETHERITE_SPEAR);
/*  871 */     generateTippedArrow(Items.TIPPED_ARROW);
/*  872 */     generatePotion(Items.POTION);
/*  873 */     generatePotion(Items.SPLASH_POTION);
/*  874 */     generatePotion(Items.LINGERING_POTION);
/*  878 */     generateFlatItem(Items.CHICKEN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  879 */     generateFlatItem(Items.COW_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  880 */     generateFlatItem(Items.PIG_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  881 */     generateFlatItem(Items.SHEEP_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  884 */     generateFlatItem(Items.CAMEL_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  885 */     generateFlatItem(Items.DONKEY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  886 */     generateFlatItem(Items.HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  887 */     generateFlatItem(Items.MULE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  890 */     generateFlatItem(Items.CAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  891 */     generateFlatItem(Items.PARROT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  892 */     generateFlatItem(Items.WOLF_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  895 */     generateFlatItem(Items.ARMADILLO_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  896 */     generateFlatItem(Items.BAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  897 */     generateFlatItem(Items.BEE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  898 */     generateFlatItem(Items.FOX_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  899 */     generateFlatItem(Items.GOAT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  900 */     generateFlatItem(Items.LLAMA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  901 */     generateFlatItem(Items.OCELOT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  902 */     generateFlatItem(Items.PANDA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  903 */     generateFlatItem(Items.POLAR_BEAR_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  904 */     generateFlatItem(Items.RABBIT_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  907 */     generateFlatItem(Items.AXOLOTL_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  908 */     generateFlatItem(Items.COD_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  909 */     generateFlatItem(Items.DOLPHIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  910 */     generateFlatItem(Items.FROG_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  911 */     generateFlatItem(Items.GLOW_SQUID_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  912 */     generateFlatItem(Items.NAUTILUS_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  913 */     generateFlatItem(Items.PUFFERFISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  914 */     generateFlatItem(Items.SALMON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  915 */     generateFlatItem(Items.SQUID_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  916 */     generateFlatItem(Items.TADPOLE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  917 */     generateFlatItem(Items.TROPICAL_FISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  918 */     generateFlatItem(Items.TURTLE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  921 */     generateFlatItem(Items.ALLAY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  922 */     generateFlatItem(Items.MOOSHROOM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  923 */     generateFlatItem(Items.SNIFFER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  924 */     generateFlatItem(Items.SULFUR_CUBE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  927 */     generateFlatItem(Items.COPPER_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  928 */     generateFlatItem(Items.IRON_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  929 */     generateFlatItem(Items.SNOW_GOLEM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  932 */     generateFlatItem(Items.TRADER_LLAMA_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  933 */     generateFlatItem(Items.VILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  934 */     generateFlatItem(Items.WANDERING_TRADER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  937 */     generateFlatItem(Items.BOGGED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  938 */     generateFlatItem(Items.CAMEL_HUSK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  939 */     generateFlatItem(Items.DROWNED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  940 */     generateFlatItem(Items.HUSK_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  941 */     generateFlatItem(Items.PARCHED_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  942 */     generateFlatItem(Items.SKELETON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  943 */     generateFlatItem(Items.SKELETON_HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  944 */     generateFlatItem(Items.STRAY_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  945 */     generateFlatItem(Items.WITHER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  946 */     generateFlatItem(Items.WITHER_SKELETON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  947 */     generateFlatItem(Items.ZOMBIE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  948 */     generateFlatItem(Items.ZOMBIE_HORSE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  949 */     generateFlatItem(Items.ZOMBIE_NAUTILUS_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  950 */     generateFlatItem(Items.ZOMBIE_VILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  954 */     generateFlatItem(Items.CAVE_SPIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  955 */     generateFlatItem(Items.SPIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  958 */     generateFlatItem(Items.BREEZE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  959 */     generateFlatItem(Items.CREAKING_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  960 */     generateFlatItem(Items.CREEPER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  961 */     generateFlatItem(Items.ELDER_GUARDIAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  962 */     generateFlatItem(Items.GUARDIAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  963 */     generateFlatItem(Items.PHANTOM_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  964 */     generateFlatItem(Items.SILVERFISH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  965 */     generateFlatItem(Items.SLIME_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  966 */     generateFlatItem(Items.WARDEN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  967 */     generateFlatItem(Items.WITCH_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  970 */     generateFlatItem(Items.EVOKER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  971 */     generateFlatItem(Items.PILLAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  972 */     generateFlatItem(Items.RAVAGER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  973 */     generateFlatItem(Items.VEX_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  974 */     generateFlatItem(Items.VINDICATOR_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  977 */     generateFlatItem(Items.BLAZE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  978 */     generateFlatItem(Items.GHAST_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  979 */     generateFlatItem(Items.HAPPY_GHAST_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  980 */     generateFlatItem(Items.HOGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  981 */     generateFlatItem(Items.MAGMA_CUBE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  982 */     generateFlatItem(Items.PIGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  983 */     generateFlatItem(Items.PIGLIN_BRUTE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  984 */     generateFlatItem(Items.STRIDER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  985 */     generateFlatItem(Items.ZOGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  986 */     generateFlatItem(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  989 */     generateFlatItem(Items.ENDER_DRAGON_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  990 */     generateFlatItem(Items.ENDERMAN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  991 */     generateFlatItem(Items.ENDERMITE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  992 */     generateFlatItem(Items.SHULKER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
/*  994 */     declareCustomModelItem(Items.AIR);
/*  995 */     declareCustomModelItem(Items.AMETHYST_CLUSTER);
/*  996 */     declareCustomModelItem(Items.SMALL_AMETHYST_BUD);
/*  997 */     declareCustomModelItem(Items.MEDIUM_AMETHYST_BUD);
/*  998 */     declareCustomModelItem(Items.LARGE_AMETHYST_BUD);
/*  999 */     declareCustomModelItem(Items.SMALL_DRIPLEAF);
/* 1000 */     declareCustomModelItem(Items.BIG_DRIPLEAF);
/* 1001 */     declareCustomModelItem(Items.HANGING_ROOTS);
/* 1002 */     declareCustomModelItem(Items.POINTED_DRIPSTONE);
/* 1003 */     declareCustomModelItem(Items.SULFUR_SPIKE);
/* 1004 */     declareCustomModelItem(Items.BONE);
/* 1005 */     declareCustomModelItem(Items.COD);
/* 1006 */     declareCustomModelItem(Items.FEATHER);
/* 1007 */     declareCustomModelItem(Items.LEAD);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ItemModelGenerators.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */