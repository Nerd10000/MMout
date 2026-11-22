/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.HolderOwner;
/*    */ 
/*    */ public final class RegistryInfo<T> extends Record {
/*    */   private final HolderOwner<T> owner;
/*    */   
/*    */   private final HolderGetter<T> getter;
/*    */   
/*    */   private final Lifecycle elementsLifecycle;
/*    */   
/*    */   public RegistryInfo(HolderOwner<T> owner, HolderGetter<T> getter, Lifecycle elementsLifecycle) {
/* 20 */     this.owner = owner;
/* 20 */     this.getter = getter;
/* 20 */     this.elementsLifecycle = elementsLifecycle;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryOps$RegistryInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/resources/RegistryOps$RegistryInfo<TT;>;
/*    */   }
/*    */   
/*    */   public HolderOwner<T> owner() {
/* 20 */     return this.owner;
/*    */   }
/*    */   
/*    */   public HolderGetter<T> getter() {
/* 20 */     return this.getter;
/*    */   }
/*    */   
/*    */   public Lifecycle elementsLifecycle() {
/* 20 */     return this.elementsLifecycle;
/*    */   }
/*    */   
/*    */   public static <T> RegistryInfo<T> fromRegistryLookup(HolderLookup.RegistryLookup<T> registry) {
/* 22 */     return new RegistryInfo<>((HolderOwner<T>)registry, (HolderGetter<T>)registry, registry.registryLifecycle());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryOps$RegistryInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */