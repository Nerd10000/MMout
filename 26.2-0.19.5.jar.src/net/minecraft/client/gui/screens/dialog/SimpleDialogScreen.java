/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.server.dialog.SimpleDialog;
/*    */ 
/*    */ public class SimpleDialogScreen<T extends SimpleDialog> extends DialogScreen<T> {
/*    */   public SimpleDialogScreen(Screen previousScreen, T dialog, DialogConnectionAccess connectionAccess) {
/* 13 */     super(previousScreen, dialog, connectionAccess);
/*    */   }
/*    */   
/*    */   protected void updateHeaderAndFooter(HeaderAndFooterLayout layout, DialogControlSet controlSet, T dialog, DialogConnectionAccess connectionAccess) {
/* 18 */     super.updateHeaderAndFooter(layout, controlSet, dialog, connectionAccess);
/* 20 */     LinearLayout buttonLayout = LinearLayout.horizontal().spacing(8);
/* 21 */     for (ActionButton action : dialog.mainActions())
/* 22 */       buttonLayout.addChild((LayoutElement)controlSet.createActionButton(action).build()); 
/* 24 */     layout.addToFooter((LayoutElement)buttonLayout);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\SimpleDialogScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */