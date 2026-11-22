/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.StaticSize<ByteTag> {
/*    */   public ByteTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 28 */     return ByteTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 33 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static byte readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 37 */     accounter.accountBytes(9L);
/* 38 */     return input.readByte();
/*    */   }
/*    */   
/*    */   public int size() {
/* 43 */     return 1;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 48 */     return "BYTE";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 53 */     return "TAG_Byte";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ByteTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */