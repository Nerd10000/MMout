/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ 
/*    */ public class ItemArgument implements ArgumentType<ItemInput> {
/* 16 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "stick{foo=bar}" });
/*    */   
/*    */   private final ItemParser parser;
/*    */   
/*    */   public ItemArgument(CommandBuildContext context) {
/* 21 */     this.parser = new ItemParser((HolderLookup.Provider)context);
/*    */   }
/*    */   
/*    */   public static ItemArgument item(CommandBuildContext context) {
/* 25 */     return new ItemArgument(context);
/*    */   }
/*    */   
/*    */   public ItemInput parse(StringReader reader) throws CommandSyntaxException {
/* 30 */     return this.parser.parse(reader);
/*    */   }
/*    */   
/*    */   public static <S> ItemInput getItem(CommandContext<S> context, String name) {
/* 34 */     return (ItemInput)context.getArgument(name, ItemInput.class);
/*    */   }
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 39 */     return this.parser.fillSuggestions(builder);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 44 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */