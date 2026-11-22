/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public class SimpleContainerData implements ContainerData {
/*    */   private final int[] ints;
/*    */   
/*    */   public SimpleContainerData(int count) {
/*  7 */     this.ints = new int[count];
/*    */   }
/*    */   
/*    */   public int get(int dataId) {
/* 12 */     return this.ints[dataId];
/*    */   }
/*    */   
/*    */   public void set(int dataId, int value) {
/* 17 */     this.ints[dataId] = value;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 22 */     return this.ints.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\SimpleContainerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */