/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public interface StaticSize<T extends Tag> extends TagType<T> {
/*    */   default void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 31 */     input.skipBytes(size());
/*    */   }
/*    */   
/*    */   default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 36 */     input.skipBytes(size() * count);
/*    */   }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TagType$StaticSize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */