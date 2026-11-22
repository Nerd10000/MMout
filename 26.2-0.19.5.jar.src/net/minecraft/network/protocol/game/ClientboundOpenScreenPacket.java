/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.inventory.MenuType;
/*    */ 
/*    */ public class ClientboundOpenScreenPacket implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenScreenPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundOpenScreenPacket::getContainerId, 
/*    */       
/* 16 */       ByteBufCodecs.registry(Registries.MENU), ClientboundOpenScreenPacket::getType, ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundOpenScreenPacket::getTitle, ClientboundOpenScreenPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   
/*    */   private final MenuType<?> type;
/*    */   
/*    */   private final Component title;
/*    */   
/*    */   public ClientboundOpenScreenPacket(int containerId, MenuType<?> type, Component title) {
/* 26 */     this.containerId = containerId;
/* 27 */     this.type = type;
/* 28 */     this.title = title;
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundOpenScreenPacket> type() {
/* 33 */     return GamePacketTypes.CLIENTBOUND_OPEN_SCREEN;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 38 */     listener.handleOpenScreen(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 42 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public MenuType<?> getType() {
/* 46 */     return this.type;
/*    */   }
/*    */   
/*    */   public Component getTitle() {
/* 50 */     return this.title;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundOpenScreenPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */