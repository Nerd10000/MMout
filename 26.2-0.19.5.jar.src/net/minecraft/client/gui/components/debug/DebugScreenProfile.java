/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DebugScreenProfile implements StringRepresentable {
/*  6 */   DEFAULT("default", "debug.options.profile.default"),
/*  7 */   PERFORMANCE("performance", "debug.options.profile.performance");
/*    */   
/*    */   public static final StringRepresentable.EnumCodec<DebugScreenProfile> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final String translationKey;
/*    */   
/*    */   static {
/* 10 */     CODEC = StringRepresentable.fromEnum(DebugScreenProfile::values);
/*    */   }
/*    */   
/*    */   DebugScreenProfile(String name, String translationKey) {
/* 15 */     this.name = name;
/* 16 */     this.translationKey = translationKey;
/*    */   }
/*    */   
/*    */   public String translationKey() {
/* 20 */     return this.translationKey;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenProfile.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */