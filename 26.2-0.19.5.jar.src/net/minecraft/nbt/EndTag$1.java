/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType<EndTag> {
/*    */   public EndTag load(DataInput input, NbtAccounter accounter) {
/* 15 */     accounter.accountBytes(8L);
/* 16 */     return EndTag.INSTANCE;
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) {
/* 21 */     accounter.accountBytes(8L);
/* 22 */     return output.visitEnd();
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, int count, NbtAccounter accounter) {}
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) {}
/*    */   
/*    */   public String getName() {
/* 35 */     return "END";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 40 */     return "TAG_End";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\EndTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */