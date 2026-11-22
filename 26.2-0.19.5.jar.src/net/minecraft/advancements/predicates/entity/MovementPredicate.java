/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MovementPredicate extends Record implements EntitySubPredicate {
/*    */   private final MinMaxBounds.Doubles x;
/*    */   
/*    */   private final MinMaxBounds.Doubles y;
/*    */   
/*    */   private final MinMaxBounds.Doubles z;
/*    */   
/*    */   private final MinMaxBounds.Doubles speed;
/*    */   
/*    */   private final MinMaxBounds.Doubles horizontalSpeed;
/*    */   
/*    */   private final MinMaxBounds.Doubles verticalSpeed;
/*    */   
/*    */   private final MinMaxBounds.Doubles fallDistance;
/*    */   
/*    */   public static final Codec<MovementPredicate> CODEC;
/*    */   
/*    */   public MovementPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z, MinMaxBounds.Doubles speed, MinMaxBounds.Doubles horizontalSpeed, MinMaxBounds.Doubles verticalSpeed, MinMaxBounds.Doubles fallDistance) {
/* 13 */     this.x = x;
/* 13 */     this.y = y;
/* 13 */     this.z = z;
/* 13 */     this.speed = speed;
/* 13 */     this.horizontalSpeed = horizontalSpeed;
/* 13 */     this.verticalSpeed = verticalSpeed;
/* 13 */     this.fallDistance = fallDistance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/entity/MovementPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/MovementPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/entity/MovementPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/entity/MovementPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/entity/MovementPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/entity/MovementPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles x() {
/* 13 */     return this.x;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles y() {
/* 13 */     return this.y;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles z() {
/* 13 */     return this.z;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles speed() {
/* 13 */     return this.speed;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles horizontalSpeed() {
/* 13 */     return this.horizontalSpeed;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles verticalSpeed() {
/* 13 */     return this.verticalSpeed;
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles fallDistance() {
/* 13 */     return this.fallDistance;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::x), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::y), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::z), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::speed), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("horizontal_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::horizontalSpeed), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("vertical_speed", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::verticalSpeed), (App)MinMaxBounds.Doubles.CODEC.optionalFieldOf("fall_distance", MinMaxBounds.Doubles.ANY).forGetter(MovementPredicate::fallDistance)).apply((Applicative)i, MovementPredicate::new));
/*    */   }
/*    */   
/*    */   public static MovementPredicate speed(MinMaxBounds.Doubles bounds) {
/* 33 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate horizontalSpeed(MinMaxBounds.Doubles bounds) {
/* 37 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate verticalSpeed(MinMaxBounds.Doubles bounds) {
/* 41 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds, MinMaxBounds.Doubles.ANY);
/*    */   }
/*    */   
/*    */   public static MovementPredicate fallDistance(MinMaxBounds.Doubles bounds) {
/* 45 */     return new MovementPredicate(MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, MinMaxBounds.Doubles.ANY, bounds);
/*    */   }
/*    */   
/*    */   public boolean matches(double x, double y, double z, double fallDistance) {
/* 49 */     if (!this.x.matches(x) || !this.y.matches(y) || !this.z.matches(z))
/* 50 */       return false; 
/* 53 */     double speedSqr = Mth.lengthSquared(x, y, z);
/* 54 */     if (!this.speed.matchesSqr(speedSqr))
/* 55 */       return false; 
/* 58 */     double horizontalSpeedSqr = Mth.lengthSquared(x, z);
/* 59 */     if (!this.horizontalSpeed.matchesSqr(horizontalSpeedSqr))
/* 60 */       return false; 
/* 63 */     double verticalSpeed = Math.abs(y);
/* 64 */     if (!this.verticalSpeed.matches(verticalSpeed))
/* 65 */       return false; 
/* 68 */     if (!this.fallDistance.matches(fallDistance))
/* 69 */       return false; 
/* 71 */     return true;
/*    */   }
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 76 */     Vec3 velocity = entity.getKnownMovement().scale(20.0D);
/* 77 */     return matches(velocity.x, velocity.y, velocity.z, entity.fallDistance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\MovementPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */