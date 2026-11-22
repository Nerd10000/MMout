/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*    */ 
/*    */ public final class ClientboundRecipeBookRemovePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final List<RecipeDisplayId> recipes;
/*    */   
/*    */   public ClientboundRecipeBookRemovePacket(List<RecipeDisplayId> recipes) {
/* 12 */     this.recipes = recipes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRecipeBookRemovePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<RecipeDisplayId> recipes() {
/* 12 */     return this.recipes;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, ClientboundRecipeBookRemovePacket> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs.list()), ClientboundRecipeBookRemovePacket::recipes, ClientboundRecipeBookRemovePacket::new);
/*    */   
/*    */   public PacketType<ClientboundRecipeBookRemovePacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_REMOVE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleRecipeBookRemove(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundRecipeBookRemovePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */