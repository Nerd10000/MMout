/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ enum null {
/*    */   public void convert(Mob from, Mob to, ConversionParams params) {
/* 79 */     Entity rootPassenger = from.getFirstPassenger();
/* 81 */     if (rootPassenger != null)
/* 82 */       rootPassenger.stopRiding(); 
/* 86 */     Entity leashHolder = from.getLeashHolder();
/* 87 */     if (leashHolder != null)
/* 88 */       from.dropLeash(); 
/* 91 */     ConversionType.convertCommon(from, to, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ConversionType$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */