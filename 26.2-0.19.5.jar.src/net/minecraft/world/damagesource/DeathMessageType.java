/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DeathMessageType implements StringRepresentable {
/*  7 */   DEFAULT("default"),
/*  8 */   FALL_VARIANTS("fall_variants"),
/*  9 */   INTENTIONAL_GAME_DESIGN("intentional_game_design");
/*    */   
/*    */   public static final Codec<DeathMessageType> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 12 */     CODEC = (Codec<DeathMessageType>)StringRepresentable.fromEnum(DeathMessageType::values);
/*    */   }
/*    */   
/*    */   DeathMessageType(String id) {
/* 17 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\damagesource\DeathMessageType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */