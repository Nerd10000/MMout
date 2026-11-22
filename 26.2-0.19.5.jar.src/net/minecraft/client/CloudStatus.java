/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CloudStatus implements StringRepresentable {
/*  9 */   OFF("false", "options.off"),
/* 10 */   FAST("fast", "options.clouds.fast"),
/* 11 */   FANCY("true", "options.clouds.fancy");
/*    */   
/*    */   public static final Codec<CloudStatus> CODEC;
/*    */   
/*    */   private final String legacyName;
/*    */   
/*    */   private final Component caption;
/*    */   
/*    */   static {
/* 14 */     CODEC = (Codec<CloudStatus>)StringRepresentable.fromEnum(CloudStatus::values);
/*    */   }
/*    */   
/*    */   CloudStatus(String legacyName, String key) {
/* 20 */     this.legacyName = legacyName;
/* 21 */     this.caption = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 25 */     return this.caption;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 30 */     return this.legacyName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\CloudStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */