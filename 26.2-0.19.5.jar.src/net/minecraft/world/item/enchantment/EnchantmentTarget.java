/*    */ package net.minecraft.world.item.enchantment;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum EnchantmentTarget implements StringRepresentable {
/*  7 */   ATTACKER("attacker"),
/*  8 */   DAMAGING_ENTITY("damaging_entity"),
/*  9 */   VICTIM("victim");
/*    */   
/*    */   public static final Codec<EnchantmentTarget> CODEC;
/*    */   
/*    */   public static final Codec<EnchantmentTarget> NON_DAMAGE_CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 11 */     CODEC = (Codec<EnchantmentTarget>)StringRepresentable.fromEnum(EnchantmentTarget::values);
/* 13 */     NON_DAMAGE_CODEC = (Codec<EnchantmentTarget>)StringRepresentable.fromEnum(() -> new EnchantmentTarget[] { ATTACKER, VICTIM });
/*    */   }
/*    */   
/*    */   EnchantmentTarget(String id) {
/* 18 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\EnchantmentTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */