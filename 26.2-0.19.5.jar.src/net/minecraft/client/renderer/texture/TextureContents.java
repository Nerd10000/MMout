/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public final class TextureContents extends Record implements Closeable {
/*    */   private final NativeImage image;
/*    */   
/*    */   private final TextureMetadataSection metadata;
/*    */   
/*    */   public TextureContents(NativeImage image, TextureMetadataSection metadata) {
/* 14 */     this.image = image;
/* 14 */     this.metadata = metadata;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/TextureContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/TextureContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/TextureContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NativeImage image() {
/* 14 */     return this.image;
/*    */   }
/*    */   
/*    */   public TextureMetadataSection metadata() {
/* 14 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public static TextureContents load(ResourceManager resourceManager, Identifier location) throws IOException {
/*    */     NativeImage image;
/* 19 */     Resource resource = resourceManager.getResourceOrThrow(location);
/* 22 */     InputStream is = resource.open();
/*    */     try {
/* 23 */       image = NativeImage.read(is);
/* 24 */       if (is != null)
/* 24 */         is.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (is != null)
/*    */         try {
/*    */           is.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 26 */     TextureMetadataSection metadata = resource.metadata().getSection(TextureMetadataSection.TYPE).orElse(null);
/* 27 */     return new TextureContents(image, metadata);
/*    */   }
/*    */   
/*    */   public static TextureContents createMissing() {
/* 31 */     return new TextureContents(MissingTextureAtlasSprite.generateMissingImage(), null);
/*    */   }
/*    */   
/*    */   public boolean blur() {
/* 35 */     return (this.metadata != null) ? this.metadata.blur() : false;
/*    */   }
/*    */   
/*    */   public boolean clamp() {
/* 39 */     return (this.metadata != null) ? this.metadata.clamp() : false;
/*    */   }
/*    */   
/*    */   public void close() {
/* 44 */     this.image.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\TextureContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */