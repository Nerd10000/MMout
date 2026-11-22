/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*    */ 
/*    */ public class ZombieVillagerRenderState extends ZombieRenderState implements VillagerDataHolderRenderState {
/*    */   public VillagerData villagerData;
/*    */   
/*    */   public VillagerData getVillagerData() {
/* 11 */     return this.villagerData;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\ZombieVillagerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */