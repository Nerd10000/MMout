/*    */ package net.minecraft.network.chat.contents.data;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.util.CompilableString;
/*    */ 
/*    */ class null extends CompilableString.CommandParserHelper<Coordinates> {
/*    */   protected Coordinates parse(StringReader reader) throws CommandSyntaxException {
/* 23 */     return BlockPosArgument.blockPos().parse(reader);
/*    */   }
/*    */   
/*    */   protected String errorMessage(String original, CommandSyntaxException exception) {
/* 28 */     return "Invalid coordinates path: " + original + ": " + exception.getMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\data\BlockDataSource$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */