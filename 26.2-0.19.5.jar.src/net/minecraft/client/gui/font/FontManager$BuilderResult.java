/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class BuilderResult extends Record {
/*     */   private final FontManager.BuilderId id;
/*     */   
/*     */   private final FontOption.Filter filter;
/*     */   
/*     */   private final Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result;
/*     */   
/*     */   private BuilderResult(FontManager.BuilderId id, FontOption.Filter filter, Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result) {
/*  98 */     this.id = id;
/*  98 */     this.filter = filter;
/*  98 */     this.result = result;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #98	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #98	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #98	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public FontManager.BuilderId id() {
/*  98 */     return this.id;
/*     */   }
/*     */   
/*     */   public FontOption.Filter filter() {
/*  98 */     return this.filter;
/*     */   }
/*     */   
/*     */   public Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result() {
/*  98 */     return this.result;
/*     */   }
/*     */   
/*     */   public Optional<List<GlyphProvider.Conditional>> resolve(Function<Identifier, List<GlyphProvider.Conditional>> resolver) {
/* 100 */     return (Optional<List<GlyphProvider.Conditional>>)this.result.map(provider -> ((Optional)provider.join()).map(()), reference -> {
/*     */           List<GlyphProvider.Conditional> resolvedReferences = resolver.apply(reference);
/*     */           if (resolvedReferences == null) {
/*     */             FontManager.LOGGER.warn("Can't find font {} referenced by builder {}, either because it's missing, failed to load or is part of loading cycle", reference, this.id);
/*     */             return Optional.empty();
/*     */           } 
/*     */           return Optional.of(resolvedReferences.stream().map(this::mergeFilters).toList());
/*     */         });
/*     */   }
/*     */   
/*     */   private GlyphProvider.Conditional mergeFilters(GlyphProvider.Conditional original) {
/* 115 */     return new GlyphProvider.Conditional(original.provider(), this.filter.merge(original.filter()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$BuilderResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */