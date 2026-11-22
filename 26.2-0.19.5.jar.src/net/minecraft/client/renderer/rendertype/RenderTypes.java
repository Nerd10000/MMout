/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
/*     */ import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class RenderTypes {
/*     */   static final BiFunction<Identifier, Boolean, RenderType> OUTLINE;
/*     */   
/*     */   static {
/*  32 */     OUTLINE = Util.memoize((texture, cullState) -> RenderType.create("outline", RenderSetup.builder(cullState.booleanValue() ? RenderPipelines.OUTLINE_CULL : RenderPipelines.OUTLINE_NO_CULL).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.OUTLINE_TARGET).setOutline(RenderSetup.OutlineProperty.IS_OUTLINE).createRenderSetup()));
/*     */   }
/*     */   
/*     */   private static RenderSetup createMovingBlockSetup(RenderPipeline pipeline, boolean translucent) {
/*  45 */     RenderSetup.RenderSetupBuilder setup = RenderSetup.builder(pipeline).useLightmap().withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, () -> RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.NEAREST, true)).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
/*  46 */     if (translucent) {
/*  47 */       setup.sortOnUpload();
/*  48 */       setup.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET);
/*     */     } 
/*  50 */     return setup.createRenderSetup();
/*     */   }
/*     */   
/*  53 */   private static final RenderType SOLID_MOVING_BLOCK = RenderType.create("solid_moving_block", createMovingBlockSetup(RenderPipelines.SOLID_BLOCK, false));
/*     */   
/*     */   public static RenderType solidMovingBlock() {
/*  56 */     return SOLID_MOVING_BLOCK;
/*     */   }
/*     */   
/*  59 */   private static final RenderType CUTOUT_MOVING_BLOCK = RenderType.create("cutout_moving_block", createMovingBlockSetup(RenderPipelines.CUTOUT_BLOCK, false));
/*     */   
/*     */   public static RenderType cutoutMovingBlock() {
/*  62 */     return CUTOUT_MOVING_BLOCK;
/*     */   }
/*     */   
/*  65 */   private static final RenderType TRANSLUCENT_MOVING_BLOCK = RenderType.create("translucent_moving_block", createMovingBlockSetup(RenderPipelines.TRANSLUCENT_BLOCK, true));
/*     */   
/*     */   private static final Function<Identifier, RenderType> ARMOR_CUTOUT_NO_CULL;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ARMOR_TRANSLUCENT;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ENTITY_SOLID;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ENTITY_SOLID_Z_OFFSET_FORWARD;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ENTITY_CUTOUT_CULL;
/*     */   
/*     */   private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_CUTOUT;
/*     */   
/*     */   private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_CUTOUT_Z_OFFSET;
/*     */   
/*     */   private static final BiFunction<Identifier, Identifier, RenderType> ENTITY_CUTOUT_DISSOLVE;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ENTITY_TRANSLUCENT_CULL_ITEM_TARGET;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ITEM_CUTOUT;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ITEM_TRANSLUCENT;
/*     */   
/*     */   private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_TRANSLUCENT;
/*     */   
/*     */   private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_TRANSLUCENT_EMISSIVE;
/*     */   
/*     */   private static final Function<Identifier, RenderType> END_CRYSTAL_BEAM;
/*     */   
/*     */   private static final BiFunction<Identifier, Boolean, RenderType> BEACON_BEAM;
/*     */   
/*     */   private static final Function<Identifier, RenderType> BANNER_PATTERN;
/*     */   
/*     */   private static final Function<Identifier, RenderType> ENTITY_SHADOW;
/*     */   
/*     */   private static final Function<Identifier, RenderType> EYES;
/*     */   
/*     */   public static RenderType translucentMovingBlock() {
/*  68 */     return TRANSLUCENT_MOVING_BLOCK;
/*     */   }
/*     */   
/*     */   static {
/*  71 */     ARMOR_CUTOUT_NO_CULL = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_CUTOUT_NO_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("armor_cutout_no_cull", state);
/*     */         });
/* 101 */     ARMOR_TRANSLUCENT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_TRANSLUCENT).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().sortOnUpload().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("armor_translucent", state);
/*     */         });
/* 119 */     ENTITY_SOLID = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SOLID).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("entity_solid", state);
/*     */         });
/* 135 */     ENTITY_SOLID_Z_OFFSET_FORWARD = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SOLID_Z_OFFSET_FORWARD).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING_FORWARD).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("entity_solid_z_offset_forward", state);
/*     */         });
/* 152 */     ENTITY_CUTOUT_CULL = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("entity_cutout_cull", state);
/*     */         });
/* 168 */     ENTITY_CUTOUT = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(affectsOutline.booleanValue() ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */           return RenderType.create("entity_cutout", state);
/*     */         });
/* 188 */     ENTITY_CUTOUT_Z_OFFSET = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_Z_OFFSET).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(affectsOutline.booleanValue() ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */           return RenderType.create("entity_cutout_z_offset", state);
/*     */         });
/* 209 */     ENTITY_CUTOUT_DISSOLVE = Util.memoize((texture, maskTexture) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_DISSOLVE).withTexture("Sampler0", texture).withTexture("DissolveMaskSampler", maskTexture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("entity_cutout_dissolve", state);
/*     */         });
/* 226 */     ENTITY_TRANSLUCENT_CULL_ITEM_TARGET = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT_CULL).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).useLightmap().useOverlay().affectsCrumbling().sortOnUpload().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("entity_translucent_cull_item_target", state);
/*     */         });
/* 244 */     ITEM_CUTOUT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ITEM_CUTOUT).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("item_cutout", state);
/*     */         });
/* 259 */     ITEM_TRANSLUCENT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ITEM_TRANSLUCENT).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).useLightmap().useOverlay().affectsCrumbling().sortOnUpload().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */           return RenderType.create("item_translucent", state);
/*     */         });
/* 276 */     ENTITY_TRANSLUCENT = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().sortOnUpload().setOutline(affectsOutline.booleanValue() ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */           return RenderType.create("entity_translucent", state);
/*     */         });
/* 297 */     ENTITY_TRANSLUCENT_EMISSIVE = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT_EMISSIVE).withTexture("Sampler0", texture).useOverlay().affectsCrumbling().sortOnUpload().setOutline(affectsOutline.booleanValue() ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */           return RenderType.create("entity_translucent_emissive", state);
/*     */         });
/* 317 */     END_CRYSTAL_BEAM = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.END_CRYSTAL_BEAM).withTexture("Sampler0", texture).useLightmap().setOutline(RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */           return RenderType.create("end_crystal_beam", state);
/*     */         });
/* 331 */     BEACON_BEAM = Util.memoize((texture, translucent) -> {
/*     */           RenderSetup state = RenderSetup.builder(translucent.booleanValue() ? RenderPipelines.BEACON_BEAM_TRANSLUCENT : RenderPipelines.BEACON_BEAM_OPAQUE).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup();
/*     */           return RenderType.create("beacon_beam", state);
/*     */         });
/* 344 */     BANNER_PATTERN = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.BANNER_PATTERN).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup();
/*     */           return RenderType.create("banner_pattern", state);
/*     */         });
/* 358 */     ENTITY_SHADOW = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SHADOW).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).createRenderSetup();
/*     */           return RenderType.create("entity_shadow", state);
/*     */         });
/* 373 */     EYES = Util.memoize(texture -> RenderType.create("eyes", RenderSetup.builder(RenderPipelines.EYES).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup()));
/*     */   }
/*     */   
/*     */   public static RenderType armorCutoutNoCull(Identifier texture) {
/*     */     return ARMOR_CUTOUT_NO_CULL.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType createArmorDecalCutoutNoCull(Identifier texture) {
/*     */     RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_DECAL_CUTOUT_NO_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */     return RenderType.create("armor_decal_cutout_no_cull", state);
/*     */   }
/*     */   
/*     */   public static RenderType armorTranslucent(Identifier texture) {
/*     */     return ARMOR_TRANSLUCENT.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entitySolid(Identifier texture) {
/*     */     return ENTITY_SOLID.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entitySolidZOffsetForward(Identifier texture) {
/*     */     return ENTITY_SOLID_Z_OFFSET_FORWARD.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entityCutoutCull(Identifier texture) {
/*     */     return ENTITY_CUTOUT_CULL.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entityCutout(Identifier texture, boolean affectsOutline) {
/*     */     return ENTITY_CUTOUT.apply(texture, Boolean.valueOf(affectsOutline));
/*     */   }
/*     */   
/*     */   public static RenderType entityCutout(Identifier texture) {
/*     */     return entityCutout(texture, true);
/*     */   }
/*     */   
/*     */   public static RenderType entityCutoutZOffset(Identifier texture, boolean affectsOutline) {
/*     */     return ENTITY_CUTOUT_Z_OFFSET.apply(texture, Boolean.valueOf(affectsOutline));
/*     */   }
/*     */   
/*     */   public static RenderType entityCutoutZOffset(Identifier texture) {
/*     */     return entityCutoutZOffset(texture, true);
/*     */   }
/*     */   
/*     */   public static RenderType entityCutoutDissolve(Identifier texture, Identifier maskTexture) {
/*     */     return ENTITY_CUTOUT_DISSOLVE.apply(texture, maskTexture);
/*     */   }
/*     */   
/*     */   public static RenderType entityTranslucentCullItemTarget(Identifier texture) {
/*     */     return ENTITY_TRANSLUCENT_CULL_ITEM_TARGET.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType itemCutout(Identifier texture) {
/*     */     return ITEM_CUTOUT.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType itemTranslucent(Identifier texture) {
/*     */     return ITEM_TRANSLUCENT.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entityTranslucent(Identifier texture, boolean affectsOutline) {
/*     */     return ENTITY_TRANSLUCENT.apply(texture, Boolean.valueOf(affectsOutline));
/*     */   }
/*     */   
/*     */   public static RenderType entityTranslucent(Identifier texture) {
/*     */     return entityTranslucent(texture, true);
/*     */   }
/*     */   
/*     */   public static RenderType entityTranslucentEmissive(Identifier texture, boolean affectsOutline) {
/*     */     return ENTITY_TRANSLUCENT_EMISSIVE.apply(texture, Boolean.valueOf(affectsOutline));
/*     */   }
/*     */   
/*     */   public static RenderType entityTranslucentEmissive(Identifier texture) {
/*     */     return entityTranslucentEmissive(texture, true);
/*     */   }
/*     */   
/*     */   public static RenderType endCrystalBeam(Identifier texture) {
/*     */     return END_CRYSTAL_BEAM.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType beaconBeam(Identifier texture, boolean translucent) {
/*     */     return BEACON_BEAM.apply(texture, Boolean.valueOf(translucent));
/*     */   }
/*     */   
/*     */   public static RenderType bannerPattern(Identifier texture) {
/*     */     return BANNER_PATTERN.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType entityShadow(Identifier texture) {
/*     */     return ENTITY_SHADOW.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType eyes(Identifier texture) {
/* 381 */     return EYES.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType breezeEyes(Identifier texture) {
/* 385 */     return ENTITY_TRANSLUCENT_EMISSIVE.apply(texture, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static RenderType breezeWind(Identifier texture, float uOffset, float vOffset) {
/* 389 */     return RenderType.create("breeze_wind", RenderSetup.builder(RenderPipelines.BREEZE_WIND)
/* 390 */         .withTexture("Sampler0", texture)
/* 391 */         .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
/* 392 */         .useLightmap()
/* 393 */         .sortOnUpload()
/* 394 */         .createRenderSetup());
/*     */   }
/*     */   
/*     */   public static RenderType energySwirl(Identifier texture, float uOffset, float vOffset) {
/* 399 */     return RenderType.create("energy_swirl", RenderSetup.builder(RenderPipelines.ENERGY_SWIRL)
/* 400 */         .withTexture("Sampler0", texture)
/* 401 */         .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
/* 402 */         .useLightmap()
/* 403 */         .useOverlay()
/* 404 */         .sortOnUpload()
/* 405 */         .createRenderSetup());
/*     */   }
/*     */   
/* 408 */   private static final RenderType LEASH = RenderType.create("leash", RenderSetup.builder(RenderPipelines.LEASH)
/* 409 */       .useLightmap()
/* 410 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType leash() {
/* 413 */     return LEASH;
/*     */   }
/*     */   
/* 416 */   private static final RenderType WATER_MASK = RenderType.create("water_mask", RenderSetup.builder(RenderPipelines.WATER_MASK).createRenderSetup());
/*     */   
/*     */   public static RenderType waterMask() {
/* 419 */     return WATER_MASK;
/*     */   }
/*     */   
/*     */   public static RenderType outline(Identifier texture) {
/* 423 */     return OUTLINE.apply(texture, Boolean.valueOf(false));
/*     */   }
/*     */   
/* 426 */   private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create("armor_entity_glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 427 */       .withTexture("Sampler0", ItemFeatureRenderer.ENCHANTED_GLINT_ARMOR)
/* 428 */       .setTextureTransform(TextureTransform.ARMOR_ENTITY_GLINT_TEXTURING)
/* 429 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 430 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType armorEntityGlint() {
/* 433 */     return ARMOR_ENTITY_GLINT;
/*     */   }
/*     */   
/* 436 */   private static final RenderType GLINT_TRANSLUCENT = RenderType.create("glint_translucent", RenderSetup.builder(RenderPipelines.GLINT)
/* 437 */       .withTexture("Sampler0", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
/* 438 */       .setTextureTransform(TextureTransform.GLINT_TEXTURING)
/* 439 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 440 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType glintTranslucent() {
/* 443 */     return GLINT_TRANSLUCENT;
/*     */   }
/*     */   
/* 446 */   private static final RenderType GLINT = RenderType.create("glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 447 */       .withTexture("Sampler0", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
/* 448 */       .setTextureTransform(TextureTransform.GLINT_TEXTURING)
/* 449 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType glint() {
/* 452 */     return GLINT;
/*     */   }
/*     */   
/* 455 */   private static final RenderType ENTITY_GLINT = RenderType.create("entity_glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 456 */       .withTexture("Sampler0", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
/* 457 */       .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
/* 458 */       .createRenderSetup());
/*     */   
/*     */   private static final Function<Identifier, RenderType> CRUMBLING;
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT;
/*     */   
/*     */   public static RenderType entityGlint() {
/* 461 */     return ENTITY_GLINT;
/*     */   }
/*     */   
/*     */   static {
/* 464 */     CRUMBLING = Util.memoize(texture -> RenderType.create("crumbling", RenderSetup.builder(RenderPipelines.CRUMBLING).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup()));
/* 475 */     TEXT = Util.memoize(texture -> RenderType.create("text", RenderSetup.builder(RenderPipelines.TEXT).withTexture("Sampler0", texture).useLightmap().createRenderSetup()));
/*     */   }
/*     */   
/*     */   public static RenderType crumbling(Identifier texture) {
/*     */     return CRUMBLING.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType text(Identifier texture) {
/* 483 */     return TEXT.apply(texture);
/*     */   }
/*     */   
/* 487 */   private static final RenderType TEXT_BACKGROUND = RenderType.create("text_background", RenderSetup.builder(RenderPipelines.TEXT_BACKGROUND)
/* 488 */       .useLightmap()
/* 489 */       .sortOnUpload()
/* 490 */       .createRenderSetup());
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT_GRAYSCALE;
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT_POLYGON_OFFSET;
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT_GRAYSCALE_POLYGON_OFFSET;
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT_SEE_THROUGH;
/*     */   
/*     */   public static RenderType textBackground() {
/* 493 */     return TEXT_BACKGROUND;
/*     */   }
/*     */   
/*     */   static {
/* 496 */     TEXT_GRAYSCALE = Util.memoize(texture -> RenderType.create("text_grayscale", RenderSetup.builder(RenderPipelines.TEXT_GRAYSCALE).withTexture("Sampler0", texture).useLightmap().createRenderSetup()));
/* 507 */     TEXT_POLYGON_OFFSET = Util.memoize(texture -> RenderType.create("text_polygon_offset", RenderSetup.builder(RenderPipelines.TEXT_POLYGON_OFFSET).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
/* 519 */     TEXT_GRAYSCALE_POLYGON_OFFSET = Util.memoize(texture -> RenderType.create("text_grayscale_polygon_offset", RenderSetup.builder(RenderPipelines.TEXT_GRAYSCALE_POLYGON_OFFSET).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
/* 531 */     TEXT_SEE_THROUGH = Util.memoize(texture -> RenderType.create("text_see_through", RenderSetup.builder(RenderPipelines.TEXT_SEE_THROUGH).withTexture("Sampler0", texture).useLightmap().createRenderSetup()));
/*     */   }
/*     */   
/*     */   public static RenderType textGrayscale(Identifier texture) {
/*     */     return TEXT_GRAYSCALE.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType textPolygonOffset(Identifier texture) {
/*     */     return TEXT_POLYGON_OFFSET.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType textGrayscalePolygonOffset(Identifier texture) {
/*     */     return TEXT_GRAYSCALE_POLYGON_OFFSET.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType textSeeThrough(Identifier texture) {
/* 539 */     return TEXT_SEE_THROUGH.apply(texture);
/*     */   }
/*     */   
/* 543 */   private static final RenderType TEXT_BACKGROUND_SEE_THROUGH = RenderType.create("text_background_see_through", RenderSetup.builder(RenderPipelines.TEXT_BACKGROUND_SEE_THROUGH)
/* 544 */       .useLightmap()
/* 545 */       .sortOnUpload()
/* 546 */       .createRenderSetup());
/*     */   
/*     */   private static final Function<Identifier, RenderType> TEXT_GRAYSCALE_SEE_THROUGH;
/*     */   
/*     */   public static RenderType textBackgroundSeeThrough() {
/* 549 */     return TEXT_BACKGROUND_SEE_THROUGH;
/*     */   }
/*     */   
/*     */   static {
/* 552 */     TEXT_GRAYSCALE_SEE_THROUGH = Util.memoize(texture -> RenderType.create("text_grayscale_see_through", RenderSetup.builder(RenderPipelines.TEXT_GRAYSCALE_SEE_THROUGH).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
/*     */   }
/*     */   
/*     */   public static RenderType textGrayscaleSeeThrough(Identifier texture) {
/* 561 */     return TEXT_GRAYSCALE_SEE_THROUGH.apply(texture);
/*     */   }
/*     */   
/* 564 */   private static final RenderType LIGHTNING = RenderType.create("lightning", RenderSetup.builder(RenderPipelines.LIGHTNING)
/* 565 */       .setOutputTarget(OutputTarget.WEATHER_TARGET)
/* 566 */       .sortOnUpload()
/* 567 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType lightning() {
/* 570 */     return LIGHTNING;
/*     */   }
/*     */   
/* 573 */   private static final RenderType DRAGON_RAYS = RenderType.create("dragon_rays", RenderSetup.builder(RenderPipelines.DRAGON_RAYS)
/* 574 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType dragonRays() {
/* 577 */     return DRAGON_RAYS;
/*     */   }
/*     */   
/* 580 */   private static final RenderType END_PORTAL = RenderType.create("end_portal", RenderSetup.builder(RenderPipelines.END_PORTAL)
/* 581 */       .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
/* 582 */       .withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
/* 583 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType endPortal() {
/* 586 */     return END_PORTAL;
/*     */   }
/*     */   
/* 589 */   private static final RenderType END_GATEWAY = RenderType.create("end_gateway", RenderSetup.builder(RenderPipelines.END_GATEWAY)
/* 590 */       .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
/* 591 */       .withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
/* 592 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType endGateway() {
/* 595 */     return END_GATEWAY;
/*     */   }
/*     */   
/* 598 */   public static final RenderType LINES = RenderType.create("lines", RenderSetup.builder(RenderPipelines.LINES)
/* 599 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 600 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 601 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType lines() {
/* 604 */     return LINES;
/*     */   }
/*     */   
/* 607 */   public static final RenderType LINES_TRANSLUCENT = RenderType.create("lines_translucent", RenderSetup.builder(RenderPipelines.LINES_TRANSLUCENT)
/* 608 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 609 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 610 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType linesTranslucent() {
/* 613 */     return LINES_TRANSLUCENT;
/*     */   }
/*     */   
/* 616 */   public static final RenderType SECONDARY_BLOCK_OUTLINE = RenderType.create("secondary_block_outline", RenderSetup.builder(RenderPipelines.SECONDARY_BLOCK_OUTLINE)
/* 617 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 618 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 619 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType secondaryBlockOutline() {
/* 622 */     return SECONDARY_BLOCK_OUTLINE;
/*     */   }
/*     */   
/* 625 */   private static final RenderType DEBUG_FILLED_BOX = RenderType.create("debug_filled_box", RenderSetup.builder(RenderPipelines.DEBUG_FILLED_BOX)
/* 626 */       .sortOnUpload()
/* 627 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 628 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugFilledBox() {
/* 631 */     return DEBUG_FILLED_BOX;
/*     */   }
/*     */   
/* 634 */   private static final RenderType DEBUG_POINT = RenderType.create("debug_point", RenderSetup.builder(RenderPipelines.DEBUG_POINTS)
/* 635 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugPoint() {
/* 638 */     return DEBUG_POINT;
/*     */   }
/*     */   
/* 641 */   private static final RenderType DEBUG_QUADS = RenderType.create("debug_quads", RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
/* 642 */       .sortOnUpload()
/* 643 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugQuads() {
/* 646 */     return DEBUG_QUADS;
/*     */   }
/*     */   
/* 649 */   private static final RenderType DEBUG_TRIANGLE_FAN = RenderType.create("debug_triangle_fan", RenderSetup.builder(RenderPipelines.DEBUG_TRIANGLE_FAN)
/* 650 */       .createRenderSetup());
/*     */   
/*     */   private static final Function<Identifier, RenderType> BLOCK_SCREEN_EFFECT;
/*     */   
/*     */   private static final Function<Identifier, RenderType> FIRE_SCREEN_EFFECT;
/*     */   
/*     */   public static RenderType debugTriangleFan() {
/* 653 */     return DEBUG_TRIANGLE_FAN;
/*     */   }
/*     */   
/*     */   static {
/* 656 */     BLOCK_SCREEN_EFFECT = Util.memoize(texture -> RenderType.create("block_screen_effect", RenderSetup.builder(RenderPipelines.BLOCK_SCREEN_EFFECT).withTexture("Sampler0", texture).createRenderSetup()));
/* 666 */     FIRE_SCREEN_EFFECT = Util.memoize(texture -> RenderType.create("fire_screen_effect", RenderSetup.builder(RenderPipelines.FIRE_SCREEN_EFFECT).withTexture("Sampler0", texture).createRenderSetup()));
/*     */   }
/*     */   
/*     */   public static RenderType blockScreenEffect(Identifier texture) {
/*     */     return BLOCK_SCREEN_EFFECT.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType fireScreenEffect(Identifier texture) {
/* 673 */     return FIRE_SCREEN_EFFECT.apply(texture);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */