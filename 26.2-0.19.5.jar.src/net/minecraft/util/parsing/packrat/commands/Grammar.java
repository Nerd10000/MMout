/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.util.parsing.packrat.DelayedException;
/*    */ import net.minecraft.util.parsing.packrat.Dictionary;
/*    */ import net.minecraft.util.parsing.packrat.ErrorCollector;
/*    */ import net.minecraft.util.parsing.packrat.ErrorEntry;
/*    */ import net.minecraft.util.parsing.packrat.NamedRule;
/*    */ import net.minecraft.util.parsing.packrat.ParseState;
/*    */ import net.minecraft.util.parsing.packrat.SuggestionSupplier;
/*    */ 
/*    */ public final class Grammar<T> extends Record implements CommandArgumentParser<T> {
/*    */   private final Dictionary<StringReader> rules;
/*    */   
/*    */   private final NamedRule<StringReader, T> top;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/commands/Grammar;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/commands/Grammar<TT;>;
/*    */   }
/*    */   
/*    */   public Dictionary<StringReader> rules() {
/* 20 */     return this.rules;
/*    */   }
/*    */   
/*    */   public NamedRule<StringReader, T> top() {
/* 20 */     return this.top;
/*    */   }
/*    */   
/*    */   public Grammar(Dictionary<StringReader> rules, NamedRule<StringReader, T> top) {
/* 22 */     rules.checkAllBound();
/*    */     this.rules = rules;
/*    */     this.top = top;
/*    */   }
/*    */   
/*    */   public Optional<T> parse(ParseState<StringReader> state) {
/* 26 */     return state.parseTopRule(this.top);
/*    */   }
/*    */   
/*    */   public T parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 31 */     ErrorCollector.LongestOnly<StringReader> errorCollector = new ErrorCollector.LongestOnly();
/* 32 */     StringReaderParserState state = new StringReaderParserState((ErrorCollector<StringReader>)errorCollector, reader);
/* 33 */     Optional<T> result = parse((ParseState<StringReader>)state);
/* 34 */     if (result.isPresent())
/* 35 */       return result.get(); 
/* 39 */     List<ErrorEntry<StringReader>> errorEntries = errorCollector.entries();
/* 46 */     List<Exception> exceptions = errorEntries.stream().mapMulti((entry, output) -> {
/*    */           Object patt0$temp = entry.reason();
/*    */           if (patt0$temp instanceof DelayedException) {
/*    */             DelayedException<?> delayedException = (DelayedException)patt0$temp;
/*    */             output.accept(delayedException.create(reader.getString(), entry.cursor()));
/*    */           } else {
/*    */             Object patt1$temp = entry.reason();
/*    */             if (patt1$temp instanceof Exception) {
/*    */               Exception exception = (Exception)patt1$temp;
/*    */               output.accept(exception);
/*    */             } 
/*    */           } 
/* 46 */         }).toList();
/* 49 */     for (Exception exception : exceptions) {
/* 50 */       if (exception instanceof CommandSyntaxException) {
/* 50 */         CommandSyntaxException cse = (CommandSyntaxException)exception;
/* 51 */         throw cse;
/*    */       } 
/*    */     } 
/* 56 */     if (exceptions.size() == 1) {
/* 56 */       RuntimeException runtimeException = (RuntimeException)exceptions.get(0);
/* 56 */       if (runtimeException instanceof RuntimeException) {
/* 56 */         RuntimeException re = runtimeException;
/* 57 */         throw re;
/*    */       } 
/*    */     } 
/* 60 */     throw new IllegalStateException("Failed to parse: " + (String)errorEntries.stream().map(ErrorEntry::toString).collect(Collectors.joining(", ")));
/*    */   }
/*    */   
/*    */   public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 65 */     StringReader reader = new StringReader(suggestionsBuilder.getInput());
/* 66 */     reader.setCursor(suggestionsBuilder.getStart());
/* 68 */     ErrorCollector.LongestOnly<StringReader> errorCollector = new ErrorCollector.LongestOnly();
/* 69 */     StringReaderParserState state = new StringReaderParserState((ErrorCollector<StringReader>)errorCollector, reader);
/* 70 */     parse((ParseState<StringReader>)state);
/* 72 */     List<ErrorEntry<StringReader>> errorEntries = errorCollector.entries();
/* 73 */     if (errorEntries.isEmpty())
/* 74 */       return suggestionsBuilder.buildFuture(); 
/* 78 */     SuggestionsBuilder offsetBuilder = suggestionsBuilder.createOffset(errorCollector.cursor());
/* 79 */     for (ErrorEntry<StringReader> entry : errorEntries) {
/* 80 */       SuggestionSupplier suggestionSupplier = entry.suggestions();
/* 80 */       if (suggestionSupplier instanceof ResourceSuggestion) {
/* 80 */         ResourceSuggestion resourceSuggestionTerm = (ResourceSuggestion)suggestionSupplier;
/* 82 */         SharedSuggestionProvider.suggestResource(resourceSuggestionTerm.possibleResources(), offsetBuilder);
/*    */         continue;
/*    */       } 
/* 84 */       SharedSuggestionProvider.suggest(entry.suggestions().possibleValues((ParseState)state), offsetBuilder);
/*    */     } 
/* 87 */     return offsetBuilder.buildFuture();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\Grammar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */