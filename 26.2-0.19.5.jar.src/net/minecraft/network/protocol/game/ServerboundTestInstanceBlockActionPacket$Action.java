/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum Action {
/* 47 */   INIT(0),
/* 48 */   QUERY(1),
/* 49 */   SET(2),
/* 50 */   RESET(3),
/* 51 */   SAVE(4),
/* 52 */   EXPORT(5),
/* 53 */   RUN(6);
/*    */   
/*    */   private static final IntFunction<Action> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Action> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 55 */     BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 56 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   
/*    */   Action(int id) {
/* 61 */     this.id = id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundTestInstanceBlockActionPacket$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */