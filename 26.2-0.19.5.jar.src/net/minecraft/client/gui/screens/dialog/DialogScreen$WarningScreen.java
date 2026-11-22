/*     */ package net.minecraft.client.gui.screens.dialog;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class WarningScreen extends ConfirmScreen {
/*     */   private final MutableObject<Screen> returnScreen;
/*     */   
/*     */   public static Screen create(Minecraft minecraft, DialogConnectionAccess connectionAccess, Screen returnScreen) {
/* 224 */     return (Screen)new WarningScreen(minecraft, connectionAccess, new MutableObject(returnScreen));
/*     */   }
/*     */   
/*     */   private WarningScreen(Minecraft minecraft, DialogConnectionAccess connectionAccess, MutableObject<Screen> returnScreen) {
/* 228 */     super(disconnect -> {
/*     */           if (disconnect) {
/*     */             connectionAccess.disconnect(DialogScreen.DISCONNECT);
/*     */           } else {
/*     */             minecraft.gui.setScreen((Screen)returnScreen.get());
/*     */           } 
/* 236 */         }(Component)Component.translatable("menu.custom_screen_info.title"), 
/* 237 */         (Component)Component.translatable("menu.custom_screen_info.contents"), 
/* 238 */         CommonComponents.disconnectButtonLabel(minecraft.isLocalServer()), CommonComponents.GUI_BACK);
/* 241 */     this.returnScreen = returnScreen;
/*     */   }
/*     */   
/*     */   public Screen returnScreen() {
/* 245 */     return (Screen)this.returnScreen.get();
/*     */   }
/*     */   
/*     */   public void updateReturnScreen(Screen newReturnScreen) {
/* 249 */     this.returnScreen.setValue(newReturnScreen);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\DialogScreen$WarningScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */