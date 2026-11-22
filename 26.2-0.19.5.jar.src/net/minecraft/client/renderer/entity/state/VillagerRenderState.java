/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*    */ 
/*    */ public class VillagerRenderState extends HoldingEntityRenderState implements VillagerDataHolderRenderState {
/*    */   public boolean isUnhappy;
/*    */   
/*    */   public VillagerData villagerData;
/*    */   
/*    */   public VillagerData getVillagerData() {
/* 12 */     return this.villagerData;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\VillagerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */