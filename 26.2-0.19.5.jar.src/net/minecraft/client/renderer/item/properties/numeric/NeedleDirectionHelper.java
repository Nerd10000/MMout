/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class NeedleDirectionHelper {
/*    */   private final boolean wobble;
/*    */   
/*    */   protected NeedleDirectionHelper(boolean wobble) {
/* 13 */     this.wobble = wobble;
/*    */   }
/*    */   
/*    */   public float get(ItemStack itemStack, ClientLevel clientLevel, ItemOwner owner, int seed) {
/* 17 */     if (owner == null)
/* 18 */       return 0.0F; 
/* 21 */     if (clientLevel == null) {
/* 21 */       Level level = owner.level();
/* 21 */       if (level instanceof ClientLevel) {
/* 21 */         ClientLevel clientLevel1 = (ClientLevel)level;
/* 22 */         clientLevel = clientLevel1;
/*    */       } 
/*    */     } 
/* 25 */     if (clientLevel == null)
/* 26 */       return 0.0F; 
/* 29 */     return calculate(itemStack, clientLevel, seed, owner);
/*    */   }
/*    */   
/*    */   protected abstract float calculate(ItemStack paramItemStack, ClientLevel paramClientLevel, int paramInt, ItemOwner paramItemOwner);
/*    */   
/*    */   protected boolean wobble() {
/* 35 */     return this.wobble;
/*    */   }
/*    */   
/*    */   protected Wobbler newWobbler(float factor) {
/* 39 */     return this.wobble ? standardWobbler(factor) : nonWobbler();
/*    */   }
/*    */   
/*    */   public static Wobbler standardWobbler(final float factor) {
/* 51 */     return new Wobbler() {
/*    */         private float rotation;
/*    */         
/*    */         private float deltaRotation;
/*    */         
/*    */         private long lastUpdateTick;
/*    */         
/*    */         public float rotation() {
/* 58 */           return this.rotation;
/*    */         }
/*    */         
/*    */         public boolean shouldUpdate(long tick) {
/* 63 */           return (this.lastUpdateTick != tick);
/*    */         }
/*    */         
/*    */         public void update(long tick, float targetRotation) {
/* 68 */           this.lastUpdateTick = tick;
/* 69 */           float tempDeltaRotation = Mth.positiveModulo(targetRotation - this.rotation + 0.5F, 1.0F) - 0.5F;
/* 71 */           this.deltaRotation += tempDeltaRotation * 0.1F;
/* 72 */           this.deltaRotation *= factor;
/* 73 */           this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0F);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static interface Wobbler {
/*    */     float rotation();
/*    */     
/*    */     boolean shouldUpdate(long param1Long);
/*    */     
/*    */     void update(long param1Long, float param1Float);
/*    */   }
/*    */   
/*    */   public static Wobbler nonWobbler() {
/* 79 */     return new Wobbler() {
/*    */         private float targetValue;
/*    */         
/*    */         public float rotation() {
/* 84 */           return this.targetValue;
/*    */         }
/*    */         
/*    */         public boolean shouldUpdate(long tick) {
/* 89 */           return true;
/*    */         }
/*    */         
/*    */         public void update(long tick, float targetRotation) {
/* 94 */           this.targetValue = targetRotation;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\NeedleDirectionHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */