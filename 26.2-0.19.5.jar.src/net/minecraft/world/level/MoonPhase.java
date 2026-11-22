/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MoonPhase implements StringRepresentable {
/*  8 */   FULL_MOON(0, "full_moon"),
/*  9 */   WANING_GIBBOUS(1, "waning_gibbous"),
/* 10 */   THIRD_QUARTER(2, "third_quarter"),
/* 11 */   WANING_CRESCENT(3, "waning_crescent"),
/* 12 */   NEW_MOON(4, "new_moon"),
/* 13 */   WAXING_CRESCENT(5, "waxing_crescent"),
/* 14 */   FIRST_QUARTER(6, "first_quarter"),
/* 15 */   WAXING_GIBBOUS(7, "waxing_gibbous");
/*    */   
/*    */   public static final Codec<MoonPhase> CODEC;
/*    */   
/*    */   public static final int COUNT;
/*    */   
/*    */   public static final int PHASE_LENGTH = 24000;
/*    */   
/*    */   private final int index;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 18 */     CODEC = (Codec<MoonPhase>)StringRepresentable.fromEnum(MoonPhase::values);
/* 20 */     COUNT = (values()).length;
/*    */   }
/*    */   
/*    */   MoonPhase(int index, String name) {
/* 27 */     this.index = index;
/* 28 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int index() {
/* 32 */     return this.index;
/*    */   }
/*    */   
/*    */   public int startTick() {
/* 36 */     return this.index * 24000;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 41 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\MoonPhase.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */