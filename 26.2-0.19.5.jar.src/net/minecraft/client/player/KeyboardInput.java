/*    */ package net.minecraft.client.player;
/*    */ 
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.world.entity.player.Input;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class KeyboardInput extends ClientInput {
/*    */   private final Options options;
/*    */   
/*    */   public KeyboardInput(Options options) {
/* 11 */     this.options = options;
/*    */   }
/*    */   
/*    */   private static float calculateImpulse(boolean positive, boolean negative) {
/* 15 */     if (positive == negative)
/* 16 */       return 0.0F; 
/* 19 */     return positive ? 1.0F : -1.0F;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 24 */     this
/*    */       
/* 31 */       .keyPresses = new Input(this.options.keyUp.isDown(), this.options.keyDown.isDown(), this.options.keyLeft.isDown(), this.options.keyRight.isDown(), this.options.keyJump.isDown(), this.options.keyShift.isDown(), this.options.keySprint.isDown());
/* 34 */     float forwardImpulse = calculateImpulse(this.keyPresses.forward(), this.keyPresses.backward());
/* 35 */     float leftImpulse = calculateImpulse(this.keyPresses.left(), this.keyPresses.right());
/* 36 */     this.moveVector = (new Vec2(leftImpulse, forwardImpulse)).normalized();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\player\KeyboardInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */