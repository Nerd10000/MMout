/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*    */ 
/*    */ public final class ServerboundPlaceRecipePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   
/*    */   private final RecipeDisplayId recipe;
/*    */   
/*    */   private final boolean useMaxItems;
/*    */   
/*    */   public ServerboundPlaceRecipePacket(int containerId, RecipeDisplayId recipe, boolean useMaxItems) {
/* 10 */     this.containerId = containerId;
/* 10 */     this.recipe = recipe;
/* 10 */     this.useMaxItems = useMaxItems;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int containerId() {
/* 10 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public RecipeDisplayId recipe() {
/* 10 */     return this.recipe;
/*    */   }
/*    */   
/*    */   public boolean useMaxItems() {
/* 10 */     return this.useMaxItems;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlaceRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundPlaceRecipePacket::containerId, RecipeDisplayId.STREAM_CODEC, ServerboundPlaceRecipePacket::recipe, ByteBufCodecs.BOOL, ServerboundPlaceRecipePacket::useMaxItems, ServerboundPlaceRecipePacket::new);
/*    */   
/*    */   public PacketType<ServerboundPlaceRecipePacket> type() {
/* 24 */     return GamePacketTypes.SERVERBOUND_PLACE_RECIPE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 29 */     listener.handlePlaceRecipe(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundPlaceRecipePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */