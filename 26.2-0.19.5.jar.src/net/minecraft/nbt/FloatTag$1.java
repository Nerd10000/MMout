/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class null implements TagType.StaticSize<FloatTag> {
/*    */   public FloatTag load(DataInput input, NbtAccounter accounter) throws IOException {
/* 22 */     return FloatTag.valueOf(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 27 */     return output.visit(readAccounted(input, accounter));
/*    */   }
/*    */   
/*    */   private static float readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/* 31 */     accounter.accountBytes(12L);
/* 32 */     return input.readFloat();
/*    */   }
/*    */   
/*    */   public int size() {
/* 37 */     return 4;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return "FLOAT";
/*    */   }
/*    */   
/*    */   public String getPrettyName() {
/* 47 */     return "TAG_Float";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\FloatTag$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */