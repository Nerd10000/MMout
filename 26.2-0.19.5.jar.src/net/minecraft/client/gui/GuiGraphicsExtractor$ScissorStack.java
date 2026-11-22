/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ 
/*     */ class ScissorStack {
/*     */   private final ScreenRectangle screenSize;
/*     */   
/* 828 */   private final Deque<ScreenRectangle> stack = new ArrayDeque<>();
/*     */   
/*     */   private ScissorStack(ScreenRectangle screenSize) {
/* 831 */     this.screenSize = screenSize;
/*     */   }
/*     */   
/*     */   public void push(ScreenRectangle rectangle) {
/* 835 */     ScreenRectangle lastRectangle = Objects.<ScreenRectangle>requireNonNullElse(this.stack.peekLast(), this.screenSize);
/* 836 */     ScreenRectangle intersection = Objects.<ScreenRectangle>requireNonNullElse(rectangle.intersection(lastRectangle), ScreenRectangle.empty());
/* 837 */     this.stack.addLast(intersection);
/*     */   }
/*     */   
/*     */   public void pop() {
/* 841 */     if (this.stack.isEmpty())
/* 842 */       throw new IllegalStateException("Scissor stack underflow"); 
/* 844 */     this.stack.removeLast();
/*     */   }
/*     */   
/*     */   public ScreenRectangle peek() {
/* 848 */     return this.stack.peekLast();
/*     */   }
/*     */   
/*     */   public boolean containsPoint(int x, int y) {
/* 852 */     if (this.stack.isEmpty())
/* 853 */       return true; 
/* 855 */     return ((ScreenRectangle)this.stack.peek()).containsPoint(x, y);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\GuiGraphicsExtractor$ScissorStack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */