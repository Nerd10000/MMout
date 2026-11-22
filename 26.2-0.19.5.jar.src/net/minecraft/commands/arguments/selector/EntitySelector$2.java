/*    */ package net.minecraft.commands.arguments.selector;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.util.CompilableString;
/*    */ 
/*    */ class null extends CompilableString.CommandParserHelper<EntitySelector> {
/*    */   protected EntitySelector parse(StringReader reader) throws CommandSyntaxException {
/* 51 */     return (new EntitySelectorParser(reader, true)).parse();
/*    */   }
/*    */   
/*    */   protected String errorMessage(String original, CommandSyntaxException exception) {
/* 56 */     return "Invalid selector component: " + original + ": " + exception.getMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\EntitySelector$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */