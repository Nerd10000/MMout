/*   */ package net.minecraft.server.dialog.input;
/*   */ 
/*   */ import com.mojang.serialization.MapCodec;
/*   */ import net.minecraft.core.registries.BuiltInRegistries;
/*   */ 
/*   */ public interface InputControl {
/*   */   public static final MapCodec<InputControl> MAP_CODEC;
/*   */   
/*   */   MapCodec<? extends InputControl> mapCodec();
/*   */   
/*   */   static {
/* 8 */     MAP_CODEC = BuiltInRegistries.INPUT_CONTROL_TYPE.byNameCodec().dispatchMap(InputControl::mapCodec, c -> c);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\InputControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */