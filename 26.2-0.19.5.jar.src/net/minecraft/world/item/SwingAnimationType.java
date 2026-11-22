/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SwingAnimationType implements StringRepresentable {
/* 13 */   NONE(0, "none"),
/* 14 */   WHACK(1, "whack"),
/* 15 */   STAB(2, "stab");
/*    */   
/*    */   private static final IntFunction<SwingAnimationType> BY_ID;
/*    */   
/*    */   public static final Codec<SwingAnimationType> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, SwingAnimationType> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 18 */     BY_ID = ByIdMap.continuous(SwingAnimationType::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 19 */     CODEC = (Codec<SwingAnimationType>)StringRepresentable.fromEnum(SwingAnimationType::values);
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SwingAnimationType::getId);
/*    */   }
/*    */   
/*    */   SwingAnimationType(int id, String name) {
/* 26 */     this.id = id;
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 31 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\SwingAnimationType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */