/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class HexColorArgument implements ArgumentType<Integer> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "F00", "FF0000" });
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_HEX;
/*    */   
/*    */   static {
/* 21 */     ERROR_INVALID_HEX = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.hexcolor.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   public static HexColorArgument hexColor() {
/* 27 */     return new HexColorArgument();
/*    */   }
/*    */   
/*    */   public static Integer getHexColor(CommandContext<CommandSourceStack> context, String name) {
/* 31 */     return (Integer)context.getArgument(name, Integer.class);
/*    */   }
/*    */   
/*    */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/* 36 */     String colorString = reader.readUnquotedString();
/* 38 */     switch (colorString.length()) {
/*    */       case 3:
/*    */       
/*    */       case 6:
/*    */       
/*    */     } 
/* 49 */     throw ERROR_INVALID_HEX.createWithContext(reader, colorString);
/*    */   }
/*    */   
/*    */   private static int duplicateDigit(int digit) {
/* 54 */     return digit * 17;
/*    */   }
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> contextBuilder, SuggestionsBuilder builder) {
/* 59 */     return SharedSuggestionProvider.suggest(EXAMPLES, builder);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 64 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\HexColorArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */