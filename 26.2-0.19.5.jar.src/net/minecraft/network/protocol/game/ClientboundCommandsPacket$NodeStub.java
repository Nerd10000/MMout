package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

interface NodeStub {
  <S> ArgumentBuilder<S, ?> build(CommandBuildContext paramCommandBuildContext, ClientboundCommandsPacket.NodeBuilder<S> paramNodeBuilder);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeStub.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */