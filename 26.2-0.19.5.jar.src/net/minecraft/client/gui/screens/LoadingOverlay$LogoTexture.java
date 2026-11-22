/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.MipmapStrategy;
/*     */ import net.minecraft.client.renderer.texture.ReloadableTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureContents;
/*     */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ 
/*     */ class LogoTexture extends ReloadableTexture {
/*     */   public LogoTexture() {
/* 172 */     super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
/*     */   }
/*     */   
/*     */   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 177 */     ResourceProvider vanillaProvider = Minecraft.getInstance().getVanillaPackResources().asProvider();
/* 178 */     InputStream resource = vanillaProvider.open(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
/*     */     try {
/* 179 */       TextureContents textureContents = new TextureContents(NativeImage.read(resource), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
/* 180 */       if (resource != null)
/* 180 */         resource.close(); 
/*     */       return textureContents;
/*     */     } catch (Throwable throwable) {
/*     */       if (resource != null)
/*     */         try {
/*     */           resource.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\LoadingOverlay$LogoTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */