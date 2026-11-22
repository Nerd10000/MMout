/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CommonButtons {
/*    */   public static SpriteIconButton language(int width, Button.OnPress onPress, boolean iconOnly) {
/* 12 */     SpriteIconButton button = SpriteIconButton.builder((Component)Component.translatable("options.language"), onPress, iconOnly).width(width).sprite(Identifier.withDefaultNamespace("icon/language"), 15, 15).narration(paramSupplier -> Component.translatable("options.language.narration")).build();
/* 13 */     button.setTooltip(Tooltip.create((Component)Component.translatable("options.language.tooltip")));
/* 14 */     return button;
/*    */   }
/*    */   
/*    */   public static SpriteIconButton accessibility(int width, Button.OnPress onPress, boolean iconOnly) {
/* 18 */     MutableComponent mutableComponent = iconOnly ? Component.translatable("options.accessibility") : Component.translatable("accessibility.onboarding.accessibility.button");
/* 23 */     SpriteIconButton button = SpriteIconButton.builder((Component)mutableComponent, onPress, iconOnly).width(width).sprite(Identifier.withDefaultNamespace("icon/accessibility"), 15, 15).narration(paramSupplier -> Component.translatable("accessibility.onboarding.accessibility.button.narration")).build();
/* 24 */     button.setTooltip(Tooltip.create((Component)Component.translatable("options.accessibility.tooltip")));
/* 25 */     return button;
/*    */   }
/*    */   
/*    */   public static FriendsButton friends(int width, Button.OnPress onPress, boolean friendsAvailable) {
/* 29 */     return new FriendsButton(width, onPress, friendsAvailable);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CommonButtons.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */