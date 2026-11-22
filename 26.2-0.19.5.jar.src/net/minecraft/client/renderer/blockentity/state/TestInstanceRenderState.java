/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ 
/*    */ public class TestInstanceRenderState extends BlockEntityRenderState {
/*    */   public BeaconRenderState beaconRenderState;
/*    */   
/*    */   public BlockEntityWithBoundingBoxRenderState blockEntityWithBoundingBoxRenderState;
/*    */   
/* 11 */   public final List<TestInstanceBlockEntity.ErrorMarker> errorMarkers = new ArrayList<>();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\TestInstanceRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */