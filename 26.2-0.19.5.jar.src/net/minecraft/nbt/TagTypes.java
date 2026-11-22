/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class TagTypes {
/*  4 */   private static final TagType<?>[] TYPES = new TagType[] { 
/*  4 */       EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, 
/*  4 */       CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE };
/*    */   
/*    */   public static TagType<?> getType(int typeId) {
/* 21 */     if (typeId < 0 || typeId >= TYPES.length)
/* 22 */       return TagType.createInvalid(typeId); 
/* 25 */     return TYPES[typeId];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TagTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */