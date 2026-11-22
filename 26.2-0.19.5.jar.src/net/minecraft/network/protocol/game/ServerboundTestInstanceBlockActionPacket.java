/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gametest.framework.GameTestInstance;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ 
/*    */ public final class ServerboundTestInstanceBlockActionPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final Action action;
/*    */   
/*    */   private final TestInstanceBlockEntity.Data data;
/*    */   
/*    */   public ServerboundTestInstanceBlockActionPacket(BlockPos pos, Action action, TestInstanceBlockEntity.Data data) {
/* 20 */     this.pos = pos;
/* 20 */     this.action = action;
/* 20 */     this.data = data;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundTestInstanceBlockActionPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 20 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Action action() {
/* 20 */     return this.action;
/*    */   }
/*    */   
/*    */   public TestInstanceBlockEntity.Data data() {
/* 20 */     return this.data;
/*    */   }
/*    */   
/* 25 */   public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundTestInstanceBlockActionPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::pos, Action.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::action, TestInstanceBlockEntity.Data.STREAM_CODEC, ServerboundTestInstanceBlockActionPacket::data, ServerboundTestInstanceBlockActionPacket::new);
/*    */   
/*    */   public ServerboundTestInstanceBlockActionPacket(BlockPos pos, Action action, Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities) {
/* 33 */     this(pos, action, new TestInstanceBlockEntity.Data(test, size, rotation, ignoreEntities, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundTestInstanceBlockActionPacket> type() {
/* 38 */     return GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 43 */     listener.handleTestInstanceBlockAction(this);
/*    */   }
/*    */   
/*    */   public enum Action {
/* 47 */     INIT(0),
/* 48 */     QUERY(1),
/* 49 */     SET(2),
/* 50 */     RESET(3),
/* 51 */     SAVE(4),
/* 52 */     EXPORT(5),
/* 53 */     RUN(6);
/*    */     
/*    */     private static final IntFunction<Action> BY_ID;
/*    */     
/*    */     public static final StreamCodec<ByteBuf, Action> STREAM_CODEC;
/*    */     
/*    */     private final int id;
/*    */     
/*    */     static {
/* 55 */       BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 56 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */     }
/*    */     
/*    */     Action(int id) {
/* 61 */       this.id = id;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundTestInstanceBlockActionPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */