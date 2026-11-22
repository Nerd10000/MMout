/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.tags.BlockItemTagId;
/*    */ 
/*    */ class null implements BlockItemTagsProvider.CombinedAppender {
/*    */   public BlockItemTagsProvider.CombinedAppender addAll(Stream<BlockItemId> ids) {
/* 31 */     appender.addAll(ids.map(BlockItemId::block));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public BlockItemTagsProvider.CombinedAppender addTag(BlockItemTagId id) {
/* 37 */     appender.addTag(id.block());
/* 38 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\BlockItemTagsProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */