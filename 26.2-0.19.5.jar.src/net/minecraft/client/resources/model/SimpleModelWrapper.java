/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*    */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class SimpleModelWrapper extends Record implements BlockStateModelPart {
/*    */   private final QuadCollection quads;
/*    */   
/*    */   private final boolean useAmbientOcclusion;
/*    */   
/*    */   private final Material.Baked particleMaterial;
/*    */   
/*    */   public SimpleModelWrapper(QuadCollection quads, boolean useAmbientOcclusion, Material.Baked particleMaterial) {
/* 21 */     this.quads = quads;
/* 21 */     this.useAmbientOcclusion = useAmbientOcclusion;
/* 21 */     this.particleMaterial = particleMaterial;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/SimpleModelWrapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/SimpleModelWrapper;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/SimpleModelWrapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/SimpleModelWrapper;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/SimpleModelWrapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/SimpleModelWrapper;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public QuadCollection quads() {
/* 21 */     return this.quads;
/*    */   }
/*    */   
/*    */   public boolean useAmbientOcclusion() {
/* 21 */     return this.useAmbientOcclusion;
/*    */   }
/*    */   
/*    */   public Material.Baked particleMaterial() {
/* 21 */     return this.particleMaterial;
/*    */   }
/*    */   
/* 26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static BlockStateModelPart bake(ModelBaker modelBakery, Identifier location, ModelState state) {
/* 29 */     ResolvedModel model = modelBakery.getModel(location);
/* 31 */     TextureSlots textureSlots = model.getTopTextureSlots();
/* 32 */     boolean hasAmbientOcclusion = model.getTopAmbientOcclusion();
/* 33 */     Material.Baked particleMaterial = model.resolveParticleMaterial(textureSlots, modelBakery);
/* 34 */     QuadCollection geometry = model.bakeTopGeometry(textureSlots, modelBakery, state);
/* 36 */     Multimap<Identifier, Identifier> forbiddenSprites = findNonBlockSprites(geometry);
/* 37 */     if (forbiddenSprites != null) {
/* 38 */       LOGGER.warn("Rejecting block model {}, since it contains sprites from outside of supported atlas: {}", location, forbiddenSprites);
/* 39 */       return modelBakery.missingBlockModelPart();
/*    */     } 
/* 42 */     return new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleMaterial);
/*    */   }
/*    */   
/*    */   public static Multimap<Identifier, Identifier> findNonBlockSprites(QuadCollection geometry) {
/*    */     HashMultimap hashMultimap;
/* 46 */     Multimap<Identifier, Identifier> forbiddenSprites = null;
/* 47 */     for (BakedQuad bakedQuad : geometry.getAll()) {
/* 48 */       TextureAtlasSprite sprite = bakedQuad.materialInfo().sprite();
/* 49 */       if (!sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
/* 50 */         if (forbiddenSprites == null)
/* 51 */           hashMultimap = HashMultimap.create(); 
/* 53 */         hashMultimap.put(sprite.atlasLocation(), sprite.contents().name());
/*    */       } 
/*    */     } 
/* 56 */     return (Multimap<Identifier, Identifier>)hashMultimap;
/*    */   }
/*    */   
/*    */   public List<BakedQuad> getQuads(Direction direction) {
/* 61 */     return this.quads.getQuads(direction);
/*    */   }
/*    */   
/*    */   public int materialFlags() {
/* 66 */     return this.quads.materialFlags();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\SimpleModelWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */