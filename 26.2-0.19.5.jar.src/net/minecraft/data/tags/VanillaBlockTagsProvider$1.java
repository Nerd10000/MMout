/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ class null extends BlockItemTagAppender<Block> {
/*    */   null(VanillaBlockTagsProvider this$0, TagAppender<Block> original) {
/* 26 */     super(original);
/*    */   }
/*    */   
/*    */   protected ResourceKey<Block> convertElement(BlockItemId element) {
/* 29 */     return element.block();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VanillaBlockTagsProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */