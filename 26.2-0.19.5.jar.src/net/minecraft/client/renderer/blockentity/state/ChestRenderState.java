/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.properties.ChestType;
/*    */ 
/*    */ public class ChestRenderState extends BlockEntityRenderState {
/*  7 */   public ChestType type = ChestType.SINGLE;
/*    */   
/*    */   public float open;
/*    */   
/*  9 */   public Direction facing = Direction.SOUTH;
/*    */   
/* 10 */   public ChestMaterialType material = ChestMaterialType.REGULAR;
/*    */   
/*    */   public enum ChestMaterialType {
/* 13 */     ENDER_CHEST, CHRISTMAS, TRAPPED, COPPER_UNAFFECTED, COPPER_EXPOSED, COPPER_WEATHERED, COPPER_OXIDIZED, REGULAR;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\ChestRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */