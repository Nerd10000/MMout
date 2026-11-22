/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ 
/*     */ public class Definition implements GlyphProviderDefinition {
/*     */   public static final MapCodec<Definition> CODEC;
/*     */   
/*     */   private final Identifier hexFile;
/*     */   
/*     */   private final List<UnihexProvider.OverrideRange> sizeOverrides;
/*     */   
/*     */   static {
/* 114 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("hex_file").forGetter(()), (App)UnihexProvider.OverrideRange.CODEC.listOf().optionalFieldOf("size_overrides", List.of()).forGetter(())).apply((Applicative)i, Definition::new));
/*     */   }
/*     */   
/*     */   private Definition(Identifier hexFile, List<UnihexProvider.OverrideRange> sizeOverrides) {
/* 123 */     this.hexFile = hexFile;
/* 124 */     this.sizeOverrides = sizeOverrides;
/*     */   }
/*     */   
/*     */   public GlyphProviderType type() {
/* 129 */     return GlyphProviderType.UNIHEX;
/*     */   }
/*     */   
/*     */   public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 134 */     return Either.left(this::load);
/*     */   }
/*     */   
/*     */   private GlyphProvider load(ResourceManager resourceManager) throws IOException {
/* 138 */     InputStream raw = resourceManager.open(this.hexFile);
/*     */     try {
/* 139 */       UnihexProvider unihexProvider = loadData(raw);
/* 140 */       if (raw != null)
/* 140 */         raw.close(); 
/*     */       return unihexProvider;
/*     */     } catch (Throwable throwable) {
/*     */       if (raw != null)
/*     */         try {
/*     */           raw.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private UnihexProvider loadData(InputStream zipFile) throws IOException {
/* 144 */     CodepointMap<UnihexProvider.LineData> bits = new CodepointMap(x$0 -> new UnihexProvider.LineData[x$0], x$0 -> new UnihexProvider.LineData[x$0][]);
/* 145 */     Objects.requireNonNull(bits);
/* 145 */     UnihexProvider.ReaderOutput output = bits::put;
/* 147 */     ZipInputStream zis = new ZipInputStream(zipFile);
/*     */     try {
/*     */       ZipEntry entry;
/* 149 */       while ((entry = zis.getNextEntry()) != null) {
/* 150 */         String name = entry.getName();
/* 151 */         if (name.endsWith(".hex")) {
/* 152 */           UnihexProvider.LOGGER.info("Found {}, loading", name);
/* 153 */           UnihexProvider.readFromStream((InputStream)new FastBufferedInputStream(zis), output);
/*     */         } 
/*     */       } 
/* 158 */       CodepointMap<UnihexProvider.Glyph> glyphs = new CodepointMap(x$0 -> new UnihexProvider.Glyph[x$0], x$0 -> new UnihexProvider.Glyph[x$0][]);
/* 160 */       for (UnihexProvider.OverrideRange sizeOverride : this.sizeOverrides) {
/* 161 */         int from = sizeOverride.from;
/* 162 */         int to = sizeOverride.to;
/* 163 */         UnihexProvider.Dimensions size = sizeOverride.dimensions;
/* 164 */         for (int c = from; c <= to; c++) {
/* 165 */           UnihexProvider.LineData codepointBits = (UnihexProvider.LineData)bits.remove(c);
/* 166 */           if (codepointBits != null)
/* 167 */             glyphs.put(c, new UnihexProvider.Glyph(codepointBits, size.left, size.right)); 
/*     */         } 
/*     */       } 
/* 172 */       bits.forEach((codepoint, glyphBits) -> {
/*     */             int packedSize = glyphBits.calculateWidth();
/*     */             int left = UnihexProvider.Dimensions.left(packedSize);
/*     */             int right = UnihexProvider.Dimensions.right(packedSize);
/*     */             glyphs.put(codepoint, new UnihexProvider.Glyph(glyphBits, left, right));
/*     */           });
/* 179 */       UnihexProvider unihexProvider = new UnihexProvider(glyphs);
/* 180 */       zis.close();
/*     */       return unihexProvider;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         zis.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$Definition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */