/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import net.minecraft.client.gui.components.ImageWidget;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public abstract class RealmsScreen extends Screen {
/*    */   protected static final int TITLE_HEIGHT = 17;
/*    */   
/*    */   protected static final int EXPIRATION_NOTIFICATION_DAYS = 7;
/*    */   
/*    */   protected static final long SIZE_LIMIT = 5368709120L;
/*    */   
/*    */   protected static final int COLOR_DARK_GRAY = -11776948;
/*    */   
/*    */   protected static final int COLOR_GREEN = -8388737;
/*    */   
/*    */   protected static final int COLOR_LINK = -13408581;
/*    */   
/*    */   protected static final int COLOR_LINK_HOVER = -9670204;
/*    */   
/*    */   protected static final int SKIN_FACE_SIZE = 32;
/*    */   
/*    */   protected static final int HARDCORE_HEART_SIZE = 8;
/*    */   
/* 23 */   protected static final Identifier LOGO_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/realms.png");
/*    */   
/*    */   protected static final int LOGO_WIDTH = 128;
/*    */   
/*    */   protected static final int LOGO_HEIGHT = 34;
/*    */   
/*    */   protected static final int LOGO_TEXTURE_WIDTH = 128;
/*    */   
/*    */   protected static final int LOGO_TEXTURE_HEIGHT = 64;
/*    */   
/*    */   public RealmsScreen(Component title) {
/* 30 */     super(title);
/*    */   }
/*    */   
/*    */   protected static int row(int i) {
/* 34 */     return 40 + i * 13;
/*    */   }
/*    */   
/*    */   protected static ImageWidget realmsLogo() {
/* 38 */     return ImageWidget.texture(128, 34, LOGO_LOCATION, 128, 64);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\realms\RealmsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */