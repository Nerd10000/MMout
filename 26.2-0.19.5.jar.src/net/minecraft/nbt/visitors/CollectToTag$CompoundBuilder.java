/*     */ package net.minecraft.nbt.visitors;
/*     */ 
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ class CompoundBuilder implements CollectToTag.ContainerBuilder {
/* 180 */   private final CompoundTag compound = new CompoundTag();
/*     */   
/* 181 */   private String lastId = "";
/*     */   
/*     */   public void acceptKey(String id) {
/* 185 */     this.lastId = id;
/*     */   }
/*     */   
/*     */   public void acceptValue(Tag tag) {
/* 190 */     this.compound.put(this.lastId, tag);
/*     */   }
/*     */   
/*     */   public Tag build() {
/* 195 */     return (Tag)this.compound;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\CollectToTag$CompoundBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */