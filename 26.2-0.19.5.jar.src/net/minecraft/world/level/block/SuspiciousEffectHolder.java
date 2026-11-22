/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface SuspiciousEffectHolder {
/*    */   SuspiciousStewEffects getSuspiciousEffects();
/*    */   
/*    */   static List<SuspiciousEffectHolder> getAllEffectHolders() {
/* 17 */     return (List<SuspiciousEffectHolder>)BuiltInRegistries.ITEM.stream().map(SuspiciousEffectHolder::tryGet).filter(Objects::nonNull).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   static SuspiciousEffectHolder tryGet(ItemLike item) {
/* 21 */     Item item2 = item.asItem();
/* 21 */     if (item2 instanceof BlockItem) {
/* 21 */       BlockItem blockItem = (BlockItem)item2;
/* 21 */       Block block = blockItem.getBlock();
/* 21 */       if (block instanceof SuspiciousEffectHolder) {
/* 21 */         SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)block;
/* 22 */         return effectHolder;
/*    */       } 
/*    */     } 
/* 24 */     Item item1 = item.asItem();
/* 24 */     if (item1 instanceof SuspiciousEffectHolder) {
/* 24 */       SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)item1;
/* 25 */       return effectHolder;
/*    */     } 
/* 27 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SuspiciousEffectHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */