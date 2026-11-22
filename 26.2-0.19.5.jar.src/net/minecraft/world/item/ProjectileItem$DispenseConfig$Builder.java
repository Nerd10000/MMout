/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Builder {
/*    */   private ProjectileItem.PositionFunction positionFunction;
/*    */   
/*    */   private float uncertainty;
/*    */   
/*    */   private float power;
/*    */   
/*    */   private OptionalInt overrideDispenseEvent;
/*    */   
/*    */   public Builder() {
/* 37 */     this.positionFunction = ((source, direction) -> DispenserBlock.getDispensePosition(source, 0.7D, new Vec3(0.0D, 0.1D, 0.0D)));
/* 38 */     this.uncertainty = 6.0F;
/* 39 */     this.power = 1.1F;
/* 40 */     this.overrideDispenseEvent = OptionalInt.empty();
/*    */   }
/*    */   
/*    */   public Builder positionFunction(ProjectileItem.PositionFunction positionFunction) {
/* 43 */     this.positionFunction = positionFunction;
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public Builder uncertainty(float uncertainty) {
/* 48 */     this.uncertainty = uncertainty;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder power(float power) {
/* 53 */     this.power = power;
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public Builder overrideDispenseEvent(int dispenseEvent) {
/* 58 */     this.overrideDispenseEvent = OptionalInt.of(dispenseEvent);
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public ProjectileItem.DispenseConfig build() {
/* 63 */     return new ProjectileItem.DispenseConfig(this.positionFunction, this.uncertainty, this.power, this.overrideDispenseEvent);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ProjectileItem$DispenseConfig$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */