/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.StaticSize<ShortTag> {
/*    */   public ShortTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 30 */     return ShortTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 35 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static short readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 39 */     accounter.accountBytes(10L);
/* 40 */     return input.readShort();
/*    */   }
/*    */   
/*    */   public int size() {
/* 45 */     return 2;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 50 */     return "SHORT";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 55 */     return "TAG_Short";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\ShortTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */