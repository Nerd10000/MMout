/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.tags.BlockItemTagId;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public abstract class BlockItemTagsProvider {
/*    */   private final Function<BlockItemTagId, CombinedAppender> tagSupplier;
/*    */   
/*    */   protected BlockItemTagsProvider(Function<BlockItemTagId, CombinedAppender> tagSupplier) {
/* 18 */     this.tagSupplier = tagSupplier;
/*    */   }
/*    */   
/*    */   protected CombinedAppender tag(BlockItemTagId tag) {
/* 22 */     return this.tagSupplier.apply(tag);
/*    */   }
/*    */   
/*    */   protected abstract void run();
/*    */   
/*    */   public static CombinedAppender wrapForBlocks(final TagAppender<Block> appender) {
/* 28 */     return new CombinedAppender() {
/*    */         public BlockItemTagsProvider.CombinedAppender addAll(Stream<BlockItemId> ids) {
/* 31 */           appender.addAll(ids.map(BlockItemId::block));
/* 32 */           return this;
/*    */         }
/*    */         
/*    */         public BlockItemTagsProvider.CombinedAppender addTag(BlockItemTagId id) {
/* 37 */           appender.addTag(id.block());
/* 38 */           return this;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static CombinedAppender wrapForItems(final TagAppender<Item> appender) {
/* 44 */     return new CombinedAppender() {
/*    */         public BlockItemTagsProvider.CombinedAppender addAll(Stream<BlockItemId> ids) {
/* 47 */           appender.addAll(ids.map(BlockItemId::item));
/* 48 */           return this;
/*    */         }
/*    */         
/*    */         public BlockItemTagsProvider.CombinedAppender addTag(BlockItemTagId id) {
/* 53 */           appender.addTag(id.item());
/* 54 */           return this;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static interface CombinedAppender {
/*    */     CombinedAppender addAll(Stream<BlockItemId> param1Stream);
/*    */     
/*    */     CombinedAppender addTag(BlockItemTagId param1BlockItemTagId);
/*    */     
/*    */     CombinedAppender add(BlockItemId... ids) {
/* 65 */       addAll(Arrays.stream(ids));
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     default CombinedAppender addAll(Collection<BlockItemId> ids) {
/* 70 */       addAll(ids.stream());
/* 71 */       return this;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\BlockItemTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */