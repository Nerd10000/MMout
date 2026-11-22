/*    */ package net.minecraft.world.entity.animal.sniffer;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum State {
/* 79 */   IDLING(0),
/* 80 */   FEELING_HAPPY(1),
/* 81 */   SCENTING(2),
/* 82 */   SNIFFING(3),
/* 83 */   SEARCHING(4),
/* 84 */   DIGGING(5),
/* 85 */   RISING(6);
/*    */   
/*    */   public static final IntFunction<State> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, State> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 88 */     BY_ID = ByIdMap.continuous(State::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 90 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, State::id);
/*    */   }
/*    */   
/*    */   State(int id) {
/* 95 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 99 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\Sniffer$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */