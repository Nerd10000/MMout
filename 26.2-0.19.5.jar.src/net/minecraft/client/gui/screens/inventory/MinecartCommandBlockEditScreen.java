/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
/*    */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*    */ import net.minecraft.world.level.BaseCommandBlock;
/*    */ 
/*    */ public class MinecartCommandBlockEditScreen extends AbstractCommandBlockEditScreen {
/*    */   private final MinecartCommandBlock minecart;
/*    */   
/*    */   public MinecartCommandBlockEditScreen(MinecartCommandBlock minecart) {
/* 11 */     this.minecart = minecart;
/*    */   }
/*    */   
/*    */   protected BaseCommandBlock getCommandBlock() {
/* 16 */     return this.minecart.getCommandBlock();
/*    */   }
/*    */   
/*    */   protected int getPreviousY() {
/* 21 */     return 150;
/*    */   }
/*    */   
/*    */   protected void init() {
/* 26 */     super.init();
/* 27 */     this.commandEdit.setValue(getCommandBlock().getCommand());
/*    */   }
/*    */   
/*    */   protected void populateAndSendPacket() {
/* 32 */     this.minecraft.getConnection().send((Packet)new ServerboundSetCommandMinecartPacket(this.minecart.getId(), this.commandEdit.getValue(), this.minecart.getCommandBlock().isTrackOutput()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\MinecartCommandBlockEditScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */