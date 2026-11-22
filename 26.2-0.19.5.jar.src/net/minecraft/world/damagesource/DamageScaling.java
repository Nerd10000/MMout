/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DamageScaling implements StringRepresentable {
/*  7 */   NEVER("never"),
/*  8 */   WHEN_CAUSED_BY_LIVING_NON_PLAYER("when_caused_by_living_non_player"),
/*  9 */   ALWAYS("always");
/*    */   
/*    */   public static final Codec<DamageScaling> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 12 */     CODEC = (Codec<DamageScaling>)StringRepresentable.fromEnum(DamageScaling::values);
/*    */   }
/*    */   
/*    */   DamageScaling(String id) {
/* 17 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\damagesource\DamageScaling.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */