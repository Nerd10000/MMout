/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.tags.BlockItemTagId;
/*    */ 
/*    */ public interface CombinedAppender {
/*    */   CombinedAppender addAll(Stream<BlockItemId> paramStream);
/*    */   
/*    */   CombinedAppender addTag(BlockItemTagId paramBlockItemTagId);
/*    */   
/*    */   CombinedAppender add(BlockItemId... ids) {
/* 65 */     addAll(Arrays.stream(ids));
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   default CombinedAppender addAll(Collection<BlockItemId> ids) {
/* 70 */     addAll(ids.stream());
/* 71 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\BlockItemTagsProvider$CombinedAppender.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */