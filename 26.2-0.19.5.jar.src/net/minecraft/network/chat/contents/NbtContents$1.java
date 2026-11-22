/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.util.CompilableString;
/*    */ 
/*    */ class null extends CompilableString.CommandParserHelper<NbtPathArgument.NbtPath> {
/*    */   protected NbtPathArgument.NbtPath parse(StringReader reader) throws CommandSyntaxException {
/* 43 */     return (new NbtPathArgument()).parse(reader);
/*    */   }
/*    */   
/*    */   protected String errorMessage(String original, CommandSyntaxException exception) {
/* 48 */     return "Invalid NBT path: " + original + ": " + exception.getMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\NbtContents$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */