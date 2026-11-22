/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TagBuilder {
/*  9 */   private final List<TagEntry> entries = new ArrayList<>();
/*    */   
/*    */   private boolean replace = false;
/*    */   
/*    */   public static TagBuilder create() {
/* 13 */     return new TagBuilder();
/*    */   }
/*    */   
/*    */   public List<TagEntry> build() {
/* 17 */     return List.copyOf(this.entries);
/*    */   }
/*    */   
/*    */   public boolean shouldReplace() {
/* 21 */     return this.replace;
/*    */   }
/*    */   
/*    */   public TagBuilder setReplace(boolean replace) {
/* 25 */     this.replace = replace;
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public TagBuilder add(TagEntry entry) {
/* 30 */     this.entries.add(entry);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public TagBuilder addElement(Identifier id) {
/* 35 */     return add(TagEntry.element(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalElement(Identifier id) {
/* 39 */     return add(TagEntry.optionalElement(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addTag(Identifier id) {
/* 43 */     return add(TagEntry.tag(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalTag(Identifier id) {
/* 47 */     return add(TagEntry.optionalTag(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */