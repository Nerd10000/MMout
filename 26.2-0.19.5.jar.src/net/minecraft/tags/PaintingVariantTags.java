/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ 
/*    */ public class PaintingVariantTags {
/* 11 */   public static final TagKey<PaintingVariant> PLACEABLE = create("placeable");
/*    */   
/*    */   private static TagKey<PaintingVariant> create(String name) {
/* 14 */     return TagKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\PaintingVariantTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */