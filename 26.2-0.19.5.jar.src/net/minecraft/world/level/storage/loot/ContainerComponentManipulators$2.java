/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BundleContents;
/*    */ 
/*    */ class null implements ContainerComponentManipulator<BundleContents> {
/*    */   public DataComponentType<BundleContents> type() {
/* 43 */     return DataComponents.BUNDLE_CONTENTS;
/*    */   }
/*    */   
/*    */   public BundleContents empty() {
/* 48 */     return BundleContents.EMPTY;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> getContents(BundleContents component) {
/* 53 */     return component.itemCopyStream();
/*    */   }
/*    */   
/*    */   public BundleContents setContents(BundleContents component, Stream<ItemStack> newContents) {
/* 58 */     BundleContents.Mutable builder = (new BundleContents.Mutable(component)).clearItems();
/* 59 */     Objects.requireNonNull(builder);
/* 59 */     newContents.forEach(builder::tryInsert);
/* 60 */     return builder.toImmutable();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */