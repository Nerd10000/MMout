/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class SimpleTexture extends ReloadableTexture {
/*    */   public SimpleTexture(Identifier location) {
/* 10 */     super(location);
/*    */   }
/*    */   
/*    */   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 15 */     return TextureContents.load(resourceManager, resourceId());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\SimpleTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */