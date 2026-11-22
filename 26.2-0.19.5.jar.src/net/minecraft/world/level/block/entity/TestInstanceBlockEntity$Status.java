/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Status implements StringRepresentable {
/* 68 */   CLEARED("cleared", 0),
/* 69 */   RUNNING("running", 1),
/* 70 */   FINISHED("finished", 2);
/*    */   
/*    */   private static final IntFunction<Status> ID_MAP;
/*    */   
/*    */   public static final Codec<Status> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Status> STREAM_CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final int index;
/*    */   
/*    */   static {
/* 72 */     ID_MAP = ByIdMap.continuous(s -> s.index, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 73 */     CODEC = (Codec<Status>)StringRepresentable.fromEnum(Status::values);
/* 74 */     STREAM_CODEC = ByteBufCodecs.idMapper(Status::byIndex, s -> s.index);
/*    */   }
/*    */   
/*    */   Status(String id, int index) {
/* 80 */     this.id = id;
/* 81 */     this.index = index;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 86 */     return this.id;
/*    */   }
/*    */   
/*    */   public static Status byIndex(int index) {
/* 90 */     return ID_MAP.apply(index);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity$Status.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */