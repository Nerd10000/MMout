/*    */ package net.minecraft.server.packs.metadata;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ public final class WithValue<T> extends Record {
/*    */   private final MetadataSectionType<T> type;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public WithValue(MetadataSectionType<T> type, T value) {
/* 16 */     this.type = type;
/* 16 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/MetadataSectionType$WithValue<TT;>;
/*    */   }
/*    */   
/*    */   public MetadataSectionType<T> type() {
/* 16 */     return this.type;
/*    */   }
/*    */   
/*    */   public T value() {
/* 16 */     return this.value;
/*    */   }
/*    */   
/*    */   public <U> Optional<U> unwrapToType(MetadataSectionType<U> type) {
/* 22 */     return (type == this.type) ? Optional.<U>of((U)this.value) : Optional.<U>empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\metadata\MetadataSectionType$WithValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */