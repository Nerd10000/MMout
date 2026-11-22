/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ public interface ButtonListDialog extends Dialog {
/*    */   MapCodec<? extends ButtonListDialog> codec();
/*    */   
/*    */   int columns();
/*    */   
/*    */   Optional<ActionButton> exitAction();
/*    */   
/*    */   default Optional<Action> onCancel() {
/* 18 */     return exitAction().flatMap(ActionButton::action);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\ButtonListDialog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */