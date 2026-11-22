/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Variant implements StringRepresentable {
/* 13 */   WHITE(0, "white"),
/* 14 */   CREAMY(1, "creamy"),
/* 15 */   CHESTNUT(2, "chestnut"),
/* 16 */   BROWN(3, "brown"),
/* 17 */   BLACK(4, "black"),
/* 18 */   GRAY(5, "gray"),
/* 19 */   DARK_BROWN(6, "dark_brown");
/*    */   
/*    */   public static final Codec<Variant> CODEC;
/*    */   
/*    */   private static final IntFunction<Variant> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 22 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 24 */     BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 26 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*    */   }
/*    */   
/*    */   Variant(int id, String name) {
/* 32 */     this.id = id;
/* 33 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 37 */     return this.id;
/*    */   }
/*    */   
/*    */   public static Variant byId(int id) {
/* 41 */     return BY_ID.apply(id);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 46 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */