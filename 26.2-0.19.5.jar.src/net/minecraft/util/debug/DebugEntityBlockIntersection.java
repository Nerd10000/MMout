/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum DebugEntityBlockIntersection {
/* 11 */   IN_BLOCK(0, 1610678016),
/* 12 */   IN_FLUID(1, 1610612991),
/* 13 */   IN_AIR(2, 1613968179);
/*    */   
/*    */   private static final IntFunction<DebugEntityBlockIntersection> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, DebugEntityBlockIntersection> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   static {
/* 16 */     BY_ID = ByIdMap.continuous(i -> i.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 17 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, i -> i.id);
/*    */   }
/*    */   
/*    */   DebugEntityBlockIntersection(int id, int color) {
/* 23 */     this.id = id;
/* 24 */     this.color = color;
/*    */   }
/*    */   
/*    */   public int color() {
/* 28 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugEntityBlockIntersection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */