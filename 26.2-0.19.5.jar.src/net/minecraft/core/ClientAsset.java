/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public interface ClientAsset {
/*    */   Identifier id();
/*    */   
/*    */   public static interface Texture extends ClientAsset {
/*    */     Identifier texturePath();
/*    */   }
/*    */   
/*    */   public static final class ResourceTexture extends Record implements Texture {
/*    */     private final Identifier id;
/*    */     
/*    */     private final Identifier texturePath;
/*    */     
/*    */     public ResourceTexture(Identifier id, Identifier texturePath) {
/* 18 */       this.id = id;
/* 18 */       this.texturePath = texturePath;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/ClientAsset$ResourceTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/ClientAsset$ResourceTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/ClientAsset$ResourceTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/ClientAsset$ResourceTexture;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier id() {
/* 18 */       return this.id;
/*    */     }
/*    */     
/*    */     public Identifier texturePath() {
/* 18 */       return this.texturePath;
/*    */     }
/*    */     
/* 22 */     public static final Codec<ResourceTexture> CODEC = Identifier.CODEC.xmap(ResourceTexture::new, ResourceTexture::id);
/*    */     
/* 24 */     public static final MapCodec<ResourceTexture> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");
/*    */     
/* 26 */     public static final StreamCodec<ByteBuf, ResourceTexture> STREAM_CODEC = Identifier.STREAM_CODEC.map(ResourceTexture::new, ResourceTexture::id);
/*    */     
/*    */     public ResourceTexture(Identifier texture) {
/* 29 */       this(texture, texture
/*    */           
/* 31 */           .withPath(path -> "textures/" + path + ".png"));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class DownloadedTexture extends Record implements Texture {
/*    */     private final Identifier texturePath;
/*    */     
/*    */     private final String url;
/*    */     
/*    */     public DownloadedTexture(Identifier texturePath, String url) {
/* 36 */       this.texturePath = texturePath;
/* 36 */       this.url = url;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/ClientAsset$DownloadedTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #36	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/ClientAsset$DownloadedTexture;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier texturePath() {
/* 36 */       return this.texturePath;
/*    */     }
/*    */     
/*    */     public String url() {
/* 36 */       return this.url;
/*    */     }
/*    */     
/*    */     public Identifier id() {
/* 42 */       return this.texturePath;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\ClientAsset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */