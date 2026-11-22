/*    */ package net.minecraft.client.resources.model.sprite;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.common.collect.Multimaps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.ModelDebugName;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class MaterialBaker {
/* 21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Material.Baked missingSprite;
/*    */   
/*    */   private final Material.Baked missingSpriteForceTranslucent;
/*    */   
/* 27 */   private final Multimap<String, Identifier> missingSprites = Multimaps.synchronizedMultimap((Multimap)HashMultimap.create());
/*    */   
/* 28 */   private final Multimap<String, String> missingReferences = Multimaps.synchronizedMultimap((Multimap)HashMultimap.create());
/*    */   
/* 30 */   private final Map<Material, Material.Baked> bakedMaterials = new ConcurrentHashMap<>();
/*    */   
/* 31 */   private final Function<Material, Material.Baked> bakerFunction = this::bake;
/*    */   
/*    */   public MaterialBaker(TextureAtlasSprite missingSprite) {
/* 34 */     this.missingSprite = new Material.Baked(missingSprite, false);
/* 35 */     this.missingSpriteForceTranslucent = new Material.Baked(missingSprite, true);
/*    */   }
/*    */   
/*    */   public Material.Baked replacementForMissingMaterial(Material material) {
/* 40 */     return material.forceTranslucent() ? this.missingSpriteForceTranslucent : this.missingSprite;
/*    */   }
/*    */   
/*    */   public Material.Baked get(Material material, ModelDebugName name) {
/* 46 */     if (material.sprite().equals(MissingTextureAtlasSprite.getLocation()))
/* 47 */       return replacementForMissingMaterial(material); 
/* 50 */     Material.Baked baked = this.bakedMaterials.computeIfAbsent(material, this.bakerFunction);
/* 51 */     if (baked == null) {
/* 53 */       this.missingSprites.put(name.debugName(), material.sprite());
/* 54 */       return replacementForMissingMaterial(material);
/*    */     } 
/* 56 */     return baked;
/*    */   }
/*    */   
/*    */   protected static Material.Baked bakeForAtlas(Material material, SpriteLoader.Preparations atlas) {
/* 62 */     TextureAtlasSprite sprite = atlas.getSprite(material.sprite());
/* 63 */     if (sprite != null)
/* 64 */       return new Material.Baked(sprite, material.forceTranslucent()); 
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public Material.Baked resolveSlot(TextureSlots slots, String id, ModelDebugName name) {
/* 70 */     Material resolvedMaterial = slots.getMaterial(id);
/* 71 */     return (resolvedMaterial != null) ? get(resolvedMaterial, name) : reportMissingReference(id, name);
/*    */   }
/*    */   
/*    */   public Material.Baked reportMissingReference(String reference, ModelDebugName responsibleModel) {
/* 76 */     this.missingReferences.put(responsibleModel.debugName(), reference);
/* 77 */     return this.missingSprite;
/*    */   }
/*    */   
/*    */   public void logMissingTextures() {
/* 81 */     this.missingSprites.asMap().forEach((location, sprites) -> LOGGER.warn("Missing textures in model {}:\n{}", location, sprites.stream().sorted().map(()).collect(Collectors.joining("\n"))));
/* 85 */     this.missingReferences.asMap().forEach((location, references) -> LOGGER.warn("Missing texture references in model {}:\n{}", location, references.stream().sorted().map(()).collect(Collectors.joining("\n"))));
/*    */   }
/*    */   
/*    */   protected abstract Material.Baked bake(Material paramMaterial);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\sprite\MaterialBaker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */