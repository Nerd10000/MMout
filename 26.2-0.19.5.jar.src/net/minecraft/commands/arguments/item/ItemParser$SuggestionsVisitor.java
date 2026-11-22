/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.suggestion.Suggestions;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class SuggestionsVisitor implements ItemParser.Visitor {
/* 267 */   private Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions = ItemParser.SUGGEST_NOTHING;
/*     */   
/*     */   public void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
/* 271 */     this.suggestions = suggestions;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Suggestions> resolveSuggestions(SuggestionsBuilder builder, StringReader reader) {
/* 275 */     return this.suggestions.apply(builder.createOffset(reader.getCursor()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemParser$SuggestionsVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */