/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*     */ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements ClientboundCommandsPacket.NodeBuilder<ClientSuggestionProvider> {
/*     */   public ArgumentBuilder<ClientSuggestionProvider, ?> createLiteral(String id) {
/* 378 */     return (ArgumentBuilder<ClientSuggestionProvider, ?>)LiteralArgumentBuilder.literal(id);
/*     */   }
/*     */   
/*     */   public ArgumentBuilder<ClientSuggestionProvider, ?> createArgument(String id, ArgumentType<?> argumentType, Identifier suggestionId) {
/* 383 */     RequiredArgumentBuilder<ClientSuggestionProvider, ?> builder = RequiredArgumentBuilder.argument(id, argumentType);
/* 384 */     if (suggestionId != null)
/* 385 */       builder.suggests(SuggestionProviders.getProvider(suggestionId)); 
/* 387 */     return (ArgumentBuilder<ClientSuggestionProvider, ?>)builder;
/*     */   }
/*     */   
/*     */   public ArgumentBuilder<ClientSuggestionProvider, ?> configure(ArgumentBuilder<ClientSuggestionProvider, ?> builder, boolean executable, boolean restricted) {
/* 392 */     if (executable)
/* 393 */       builder.executes(c -> 0); 
/* 395 */     if (restricted)
/* 396 */       builder.requires((Predicate)Commands.hasPermission(ClientPacketListener.RESTRICTED_COMMAND_CHECK)); 
/* 398 */     return builder;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientPacketListener$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */