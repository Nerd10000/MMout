/*    */ package com.mojang.blaze3d.font;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import it.unimi.dsi.fastutil.ints.IntSets;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
/*    */ import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
/*    */ import net.minecraft.client.gui.font.providers.GlyphProviderType;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpaceProvider implements GlyphProvider {
/*    */   private final Int2ObjectMap<EmptyGlyph> glyphs;
/*    */   
/*    */   public SpaceProvider(Map<Integer, Float> advances) {
/* 23 */     this.glyphs = (Int2ObjectMap<EmptyGlyph>)new Int2ObjectOpenHashMap(advances.size());
/* 24 */     advances.forEach((codepoint, advance) -> this.glyphs.put(codepoint.intValue(), new EmptyGlyph(advance.floatValue())));
/*    */   }
/*    */   
/*    */   public UnbakedGlyph getGlyph(int codepoint) {
/* 29 */     return (UnbakedGlyph)this.glyphs.get(codepoint);
/*    */   }
/*    */   
/*    */   public IntSet getSupportedGlyphs() {
/* 34 */     return IntSets.unmodifiable(this.glyphs.keySet());
/*    */   }
/*    */   
/*    */   public static final class Definition extends Record implements GlyphProviderDefinition {
/*    */     private final Map<Integer, Float> advances;
/*    */     
/*    */     public static final MapCodec<Definition> CODEC;
/*    */     
/*    */     public Definition(Map<Integer, Float> advances) {
/* 37 */       this.advances = advances;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/font/SpaceProvider$Definition;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/font/SpaceProvider$Definition;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/font/SpaceProvider$Definition;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/font/SpaceProvider$Definition;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/font/SpaceProvider$Definition;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #37	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/font/SpaceProvider$Definition;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Map<Integer, Float> advances() {
/* 37 */       return this.advances;
/*    */     }
/*    */     
/*    */     static {
/* 38 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap(ExtraCodecs.CODEPOINT, (Codec)Codec.FLOAT).fieldOf("advances").forGetter(Definition::advances)).apply((Applicative)i, Definition::new));
/*    */     }
/*    */     
/*    */     public GlyphProviderType type() {
/* 44 */       return GlyphProviderType.SPACE;
/*    */     }
/*    */     
/*    */     public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 49 */       GlyphProviderDefinition.Loader loader = resourceManager -> new SpaceProvider(this.advances);
/* 50 */       return Either.left(loader);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\SpaceProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */