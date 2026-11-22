/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshTransformer;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ 
/*    */ public final class BabyModelTransform extends Record implements MeshTransformer {
/*    */   private final boolean scaleHead;
/*    */   
/*    */   private final float babyYHeadOffset;
/*    */   
/*    */   private final float babyZHeadOffset;
/*    */   
/*    */   private final float babyHeadScale;
/*    */   
/*    */   private final float babyBodyScale;
/*    */   
/*    */   private final float bodyYOffset;
/*    */   
/*    */   private final Set<String> headParts;
/*    */   
/*    */   public BabyModelTransform(boolean scaleHead, float babyYHeadOffset, float babyZHeadOffset, float babyHeadScale, float babyBodyScale, float bodyYOffset, Set<String> headParts) {
/* 13 */     this.scaleHead = scaleHead;
/* 13 */     this.babyYHeadOffset = babyYHeadOffset;
/* 13 */     this.babyZHeadOffset = babyZHeadOffset;
/* 13 */     this.babyHeadScale = babyHeadScale;
/* 13 */     this.babyBodyScale = babyBodyScale;
/* 13 */     this.bodyYOffset = bodyYOffset;
/* 13 */     this.headParts = headParts;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/BabyModelTransform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/BabyModelTransform;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/BabyModelTransform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/BabyModelTransform;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/BabyModelTransform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/BabyModelTransform;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean scaleHead() {
/* 13 */     return this.scaleHead;
/*    */   }
/*    */   
/*    */   public float babyYHeadOffset() {
/* 13 */     return this.babyYHeadOffset;
/*    */   }
/*    */   
/*    */   public float babyZHeadOffset() {
/* 13 */     return this.babyZHeadOffset;
/*    */   }
/*    */   
/*    */   public float babyHeadScale() {
/* 13 */     return this.babyHeadScale;
/*    */   }
/*    */   
/*    */   public float babyBodyScale() {
/* 13 */     return this.babyBodyScale;
/*    */   }
/*    */   
/*    */   public float bodyYOffset() {
/* 13 */     return this.bodyYOffset;
/*    */   }
/*    */   
/*    */   public Set<String> headParts() {
/* 13 */     return this.headParts;
/*    */   }
/*    */   
/*    */   public BabyModelTransform(Set<String> headParts) {
/* 23 */     this(false, 5.0F, 2.0F, headParts);
/*    */   }
/*    */   
/*    */   public BabyModelTransform(boolean scaleHead, float babyYHeadOffset, float babyZHeadOffset, Set<String> headParts) {
/* 27 */     this(scaleHead, babyYHeadOffset, babyZHeadOffset, 2.0F, 2.0F, 24.0F, headParts);
/*    */   }
/*    */   
/*    */   public MeshDefinition apply(MeshDefinition mesh) {
/* 32 */     float headScale = this.scaleHead ? (1.5F / this.babyHeadScale) : 1.0F;
/* 33 */     float bodyScale = 1.0F / this.babyBodyScale;
/* 34 */     UnaryOperator<PartPose> headTransform = p -> p.translated(0.0F, this.babyYHeadOffset, this.babyZHeadOffset).scaled(headScale);
/* 35 */     UnaryOperator<PartPose> bodyTransform = p -> p.translated(0.0F, this.bodyYOffset, 0.0F).scaled(bodyScale);
/* 37 */     MeshDefinition babyMesh = new MeshDefinition();
/* 40 */     for (Map.Entry<String, PartDefinition> entry : (Iterable<Map.Entry<String, PartDefinition>>)mesh.getRoot().getChildren()) {
/* 41 */       String name = entry.getKey();
/* 42 */       PartDefinition part = entry.getValue();
/* 43 */       babyMesh.getRoot().addOrReplaceChild(name, part.transformed(this.headParts.contains(name) ? headTransform : bodyTransform));
/*    */     } 
/* 46 */     return babyMesh;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\BabyModelTransform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */