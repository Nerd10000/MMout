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
/*    */ public enum CraftingBookCategory implements StringRepresentable {
/* 13 */   BUILDING("building", 0),
/* 14 */   REDSTONE("redstone", 1),
/* 15 */   EQUIPMENT("equipment", 2),
/* 16 */   MISC("misc", 3);
/*    */   
/*    */   public static final Codec<CraftingBookCategory> CODEC;
/*    */   
/*    */   public static final IntFunction<CraftingBookCategory> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, CraftingBookCategory> STREAM_CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   static {
/* 19 */     CODEC = (Codec<CraftingBookCategory>)StringRepresentable.fromEnum(CraftingBookCategory::values);
/* 21 */     BY_ID = ByIdMap.continuous(CraftingBookCategory::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CraftingBookCategory::id);
/*    */   }
/*    */   
/*    */   CraftingBookCategory(String name, int id) {
/* 29 */     this.name = name;
/* 30 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   private int id() {
/* 39 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\CraftingBookCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */