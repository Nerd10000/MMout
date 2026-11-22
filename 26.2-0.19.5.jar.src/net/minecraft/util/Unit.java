/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public enum Unit {
/*  9 */   INSTANCE;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Unit> STREAM_CODEC;
/*    */   
/*    */   public static final Codec<Unit> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = MapCodec.unitCodec(INSTANCE);
/* 12 */     STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Unit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */