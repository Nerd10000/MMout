/*    */ package net.minecraft.client.renderer.feature;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*    */ 
/*    */ public final class FeatureRendererType<Submit extends SubmitNode> extends Record {
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/feature/FeatureRendererType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FeatureRendererType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/feature/FeatureRendererType<TSubmit;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/feature/FeatureRendererType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/FeatureRendererType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/feature/FeatureRendererType<TSubmit;>;
/*    */   }
/*    */   
/*    */   public int id() {
/*  8 */     return this.id;
/*    */   }
/*    */   
/*    */   public String name() {
/*  8 */     return this.name;
/*    */   }
/*    */   
/* 12 */   private static final AtomicInteger NEXT_ID = new AtomicInteger();
/*    */   
/*    */   @Deprecated
/*    */   public FeatureRendererType(int id, String name) {
/* 16 */     this.id = id;
/* 16 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static <Submit extends SubmitNode> FeatureRendererType<Submit> create(String name) {
/* 20 */     return new FeatureRendererType<>(NEXT_ID.getAndIncrement(), name);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\feature\FeatureRendererType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */