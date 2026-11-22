/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public interface StreamTagVisitor {
/*    */   ValueResult visitEnd();
/*    */   
/*    */   ValueResult visit(String paramString);
/*    */   
/*    */   ValueResult visit(byte paramByte);
/*    */   
/*    */   ValueResult visit(short paramShort);
/*    */   
/*    */   ValueResult visit(int paramInt);
/*    */   
/*    */   ValueResult visit(long paramLong);
/*    */   
/*    */   ValueResult visit(float paramFloat);
/*    */   
/*    */   ValueResult visit(double paramDouble);
/*    */   
/*    */   ValueResult visit(byte[] paramArrayOfbyte);
/*    */   
/*    */   ValueResult visit(int[] paramArrayOfint);
/*    */   
/*    */   ValueResult visit(long[] paramArrayOflong);
/*    */   
/*    */   ValueResult visitList(TagType<?> paramTagType, int paramInt);
/*    */   
/*    */   EntryResult visitEntry(TagType<?> paramTagType);
/*    */   
/*    */   EntryResult visitEntry(TagType<?> paramTagType, String paramString);
/*    */   
/*    */   EntryResult visitElement(TagType<?> paramTagType, int paramInt);
/*    */   
/*    */   ValueResult visitContainerEnd();
/*    */   
/*    */   ValueResult visitRootEntry(TagType<?> paramTagType);
/*    */   
/*    */   public enum ValueResult {
/* 14 */     CONTINUE, BREAK, HALT;
/*    */   }
/*    */   
/*    */   public enum EntryResult {
/* 23 */     ENTER, SKIP, BREAK, HALT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\StreamTagVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */