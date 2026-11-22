/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.RedirectModifier;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public interface ModifierAdapter<T> extends CustomModifierExecutor<T>, RedirectModifier<T> {
/*    */   default Collection<T> apply(CommandContext<T> context) throws CommandSyntaxException {
/* 17 */     throw new UnsupportedOperationException("This function should not run");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\CustomModifierExecutor$ModifierAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */