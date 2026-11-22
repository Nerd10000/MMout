/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Input;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class ClientInput {
/*  8 */   public Input keyPresses = Input.EMPTY;
/*    */   
/*  9 */   protected Vec2 moveVector = Vec2.ZERO;
/*    */   
/*    */   public void tick() {}
/*    */   
/*    */   public Vec2 getMoveVector() {
/* 16 */     return this.moveVector;
/*    */   }
/*    */   
/*    */   public boolean hasForwardImpulse() {
/* 20 */     return (this.moveVector.y > 1.0E-5F);
/*    */   }
/*    */   
/*    */   public void makeJump() {
/* 24 */     this.keyPresses = new Input(this.keyPresses.forward(), this.keyPresses.backward(), this.keyPresses.left(), this.keyPresses.right(), true, this.keyPresses.shift(), this.keyPresses.sprint());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\player\ClientInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */