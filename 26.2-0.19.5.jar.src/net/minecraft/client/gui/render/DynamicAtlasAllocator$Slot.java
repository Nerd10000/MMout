/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ public class Slot {
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private boolean fresh = true;
/*     */   
/*     */   private boolean discardAfterFrame;
/*     */   
/* 113 */   private DynamicAtlasAllocator.SlotState externalState = DynamicAtlasAllocator.SlotState.EMPTY;
/*     */   
/*     */   private Slot(int x, int y) {
/* 116 */     this.x = x;
/* 117 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int x() {
/* 121 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/* 125 */     return this.y;
/*     */   }
/*     */   
/*     */   public DynamicAtlasAllocator.SlotState state() {
/* 129 */     return this.externalState;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\DynamicAtlasAllocator$Slot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */