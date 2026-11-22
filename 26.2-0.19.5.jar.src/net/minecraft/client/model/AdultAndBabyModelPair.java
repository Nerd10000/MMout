/*   */ package net.minecraft.client.model;
/*   */ 
/*   */ public final class AdultAndBabyModelPair<T extends Model<?>> extends Record {
/*   */   private final T adultModel;
/*   */   
/*   */   private final T babyModel;
/*   */   
/*   */   public AdultAndBabyModelPair(T adultModel, T babyModel) {
/* 3 */     this.adultModel = adultModel;
/* 3 */     this.babyModel = babyModel;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/AdultAndBabyModelPair;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/AdultAndBabyModelPair;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/AdultAndBabyModelPair;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	8	0	this	Lnet/minecraft/client/model/AdultAndBabyModelPair<TT;>;
/*   */   }
/*   */   
/*   */   public T adultModel() {
/* 3 */     return this.adultModel;
/*   */   }
/*   */   
/*   */   public T babyModel() {
/* 3 */     return this.babyModel;
/*   */   }
/*   */   
/*   */   public T getModel(boolean isBaby) {
/* 5 */     return isBaby ? this.babyModel : this.adultModel;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\AdultAndBabyModelPair.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */