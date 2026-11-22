/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.client.renderer.texture.SpriteContents;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public interface SpriteSource {
/* 15 */   public static final FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures", ".png");
/*    */   
/*    */   void run(ResourceManager paramResourceManager, Output paramOutput);
/*    */   
/*    */   MapCodec<? extends SpriteSource> codec();
/*    */   
/*    */   public static interface Output {
/*    */     default void add(Identifier id, Resource resource) {
/* 24 */       add(id, loader -> loader.loadSprite(id, resource));
/*    */     }
/*    */     
/*    */     void add(Identifier param1Identifier, SpriteSource.DiscardableLoader param1DiscardableLoader);
/*    */     
/*    */     void removeAll(Predicate<Identifier> param1Predicate);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Loader {
/*    */     SpriteContents get(SpriteResourceLoader param1SpriteResourceLoader);
/*    */   }
/*    */   
/*    */   public static interface DiscardableLoader extends Loader {
/*    */     default void discard() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\SpriteSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */