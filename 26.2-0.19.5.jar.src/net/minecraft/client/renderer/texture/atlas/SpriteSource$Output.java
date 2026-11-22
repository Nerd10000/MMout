/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.client.renderer.texture.SpriteContents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ 
/*    */ public interface Output {
/*    */   default void add(Identifier id, Resource resource) {
/* 24 */     add(id, loader -> loader.loadSprite(id, resource));
/*    */   }
/*    */   
/*    */   void add(Identifier paramIdentifier, SpriteSource.DiscardableLoader paramDiscardableLoader);
/*    */   
/*    */   void removeAll(Predicate<Identifier> paramPredicate);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\SpriteSource$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */