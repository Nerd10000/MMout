/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DialogAction implements StringRepresentable {
/* 13 */   CLOSE(0, "close"),
/* 14 */   NONE(1, "none"),
/* 15 */   WAIT_FOR_RESPONSE(2, "wait_for_response");
/*    */   
/*    */   public static final IntFunction<DialogAction> BY_ID;
/*    */   
/*    */   public static final Codec<DialogAction> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, DialogAction> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 18 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 20 */     CODEC = (Codec<DialogAction>)StringRepresentable.fromEnum(DialogAction::values);
/* 21 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, s -> s.id);
/*    */   }
/*    */   
/*    */   DialogAction(int id, String name) {
/* 27 */     this.id = id;
/* 28 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean willUnpause() {
/* 37 */     return (this == CLOSE || this == WAIT_FOR_RESPONSE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\DialogAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */