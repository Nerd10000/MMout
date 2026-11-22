/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum ContainerInput {
/* 11 */   PICKUP(0),
/* 12 */   QUICK_MOVE(1),
/* 13 */   SWAP(2),
/* 14 */   CLONE(3),
/* 15 */   THROW(4),
/* 16 */   QUICK_CRAFT(5),
/* 17 */   PICKUP_ALL(6);
/*    */   
/*    */   private static final IntFunction<ContainerInput> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, ContainerInput> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 20 */     BY_ID = ByIdMap.continuous(ContainerInput::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 22 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ContainerInput::id);
/*    */   }
/*    */   
/*    */   ContainerInput(int id) {
/* 27 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 31 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ContainerInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */