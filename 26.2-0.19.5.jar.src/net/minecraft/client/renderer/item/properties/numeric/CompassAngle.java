/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CompassAngle implements RangeSelectItemModelProperty {
/*    */   public static final MapCodec<CompassAngle> MAP_CODEC;
/*    */   
/*    */   private final CompassAngleState state;
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = CompassAngleState.MAP_CODEC.xmap(CompassAngle::new, c -> c.state);
/*    */   }
/*    */   
/*    */   public CompassAngle(boolean wobble, CompassAngleState.CompassTarget compassTarget) {
/* 15 */     this(new CompassAngleState(wobble, compassTarget));
/*    */   }
/*    */   
/*    */   private CompassAngle(CompassAngleState state) {
/* 19 */     this.state = state;
/*    */   }
/*    */   
/*    */   public float get(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed) {
/* 24 */     return this.state.get(itemStack, level, owner, seed);
/*    */   }
/*    */   
/*    */   public MapCodec<CompassAngle> type() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\CompassAngle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */