/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.references.BlockItemId;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ 
/*    */ class null extends BlockItemTagAppender<Item> {
/*    */   null(VanillaItemTagsProvider this$0, TagAppender<Item> original) {
/* 25 */     super(original);
/*    */   }
/*    */   
/*    */   protected ResourceKey<Item> convertElement(BlockItemId element) {
/* 28 */     return element.item();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\VanillaItemTagsProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */