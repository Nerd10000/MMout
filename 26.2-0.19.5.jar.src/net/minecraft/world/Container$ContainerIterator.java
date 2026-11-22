/*     */ package net.minecraft.world;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ContainerIterator implements Iterator<ItemStack> {
/*     */   private final Container container;
/*     */   
/*     */   private int index;
/*     */   
/*     */   private final int size;
/*     */   
/*     */   public ContainerIterator(Container container) {
/* 131 */     this.container = container;
/* 132 */     this.size = container.getContainerSize();
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 137 */     return (this.index < this.size);
/*     */   }
/*     */   
/*     */   public ItemStack next() {
/* 142 */     if (!hasNext())
/* 143 */       throw new NoSuchElementException(); 
/* 145 */     return this.container.getItem(this.index++);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\Container$ContainerIterator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */