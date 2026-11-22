/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*    */ import net.minecraft.world.waypoints.WaypointManager;
/*    */ 
/*    */ enum Operation {
/* 73 */   TRACK(WaypointManager::trackWaypoint),
/* 74 */   UNTRACK(WaypointManager::untrackWaypoint),
/* 75 */   UPDATE(WaypointManager::updateWaypoint);
/*    */   
/*    */   private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
/*    */   
/*    */   public static final IntFunction<Operation> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC;
/*    */   
/*    */   static {
/* 80 */     BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 81 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*    */   }
/*    */   
/*    */   Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) {
/* 84 */     this.action = action;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundTrackedWaypointPacket$Operation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */