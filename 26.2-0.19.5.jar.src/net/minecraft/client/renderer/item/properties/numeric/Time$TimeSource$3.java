/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.MoonPhase;
/*    */ 
/*    */ enum null {
/*    */   public float get(ClientLevel level, ItemStack itemStack, ItemOwner owner, RandomSource random) {
/* 63 */     return ((MoonPhase)level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, owner.position())).index() / MoonPhase.COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\Time$TimeSource$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */