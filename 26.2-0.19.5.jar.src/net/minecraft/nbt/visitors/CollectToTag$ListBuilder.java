/*     */ package net.minecraft.nbt.visitors;
/*     */ 
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class ListBuilder implements CollectToTag.ContainerBuilder {
/* 200 */   private final ListTag list = new ListTag();
/*     */   
/*     */   public void acceptValue(Tag tag) {
/* 204 */     this.list.addAndUnwrap(tag);
/*     */   }
/*     */   
/*     */   public Tag build() {
/* 209 */     return (Tag)this.list;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\CollectToTag$ListBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */