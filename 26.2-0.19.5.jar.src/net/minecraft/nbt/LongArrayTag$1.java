/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.VariableSize<LongArrayTag> {
/*    */   public LongArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 23 */     return new LongArrayTag(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 28 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static long[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/* 33 */     int length = input.readInt();
/* 34 */     accounter.accountBytes(8L, length);
/* 35 */     long[] data = new long[length];
/* 36 */     for (int i = 0; i < length; i++)
/* 37 */       data[i] = input.readLong(); 
/* 39 */     return data;
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 44 */     input.skipBytes(input.readInt() * 8);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 49 */     return "LONG[]";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 54 */     return "TAG_Long_Array";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\LongArrayTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */