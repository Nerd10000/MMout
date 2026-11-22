/*    */ package net.minecraft.world;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public enum InteractionHand {
/* 12 */   MAIN_HAND(0),
/* 13 */   OFF_HAND(1);
/*    */   
/*    */   private static final IntFunction<InteractionHand> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, InteractionHand> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 16 */     BY_ID = ByIdMap.continuous(h -> h.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 17 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, h -> h.id);
/*    */   }
/*    */   
/*    */   InteractionHand(int id) {
/* 22 */     this.id = id;
/*    */   }
/*    */   
/*    */   public EquipmentSlot asEquipmentSlot() {
/* 26 */     return (this == MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\InteractionHand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */