/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ 
/*     */ public final class Definition extends Record implements GlyphProviderDefinition {
/*     */   private final Identifier file;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int ascent;
/*     */   
/*     */   private final int[][] codepointGrid;
/*     */   
/*     */   private static final Codec<int[][]> CODEPOINT_GRID_CODEC;
/*     */   
/*     */   public static final MapCodec<Definition> CODEC;
/*     */   
/*     */   public Definition(Identifier file, int height, int ascent, int[][] codepointGrid) {
/*  60 */     this.file = file;
/*  60 */     this.height = height;
/*  60 */     this.ascent = ascent;
/*  60 */     this.codepointGrid = codepointGrid;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #60	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #60	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #60	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier file() {
/*  60 */     return this.file;
/*     */   }
/*     */   
/*     */   public int height() {
/*  60 */     return this.height;
/*     */   }
/*     */   
/*     */   public int ascent() {
/*  60 */     return this.ascent;
/*     */   }
/*     */   
/*     */   public int[][] codepointGrid() {
/*  60 */     return this.codepointGrid;
/*     */   }
/*     */   
/*     */   static {
/*  84 */     CODEPOINT_GRID_CODEC = Codec.STRING.listOf().xmap(input -> {
/*     */           int lineCount = input.size();
/*     */           int[][] result = new int[lineCount][];
/*     */           for (int i = 0; i < lineCount; i++)
/*     */             result[i] = ((String)input.get(i)).codePoints().toArray(); 
/*     */           return (Function)result;
/*     */         }grid -> {
/*     */           List<String> result = new ArrayList<>(grid.length);
/*     */           for (int[] line : grid)
/*     */             result.add(new String(line, 0, line.length)); 
/*     */           return (Function)result;
/*  84 */         }).validate(Definition::validateDimensions);
/* 114 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("file").forGetter(Definition::file), (App)Codec.INT.optionalFieldOf("height", Integer.valueOf(8)).forGetter(Definition::height), (App)Codec.INT.fieldOf("ascent").forGetter(Definition::ascent), (App)CODEPOINT_GRID_CODEC.fieldOf("chars").forGetter(Definition::codepointGrid)).apply((Applicative)i, Definition::new)).validate(Definition::validate);
/*     */   }
/*     */   
/*     */   private static DataResult<int[][]> validateDimensions(int[][] grid) {
/*     */     int lineCount = grid.length;
/*     */     if (lineCount == 0)
/*     */       return DataResult.error(() -> "Expected to find data in codepoint grid"); 
/*     */     int[] firstLine = grid[0];
/*     */     int lineWidth = firstLine.length;
/*     */     if (lineWidth == 0)
/*     */       return DataResult.error(() -> "Expected to find data in codepoint grid"); 
/*     */     for (int i = 1; i < lineCount; i++) {
/*     */       int[] line = grid[i];
/*     */       if (line.length != lineWidth)
/*     */         return DataResult.error(() -> "Lines in codepoint grid have to be the same length (found: " + line.length + " codepoints, expected: " + lineWidth + "), pad with \\u0000"); 
/*     */     } 
/*     */     return DataResult.success(grid);
/*     */   }
/*     */   
/*     */   private static DataResult<Definition> validate(Definition builder) {
/* 117 */     if (builder.ascent > builder.height)
/* 118 */       return DataResult.error(() -> "Ascent " + builder.ascent + " higher than height " + builder.height); 
/* 120 */     return DataResult.success(builder);
/*     */   }
/*     */   
/*     */   public GlyphProviderType type() {
/* 125 */     return GlyphProviderType.BITMAP;
/*     */   }
/*     */   
/*     */   public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 130 */     return Either.left(this::load);
/*     */   }
/*     */   
/*     */   private GlyphProvider load(ResourceManager resourceManager) throws IOException {
/* 134 */     Identifier texture = this.file.withPrefix("textures/");
/* 135 */     InputStream resource = resourceManager.open(texture);
/*     */     try {
/* 136 */       NativeImage image = NativeImage.read(NativeImage.Format.RGBA, resource);
/* 138 */       int w = image.getWidth();
/* 139 */       int h = image.getHeight();
/* 141 */       int glyphWidth = w / (this.codepointGrid[0]).length;
/* 142 */       int glyphHeight = h / this.codepointGrid.length;
/* 144 */       float pixelScale = this.height / glyphHeight;
/* 146 */       CodepointMap<BitmapProvider.Glyph> charMap = new CodepointMap(x$0 -> new BitmapProvider.Glyph[x$0], x$0 -> new BitmapProvider.Glyph[x$0][]);
/* 148 */       BitmapProvider.ImageDataHolder imageDataHolder = new BitmapProvider.ImageDataHolder(texture, image);
/* 150 */       for (int slotY = 0; slotY < this.codepointGrid.length; slotY++) {
/* 151 */         int linePos = 0;
/* 152 */         for (int c : this.codepointGrid[slotY]) {
/* 153 */           int slotX = linePos++;
/* 154 */           if (c != 0) {
/* 157 */             int actualGlyphWidth = getActualGlyphWidth(image, glyphWidth, glyphHeight, slotX, slotY);
/* 159 */             BitmapProvider.Glyph prev = (BitmapProvider.Glyph)charMap.put(c, new BitmapProvider.Glyph(pixelScale, imageDataHolder, slotX * glyphWidth, slotY * glyphHeight, glyphWidth, glyphHeight, (int)(0.5D + (actualGlyphWidth * pixelScale)) + 1, this.ascent));
/* 160 */             if (prev != null)
/* 161 */               BitmapProvider.LOGGER.warn("Codepoint '{}' declared multiple times in {}", Integer.toHexString(c), texture); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 166 */       BitmapProvider bitmapProvider = new BitmapProvider(imageDataHolder, charMap);
/* 167 */       if (resource != null)
/* 167 */         resource.close(); 
/*     */       return bitmapProvider;
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
/*     */   
/*     */   private int getActualGlyphWidth(NativeImage image, int glyphWidth, int glyphHeight, int xGlyph, int yGlyph) {
/* 171 */     int width = glyphWidth - 1;
/* 172 */     for (; width >= 0; width--) {
/* 173 */       int xPixel = xGlyph * glyphWidth + width;
/* 174 */       for (int y = 0; y < glyphHeight; y++) {
/* 175 */         int yPixel = yGlyph * glyphHeight + y;
/* 177 */         if (image.getLuminanceOrAlpha(xPixel, yPixel) != 0)
/* 178 */           return width + 1; 
/*     */       } 
/*     */     } 
/* 182 */     return width + 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\BitmapProvider$Definition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */