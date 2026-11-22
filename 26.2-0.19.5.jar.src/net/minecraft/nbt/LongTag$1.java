/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.StaticSize<LongTag> {
/*    */   public LongTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 30 */     return LongTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 35 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static long readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 39 */     accounter.accountBytes(16L);
/* 40 */     return input.readLong();
/*    */   }
/*    */   
/*    */   public int size() {
/* 45 */     return 8;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 50 */     return "LONG";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 55 */     return "TAG_Long";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\LongTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */