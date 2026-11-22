/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MusicToastDisplayState implements StringRepresentable {
/*  8 */   NEVER("never", "options.musicToast.never"),
/*  9 */   PAUSE("pause", "options.musicToast.pauseMenu"),
/* 10 */   PAUSE_AND_TOAST("pause_and_toast", "options.musicToast.pauseMenuAndToast");
/*    */   
/*    */   public static final Codec<MusicToastDisplayState> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Component text;
/*    */   
/*    */   private final Component tooltip;
/*    */   
/*    */   static {
/* 13 */     CODEC = (Codec<MusicToastDisplayState>)StringRepresentable.fromEnum(MusicToastDisplayState::values);
/*    */   }
/*    */   
/*    */   MusicToastDisplayState(String name, String translationKey) {
/* 20 */     this.name = name;
/* 21 */     this.text = (Component)Component.translatable(translationKey);
/* 22 */     this.tooltip = (Component)Component.translatable(translationKey + ".tooltip");
/*    */   }
/*    */   
/*    */   public Component text() {
/* 26 */     return this.text;
/*    */   }
/*    */   
/*    */   public Component tooltip() {
/* 30 */     return this.tooltip;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean renderInPauseScreen() {
/* 39 */     return (this != NEVER);
/*    */   }
/*    */   
/*    */   public boolean renderToast() {
/* 43 */     return (this == PAUSE_AND_TOAST);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\MusicToastDisplayState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */