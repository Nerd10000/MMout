/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum MapPostProcessing {
/* 11 */   LOCK(0),
/* 12 */   SCALE(1);
/*    */   
/*    */   public static final IntFunction<MapPostProcessing> ID_MAP;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, MapPostProcessing> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 15 */     ID_MAP = ByIdMap.continuous(MapPostProcessing::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 16 */     STREAM_CODEC = ByteBufCodecs.idMapper(ID_MAP, MapPostProcessing::id);
/*    */   }
/*    */   
/*    */   MapPostProcessing(int id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 25 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\MapPostProcessing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */