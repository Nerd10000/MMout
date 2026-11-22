/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface NumberProvider extends LootContextUser {
/*    */   float getFloat(LootContext paramLootContext);
/*    */   
/*    */   default int getInt(LootContext context) {
/* 12 */     return Math.round(getFloat(context));
/*    */   }
/*    */   
/*    */   MapCodec<? extends NumberProvider> codec();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\NumberProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */