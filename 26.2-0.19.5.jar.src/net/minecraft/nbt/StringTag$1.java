/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.VariableSize<StringTag> {
/*    */   public StringTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 19 */     return StringTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 24 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static String readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 28 */     accounter.accountBytes(36L);
/* 31 */     String data = input.readUTF();
/* 32 */     accounter.accountBytes(2L, data.length());
/* 33 */     return data;
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 38 */     StringTag.skipString(input);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 43 */     return "STRING";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 48 */     return "TAG_String";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\StringTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */