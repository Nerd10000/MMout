/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.ChestSpecialRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ 
/*     */ public class Sheets {
/*  24 */   public static final Identifier SHULKER_SHEET = Identifier.withDefaultNamespace("textures/atlas/shulker_boxes.png");
/*     */   
/*  25 */   public static final Identifier BANNER_SHEET = Identifier.withDefaultNamespace("textures/atlas/banner_patterns.png");
/*     */   
/*  26 */   public static final Identifier SHIELD_SHEET = Identifier.withDefaultNamespace("textures/atlas/shield_patterns.png");
/*     */   
/*  27 */   public static final Identifier CHEST_SHEET = Identifier.withDefaultNamespace("textures/atlas/chest.png");
/*     */   
/*  28 */   public static final Identifier ARMOR_TRIMS_SHEET = Identifier.withDefaultNamespace("textures/atlas/armor_trims.png");
/*     */   
/*  29 */   public static final Identifier DECORATED_POT_SHEET = Identifier.withDefaultNamespace("textures/atlas/decorated_pot.png");
/*     */   
/*  30 */   public static final Identifier GUI_SHEET = Identifier.withDefaultNamespace("textures/atlas/gui.png");
/*     */   
/*  31 */   public static final Identifier MAP_DECORATIONS_SHEET = Identifier.withDefaultNamespace("textures/atlas/map_decorations.png");
/*     */   
/*  32 */   public static final Identifier PAINTINGS_SHEET = Identifier.withDefaultNamespace("textures/atlas/paintings.png");
/*     */   
/*  33 */   public static final Identifier CELESTIAL_SHEET = Identifier.withDefaultNamespace("textures/atlas/celestials.png");
/*     */   
/*  35 */   private static final RenderType ARMOR_TRIMS_SHEET_TYPE = RenderTypes.armorCutoutNoCull(ARMOR_TRIMS_SHEET);
/*     */   
/*  36 */   private static final RenderType ARMOR_TRIMS_DECAL_SHEET_TYPE = RenderTypes.createArmorDecalCutoutNoCull(ARMOR_TRIMS_SHEET);
/*     */   
/*  38 */   private static final RenderType CUTOUT_BLOCK_ITEM_SHEET = RenderTypes.itemCutout(TextureAtlas.LOCATION_BLOCKS);
/*     */   
/*  39 */   private static final RenderType TRANSLUCENT_BLOCK_ITEM_SHEET = RenderTypes.itemTranslucent(TextureAtlas.LOCATION_BLOCKS);
/*     */   
/*  40 */   private static final RenderType CUTOUT_ITEM_SHEET = RenderTypes.itemCutout(TextureAtlas.LOCATION_ITEMS);
/*     */   
/*  41 */   private static final RenderType TRANSLUCENT_ITEM_SHEET = RenderTypes.itemTranslucent(TextureAtlas.LOCATION_ITEMS);
/*     */   
/*  43 */   public static final SpriteMapper ITEMS_MAPPER = new SpriteMapper(TextureAtlas.LOCATION_ITEMS, "item");
/*     */   
/*  44 */   public static final SpriteMapper BLOCKS_MAPPER = new SpriteMapper(TextureAtlas.LOCATION_BLOCKS, "block");
/*     */   
/*  45 */   public static final SpriteMapper BLOCK_ENTITIES_MAPPER = new SpriteMapper(TextureAtlas.LOCATION_BLOCKS, "entity");
/*     */   
/*  47 */   public static final SpriteMapper BANNER_MAPPER = new SpriteMapper(BANNER_SHEET, "entity/banner");
/*     */   
/*  48 */   public static final SpriteMapper SHIELD_MAPPER = new SpriteMapper(SHIELD_SHEET, "entity/shield");
/*     */   
/*  49 */   public static final SpriteMapper CHEST_MAPPER = new SpriteMapper(CHEST_SHEET, "entity/chest");
/*     */   
/*  50 */   public static final SpriteMapper DECORATED_POT_MAPPER = new SpriteMapper(DECORATED_POT_SHEET, "entity/decorated_pot");
/*     */   
/*  51 */   public static final SpriteMapper SHULKER_MAPPER = new SpriteMapper(SHULKER_SHEET, "entity/shulker");
/*     */   
/*  53 */   public static final SpriteId DEFAULT_SHULKER_TEXTURE_LOCATION = SHULKER_MAPPER.defaultNamespaceApply("shulker");
/*     */   
/*  54 */   public static final List<SpriteId> SHULKER_TEXTURE_LOCATION = (List<SpriteId>)Arrays.<DyeColor>stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId)).map(Sheets::createShulkerSprite).collect(ImmutableList.toImmutableList());
/*     */   
/*  56 */   public static final SpriteId BANNER_BASE = BANNER_MAPPER.defaultNamespaceApply("banner_base");
/*     */   
/*  57 */   public static final SpriteId SHIELD_BASE = SHIELD_MAPPER.defaultNamespaceApply("shield_base");
/*     */   
/*  58 */   public static final SpriteId SHIELD_BASE_NO_PATTERN = SHIELD_MAPPER.defaultNamespaceApply("shield_base_nopattern");
/*     */   
/*  60 */   public static final SpriteId BANNER_PATTERN_BASE = BANNER_MAPPER.defaultNamespaceApply("base");
/*     */   
/*  61 */   public static final SpriteId SHIELD_PATTERN_BASE = SHIELD_MAPPER.defaultNamespaceApply("base");
/*     */   
/*  63 */   private static final Map<Identifier, SpriteId> BANNER_SPRITES = new HashMap<>();
/*     */   
/*  64 */   private static final Map<Identifier, SpriteId> SHIELD_SPRITES = new HashMap<>();
/*     */   
/*  66 */   public static final SpriteId DECORATED_POT_BASE = DECORATED_POT_MAPPER.defaultNamespaceApply("decorated_pot_base");
/*     */   
/*  67 */   public static final SpriteId DECORATED_POT_SIDE = DECORATED_POT_MAPPER.defaultNamespaceApply("decorated_pot_side");
/*     */   
/*  69 */   public static final SpriteId ENDER_CHEST_LOCATION = CHEST_MAPPER.defaultNamespaceApply("ender");
/*     */   
/*  71 */   public static final MultiblockChestResources<SpriteId> CHEST_REGULAR = ChestSpecialRenderer.REGULAR.map(CHEST_MAPPER::apply);
/*     */   
/*     */   static {
/*  71 */     Objects.requireNonNull(CHEST_MAPPER);
/*     */   }
/*     */   
/*  72 */   public static final MultiblockChestResources<SpriteId> CHEST_TRAPPED = ChestSpecialRenderer.TRAPPED.map(CHEST_MAPPER::apply);
/*     */   
/*     */   static {
/*  72 */     Objects.requireNonNull(CHEST_MAPPER);
/*     */   }
/*     */   
/*  73 */   public static final MultiblockChestResources<SpriteId> CHEST_CHRISTMAS = ChestSpecialRenderer.CHRISTMAS.map(CHEST_MAPPER::apply);
/*     */   
/*     */   public static final WeatheringCopperCollection.ByState<MultiblockChestResources<SpriteId>> CHEST_COPPER;
/*     */   
/*     */   static {
/*  73 */     Objects.requireNonNull(CHEST_MAPPER);
/*     */   }
/*     */   
/*     */   static {
/*  74 */     CHEST_COPPER = ChestSpecialRenderer.COPPER.map(r -> {
/*     */           Objects.requireNonNull(CHEST_MAPPER);
/*     */           return (Function)r.map(CHEST_MAPPER::apply);
/*     */         });
/*     */   }
/*     */   
/*     */   public static RenderType armorTrimsSheet(boolean decal) {
/*  77 */     return decal ? ARMOR_TRIMS_DECAL_SHEET_TYPE : ARMOR_TRIMS_SHEET_TYPE;
/*     */   }
/*     */   
/*     */   public static RenderType cutoutBlockItemSheet() {
/*  81 */     return CUTOUT_BLOCK_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType cutoutItemSheet() {
/*  85 */     return CUTOUT_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType translucentItemSheet() {
/*  89 */     return TRANSLUCENT_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static RenderType translucentBlockItemSheet() {
/*  93 */     return TRANSLUCENT_BLOCK_ITEM_SHEET;
/*     */   }
/*     */   
/*     */   public static SpriteId getShulkerBoxSprite(DyeColor color) {
/*  97 */     return SHULKER_TEXTURE_LOCATION.get(color.getId());
/*     */   }
/*     */   
/*     */   public static Identifier colorToShulkerSprite(DyeColor color) {
/* 101 */     return Identifier.withDefaultNamespace("shulker_" + color.getName());
/*     */   }
/*     */   
/*     */   public static SpriteId createShulkerSprite(DyeColor color) {
/* 105 */     return SHULKER_MAPPER.apply(colorToShulkerSprite(color));
/*     */   }
/*     */   
/*     */   public static SpriteId getBannerSprite(Holder<BannerPattern> pattern) {
/* 109 */     Objects.requireNonNull(BANNER_MAPPER);
/* 109 */     return BANNER_SPRITES.computeIfAbsent(((BannerPattern)pattern.value()).assetId(), BANNER_MAPPER::apply);
/*     */   }
/*     */   
/*     */   public static SpriteId getShieldSprite(Holder<BannerPattern> pattern) {
/* 113 */     Objects.requireNonNull(SHIELD_MAPPER);
/* 113 */     return SHIELD_SPRITES.computeIfAbsent(((BannerPattern)pattern.value()).assetId(), SHIELD_MAPPER::apply);
/*     */   }
/*     */   
/*     */   public static SpriteId chooseSprite(ChestRenderState.ChestMaterialType materialType, ChestType type) {
/* 117 */     switch (materialType) {
/*     */       default:
/* 117 */         throw new MatchException(null, null);
/*     */       case ENDER_CHEST:
/*     */       
/*     */       case REGULAR:
/*     */       
/*     */       case CHRISTMAS:
/*     */       
/*     */       case TRAPPED:
/*     */       
/*     */       case COPPER_UNAFFECTED:
/*     */       
/*     */       case COPPER_EXPOSED:
/*     */       
/*     */       case COPPER_WEATHERED:
/*     */       
/*     */       case COPPER_OXIDIZED:
/*     */         break;
/*     */     } 
/* 117 */     return (
/*     */       
/* 126 */       (MultiblockChestResources<SpriteId>)CHEST_COPPER.oxidized()).select(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Sheets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */