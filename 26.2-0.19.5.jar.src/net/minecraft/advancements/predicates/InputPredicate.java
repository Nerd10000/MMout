/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.player.Input;
/*    */ 
/*    */ public final class InputPredicate extends Record {
/*    */   private final Optional<Boolean> forward;
/*    */   
/*    */   private final Optional<Boolean> backward;
/*    */   
/*    */   private final Optional<Boolean> left;
/*    */   
/*    */   private final Optional<Boolean> right;
/*    */   
/*    */   private final Optional<Boolean> jump;
/*    */   
/*    */   private final Optional<Boolean> sneak;
/*    */   
/*    */   private final Optional<Boolean> sprint;
/*    */   
/*    */   public static final Codec<InputPredicate> CODEC;
/*    */   
/*    */   public InputPredicate(Optional<Boolean> forward, Optional<Boolean> backward, Optional<Boolean> left, Optional<Boolean> right, Optional<Boolean> jump, Optional<Boolean> sneak, Optional<Boolean> sprint) {
/*  9 */     this.forward = forward;
/*  9 */     this.backward = backward;
/*  9 */     this.left = left;
/*  9 */     this.right = right;
/*  9 */     this.jump = jump;
/*  9 */     this.sneak = sneak;
/*  9 */     this.sprint = sprint;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/InputPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/InputPredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/InputPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/InputPredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/InputPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/InputPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> forward() {
/*  9 */     return this.forward;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> backward() {
/*  9 */     return this.backward;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> left() {
/*  9 */     return this.left;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> right() {
/*  9 */     return this.right;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> jump() {
/*  9 */     return this.jump;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> sneak() {
/*  9 */     return this.sneak;
/*    */   }
/*    */   
/*    */   public Optional<Boolean> sprint() {
/*  9 */     return this.sprint;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("forward").forGetter(InputPredicate::forward), (App)Codec.BOOL.optionalFieldOf("backward").forGetter(InputPredicate::backward), (App)Codec.BOOL.optionalFieldOf("left").forGetter(InputPredicate::left), (App)Codec.BOOL.optionalFieldOf("right").forGetter(InputPredicate::right), (App)Codec.BOOL.optionalFieldOf("jump").forGetter(InputPredicate::jump), (App)Codec.BOOL.optionalFieldOf("sneak").forGetter(InputPredicate::sneak), (App)Codec.BOOL.optionalFieldOf("sprint").forGetter(InputPredicate::sprint)).apply((Applicative)i, InputPredicate::new));
/*    */   }
/*    */   
/*    */   public boolean matches(Input input) {
/* 29 */     return (matches(this.forward, input.forward()) && 
/* 30 */       matches(this.backward, input.backward()) && 
/* 31 */       matches(this.left, input.left()) && 
/* 32 */       matches(this.right, input.right()) && 
/* 33 */       matches(this.jump, input.jump()) && 
/* 34 */       matches(this.sneak, input.shift()) && 
/* 35 */       matches(this.sprint, input.sprint()));
/*    */   }
/*    */   
/*    */   private boolean matches(Optional<Boolean> match, boolean value) {
/* 39 */     return ((Boolean)match.<Boolean>map(b -> Boolean.valueOf((b.booleanValue() == value))).orElse(Boolean.valueOf(true))).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\InputPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */