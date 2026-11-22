/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagBuilder;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public interface TagAppender<T> {
/*    */   TagAppender<T> add(ResourceKey<T> paramResourceKey);
/*    */   
/*    */   TagAppender<T> add(ResourceKey<T>... elements) {
/* 15 */     return addAll(Arrays.stream(elements));
/*    */   }
/*    */   
/*    */   default TagAppender<T> addAll(Collection<ResourceKey<T>> elements) {
/* 19 */     elements.forEach(this::add);
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   default TagAppender<T> addAll(Stream<ResourceKey<T>> elements) {
/* 24 */     elements.forEach(this::add);
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   TagAppender<T> addOptional(ResourceKey<T> paramResourceKey);
/*    */   
/*    */   TagAppender<T> addTag(TagKey<T> paramTagKey);
/*    */   
/*    */   TagAppender<T> addOptionalTag(TagKey<T> paramTagKey);
/*    */   
/*    */   static <T> TagAppender<T> forBuilder(final TagBuilder builder) {
/* 35 */     return new TagAppender<T>() {
/*    */         public TagAppender<T> add(ResourceKey<T> element) {
/* 38 */           builder.addElement(element.identifier());
/* 39 */           return this;
/*    */         }
/*    */         
/*    */         public TagAppender<T> addOptional(ResourceKey<T> element) {
/* 44 */           builder.addOptionalElement(element.identifier());
/* 45 */           return this;
/*    */         }
/*    */         
/*    */         public TagAppender<T> addTag(TagKey<T> tag) {
/* 50 */           builder.addTag(tag.location());
/* 51 */           return this;
/*    */         }
/*    */         
/*    */         public TagAppender<T> addOptionalTag(TagKey<T> tag) {
/* 56 */           builder.addOptionalTag(tag.location());
/* 57 */           return this;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\TagAppender.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */