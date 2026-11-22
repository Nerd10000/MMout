/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.UUID;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*    */ import net.minecraft.world.waypoints.Waypoint;
/*    */ import net.minecraft.world.waypoints.WaypointManager;
/*    */ 
/*    */ public final class ClientboundTrackedWaypointPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Operation operation;
/*    */   
/*    */   private final TrackedWaypoint waypoint;
/*    */   
/*    */   public ClientboundTrackedWaypointPacket(Operation operation, TrackedWaypoint waypoint) {
/* 20 */     this.operation = operation;
/* 20 */     this.waypoint = waypoint;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTrackedWaypointPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Operation operation() {
/* 20 */     return this.operation;
/*    */   }
/*    */   
/*    */   public TrackedWaypoint waypoint() {
/* 20 */     return this.waypoint;
/*    */   }
/*    */   
/* 24 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTrackedWaypointPacket> STREAM_CODEC = StreamCodec.composite(Operation.STREAM_CODEC, ClientboundTrackedWaypointPacket::operation, TrackedWaypoint.STREAM_CODEC, ClientboundTrackedWaypointPacket::waypoint, ClientboundTrackedWaypointPacket::new);
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket removeWaypoint(UUID identifier) {
/* 31 */     return new ClientboundTrackedWaypointPacket(Operation.UNTRACK, TrackedWaypoint.empty(identifier));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket addWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) {
/* 35 */     return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setPosition(identifier, icon, position));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket updateWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) {
/* 39 */     return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setPosition(identifier, icon, position));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket addWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) {
/* 43 */     return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setChunk(identifier, icon, chunk));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket updateWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) {
/* 47 */     return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setChunk(identifier, icon, chunk));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket addWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) {
/* 51 */     return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setAzimuth(identifier, icon, angle));
/*    */   }
/*    */   
/*    */   public static ClientboundTrackedWaypointPacket updateWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) {
/* 55 */     return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setAzimuth(identifier, icon, angle));
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundTrackedWaypointPacket> type() {
/* 60 */     return GamePacketTypes.CLIENTBOUND_WAYPOINT;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 65 */     listener.handleWaypoint(this);
/*    */   }
/*    */   
/*    */   public void apply(TrackedWaypointManager manager) {
/* 69 */     this.operation.action.accept(manager, this.waypoint);
/*    */   }
/*    */   
/*    */   private enum Operation {
/* 73 */     TRACK((String)WaypointManager::trackWaypoint),
/* 74 */     UNTRACK((String)WaypointManager::untrackWaypoint),
/* 75 */     UPDATE((String)WaypointManager::updateWaypoint);
/*    */     
/*    */     private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
/*    */     
/* 80 */     public static final IntFunction<Operation> BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*    */     
/* 81 */     public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) {
/* 84 */       this.action = action;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTrackedWaypointPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */