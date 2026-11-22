/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.level.block.ColorCollection;
/*    */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*    */ 
/*    */ public abstract class BlockItemTagAppender<Element> implements TagAppender<Element> {
/*    */   private final TagAppender<Element> original;
/*    */   
/*    */   public BlockItemTagAppender(TagAppender<Element> original) {
/* 15 */     this.original = original;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> add(ResourceKey<Element> element) {
/* 22 */     this.original.add(element);
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> add(BlockItemId... ids) {
/* 27 */     this.original.addAll(Arrays.<BlockItemId>stream(ids).map(this::convertElement));
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> addAll(ColorCollection<ResourceKey<Element>> collection) {
/* 32 */     collection.forEach(this::add);
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> addAll(WeatheringCopperCollection<ResourceKey<Element>> collection) {
/* 37 */     collection.forEach(this::add);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public final BlockItemTagAppender<Element> add(ResourceKey<Element>... elements) {
/* 44 */     this.original.add(elements);
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> addOptional(ResourceKey<Element> element) {
/* 50 */     this.original.addOptional(element);
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> addTag(TagKey<Element> tag) {
/* 56 */     this.original.addTag(tag);
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagAppender<Element> addOptionalTag(TagKey<Element> tag) {
/* 62 */     this.original.addOptionalTag(tag);
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   protected abstract ResourceKey<Element> convertElement(BlockItemId paramBlockItemId);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\BlockItemTagAppender.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */