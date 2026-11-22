/*     */ package net.minecraft.nbt.visitors;
/*     */ 
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class RootBuilder implements CollectToTag.ContainerBuilder {
/*     */   private Tag result;
/*     */   
/*     */   public void acceptValue(Tag tag) {
/* 170 */     this.result = tag;
/*     */   }
/*     */   
/*     */   public Tag build() {
/* 175 */     return this.result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\CollectToTag$RootBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */