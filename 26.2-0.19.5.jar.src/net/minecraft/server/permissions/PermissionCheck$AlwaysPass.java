/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public class AlwaysPass implements PermissionCheck {
/* 21 */   public static final AlwaysPass INSTANCE = new AlwaysPass();
/*    */   
/* 23 */   public static final MapCodec<AlwaysPass> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/*    */   public boolean check(PermissionSet source) {
/* 27 */     return true;
/*    */   }
/*    */   
/*    */   public MapCodec<AlwaysPass> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\PermissionCheck$AlwaysPass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */