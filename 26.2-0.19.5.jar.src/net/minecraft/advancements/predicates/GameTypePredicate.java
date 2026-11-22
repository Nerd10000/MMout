/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public final class GameTypePredicate extends Record {
/*    */   private final List<GameType> types;
/*    */   
/*    */   public GameTypePredicate(List<GameType> types) {
/*  9 */     this.types = types;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/GameTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/GameTypePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/GameTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/GameTypePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/GameTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/GameTypePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<GameType> types() {
/*  9 */     return this.types;
/*    */   }
/*    */   
/* 10 */   public static final GameTypePredicate ANY = of(GameType.values());
/*    */   
/* 11 */   public static final GameTypePredicate SURVIVAL_LIKE = of(new GameType[] { GameType.SURVIVAL, GameType.ADVENTURE });
/*    */   
/* 13 */   public static final Codec<GameTypePredicate> CODEC = GameType.CODEC.listOf().xmap(GameTypePredicate::new, GameTypePredicate::types);
/*    */   
/*    */   public static GameTypePredicate of(GameType... types) {
/* 16 */     return new GameTypePredicate(Arrays.<GameType>stream(types).toList());
/*    */   }
/*    */   
/*    */   public boolean matches(GameType type) {
/* 20 */     return this.types.contains(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\GameTypePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */