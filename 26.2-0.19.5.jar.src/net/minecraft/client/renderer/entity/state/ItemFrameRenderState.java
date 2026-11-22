/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.state.MapRenderState;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.saveddata.maps.MapId;
/*    */ 
/*    */ public class ItemFrameRenderState extends EntityRenderState {
/* 11 */   public Direction direction = Direction.NORTH;
/*    */   
/* 12 */   public final BlockModelRenderState frameModel = new BlockModelRenderState();
/*    */   
/* 13 */   public final ItemStackRenderState item = new ItemStackRenderState();
/*    */   
/*    */   public int rotation;
/*    */   
/*    */   public boolean isGlowFrame;
/*    */   
/*    */   public MapId mapId;
/*    */   
/* 17 */   public final MapRenderState mapRenderState = new MapRenderState();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\ItemFrameRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */