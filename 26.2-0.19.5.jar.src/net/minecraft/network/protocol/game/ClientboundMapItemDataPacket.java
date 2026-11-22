/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.level.saveddata.maps.MapDecoration;
/*    */ import net.minecraft.world.level.saveddata.maps.MapId;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public final class ClientboundMapItemDataPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final MapId mapId;
/*    */   
/*    */   private final byte scale;
/*    */   
/*    */   private final boolean locked;
/*    */   
/*    */   private final Optional<List<MapDecoration>> decorations;
/*    */   
/*    */   private final Optional<MapItemSavedData.MapPatch> colorPatch;
/*    */   
/*    */   public ClientboundMapItemDataPacket(MapId mapId, byte scale, boolean locked, Optional<List<MapDecoration>> decorations, Optional<MapItemSavedData.MapPatch> colorPatch) {
/* 17 */     this.mapId = mapId;
/* 17 */     this.scale = scale;
/* 17 */     this.locked = locked;
/* 17 */     this.decorations = decorations;
/* 17 */     this.colorPatch = colorPatch;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MapId mapId() {
/* 17 */     return this.mapId;
/*    */   }
/*    */   
/*    */   public byte scale() {
/* 17 */     return this.scale;
/*    */   }
/*    */   
/*    */   public boolean locked() {
/* 17 */     return this.locked;
/*    */   }
/*    */   
/*    */   public Optional<List<MapDecoration>> decorations() {
/* 17 */     return this.decorations;
/*    */   }
/*    */   
/*    */   public Optional<MapItemSavedData.MapPatch> colorPatch() {
/* 17 */     return this.colorPatch;
/*    */   }
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMapItemDataPacket> STREAM_CODEC = StreamCodec.composite(MapId.STREAM_CODEC, ClientboundMapItemDataPacket::mapId, ByteBufCodecs.BYTE, ClientboundMapItemDataPacket::scale, ByteBufCodecs.BOOL, ClientboundMapItemDataPacket::locked, MapDecoration.STREAM_CODEC
/*    */       
/* 29 */       .apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), ClientboundMapItemDataPacket::decorations, MapItemSavedData.MapPatch.STREAM_CODEC, ClientboundMapItemDataPacket::colorPatch, ClientboundMapItemDataPacket::new);
/*    */   
/*    */   public ClientboundMapItemDataPacket(MapId mapId, byte scale, boolean locked, Collection<MapDecoration> decorations, MapItemSavedData.MapPatch colorPatch) {
/* 35 */     this(mapId, scale, locked, (decorations != null) ? Optional.<List<MapDecoration>>of(List.copyOf(decorations)) : Optional.<List<MapDecoration>>empty(), Optional.ofNullable(colorPatch));
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundMapItemDataPacket> type() {
/* 40 */     return GamePacketTypes.CLIENTBOUND_MAP_ITEM_DATA;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 45 */     listener.handleMapItemData(this);
/*    */   }
/*    */   
/*    */   public void applyToMap(MapItemSavedData map) {
/* 49 */     Objects.requireNonNull(map);
/* 49 */     this.decorations.ifPresent(map::addClientSideDecorations);
/* 50 */     this.colorPatch.ifPresent(patch -> patch.applyToMap(map));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundMapItemDataPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */