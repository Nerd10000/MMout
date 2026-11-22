/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.PaintingVariantTags;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariants;
/*    */ 
/*    */ public class PaintingVariantTagsProvider extends TagsProvider<PaintingVariant> {
/*    */   public PaintingVariantTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.PAINTING_VARIANT, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(PaintingVariantTags.PLACEABLE)
/* 20 */       .add((ResourceKey<PaintingVariant>[])new ResourceKey[] { 
/*    */           PaintingVariants.KEBAB, PaintingVariants.AZTEC, PaintingVariants.ALBAN, PaintingVariants.AZTEC2, PaintingVariants.BOMB, PaintingVariants.PLANT, PaintingVariants.WASTELAND, PaintingVariants.POOL, PaintingVariants.COURBET, PaintingVariants.SEA, 
/*    */           PaintingVariants.SUNSET, PaintingVariants.CREEBET, PaintingVariants.WANDERER, PaintingVariants.GRAHAM, PaintingVariants.MATCH, PaintingVariants.BUST, PaintingVariants.STAGE, PaintingVariants.VOID, PaintingVariants.SKULL_AND_ROSES, PaintingVariants.WITHER, 
/*    */           PaintingVariants.FIGHTERS, PaintingVariants.POINTER, PaintingVariants.PIGSCENE, PaintingVariants.BURNING_SKULL, PaintingVariants.SKELETON, PaintingVariants.DONKEY_KONG, PaintingVariants.BAROQUE, PaintingVariants.HUMBLE, PaintingVariants.MEDITATIVE, PaintingVariants.PRAIRIE_RIDE, 
/*    */           PaintingVariants.UNPACKED, PaintingVariants.BACKYARD, PaintingVariants.BOUQUET, PaintingVariants.CAVEBIRD, PaintingVariants.CHANGING, PaintingVariants.COTAN, PaintingVariants.ENDBOSS, PaintingVariants.FERN, PaintingVariants.FINDING, PaintingVariants.LOWMIST, 
/*    */           PaintingVariants.ORB, PaintingVariants.OWLEMONS, PaintingVariants.PASSAGE, PaintingVariants.POND, PaintingVariants.SUNFLOWERS, PaintingVariants.TIDES, PaintingVariants.DENNIS });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\PaintingVariantTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */