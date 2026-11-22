/*     */ package net.minecraft.commands;
/*     */ 
/*     */ import com.mojang.brigadier.suggestion.SuggestionProvider;
/*     */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*     */ import com.mojang.brigadier.tree.CommandNode;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ 
/*     */ class null implements ClientboundCommandsPacket.NodeInspector<CommandSourceStack> {
/* 185 */   private final CommandSourceStack noPermissionSource = Commands.createCompilationContext(PermissionSet.NO_PERMISSIONS);
/*     */   
/*     */   public Identifier suggestionId(ArgumentCommandNode<CommandSourceStack, ?> node) {
/* 189 */     SuggestionProvider<CommandSourceStack> suggestionProvider = node.getCustomSuggestions();
/* 190 */     return (suggestionProvider != null) ? SuggestionProviders.getName(suggestionProvider) : null;
/*     */   }
/*     */   
/*     */   public boolean isExecutable(CommandNode<CommandSourceStack> node) {
/* 195 */     return (node.getCommand() != null);
/*     */   }
/*     */   
/*     */   public boolean isRestricted(CommandNode<CommandSourceStack> node) {
/* 200 */     Predicate<CommandSourceStack> requirement = node.getRequirement();
/* 203 */     return !requirement.test(this.noPermissionSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\Commands$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */