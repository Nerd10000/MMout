/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.ParseResults;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.commands.execution.tasks.BuildContexts;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public interface CommandFunction<T> {
/*     */   Identifier id();
/*     */   
/*     */   InstantiatedFunction<T> instantiate(CompoundTag paramCompoundTag, CommandDispatcher<T> paramCommandDispatcher) throws FunctionInstantiationException;
/*     */   
/*     */   private static boolean shouldConcatenateNextLine(CharSequence line) {
/*  29 */     int length = line.length();
/*  30 */     return (length > 0 && line.charAt(length - 1) == '\\');
/*     */   }
/*     */   
/*     */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> CommandFunction<T> fromLines(Identifier id, CommandDispatcher<T> dispatcher, T compilationContext, List<String> lines) {
/*  34 */     FunctionBuilder<T> functionBuilder = new FunctionBuilder<>();
/*  35 */     int i = 0;
/*     */     while (true) {
/*  35 */       if (i < lines.size()) {
/*  36 */         int lineNumber = i + 1;
/*  39 */         String inputLine = ((String)lines.get(i)).trim();
/*  41 */         if (shouldConcatenateNextLine(inputLine)) {
/*  42 */           StringBuilder builder = new StringBuilder(inputLine);
/*     */           for (;; i++) {
/*  44 */             i++;
/*  45 */             if (i == lines.size())
/*  46 */               throw new IllegalArgumentException("Line continuation at end of file"); 
/*  48 */             builder.deleteCharAt(builder.length() - 1);
/*  49 */             String innerLine = ((String)lines.get(i)).trim();
/*  50 */             builder.append(innerLine);
/*  51 */             checkCommandLineLength(builder);
/*  52 */             if (!shouldConcatenateNextLine(builder)) {
/*  53 */               String line = builder.toString();
/*  58 */               checkCommandLineLength(line);
/*  60 */               StringReader input = new StringReader(line);
/*  62 */               if (!input.canRead() || input.peek() == '#')
/*     */                 break; 
/*  66 */               if (input.peek() == '/') {
/*  67 */                 input.skip();
/*  68 */                 if (input.peek() == '/')
/*  69 */                   throw new IllegalArgumentException("Unknown or invalid command '" + line + "' on line " + lineNumber + " (if you intended to make a comment, use '#' not '//')"); 
/*  71 */                 String name = input.readUnquotedString();
/*  72 */                 throw new IllegalArgumentException("Unknown or invalid command '" + line + "' on line " + lineNumber + " (did you mean '" + name + "'? Do not use a preceding forwards slash.)");
/*     */               } 
/*  74 */               if (input.peek() == '$') {
/*  76 */                 functionBuilder.addMacro(line.substring(1), lineNumber, compilationContext);
/*     */               } else {
/*     */                 try {
/*  79 */                   functionBuilder.addCommand(parseCommand(dispatcher, compilationContext, input));
/*  80 */                 } catch (CommandSyntaxException e) {
/*  81 */                   throw new IllegalArgumentException("Whilst parsing command on line " + lineNumber + ": " + e.getMessage());
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               continue;
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           String line = inputLine;
/*     */           continue;
/*     */         } 
/*     */       } else {
/*     */         break;
/*     */       } 
/*     */       i++;
/*     */     } 
/*  86 */     return functionBuilder.build(id);
/*     */   }
/*     */   
/*     */   static void checkCommandLineLength(CharSequence line) {
/*  90 */     if (line.length() > 2000000) {
/*  91 */       CharSequence truncated = line.subSequence(0, Math.min(512, 2000000));
/*  92 */       throw new IllegalStateException("Command too long: " + line.length() + " characters, contents: " + String.valueOf(truncated) + "...");
/*     */     } 
/*     */   }
/*     */   
/*     */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> UnboundEntryAction<T> parseCommand(CommandDispatcher<T> dispatcher, T compilationContext, StringReader input) throws CommandSyntaxException {
/*  98 */     ParseResults<T> parse = dispatcher.parse(input, compilationContext);
/*  99 */     Commands.validateParseResults(parse);
/* 101 */     Optional<ContextChain<T>> commandChain = ContextChain.tryFlatten(parse.getContext().build(input.getString()));
/* 102 */     if (commandChain.isEmpty())
/* 103 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader()); 
/* 105 */     return (UnboundEntryAction<T>)new BuildContexts.Unbound(input.getString(), commandChain.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\CommandFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */