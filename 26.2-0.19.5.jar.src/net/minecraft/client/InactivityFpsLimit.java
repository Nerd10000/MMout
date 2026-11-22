/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum InactivityFpsLimit implements StringRepresentable {
/*  8 */   MINIMIZED("minimized", "options.inactivityFpsLimit.minimized"),
/*  9 */   AFK("afk", "options.inactivityFpsLimit.afk");
/*    */   
/*    */   public static final Codec<InactivityFpsLimit> CODEC;
/*    */   
/*    */   private final String serializedName;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 12 */     CODEC = (Codec<InactivityFpsLimit>)StringRepresentable.fromEnum(InactivityFpsLimit::values);
/*    */   }
/*    */   
/*    */   InactivityFpsLimit(String serializedName, String key) {
/* 18 */     this.serializedName = serializedName;
/* 19 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 23 */     return this.caption;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.serializedName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\InactivityFpsLimit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */