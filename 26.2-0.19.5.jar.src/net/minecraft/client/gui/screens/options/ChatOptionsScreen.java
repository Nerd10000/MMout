/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ChatOptionsScreen extends OptionsSubScreen {
/*  9 */   private static final Component TITLE = (Component)Component.translatable("options.chat.title");
/*    */   
/*    */   private static OptionInstance<?>[] options(Options options) {
/* 12 */     return (OptionInstance<?>[])new OptionInstance[] { 
/* 12 */         options
/* 13 */         .chatVisibility(), options.chatColors(), options
/* 14 */         .chatLinks(), options.chatLinksPrompt(), options
/* 15 */         .chatOpacity(), options.textBackgroundOpacity(), options
/* 16 */         .chatScale(), options.chatLineSpacing(), options
/* 17 */         .chatDelay(), options.chatWidth(), 
/* 17 */         options
/* 18 */         .chatHeightFocused(), options.chatHeightUnfocused(), options
/* 19 */         .narrator(), options.autoSuggestions(), options
/* 20 */         .hideMatchedNames(), options.reducedDebugInfo(), options
/* 21 */         .onlyShowSecureChat(), options.saveChatDrafts() };
/*    */   }
/*    */   
/*    */   public ChatOptionsScreen(Screen lastScreen, Options options) {
/* 26 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */   
/*    */   protected void addOptions() {
/* 31 */     this.list.addSmall((OptionInstance[])options(this.options));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\ChatOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */