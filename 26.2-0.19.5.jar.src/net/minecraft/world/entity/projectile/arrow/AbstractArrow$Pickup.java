/*    */ package net.minecraft.world.entity.projectile.arrow;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ 
/*    */ public enum Pickup {
/* 74 */   DISALLOWED, ALLOWED, CREATIVE_ONLY;
/*    */   
/*    */   public static final Codec<Pickup> LEGACY_CODEC;
/*    */   
/*    */   static {
/* 76 */     LEGACY_CODEC = Codec.BYTE.xmap(Pickup::byOrdinal, p -> Byte.valueOf((byte)p.ordinal()));
/*    */   }
/*    */   
/*    */   public static Pickup byOrdinal(int ordinal) {
/* 79 */     if (ordinal < 0 || ordinal > (values()).length)
/* 80 */       ordinal = 0; 
/* 83 */     return values()[ordinal];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\arrow\AbstractArrow$Pickup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */