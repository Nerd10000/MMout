/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.InputConstants;
/*    */ import java.util.Arrays;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class MouseSettingsScreen extends OptionsSubScreen {
/* 13 */   private static final Component TITLE = (Component)Component.translatable("options.mouse_settings.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 15 */     return (OptionInstance<?>[])new OptionInstance[] { options
/* 16 */         .sensitivity(), options
/* 17 */         .mouseWheelSensitivity(), options
/* 18 */         .discreteMouseScroll(), options
/* 19 */         .invertMouseX(), options
/* 20 */         .invertMouseY(), options
/* 21 */         .allowCursorChanges() };
/*    */   }
/*    */   
/*    */   public MouseSettingsScreen(Screen lastScreen, Options options) {
/* 26 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */   
/*    */   protected void addOptions() {
/* 31 */     if (InputConstants.isRawMouseInputSupported()) {
/* 32 */       this.list.addSmall((OptionInstance[])Stream.concat(Arrays.stream((Object[])options(this.options)), Stream.of(this.options.rawMouseInput())).toArray(x$0 -> new OptionInstance[x$0]));
/*    */     } else {
/* 34 */       this.list.addSmall((OptionInstance[])options(this.options));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\MouseSettingsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */