/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransform;
/*     */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ 
/*     */ public interface ResolvedModel extends ModelDebugName {
/*     */   public static final boolean DEFAULT_AMBIENT_OCCLUSION = true;
/*     */   
/*  16 */   public static final UnbakedModel.GuiLight DEFAULT_GUI_LIGHT = UnbakedModel.GuiLight.SIDE;
/*     */   
/*     */   UnbakedModel wrapped();
/*     */   
/*     */   ResolvedModel parent();
/*     */   
/*     */   static TextureSlots findTopTextureSlots(ResolvedModel top) {
/*  23 */     ResolvedModel current = top;
/*  24 */     TextureSlots.Resolver resolver = new TextureSlots.Resolver();
/*  25 */     while (current != null) {
/*  26 */       resolver.addLast(current.wrapped().textureSlots());
/*  27 */       current = current.parent();
/*     */     } 
/*  30 */     return resolver.resolve(top);
/*     */   }
/*     */   
/*     */   default TextureSlots getTopTextureSlots() {
/*  34 */     return findTopTextureSlots(this);
/*     */   }
/*     */   
/*     */   static boolean findTopAmbientOcclusion(ResolvedModel current) {
/*  38 */     while (current != null) {
/*  39 */       Boolean hasAmbientOcclusion = current.wrapped().ambientOcclusion();
/*  40 */       if (hasAmbientOcclusion != null)
/*  41 */         return hasAmbientOcclusion.booleanValue(); 
/*  43 */       current = current.parent();
/*     */     } 
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   default boolean getTopAmbientOcclusion() {
/*  49 */     return findTopAmbientOcclusion(this);
/*     */   }
/*     */   
/*     */   static UnbakedModel.GuiLight findTopGuiLight(ResolvedModel current) {
/*  53 */     while (current != null) {
/*  54 */       UnbakedModel.GuiLight guiLight = current.wrapped().guiLight();
/*  55 */       if (guiLight != null)
/*  56 */         return guiLight; 
/*  58 */       current = current.parent();
/*     */     } 
/*  60 */     return DEFAULT_GUI_LIGHT;
/*     */   }
/*     */   
/*     */   default UnbakedModel.GuiLight getTopGuiLight() {
/*  64 */     return findTopGuiLight(this);
/*     */   }
/*     */   
/*     */   static UnbakedGeometry findTopGeometry(ResolvedModel current) {
/*  68 */     while (current != null) {
/*  69 */       UnbakedGeometry geometry = current.wrapped().geometry();
/*  70 */       if (geometry != null)
/*  71 */         return geometry; 
/*  73 */       current = current.parent();
/*     */     } 
/*  75 */     return UnbakedGeometry.EMPTY;
/*     */   }
/*     */   
/*     */   default UnbakedGeometry getTopGeometry() {
/*  79 */     return findTopGeometry(this);
/*     */   }
/*     */   
/*     */   default QuadCollection bakeTopGeometry(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/*  83 */     return getTopGeometry().bake(textureSlots, baker, state, this);
/*     */   }
/*     */   
/*     */   static Material.Baked resolveParticleMaterial(TextureSlots textureSlots, ModelBaker baker, ModelDebugName resolvedModel) {
/*  87 */     return baker.materials().resolveSlot(textureSlots, "particle", resolvedModel);
/*     */   }
/*     */   
/*     */   default Material.Baked resolveParticleMaterial(TextureSlots textureSlots, ModelBaker baker) {
/*  91 */     return resolveParticleMaterial(textureSlots, baker, this);
/*     */   }
/*     */   
/*     */   static ItemTransform findTopTransform(ResolvedModel current, ItemDisplayContext type) {
/*  95 */     while (current != null) {
/*  96 */       ItemTransforms transforms = current.wrapped().transforms();
/*  97 */       if (transforms != null) {
/*  98 */         ItemTransform transform = transforms.getTransform(type);
/*  99 */         if (transform != ItemTransform.NO_TRANSFORM)
/* 100 */           return transform; 
/*     */       } 
/* 103 */       current = current.parent();
/*     */     } 
/* 105 */     return ItemTransform.NO_TRANSFORM;
/*     */   }
/*     */   
/*     */   static ItemTransforms findTopTransforms(ResolvedModel top) {
/* 109 */     ItemTransform thirdPersonLeftHand = findTopTransform(top, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
/* 110 */     ItemTransform thirdPersonRightHand = findTopTransform(top, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
/* 111 */     ItemTransform firstPersonLeftHand = findTopTransform(top, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
/* 112 */     ItemTransform firstPersonRightHand = findTopTransform(top, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
/* 113 */     ItemTransform head = findTopTransform(top, ItemDisplayContext.HEAD);
/* 114 */     ItemTransform gui = findTopTransform(top, ItemDisplayContext.GUI);
/* 115 */     ItemTransform ground = findTopTransform(top, ItemDisplayContext.GROUND);
/* 116 */     ItemTransform fixed = findTopTransform(top, ItemDisplayContext.FIXED);
/* 117 */     ItemTransform fixedFromBottom = findTopTransform(top, ItemDisplayContext.ON_SHELF);
/* 118 */     return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, fixedFromBottom);
/*     */   }
/*     */   
/*     */   default ItemTransforms getTopTransforms() {
/* 122 */     return findTopTransforms(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ResolvedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */