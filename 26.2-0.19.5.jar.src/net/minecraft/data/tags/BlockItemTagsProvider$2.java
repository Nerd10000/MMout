/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.tags.BlockItemTagId;
/*    */ 
/*    */ class null implements BlockItemTagsProvider.CombinedAppender {
/*    */   public BlockItemTagsProvider.CombinedAppender addAll(Stream<BlockItemId> ids) {
/* 47 */     appender.addAll(ids.map(BlockItemId::item));
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagsProvider.CombinedAppender addTag(BlockItemTagId id) {
/* 53 */     appender.addTag(id.item());
/* 54 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\BlockItemTagsProvider$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */