/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.inventory.ContainerInput;
/*    */ 
/*    */ public final class ServerboundContainerClickPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int containerId;
/*    */   
/*    */   private final int stateId;
/*    */   
/*    */   private final short slotNum;
/*    */   
/*    */   private final byte buttonNum;
/*    */   
/*    */   private final ContainerInput containerInput;
/*    */   
/*    */   private final Int2ObjectMap<HashedStack> changedSlots;
/*    */   
/*    */   private final HashedStack carriedItem;
/*    */   
/*    */   private static final int MAX_SLOT_COUNT = 128;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int containerId() {
/* 14 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int stateId() {
/* 14 */     return this.stateId;
/*    */   }
/*    */   
/*    */   public short slotNum() {
/* 14 */     return this.slotNum;
/*    */   }
/*    */   
/*    */   public byte buttonNum() {
/* 14 */     return this.buttonNum;
/*    */   }
/*    */   
/*    */   public ContainerInput containerInput() {
/* 14 */     return this.containerInput;
/*    */   }
/*    */   
/*    */   public Int2ObjectMap<HashedStack> changedSlots() {
/* 14 */     return this.changedSlots;
/*    */   }
/*    */   
/*    */   public HashedStack carriedItem() {
/* 14 */     return this.carriedItem;
/*    */   }
/*    */   
/* 25 */   private static final StreamCodec<RegistryFriendlyByteBuf, Int2ObjectMap<HashedStack>> SLOTS_STREAM_CODEC = ByteBufCodecs.map(it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap::new, ByteBufCodecs.SHORT
/*    */       
/* 27 */       .map(Short::intValue, Integer::shortValue), HashedStack.STREAM_CODEC, 128);
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundContainerClickPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundContainerClickPacket::containerId, ByteBufCodecs.VAR_INT, ServerboundContainerClickPacket::stateId, ByteBufCodecs.SHORT, ServerboundContainerClickPacket::slotNum, ByteBufCodecs.BYTE, ServerboundContainerClickPacket::buttonNum, ContainerInput.STREAM_CODEC, ServerboundContainerClickPacket::containerInput, SLOTS_STREAM_CODEC, ServerboundContainerClickPacket::changedSlots, HashedStack.STREAM_CODEC, ServerboundContainerClickPacket::carriedItem, ServerboundContainerClickPacket::new);
/*    */   
/*    */   public ServerboundContainerClickPacket(int containerId, int stateId, short slotNum, byte buttonNum, ContainerInput containerInput, Int2ObjectMap<HashedStack> changedSlots, HashedStack carriedItem) {
/* 44 */     changedSlots = Int2ObjectMaps.unmodifiable(changedSlots);
/*    */     this.containerId = containerId;
/*    */     this.stateId = stateId;
/*    */     this.slotNum = slotNum;
/*    */     this.buttonNum = buttonNum;
/*    */     this.containerInput = containerInput;
/*    */     this.changedSlots = changedSlots;
/*    */     this.carriedItem = carriedItem;
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundContainerClickPacket> type() {
/* 49 */     return GamePacketTypes.SERVERBOUND_CONTAINER_CLICK;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 54 */     listener.handleContainerClick(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundContainerClickPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */