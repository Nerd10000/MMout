/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ 
/*    */ public class DiscFragmentItem extends Item {
/*    */   public DiscFragmentItem(Item.Properties properties) {
/* 12 */     super(properties);
/*    */   }
/*    */   
/*    */   public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
/* 17 */     builder.accept(getDisplayName().withStyle(ChatFormatting.GRAY));
/*    */   }
/*    */   
/*    */   public MutableComponent getDisplayName() {
/* 21 */     return Component.translatable(this.descriptionId + ".desc");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\DiscFragmentItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */