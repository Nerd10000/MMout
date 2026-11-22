/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteList;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.FastBufferedInputStream;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class UnihexProvider implements GlyphProvider {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int GLYPH_HEIGHT = 16;
/*     */   
/*     */   private static final int DIGITS_PER_BYTE = 2;
/*     */   
/*     */   private static final int DIGITS_FOR_WIDTH_8 = 32;
/*     */   
/*     */   private static final int DIGITS_FOR_WIDTH_16 = 64;
/*     */   
/*     */   private static final int DIGITS_FOR_WIDTH_24 = 96;
/*     */   
/*     */   private static final int DIGITS_FOR_WIDTH_32 = 128;
/*     */   
/*     */   private final CodepointMap<Glyph> glyphs;
/*     */   
/*     */   private UnihexProvider(CodepointMap<Glyph> glyphs) {
/*  53 */     this.glyphs = glyphs;
/*     */   }
/*     */   
/*     */   public UnbakedGlyph getGlyph(int codepoint) {
/*  58 */     return (UnbakedGlyph)this.glyphs.get(codepoint);
/*     */   }
/*     */   
/*     */   public IntSet getSupportedGlyphs() {
/*  63 */     return this.glyphs.keySet();
/*     */   }
/*     */   
/*     */   private static final class OverrideRange extends Record {
/*     */     private final int from;
/*     */     
/*     */     private final int to;
/*     */     
/*     */     private final UnihexProvider.Dimensions dimensions;
/*     */     
/*     */     private static final Codec<OverrideRange> RAW_CODEC;
/*     */     
/*     */     public static final Codec<OverrideRange> CODEC;
/*     */     
/*     */     private OverrideRange(int from, int to, UnihexProvider.Dimensions dimensions) {
/*  66 */       this.from = from;
/*  66 */       this.to = to;
/*  66 */       this.dimensions = dimensions;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int from() {
/*  66 */       return this.from;
/*     */     }
/*     */     
/*     */     public int to() {
/*  66 */       return this.to;
/*     */     }
/*     */     
/*     */     public UnihexProvider.Dimensions dimensions() {
/*  66 */       return this.dimensions;
/*     */     }
/*     */     
/*     */     static {
/*  67 */       RAW_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.CODEPOINT.fieldOf("from").forGetter(OverrideRange::from), (App)ExtraCodecs.CODEPOINT.fieldOf("to").forGetter(OverrideRange::to), (App)UnihexProvider.Dimensions.MAP_CODEC.forGetter(OverrideRange::dimensions)).apply((Applicative)i, OverrideRange::new));
/*  73 */       CODEC = RAW_CODEC.validate(o -> (o.from >= o.to) ? DataResult.error(()) : DataResult.success(o));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Dimensions extends Record {
/*     */     private final int left;
/*     */     
/*     */     private final int right;
/*     */     
/*     */     public static final MapCodec<Dimensions> MAP_CODEC;
/*     */     
/*     */     public Dimensions(int left, int right) {
/*  86 */       this.left = left;
/*  86 */       this.right = right;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #86	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int left() {
/*  86 */       return this.left;
/*     */     }
/*     */     
/*     */     public int right() {
/*  86 */       return this.right;
/*     */     }
/*     */     
/*     */     static {
/*  87 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("left").forGetter(Dimensions::left), (App)Codec.INT.fieldOf("right").forGetter(Dimensions::right)).apply((Applicative)i, Dimensions::new));
/*     */     }
/*     */     
/*  92 */     public static final Codec<Dimensions> CODEC = MAP_CODEC.codec();
/*     */     
/*     */     public int pack() {
/*  95 */       return pack(this.left, this.right);
/*     */     }
/*     */     
/*     */     public static int pack(int left, int right) {
/*  99 */       return (left & 0xFF) << 8 | right & 0xFF;
/*     */     }
/*     */     
/*     */     public static int left(int packed) {
/* 104 */       return (byte)(packed >> 8);
/*     */     }
/*     */     
/*     */     public static int right(int packed) {
/* 109 */       return (byte)packed;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Definition implements GlyphProviderDefinition {
/*     */     public static final MapCodec<Definition> CODEC;
/*     */     
/*     */     private final Identifier hexFile;
/*     */     
/*     */     private final List<UnihexProvider.OverrideRange> sizeOverrides;
/*     */     
/*     */     static {
/* 114 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("hex_file").forGetter(()), (App)UnihexProvider.OverrideRange.CODEC.listOf().optionalFieldOf("size_overrides", List.of()).forGetter(())).apply((Applicative)i, Definition::new));
/*     */     }
/*     */     
/*     */     private Definition(Identifier hexFile, List<UnihexProvider.OverrideRange> sizeOverrides) {
/* 123 */       this.hexFile = hexFile;
/* 124 */       this.sizeOverrides = sizeOverrides;
/*     */     }
/*     */     
/*     */     public GlyphProviderType type() {
/* 129 */       return GlyphProviderType.UNIHEX;
/*     */     }
/*     */     
/*     */     public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 134 */       return Either.left(this::load);
/*     */     }
/*     */     
/*     */     private GlyphProvider load(ResourceManager resourceManager) throws IOException {
/* 138 */       InputStream raw = resourceManager.open(this.hexFile);
/*     */       try {
/* 139 */         UnihexProvider unihexProvider = loadData(raw);
/* 140 */         if (raw != null)
/* 140 */           raw.close(); 
/*     */         return unihexProvider;
/*     */       } catch (Throwable throwable) {
/*     */         if (raw != null)
/*     */           try {
/*     */             raw.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     private UnihexProvider loadData(InputStream zipFile) throws IOException {
/* 144 */       CodepointMap<UnihexProvider.LineData> bits = new CodepointMap(x$0 -> new UnihexProvider.LineData[x$0], x$0 -> new UnihexProvider.LineData[x$0][]);
/* 145 */       Objects.requireNonNull(bits);
/* 145 */       UnihexProvider.ReaderOutput output = bits::put;
/* 147 */       ZipInputStream zis = new ZipInputStream(zipFile);
/*     */       try {
/*     */         ZipEntry entry;
/* 149 */         while ((entry = zis.getNextEntry()) != null) {
/* 150 */           String name = entry.getName();
/* 151 */           if (name.endsWith(".hex")) {
/* 152 */             UnihexProvider.LOGGER.info("Found {}, loading", name);
/* 153 */             UnihexProvider.readFromStream((InputStream)new FastBufferedInputStream(zis), output);
/*     */           } 
/*     */         } 
/* 158 */         CodepointMap<UnihexProvider.Glyph> glyphs = new CodepointMap(x$0 -> new UnihexProvider.Glyph[x$0], x$0 -> new UnihexProvider.Glyph[x$0][]);
/* 160 */         for (UnihexProvider.OverrideRange sizeOverride : this.sizeOverrides) {
/* 161 */           int from = sizeOverride.from;
/* 162 */           int to = sizeOverride.to;
/* 163 */           UnihexProvider.Dimensions size = sizeOverride.dimensions;
/* 164 */           for (int c = from; c <= to; c++) {
/* 165 */             UnihexProvider.LineData codepointBits = (UnihexProvider.LineData)bits.remove(c);
/* 166 */             if (codepointBits != null)
/* 167 */               glyphs.put(c, new UnihexProvider.Glyph(codepointBits, size.left, size.right)); 
/*     */           } 
/*     */         } 
/* 172 */         bits.forEach((codepoint, glyphBits) -> {
/*     */               int packedSize = glyphBits.calculateWidth();
/*     */               int left = UnihexProvider.Dimensions.left(packedSize);
/*     */               int right = UnihexProvider.Dimensions.right(packedSize);
/*     */               glyphs.put(codepoint, new UnihexProvider.Glyph(glyphBits, left, right));
/*     */             });
/* 179 */         UnihexProvider unihexProvider = new UnihexProvider(glyphs);
/* 180 */         zis.close();
/*     */         return unihexProvider;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           zis.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface LineData {
/*     */     int line(int param1Int);
/*     */     
/*     */     int bitWidth();
/*     */     
/*     */     default int mask() {
/* 190 */       int mask = 0;
/* 191 */       for (int i = 0; i < 16; i++)
/* 192 */         mask |= line(i); 
/* 194 */       return mask;
/*     */     }
/*     */     
/*     */     default int calculateWidth() {
/* 198 */       int left, right, mask = mask();
/* 199 */       int bitWidth = bitWidth();
/* 203 */       if (mask == 0) {
/* 205 */         left = 0;
/* 206 */         right = bitWidth;
/*     */       } else {
/* 208 */         left = Integer.numberOfLeadingZeros(mask);
/* 209 */         right = 32 - Integer.numberOfTrailingZeros(mask) - 1;
/*     */       } 
/* 211 */       return UnihexProvider.Dimensions.pack(left, right);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void unpackBitsToBytes(IntBuffer output, int value, int left, int right) {
/* 218 */     int startBit = 32 - left - 1;
/* 219 */     int endBit = 32 - right - 1;
/* 221 */     for (int i = startBit; i >= endBit; i--) {
/* 222 */       if (i >= 32 || i < 0) {
/* 223 */         output.put(0);
/*     */       } else {
/* 225 */         boolean isSet = ((value >> i & 0x1) != 0);
/* 226 */         output.put(isSet ? -1 : 0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void unpackBitsToBytes(IntBuffer output, LineData data, int left, int right) {
/* 232 */     for (int i = 0; i < 16; i++) {
/* 233 */       int line = data.line(i);
/* 234 */       unpackBitsToBytes(output, line, left, right);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class ByteContents extends Record implements LineData {
/*     */     private final byte[] contents;
/*     */     
/*     */     private ByteContents(byte[] contents) {
/* 238 */       this.contents = contents;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #238	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #238	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #238	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ByteContents;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public byte[] contents() {
/* 238 */       return this.contents;
/*     */     }
/*     */     
/*     */     public int line(int index) {
/* 241 */       return this.contents[index] << 24;
/*     */     }
/*     */     
/*     */     private static UnihexProvider.LineData read(int line, ByteList input) {
/* 245 */       byte[] content = new byte[16];
/* 246 */       int pos = 0;
/* 247 */       for (int i = 0; i < 16; i++) {
/* 248 */         int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 249 */         int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 250 */         byte v = (byte)(n1 << 4 | n0);
/* 251 */         content[i] = v;
/*     */       } 
/* 253 */       return new ByteContents(content);
/*     */     }
/*     */     
/*     */     public int bitWidth() {
/* 258 */       return 8;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ShortContents extends Record implements LineData {
/*     */     private final short[] contents;
/*     */     
/*     */     private ShortContents(short[] contents) {
/* 262 */       this.contents = contents;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #262	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #262	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #262	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ShortContents;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public short[] contents() {
/* 262 */       return this.contents;
/*     */     }
/*     */     
/*     */     public int line(int index) {
/* 265 */       return this.contents[index] << 16;
/*     */     }
/*     */     
/*     */     private static UnihexProvider.LineData read(int line, ByteList input) {
/* 269 */       short[] content = new short[16];
/* 270 */       int pos = 0;
/* 271 */       for (int i = 0; i < 16; i++) {
/* 272 */         int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 273 */         int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 274 */         int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 275 */         int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 276 */         short v = (short)(n3 << 12 | n2 << 8 | n1 << 4 | n0);
/* 277 */         content[i] = v;
/*     */       } 
/* 279 */       return new ShortContents(content);
/*     */     }
/*     */     
/*     */     public int bitWidth() {
/* 284 */       return 16;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class IntContents extends Record implements LineData {
/*     */     private final int[] contents;
/*     */     
/*     */     private final int bitWidth;
/*     */     
/*     */     private static final int SIZE_24 = 24;
/*     */     
/*     */     private IntContents(int[] contents, int bitWidth) {
/* 288 */       this.contents = contents;
/* 288 */       this.bitWidth = bitWidth;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$IntContents;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int[] contents() {
/* 288 */       return this.contents;
/*     */     }
/*     */     
/*     */     public int bitWidth() {
/* 288 */       return this.bitWidth;
/*     */     }
/*     */     
/*     */     public int line(int index) {
/* 293 */       return this.contents[index];
/*     */     }
/*     */     
/*     */     private static UnihexProvider.LineData read24(int line, ByteList input) {
/* 297 */       int[] content = new int[16];
/* 298 */       int mask = 0;
/* 299 */       int pos = 0;
/* 300 */       for (int i = 0; i < 16; i++) {
/* 301 */         int n5 = UnihexProvider.decodeHex(line, input, pos++);
/* 302 */         int n4 = UnihexProvider.decodeHex(line, input, pos++);
/* 303 */         int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 304 */         int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 305 */         int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 306 */         int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 307 */         int v = n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
/* 308 */         content[i] = v << 8;
/* 309 */         mask |= v;
/*     */       } 
/* 311 */       return new IntContents(content, 24);
/*     */     }
/*     */     
/*     */     public static UnihexProvider.LineData read32(int line, ByteList input) {
/* 315 */       int[] content = new int[16];
/* 316 */       int mask = 0;
/* 317 */       int pos = 0;
/* 318 */       for (int i = 0; i < 16; i++) {
/* 319 */         int n7 = UnihexProvider.decodeHex(line, input, pos++);
/* 320 */         int n6 = UnihexProvider.decodeHex(line, input, pos++);
/* 321 */         int n5 = UnihexProvider.decodeHex(line, input, pos++);
/* 322 */         int n4 = UnihexProvider.decodeHex(line, input, pos++);
/* 323 */         int n3 = UnihexProvider.decodeHex(line, input, pos++);
/* 324 */         int n2 = UnihexProvider.decodeHex(line, input, pos++);
/* 325 */         int n1 = UnihexProvider.decodeHex(line, input, pos++);
/* 326 */         int n0 = UnihexProvider.decodeHex(line, input, pos++);
/* 327 */         int v = n7 << 28 | n6 << 24 | n5 << 20 | n4 << 16 | n3 << 12 | n2 << 8 | n1 << 4 | n0;
/* 328 */         content[i] = v;
/* 329 */         mask |= v;
/*     */       } 
/* 331 */       return new IntContents(content, 32);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Glyph extends Record implements UnbakedGlyph {
/*     */     private final UnihexProvider.LineData contents;
/*     */     
/*     */     private final int left;
/*     */     
/*     */     private final int right;
/*     */     
/*     */     private Glyph(UnihexProvider.LineData contents, int left, int right) {
/* 335 */       this.contents = contents;
/* 335 */       this.left = left;
/* 335 */       this.right = right;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #335	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #335	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #335	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Glyph;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UnihexProvider.LineData contents() {
/* 335 */       return this.contents;
/*     */     }
/*     */     
/*     */     public int left() {
/* 335 */       return this.left;
/*     */     }
/*     */     
/*     */     public int right() {
/* 335 */       return this.right;
/*     */     }
/*     */     
/*     */     public int width() {
/* 337 */       return this.right - this.left + 1;
/*     */     }
/*     */     
/*     */     public GlyphInfo info() {
/* 342 */       return new GlyphInfo() {
/*     */           public float getAdvance() {
/* 345 */             return UnihexProvider.Glyph.this.width() / 2.0F + 1.0F;
/*     */           }
/*     */           
/*     */           public float getShadowOffset() {
/* 350 */             return 0.5F;
/*     */           }
/*     */           
/*     */           public float getBoldOffset() {
/* 355 */             return 0.5F;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher) {
/* 362 */       return stitcher.stitch(info(), new GlyphBitmap() {
/*     */             public float getOversample() {
/* 365 */               return 2.0F;
/*     */             }
/*     */             
/*     */             public int getPixelWidth() {
/* 370 */               return UnihexProvider.Glyph.this.width();
/*     */             }
/*     */             
/*     */             public int getPixelHeight() {
/* 375 */               return 16;
/*     */             }
/*     */             
/*     */             public void upload(int x, int y, GpuTexture texture) {
/* 380 */               IntBuffer targetBuffer = MemoryUtil.memAllocInt(UnihexProvider.Glyph.this.width() * 16);
/* 381 */               UnihexProvider.unpackBitsToBytes(targetBuffer, UnihexProvider.Glyph.this.contents, UnihexProvider.Glyph.this.left, UnihexProvider.Glyph.this.right);
/* 382 */               targetBuffer.rewind();
/* 383 */               RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, MemoryUtil.memByteBuffer(targetBuffer), 0, 0, x, y, UnihexProvider.Glyph.this.width(), 16);
/* 384 */               MemoryUtil.memFree(targetBuffer);
/*     */             }
/*     */             
/*     */             public boolean isColored() {
/* 390 */               return true;
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements GlyphInfo {
/*     */     public float getAdvance() {
/*     */       return UnihexProvider.Glyph.this.width() / 2.0F + 1.0F;
/*     */     }
/*     */     
/*     */     public float getShadowOffset() {
/*     */       return 0.5F;
/*     */     }
/*     */     
/*     */     public float getBoldOffset() {
/*     */       return 0.5F;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements GlyphBitmap {
/*     */     public float getOversample() {
/*     */       return 2.0F;
/*     */     }
/*     */     
/*     */     public int getPixelWidth() {
/*     */       return UnihexProvider.Glyph.this.width();
/*     */     }
/*     */     
/*     */     public int getPixelHeight() {
/*     */       return 16;
/*     */     }
/*     */     
/*     */     public void upload(int x, int y, GpuTexture texture) {
/*     */       IntBuffer targetBuffer = MemoryUtil.memAllocInt(UnihexProvider.Glyph.this.width() * 16);
/*     */       UnihexProvider.unpackBitsToBytes(targetBuffer, UnihexProvider.Glyph.this.contents, UnihexProvider.Glyph.this.left, UnihexProvider.Glyph.this.right);
/*     */       targetBuffer.rewind();
/*     */       RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, MemoryUtil.memByteBuffer(targetBuffer), 0, 0, x, y, UnihexProvider.Glyph.this.width(), 16);
/*     */       MemoryUtil.memFree(targetBuffer);
/*     */     }
/*     */     
/*     */     public boolean isColored() {
/* 390 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void readFromStream(InputStream input, ReaderOutput output) throws IOException {
/*     */     // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore_2
/*     */     //   2: new it/unimi/dsi/fastutil/bytes/ByteArrayList
/*     */     //   5: dup
/*     */     //   6: sipush #128
/*     */     //   9: invokespecial <init> : (I)V
/*     */     //   12: astore_3
/*     */     //   13: aload_0
/*     */     //   14: aload_3
/*     */     //   15: bipush #58
/*     */     //   17: invokestatic copyUntil : (Ljava/io/InputStream;Lit/unimi/dsi/fastutil/bytes/ByteList;I)Z
/*     */     //   20: istore #4
/*     */     //   22: aload_3
/*     */     //   23: invokeinterface size : ()I
/*     */     //   28: istore #5
/*     */     //   30: iload #5
/*     */     //   32: ifne -> 43
/*     */     //   35: iload #4
/*     */     //   37: ifne -> 43
/*     */     //   40: goto -> 254
/*     */     //   43: iload #4
/*     */     //   45: ifeq -> 67
/*     */     //   48: iload #5
/*     */     //   50: iconst_4
/*     */     //   51: if_icmpeq -> 81
/*     */     //   54: iload #5
/*     */     //   56: iconst_5
/*     */     //   57: if_icmpeq -> 81
/*     */     //   60: iload #5
/*     */     //   62: bipush #6
/*     */     //   64: if_icmpeq -> 81
/*     */     //   67: new java/lang/IllegalArgumentException
/*     */     //   70: dup
/*     */     //   71: iload_2
/*     */     //   72: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   77: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   80: athrow
/*     */     //   81: iconst_0
/*     */     //   82: istore #6
/*     */     //   84: iconst_0
/*     */     //   85: istore #7
/*     */     //   87: iload #7
/*     */     //   89: iload #5
/*     */     //   91: if_icmpge -> 119
/*     */     //   94: iload #6
/*     */     //   96: iconst_4
/*     */     //   97: ishl
/*     */     //   98: iload_2
/*     */     //   99: aload_3
/*     */     //   100: iload #7
/*     */     //   102: invokeinterface getByte : (I)B
/*     */     //   107: invokestatic decodeHex : (IB)I
/*     */     //   110: ior
/*     */     //   111: istore #6
/*     */     //   113: iinc #7, 1
/*     */     //   116: goto -> 87
/*     */     //   119: aload_3
/*     */     //   120: invokeinterface clear : ()V
/*     */     //   125: aload_0
/*     */     //   126: aload_3
/*     */     //   127: bipush #10
/*     */     //   129: invokestatic copyUntil : (Ljava/io/InputStream;Lit/unimi/dsi/fastutil/bytes/ByteList;I)Z
/*     */     //   132: pop
/*     */     //   133: aload_3
/*     */     //   134: invokeinterface size : ()I
/*     */     //   139: istore #7
/*     */     //   141: iload #7
/*     */     //   143: lookupswitch default -> 216, 32 -> 184, 64 -> 192, 96 -> 200, 128 -> 208
/*     */     //   184: iload_2
/*     */     //   185: aload_3
/*     */     //   186: invokestatic read : (ILit/unimi/dsi/fastutil/bytes/ByteList;)Lnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;
/*     */     //   189: goto -> 230
/*     */     //   192: iload_2
/*     */     //   193: aload_3
/*     */     //   194: invokestatic read : (ILit/unimi/dsi/fastutil/bytes/ByteList;)Lnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;
/*     */     //   197: goto -> 230
/*     */     //   200: iload_2
/*     */     //   201: aload_3
/*     */     //   202: invokestatic read24 : (ILit/unimi/dsi/fastutil/bytes/ByteList;)Lnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;
/*     */     //   205: goto -> 230
/*     */     //   208: iload_2
/*     */     //   209: aload_3
/*     */     //   210: invokestatic read32 : (ILit/unimi/dsi/fastutil/bytes/ByteList;)Lnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;
/*     */     //   213: goto -> 230
/*     */     //   216: new java/lang/IllegalArgumentException
/*     */     //   219: dup
/*     */     //   220: iload_2
/*     */     //   221: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   226: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   229: athrow
/*     */     //   230: astore #8
/*     */     //   232: aload_1
/*     */     //   233: iload #6
/*     */     //   235: aload #8
/*     */     //   237: invokeinterface accept : (ILnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;)V
/*     */     //   242: iinc #2, 1
/*     */     //   245: aload_3
/*     */     //   246: invokeinterface clear : ()V
/*     */     //   251: goto -> 13
/*     */     //   254: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #404	-> 0
/*     */     //   #406	-> 2
/*     */     //   #408	-> 13
/*     */     //   #409	-> 22
/*     */     //   #410	-> 30
/*     */     //   #411	-> 40
/*     */     //   #415	-> 43
/*     */     //   #416	-> 67
/*     */     //   #418	-> 81
/*     */     //   #419	-> 84
/*     */     //   #420	-> 94
/*     */     //   #419	-> 113
/*     */     //   #423	-> 119
/*     */     //   #424	-> 125
/*     */     //   #425	-> 133
/*     */     //   #426	-> 141
/*     */     //   #427	-> 184
/*     */     //   #428	-> 192
/*     */     //   #429	-> 200
/*     */     //   #430	-> 208
/*     */     //   #432	-> 216
/*     */     //   #433	-> 230
/*     */     //   #435	-> 232
/*     */     //   #436	-> 242
/*     */     //   #437	-> 245
/*     */     //   #438	-> 251
/*     */     //   #439	-> 254
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   87	32	7	i	I
/*     */     //   22	229	4	foundColon	Z
/*     */     //   30	221	5	codepointDigitCount	I
/*     */     //   84	167	6	codepoint	I
/*     */     //   141	110	7	dataDigitCount	I
/*     */     //   232	19	8	contents	Lnet/minecraft/client/gui/font/providers/UnihexProvider$LineData;
/*     */     //   0	255	0	input	Ljava/io/InputStream;
/*     */     //   0	255	1	output	Lnet/minecraft/client/gui/font/providers/UnihexProvider$ReaderOutput;
/*     */     //   2	253	2	line	I
/*     */     //   13	242	3	buffer	Lit/unimi/dsi/fastutil/bytes/ByteList;
/*     */   }
/*     */   
/*     */   private static int decodeHex(int line, ByteList input, int index) {
/* 442 */     return decodeHex(line, input.getByte(index));
/*     */   }
/*     */   
/*     */   private static int decodeHex(int line, byte b) {
/* 446 */     switch (b) {
/*     */       case 48:
/*     */       
/*     */       case 49:
/*     */       
/*     */       case 50:
/*     */       
/*     */       case 51:
/*     */       
/*     */       case 52:
/*     */       
/*     */       case 53:
/*     */       
/*     */       case 54:
/*     */       
/*     */       case 55:
/*     */       
/*     */       case 56:
/*     */       
/*     */       case 57:
/*     */       
/*     */       case 65:
/*     */       
/*     */       case 66:
/*     */       
/*     */       case 67:
/*     */       
/*     */       case 68:
/*     */       
/*     */       case 69:
/*     */       
/*     */       case 70:
/*     */       
/*     */     } 
/* 464 */     throw new IllegalArgumentException("Invalid entry at line " + line + ": expected hex digit, got " + (char)b);
/*     */   }
/*     */   
/*     */   private static boolean copyUntil(InputStream input, ByteList output, int delimiter) throws IOException {
/*     */     while (true) {
/* 470 */       int b = input.read();
/* 471 */       if (b == -1)
/* 472 */         return false; 
/* 473 */       if (b == delimiter)
/* 474 */         return true; 
/* 476 */       output.add((byte)b);
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ReaderOutput {
/*     */     void accept(int param1Int, UnihexProvider.LineData param1LineData);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */