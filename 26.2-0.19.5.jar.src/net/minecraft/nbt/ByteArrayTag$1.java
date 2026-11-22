/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.VariableSize<ByteArrayTag> {
/*    */   public ByteArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 23 */     return new ByteArrayTag(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 28 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static byte[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/* 33 */     int length = input.readInt();
/* 34 */     accounter.accountBytes(1L, length);
/* 35 */     byte[] data = new byte[length];
/* 36 */     input.readFully(data);
/* 37 */     return data;
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 42 */     input.skipBytes(input.readInt() * 1);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return "BYTE[]";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 52 */     return "TAG_Byte_Array";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ByteArrayTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */