/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.VariableSize<IntArrayTag> {
/*    */   public IntArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 23 */     return new IntArrayTag(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 28 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static int[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 32 */     accounter.accountBytes(24L);
/* 34 */     int length = input.readInt();
/* 35 */     accounter.accountBytes(4L, length);
/* 36 */     int[] data = new int[length];
/* 37 */     for (int i = 0; i < length; i++)
/* 38 */       data[i] = input.readInt(); 
/* 40 */     return data;
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 45 */     input.skipBytes(input.readInt() * 4);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 50 */     return "INT[]";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 55 */     return "TAG_Int_Array";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\IntArrayTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */