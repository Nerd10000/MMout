/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class Ints implements RangeArgument<MinMaxBounds.Ints> {
/* 15 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5", "0", "-5", "-100..", "..100" });
/*    */   
/*    */   public static MinMaxBounds.Ints getRange(CommandContext<CommandSourceStack> context, String name) {
/* 18 */     return (MinMaxBounds.Ints)context.getArgument(name, MinMaxBounds.Ints.class);
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Ints parse(StringReader reader) throws CommandSyntaxException {
/* 23 */     return MinMaxBounds.Ints.fromReader(reader);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 28 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\RangeArgument$Ints.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */