/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.server.ServerLinks;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.ButtonListDialog;
/*    */ import net.minecraft.server.dialog.CommonButtonData;
/*    */ import net.minecraft.server.dialog.ServerLinksDialog;
/*    */ import net.minecraft.server.dialog.action.StaticAction;
/*    */ 
/*    */ public class ServerLinksDialogScreen extends ButtonListDialogScreen<ServerLinksDialog> {
/*    */   public ServerLinksDialogScreen(Screen previousScreen, ServerLinksDialog dialog, DialogConnectionAccess connectionAccess) {
/* 17 */     super(previousScreen, dialog, connectionAccess);
/*    */   }
/*    */   
/*    */   protected Stream<ActionButton> createListActions(ServerLinksDialog dialog, DialogConnectionAccess connectionAccess) {
/* 22 */     return connectionAccess.serverLinks().entries().stream().map(entry -> createDialogClickAction(dialog, entry));
/*    */   }
/*    */   
/*    */   private static ActionButton createDialogClickAction(ServerLinksDialog data, ServerLinks.Entry entry) {
/* 26 */     return new ActionButton(new CommonButtonData(entry
/*    */           
/* 28 */           .displayName(), data
/* 29 */           .buttonWidth()), 
/*    */         
/* 31 */         Optional.of(new StaticAction((ClickEvent)new ClickEvent.OpenUrl(entry.link()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\ServerLinksDialogScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */