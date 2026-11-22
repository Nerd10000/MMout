/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PresenceSharing implements StringRepresentable {
/*  8 */   NONE("none"),
/*  9 */   LIMITED("limited"),
/* 10 */   ALL("all");
/*    */   
/*    */   public static final Codec<PresenceSharing> CODEC;
/*    */   
/*    */   public static final String TRANSLATION_KEY_BASE = "options.sharePresence";
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Component translatable;
/*    */   
/*    */   private final Component tooltip;
/*    */   
/*    */   static {
/* 13 */     CODEC = (Codec<PresenceSharing>)StringRepresentable.fromEnum(PresenceSharing::values);
/*    */   }
/*    */   
/*    */   PresenceSharing(String name) {
/* 21 */     this.name = name;
/* 22 */     this.translatable = (Component)Component.translatable("options.sharePresence." + name);
/* 23 */     this.tooltip = (Component)Component.translatable("options.sharePresence." + name + ".tooltip");
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getTranslation() {
/* 32 */     return this.translatable;
/*    */   }
/*    */   
/*    */   public Component getTooltip() {
/* 36 */     return this.tooltip;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\PresenceSharing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */