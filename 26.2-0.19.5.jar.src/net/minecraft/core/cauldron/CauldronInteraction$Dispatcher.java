/*    */ package net.minecraft.core.cauldron;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class Dispatcher {
/* 24 */   private final Map<TagKey<Item>, CauldronInteraction> tags = new HashMap<>();
/*    */   
/* 25 */   private final Map<Item, CauldronInteraction> items = new HashMap<>();
/*    */   
/*    */   void put(Item item, CauldronInteraction interaction) {
/* 29 */     this.items.put(item, interaction);
/*    */   }
/*    */   
/*    */   void put(TagKey<Item> tag, CauldronInteraction interaction) {
/* 34 */     this.tags.put(tag, interaction);
/*    */   }
/*    */   
/*    */   public CauldronInteraction get(ItemStack itemStack) {
/* 38 */     for (Map.Entry<TagKey<Item>, CauldronInteraction> e : this.tags.entrySet()) {
/* 39 */       if (itemStack.is(e.getKey()))
/* 40 */         return e.getValue(); 
/*    */     } 
/* 44 */     return this.items.getOrDefault(itemStack.getItem(), CauldronInteraction.DEFAULT);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\cauldron\CauldronInteraction$Dispatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */