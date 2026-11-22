/*     */ package net.minecraft.client.renderer.texture.atlas.sources;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class Unstitcher extends Record implements SpriteSource {
/*     */   private final Identifier resource;
/*     */   
/*     */   private final List<Region> regions;
/*     */   
/*     */   private final double xDivisor;
/*     */   
/*     */   private final double yDivisor;
/*     */   
/*     */   public Unstitcher(Identifier resource, List<Region> regions, double xDivisor, double yDivisor) {
/*  23 */     this.resource = resource;
/*  23 */     this.regions = regions;
/*  23 */     this.xDivisor = xDivisor;
/*  23 */     this.yDivisor = yDivisor;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier resource() {
/*  23 */     return this.resource;
/*     */   }
/*     */   
/*     */   public List<Region> regions() {
/*  23 */     return this.regions;
/*     */   }
/*     */   
/*     */   public double xDivisor() {
/*  23 */     return this.xDivisor;
/*     */   }
/*     */   
/*     */   public double yDivisor() {
/*  23 */     return this.yDivisor;
/*     */   }
/*     */   
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<Unstitcher> MAP_CODEC;
/*     */   
/*     */   static {
/*  31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("resource").forGetter(Unstitcher::resource), (App)ExtraCodecs.nonEmptyList(Region.CODEC.listOf()).fieldOf("regions").forGetter(Unstitcher::regions), (App)Codec.DOUBLE.optionalFieldOf("divisor_x", Double.valueOf(1.0D)).forGetter(Unstitcher::xDivisor), (App)Codec.DOUBLE.optionalFieldOf("divisor_y", Double.valueOf(1.0D)).forGetter(Unstitcher::yDivisor)).apply((Applicative)i, Unstitcher::new));
/*     */   }
/*     */   
/*     */   public void run(ResourceManager resourceManager, SpriteSource.Output output) {
/*  40 */     Identifier resourceId = TEXTURE_ID_CONVERTER.idToFile(this.resource);
/*  41 */     Optional<Resource> resource = resourceManager.getResource(resourceId);
/*  42 */     if (resource.isPresent()) {
/*  43 */       LazyLoadedImage image = new LazyLoadedImage(resourceId, resource.get(), this.regions.size());
/*  44 */       for (Region region : this.regions)
/*  45 */         output.add(region.sprite, new RegionInstance(image, region, this.xDivisor, this.yDivisor)); 
/*     */     } else {
/*  48 */       LOGGER.warn("Missing sprite: {}", resourceId);
/*     */     } 
/*     */   }
/*     */   
/*     */   public MapCodec<Unstitcher> codec() {
/*  54 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public static final class Region extends Record {
/*     */     private final Identifier sprite;
/*     */     
/*     */     private final double x;
/*     */     
/*     */     private final double y;
/*     */     
/*     */     private final double width;
/*     */     
/*     */     private final double height;
/*     */     
/*     */     public static final Codec<Region> CODEC;
/*     */     
/*     */     public Region(Identifier sprite, double x, double y, double width, double height) {
/*  57 */       this.sprite = sprite;
/*  57 */       this.x = x;
/*  57 */       this.y = y;
/*  57 */       this.width = width;
/*  57 */       this.height = height;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier sprite() {
/*  57 */       return this.sprite;
/*     */     }
/*     */     
/*     */     public double x() {
/*  57 */       return this.x;
/*     */     }
/*     */     
/*     */     public double y() {
/*  57 */       return this.y;
/*     */     }
/*     */     
/*     */     public double width() {
/*  57 */       return this.width;
/*     */     }
/*     */     
/*     */     public double height() {
/*  57 */       return this.height;
/*     */     }
/*     */     
/*     */     static {
/*  58 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sprite").forGetter(Region::sprite), (App)Codec.DOUBLE.fieldOf("x").forGetter(Region::x), (App)Codec.DOUBLE.fieldOf("y").forGetter(Region::y), (App)Codec.DOUBLE.fieldOf("width").forGetter(Region::width), (App)Codec.DOUBLE.fieldOf("height").forGetter(Region::height)).apply((Applicative)i, Region::new));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RegionInstance implements SpriteSource.DiscardableLoader {
/*     */     private final LazyLoadedImage image;
/*     */     
/*     */     private final Unstitcher.Region region;
/*     */     
/*     */     private final double xDivisor;
/*     */     
/*     */     private final double yDivisor;
/*     */     
/*     */     private RegionInstance(LazyLoadedImage image, Unstitcher.Region region, double xDivisor, double yDivisor) {
/*  74 */       this.image = image;
/*  75 */       this.region = region;
/*  76 */       this.xDivisor = xDivisor;
/*  77 */       this.yDivisor = yDivisor;
/*     */     }
/*     */     
/*     */     public SpriteContents get(SpriteResourceLoader loader) {
/*     */       try {
/*  83 */         NativeImage fullImage = this.image.get();
/*  85 */         double xScale = fullImage.getWidth() / this.xDivisor;
/*  86 */         double yScale = fullImage.getHeight() / this.yDivisor;
/*  88 */         int x = Mth.floor(this.region.x * xScale);
/*  89 */         int y = Mth.floor(this.region.y * yScale);
/*  91 */         int width = Mth.floor(this.region.width * xScale);
/*  92 */         int height = Mth.floor(this.region.height * yScale);
/*  94 */         NativeImage target = new NativeImage(NativeImage.Format.RGBA, width, height, false);
/*  95 */         fullImage.copyRect(target, x, y, 0, 0, width, height, false, false);
/* 101 */         return new SpriteContents(this.region.sprite, new FrameSize(width, height), target);
/* 102 */       } catch (Exception e) {
/* 103 */         Unstitcher.LOGGER.error("Failed to unstitch region {}", this.region.sprite, e);
/*     */       } finally {
/* 105 */         this.image.release();
/*     */       } 
/* 107 */       return MissingTextureAtlasSprite.create();
/*     */     }
/*     */     
/*     */     public void discard() {
/* 112 */       this.image.release();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\Unstitcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */