/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*    */ 
/*    */ public final class ClientboundPlaceGhostRecipePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int containerId;
/*    */   
/*    */   private final RecipeDisplay recipeDisplay;
/*    */   
/*    */   public ClientboundPlaceGhostRecipePacket(int containerId, RecipeDisplay recipeDisplay) {
/* 10 */     this.containerId = containerId;
/* 10 */     this.recipeDisplay = recipeDisplay;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int containerId() {
/* 10 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public RecipeDisplay recipeDisplay() {
/* 10 */     return this.recipeDisplay;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlaceGhostRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundPlaceGhostRecipePacket::containerId, RecipeDisplay.STREAM_CODEC, ClientboundPlaceGhostRecipePacket::recipeDisplay, ClientboundPlaceGhostRecipePacket::new);
/*    */   
/*    */   public PacketType<ClientboundPlaceGhostRecipePacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_PLACE_GHOST_RECIPE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handlePlaceRecipe(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlaceGhostRecipePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */