/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType<EndTag> {
/*    */   private IOException createException() {
/* 54 */     return new IOException("Invalid tag id: " + id);
/*    */   }
/*    */   
/*    */   public EndTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 59 */     throw createException();
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 64 */     throw createException();
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, int count, NbtAccounter accounter) throws IOException {
/* 69 */     throw createException();
/*    */   }
/*    */   
/*    */   public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 74 */     throw createException();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 79 */     return "INVALID[" + id + "]";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 84 */     return "UNKNOWN_" + id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TagType$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */