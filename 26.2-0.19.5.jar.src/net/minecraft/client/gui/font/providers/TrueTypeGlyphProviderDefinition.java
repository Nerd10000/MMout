/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphProvider;
/*    */ import com.mojang.blaze3d.font.TrueTypeGlyphProvider;
/*    */ import com.mojang.blaze3d.platform.TextureUtil;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.Util;
/*    */ import org.lwjgl.PointerBuffer;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.lwjgl.util.freetype.FT_Face;
/*    */ import org.lwjgl.util.freetype.FreeType;
/*    */ 
/*    */ public final class TrueTypeGlyphProviderDefinition extends Record implements GlyphProviderDefinition {
/*    */   private final Identifier location;
/*    */   
/*    */   private final float size;
/*    */   
/*    */   private final float oversample;
/*    */   
/*    */   private final Shift shift;
/*    */   
/*    */   private final String skip;
/*    */   
/*    */   private static final Codec<String> SKIP_LIST_CODEC;
/*    */   
/*    */   public static final MapCodec<TrueTypeGlyphProviderDefinition> CODEC;
/*    */   
/*    */   public TrueTypeGlyphProviderDefinition(Identifier location, float size, float oversample, Shift shift, String skip) {
/* 24 */     this.location = location;
/* 24 */     this.size = size;
/* 24 */     this.oversample = oversample;
/* 24 */     this.shift = shift;
/* 24 */     this.skip = skip;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier location() {
/* 24 */     return this.location;
/*    */   }
/*    */   
/*    */   public float size() {
/* 24 */     return this.size;
/*    */   }
/*    */   
/*    */   public float oversample() {
/* 24 */     return this.oversample;
/*    */   }
/*    */   
/*    */   public Shift shift() {
/* 24 */     return this.shift;
/*    */   }
/*    */   
/*    */   public String skip() {
/* 24 */     return this.skip;
/*    */   }
/*    */   
/*    */   public static final class Shift extends Record {
/*    */     private final float x;
/*    */     
/*    */     private final float y;
/*    */     
/*    */     public Shift(float x, float y) {
/* 31 */       this.x = x;
/* 31 */       this.y = y;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/TrueTypeGlyphProviderDefinition$Shift;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float x() {
/* 31 */       return this.x;
/*    */     }
/*    */     
/*    */     public float y() {
/* 31 */       return this.y;
/*    */     }
/*    */     
/* 32 */     public static final Shift NONE = new Shift(0.0F, 0.0F);
/*    */     
/*    */     public static final Codec<Shift> CODEC;
/*    */     
/*    */     static {
/* 34 */       CODEC = Codec.floatRange(-512.0F, 512.0F).listOf().comapFlatMap(input -> Util.fixedSize(input, 2).map(()), shift -> List.of(Float.valueOf(shift.x), Float.valueOf(shift.y)));
/*    */     }
/*    */   }
/*    */   
/*    */   static {
/* 41 */     SKIP_LIST_CODEC = Codec.withAlternative((Codec)Codec.STRING, Codec.STRING
/*    */         
/* 43 */         .listOf(), list -> String.join("", list));
/* 47 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("file").forGetter(TrueTypeGlyphProviderDefinition::location), (App)Codec.FLOAT.optionalFieldOf("size", Float.valueOf(11.0F)).forGetter(TrueTypeGlyphProviderDefinition::size), (App)Codec.FLOAT.optionalFieldOf("oversample", Float.valueOf(1.0F)).forGetter(TrueTypeGlyphProviderDefinition::oversample), (App)Shift.CODEC.optionalFieldOf("shift", Shift.NONE).forGetter(TrueTypeGlyphProviderDefinition::shift), (App)SKIP_LIST_CODEC.optionalFieldOf("skip", "").forGetter(TrueTypeGlyphProviderDefinition::skip)).apply((Applicative)i, TrueTypeGlyphProviderDefinition::new));
/*    */   }
/*    */   
/*    */   public GlyphProviderType type() {
/* 57 */     return GlyphProviderType.TTF;
/*    */   }
/*    */   
/*    */   public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 62 */     return Either.left(this::load);
/*    */   }
/*    */   
/*    */   private GlyphProvider load(ResourceManager resourceManager) throws IOException {
/* 66 */     FT_Face face = null;
/* 67 */     ByteBuffer fontData = null;
/*    */     try {
/* 68 */       InputStream resource = resourceManager.open(this.location.withPrefix("font/"));
/*    */       try {
/* 69 */         fontData = TextureUtil.readResource(resource);
/* 71 */         synchronized (FreeTypeUtil.LIBRARY_LOCK) {
/* 72 */           MemoryStack stack = MemoryStack.stackPush();
/*    */           try {
/* 73 */             PointerBuffer faceBuffer = stack.mallocPointer(1);
/* 74 */             FreeTypeUtil.assertError(FreeType.FT_New_Memory_Face(FreeTypeUtil.getLibrary(), fontData, 0L, faceBuffer), "Initializing font face");
/* 75 */             face = FT_Face.create(faceBuffer.get());
/* 76 */             if (stack != null)
/* 76 */               stack.close(); 
/*    */           } catch (Throwable throwable) {
/*    */             if (stack != null)
/*    */               try {
/*    */                 stack.close();
/*    */               } catch (Throwable throwable1) {
/*    */                 throwable.addSuppressed(throwable1);
/*    */               }  
/*    */             throw throwable;
/*    */           } 
/* 78 */           String format = FreeType.FT_Get_Font_Format(face);
/* 79 */           if (!"TrueType".equals(format))
/* 80 */             throw new IOException("Font is not in TTF format, was " + format); 
/* 83 */           FreeTypeUtil.assertError(FreeType.FT_Select_Charmap(face, FreeType.FT_ENCODING_UNICODE), "Find unicode charmap");
/* 85 */           TrueTypeGlyphProvider trueTypeGlyphProvider = new TrueTypeGlyphProvider(fontData, face, this.size, this.oversample, this.shift.x, this.shift.y, this.skip);
/* 87 */           if (resource != null)
/* 87 */             resource.close(); 
/*    */           return (GlyphProvider)trueTypeGlyphProvider;
/*    */         } 
/*    */       } catch (Throwable throwable) {
/*    */         if (resource != null)
/*    */           try {
/*    */             resource.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 87 */     } catch (Exception ex) {
/* 88 */       synchronized (FreeTypeUtil.LIBRARY_LOCK) {
/* 89 */         if (face != null)
/* 90 */           FreeType.FT_Done_Face(face); 
/*    */       } 
/* 93 */       MemoryUtil.memFree(fontData);
/* 94 */       throw ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\TrueTypeGlyphProviderDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */