/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class Condition {
/*    */   private static final int NOT_TRIGGERED = -1;
/*    */   
/* 15 */   private int triggerTime = -1;
/*    */   
/*    */   public void trigger(int time) {
/* 18 */     if (this.triggerTime != -1)
/* 19 */       throw new IllegalStateException("Condition already triggered at " + this.triggerTime); 
/* 21 */     this.triggerTime = time;
/*    */   }
/*    */   
/*    */   public void assertTriggeredThisTick() {
/* 25 */     int tick = GameTestSequence.this.parent.getTick();
/* 26 */     if (this.triggerTime != tick) {
/* 27 */       if (this.triggerTime == -1)
/* 28 */         throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_not_triggered"), tick); 
/* 30 */       throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_already_triggered", new Object[] { Integer.valueOf(this.triggerTime) }), tick);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestSequence$Condition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */