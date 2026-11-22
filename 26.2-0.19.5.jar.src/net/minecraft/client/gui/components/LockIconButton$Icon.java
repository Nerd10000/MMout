/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ enum Icon {
/* 45 */   LOCKED(Identifier.withDefaultNamespace("widget/locked_button")),
/* 46 */   LOCKED_HOVER(Identifier.withDefaultNamespace("widget/locked_button_highlighted")),
/* 47 */   LOCKED_DISABLED(Identifier.withDefaultNamespace("widget/locked_button_disabled")),
/* 48 */   UNLOCKED(Identifier.withDefaultNamespace("widget/unlocked_button")),
/* 49 */   UNLOCKED_HOVER(Identifier.withDefaultNamespace("widget/unlocked_button_highlighted")),
/* 50 */   UNLOCKED_DISABLED(Identifier.withDefaultNamespace("widget/unlocked_button_disabled"));
/*    */   
/*    */   private final Identifier sprite;
/*    */   
/*    */   Icon(Identifier sprite) {
/* 56 */     this.sprite = sprite;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\LockIconButton$Icon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */