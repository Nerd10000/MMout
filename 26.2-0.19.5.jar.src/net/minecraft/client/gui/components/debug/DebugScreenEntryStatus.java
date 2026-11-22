/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DebugScreenEntryStatus implements StringRepresentable {
/*  7 */   ALWAYS_ON("alwaysOn"),
/*  8 */   IN_OVERLAY("inOverlay"),
/*  9 */   NEVER("never");
/*    */   
/*    */   public static final Codec<DebugScreenEntryStatus> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 12 */     CODEC = (Codec<DebugScreenEntryStatus>)StringRepresentable.fromEnum(DebugScreenEntryStatus::values);
/*    */   }
/*    */   
/*    */   DebugScreenEntryStatus(String name) {
/* 16 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 21 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenEntryStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */