/*    */ package net.minecraft.client.gui.screens.options.controls;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
/*    */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ControlsScreen extends OptionsSubScreen {
/* 12 */   private static final Component TITLE = (Component)Component.translatable("controls.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 15 */     return (OptionInstance<?>[])new OptionInstance[] { options
/* 16 */         .toggleCrouch(), options
/* 17 */         .toggleSprint(), options
/* 18 */         .toggleAttack(), options
/* 19 */         .toggleUse(), options
/* 20 */         .autoJump(), options
/* 21 */         .sprintWindow(), options
/* 22 */         .operatorItemsTab() };
/*    */   }
/*    */   
/*    */   public ControlsScreen(Screen lastScreen, Options options) {
/* 27 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */   
/*    */   protected void addOptions() {
/* 32 */     this.list.addSmall(
/* 33 */         (AbstractWidget)Button.builder((Component)Component.translatable("options.mouse_settings"), paramButton -> this.minecraft.gui.setScreen((Screen)new MouseSettingsScreen((Screen)this, this.options))).build(), 
/* 34 */         (AbstractWidget)Button.builder((Component)Component.translatable("controls.keybinds"), paramButton -> this.minecraft.gui.setScreen((Screen)new KeyBindsScreen((Screen)this, this.options))).build());
/* 36 */     this.list.addSmall((OptionInstance[])options(this.options));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\controls\ControlsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */