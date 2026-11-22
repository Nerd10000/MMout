/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContextBuilder;
/*     */ import com.mojang.brigadier.context.ParsedArgument;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import net.minecraft.client.multiplayer.ClientSuggestionProvider;
/*     */ import net.minecraft.commands.ArgumentVisitor;
/*     */ 
/*     */ class Visitor implements ArgumentVisitor.Output<ClientSuggestionProvider> {
/*     */   private boolean foundMessageArgument;
/*     */   
/*     */   public <T> void accept(CommandContextBuilder<ClientSuggestionProvider> context, ArgumentCommandNode<ClientSuggestionProvider, T> argument, ParsedArgument<ClientSuggestionProvider, T> value) {
/* 271 */     this.foundMessageArgument |= (value != null && value.getResult() instanceof net.minecraft.commands.arguments.MessageArgument.Message) ? 1 : 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CommandSuggestions$1Visitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */