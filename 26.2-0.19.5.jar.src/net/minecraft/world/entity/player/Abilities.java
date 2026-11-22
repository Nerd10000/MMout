/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class Abilities {
/*    */   private static final boolean DEFAULT_INVULNERABLE = false;
/*    */   
/*    */   private static final boolean DEFAULY_FLYING = false;
/*    */   
/*    */   private static final boolean DEFAULT_MAY_FLY = false;
/*    */   
/*    */   private static final boolean DEFAULT_INSTABUILD = false;
/*    */   
/*    */   private static final boolean DEFAULT_MAY_BUILD = true;
/*    */   
/*    */   private static final float DEFAULT_FLYING_SPEED = 0.05F;
/*    */   
/*    */   private static final float DEFAULT_WALKING_SPEED = 0.1F;
/*    */   
/*    */   public boolean invulnerable;
/*    */   
/*    */   public boolean flying;
/*    */   
/*    */   public boolean mayfly;
/*    */   
/*    */   public boolean instabuild;
/*    */   
/*    */   public boolean mayBuild = true;
/*    */   
/* 21 */   private float flyingSpeed = 0.05F;
/*    */   
/* 22 */   private float walkingSpeed = 0.1F;
/*    */   
/*    */   public float getFlyingSpeed() {
/* 25 */     return this.flyingSpeed;
/*    */   }
/*    */   
/*    */   public void setFlyingSpeed(float value) {
/* 29 */     this.flyingSpeed = value;
/*    */   }
/*    */   
/*    */   public float getWalkingSpeed() {
/* 33 */     return this.walkingSpeed;
/*    */   }
/*    */   
/*    */   public void setWalkingSpeed(float value) {
/* 37 */     this.walkingSpeed = value;
/*    */   }
/*    */   
/*    */   public Packed pack() {
/* 41 */     return new Packed(this.invulnerable, this.flying, this.mayfly, this.instabuild, this.mayBuild, this.flyingSpeed, this.walkingSpeed);
/*    */   }
/*    */   
/*    */   public void apply(Packed packed) {
/* 53 */     this.invulnerable = packed.invulnerable;
/* 54 */     this.flying = packed.flying;
/* 55 */     this.mayfly = packed.mayFly;
/* 56 */     this.instabuild = packed.instabuild;
/* 57 */     this.mayBuild = packed.mayBuild;
/* 58 */     this.flyingSpeed = packed.flyingSpeed;
/* 59 */     this.walkingSpeed = packed.walkingSpeed;
/*    */   }
/*    */   
/*    */   public static final class Packed extends Record {
/*    */     private final boolean invulnerable;
/*    */     
/*    */     private final boolean flying;
/*    */     
/*    */     private final boolean mayFly;
/*    */     
/*    */     private final boolean instabuild;
/*    */     
/*    */     private final boolean mayBuild;
/*    */     
/*    */     private final float flyingSpeed;
/*    */     
/*    */     private final float walkingSpeed;
/*    */     
/*    */     public static final Codec<Packed> CODEC;
/*    */     
/*    */     public Packed(boolean invulnerable, boolean flying, boolean mayFly, boolean instabuild, boolean mayBuild, float flyingSpeed, float walkingSpeed) {
/* 62 */       this.invulnerable = invulnerable;
/* 62 */       this.flying = flying;
/* 62 */       this.mayFly = mayFly;
/* 62 */       this.instabuild = instabuild;
/* 62 */       this.mayBuild = mayBuild;
/* 62 */       this.flyingSpeed = flyingSpeed;
/* 62 */       this.walkingSpeed = walkingSpeed;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Abilities$Packed;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Abilities$Packed;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Abilities$Packed;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/player/Abilities$Packed;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public boolean invulnerable() {
/* 62 */       return this.invulnerable;
/*    */     }
/*    */     
/*    */     public boolean flying() {
/* 62 */       return this.flying;
/*    */     }
/*    */     
/*    */     public boolean mayFly() {
/* 62 */       return this.mayFly;
/*    */     }
/*    */     
/*    */     public boolean instabuild() {
/* 62 */       return this.instabuild;
/*    */     }
/*    */     
/*    */     public boolean mayBuild() {
/* 62 */       return this.mayBuild;
/*    */     }
/*    */     
/*    */     public float flyingSpeed() {
/* 62 */       return this.flyingSpeed;
/*    */     }
/*    */     
/*    */     public float walkingSpeed() {
/* 62 */       return this.walkingSpeed;
/*    */     }
/*    */     
/*    */     static {
/* 71 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "invulnerable", Boolean.valueOf(false)).forGetter(Packed::invulnerable), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "flying", Boolean.valueOf(false)).forGetter(Packed::flying), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "mayfly", Boolean.valueOf(false)).forGetter(Packed::mayFly), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "instabuild", Boolean.valueOf(false)).forGetter(Packed::instabuild), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.BOOL, "mayBuild", Boolean.valueOf(true)).forGetter(Packed::mayBuild), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.FLOAT, "flySpeed", Float.valueOf(0.05F)).forGetter(Packed::flyingSpeed), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.FLOAT, "walkSpeed", Float.valueOf(0.1F)).forGetter(Packed::walkingSpeed)).apply((Applicative)i, Packed::new));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Abilities.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */