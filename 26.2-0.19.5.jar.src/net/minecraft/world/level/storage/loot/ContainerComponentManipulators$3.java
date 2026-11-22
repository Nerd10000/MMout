/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.ChargedProjectiles;
/*    */ 
/*    */ class null implements ContainerComponentManipulator<ChargedProjectiles> {
/*    */   public DataComponentType<ChargedProjectiles> type() {
/* 67 */     return DataComponents.CHARGED_PROJECTILES;
/*    */   }
/*    */   
/*    */   public ChargedProjectiles empty() {
/* 72 */     return ChargedProjectiles.EMPTY;
/*    */   }
/*    */   
/*    */   public Stream<ItemStack> getContents(ChargedProjectiles component) {
/* 77 */     return component.itemCopies().stream();
/*    */   }
/*    */   
/*    */   public ChargedProjectiles setContents(ChargedProjectiles component, Stream<ItemStack> newContents) {
/* 82 */     return ChargedProjectiles.ofNonEmpty(newContents.filter(s -> !s.isEmpty()).toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\ContainerComponentManipulators$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */