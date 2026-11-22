package net.minecraft.network.protocol.game;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.resources.Identifier;

public interface NodeInspector<S> {
  Identifier suggestionId(ArgumentCommandNode<S, ?> paramArgumentCommandNode);
  
  boolean isExecutable(CommandNode<S> paramCommandNode);
  
  boolean isRestricted(CommandNode<S> paramCommandNode);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeInspector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */