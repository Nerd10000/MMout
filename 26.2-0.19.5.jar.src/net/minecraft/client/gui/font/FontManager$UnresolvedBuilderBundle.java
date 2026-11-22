/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.DependencySorter;
/*     */ 
/*     */ final class UnresolvedBuilderBundle extends Record implements DependencySorter.Entry<Identifier> {
/*     */   private final Identifier fontId;
/*     */   
/*     */   private final List<FontManager.BuilderResult> builders;
/*     */   
/*     */   private final Set<Identifier> dependencies;
/*     */   
/*     */   private UnresolvedBuilderBundle(Identifier fontId, List<FontManager.BuilderResult> builders, Set<Identifier> dependencies) {
/* 119 */     this.fontId = fontId;
/* 119 */     this.builders = builders;
/* 119 */     this.dependencies = dependencies;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #119	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier fontId() {
/* 119 */     return this.fontId;
/*     */   }
/*     */   
/*     */   public List<FontManager.BuilderResult> builders() {
/* 119 */     return this.builders;
/*     */   }
/*     */   
/*     */   public Set<Identifier> dependencies() {
/* 119 */     return this.dependencies;
/*     */   }
/*     */   
/*     */   public UnresolvedBuilderBundle(Identifier fontId) {
/* 125 */     this(fontId, new ArrayList<>(), new HashSet<>());
/*     */   }
/*     */   
/*     */   public void add(FontManager.BuilderId builderId, FontOption.Filter filter, GlyphProviderDefinition.Reference reference) {
/* 133 */     this.builders.add(new FontManager.BuilderResult(builderId, filter, Either.right(reference.id())));
/* 134 */     this.dependencies.add(reference.id());
/*     */   }
/*     */   
/*     */   public void add(FontManager.BuilderId builderId, FontOption.Filter filter, CompletableFuture<Optional<GlyphProvider>> provider) {
/* 138 */     this.builders.add(new FontManager.BuilderResult(builderId, filter, Either.left(provider)));
/*     */   }
/*     */   
/*     */   private Stream<CompletableFuture<Optional<GlyphProvider>>> listBuilders() {
/* 142 */     return this.builders.stream().flatMap(e -> e.result.left().stream());
/*     */   }
/*     */   
/*     */   public Optional<List<GlyphProvider.Conditional>> resolve(Function<Identifier, List<GlyphProvider.Conditional>> resolver) {
/* 146 */     List<GlyphProvider.Conditional> resolved = new ArrayList<>();
/* 147 */     for (FontManager.BuilderResult builder : this.builders) {
/* 148 */       Optional<List<GlyphProvider.Conditional>> resolvedBuilder = builder.resolve(resolver);
/* 149 */       if (resolvedBuilder.isPresent()) {
/* 150 */         resolved.addAll(resolvedBuilder.get());
/*     */         continue;
/*     */       } 
/* 152 */       return Optional.empty();
/*     */     } 
/* 155 */     return Optional.of(resolved);
/*     */   }
/*     */   
/*     */   public void visitRequiredDependencies(Consumer<Identifier> output) {
/* 160 */     this.dependencies.forEach(output);
/*     */   }
/*     */   
/*     */   public void visitOptionalDependencies(Consumer<Identifier> output) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$UnresolvedBuilderBundle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */