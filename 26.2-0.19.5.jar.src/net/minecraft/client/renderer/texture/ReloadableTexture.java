/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public abstract class ReloadableTexture extends AbstractTexture {
/*    */   private final Identifier resourceId;
/*    */   
/*    */   public ReloadableTexture(Identifier resourceId) {
/* 19 */     this.resourceId = resourceId;
/*    */   }
/*    */   
/*    */   public Identifier resourceId() {
/* 23 */     return this.resourceId;
/*    */   }
/*    */   
/*    */   public void apply(TextureContents contents) {
/* 27 */     boolean clamp = contents.clamp();
/* 28 */     boolean blur = contents.blur();
/* 29 */     AddressMode addressMode = clamp ? AddressMode.CLAMP_TO_EDGE : AddressMode.REPEAT;
/* 30 */     FilterMode minMag = blur ? FilterMode.LINEAR : FilterMode.NEAREST;
/* 31 */     this.sampler = RenderSystem.getSamplerCache().getSampler(addressMode, addressMode, minMag, minMag, false);
/* 33 */     NativeImage image = contents.image();
/*    */     try {
/* 34 */       doLoad(image);
/* 35 */       if (image != null)
/* 35 */         image.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (image != null)
/*    */         try {
/*    */           image.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void doLoad(NativeImage image) {
/* 39 */     GpuDevice device = RenderSystem.getDevice();
/* 40 */     close();
/* 41 */     Objects.requireNonNull(this.resourceId);
/* 41 */     this.texture = device.createTexture(this.resourceId::toString, 5, GpuFormat.RGBA8_UNORM, image.getWidth(), image.getHeight(), 1, 1);
/* 42 */     this.textureView = device.createTextureView(this.texture);
/* 43 */     device.createCommandEncoder().writeToTexture(this.texture, image);
/*    */   }
/*    */   
/*    */   public abstract TextureContents loadContents(ResourceManager paramResourceManager) throws IOException;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\ReloadableTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */