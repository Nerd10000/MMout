/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CookingBookCategory implements StringRepresentable {
/* 13 */   FOOD(0, "food"),
/* 14 */   BLOCKS(1, "blocks"),
/* 15 */   MISC(2, "misc");
/*    */   
/*    */   private static final IntFunction<CookingBookCategory> BY_ID;
/*    */   
/*    */   public static final Codec<CookingBookCategory> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, CookingBookCategory> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 18 */     BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 20 */     CODEC = (Codec<CookingBookCategory>)StringRepresentable.fromEnum(CookingBookCategory::values);
/* 21 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   
/*    */   CookingBookCategory(int id, String name) {
/* 27 */     this.id = id;
/* 28 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\CookingBookCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */