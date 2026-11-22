/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public interface VariableSize<T extends Tag> extends TagType<T> {
/*    */   default void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 45 */     for (int i = 0; i < count; i++)
/* 46 */       skip(input, accounter); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TagType$VariableSize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */