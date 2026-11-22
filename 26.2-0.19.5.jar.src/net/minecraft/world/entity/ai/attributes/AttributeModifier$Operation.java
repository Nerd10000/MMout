/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Operation implements StringRepresentable {
/* 17 */   ADD_VALUE("add_value", 0),
/* 18 */   ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
/* 19 */   ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);
/*    */   
/*    */   public static final IntFunction<Operation> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC;
/*    */   
/*    */   public static final Codec<Operation> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 21 */     BY_ID = ByIdMap.continuous(Operation::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Operation::id);
/* 25 */     CODEC = (Codec<Operation>)StringRepresentable.fromEnum(Operation::values);
/*    */   }
/*    */   
/*    */   Operation(String name, int id) {
/* 31 */     this.name = name;
/* 32 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 36 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\AttributeModifier$Operation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */