/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum LiquidSettings implements StringRepresentable {
/*  7 */   IGNORE_WATERLOGGING("ignore_waterlogging"),
/*  8 */   APPLY_WATERLOGGING("apply_waterlogging");
/*    */   
/*    */   public static final Codec<LiquidSettings> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 10 */     CODEC = StringRepresentable.fromValues(LiquidSettings::values);
/*    */   }
/*    */   
/*    */   LiquidSettings(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LiquidSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */