/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ class null implements TagAppender<T> {
/*    */   public TagAppender<T> add(ResourceKey<T> element) {
/* 38 */     builder.addElement(element.identifier());
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public TagAppender<T> addOptional(ResourceKey<T> element) {
/* 44 */     builder.addOptionalElement(element.identifier());
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public TagAppender<T> addTag(TagKey<T> tag) {
/* 50 */     builder.addTag(tag.location());
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public TagAppender<T> addOptionalTag(TagKey<T> tag) {
/* 56 */     builder.addOptionalTag(tag.location());
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\TagAppender$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */