/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.ButtonListDialog;
/*    */ import net.minecraft.server.dialog.CommonButtonData;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.server.dialog.DialogListDialog;
/*    */ import net.minecraft.server.dialog.action.StaticAction;
/*    */ 
/*    */ public class DialogListDialogScreen extends ButtonListDialogScreen<DialogListDialog> {
/*    */   public DialogListDialogScreen(Screen previousScreen, DialogListDialog dialog, DialogConnectionAccess connectionAccess) {
/* 18 */     super(previousScreen, dialog, connectionAccess);
/*    */   }
/*    */   
/*    */   protected Stream<ActionButton> createListActions(DialogListDialog data, DialogConnectionAccess connectionAccess) {
/* 23 */     return data.dialogs().stream().map(subDialog -> createDialogClickAction(data, subDialog));
/*    */   }
/*    */   
/*    */   private static ActionButton createDialogClickAction(DialogListDialog data, Holder<Dialog> subDialog) {
/* 27 */     return new ActionButton(new CommonButtonData(((Dialog)subDialog
/*    */           
/* 29 */           .value()).common().computeExternalTitle(), data
/* 30 */           .buttonWidth()), 
/*    */         
/* 32 */         Optional.of(new StaticAction((ClickEvent)new ClickEvent.ShowDialog(subDialog))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\DialogListDialogScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */