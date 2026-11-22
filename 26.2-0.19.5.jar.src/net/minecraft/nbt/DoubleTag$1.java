/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.StaticSize<DoubleTag> {
/*    */   public DoubleTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 22 */     return DoubleTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 27 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static double readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 31 */     accounter.accountBytes(16L);
/* 32 */     return input.readDouble();
/*    */   }
/*    */   
/*    */   public int size() {
/* 37 */     return 8;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return "DOUBLE";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 47 */     return "TAG_Double";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\DoubleTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */