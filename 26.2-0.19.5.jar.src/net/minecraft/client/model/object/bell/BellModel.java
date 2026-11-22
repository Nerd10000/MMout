/*    */ package net.minecraft.client.model.object.bell;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class BellModel extends Model<BellModel.State> {
/*    */   private static final String BELL_BODY = "bell_body";
/*    */   
/*    */   private final ModelPart bellBody;
/*    */   
/*    */   public BellModel(ModelPart root) {
/* 21 */     super(root, RenderTypes::entitySolid);
/* 22 */     this.bellBody = root.getChild("bell_body");
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 26 */     MeshDefinition mesh = new MeshDefinition();
/* 27 */     PartDefinition root = mesh.getRoot();
/* 29 */     PartDefinition bellBody = root.addOrReplaceChild("bell_body", 
/* 30 */         CubeListBuilder.create()
/* 31 */         .texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F), 
/* 32 */         PartPose.offset(8.0F, 12.0F, 8.0F));
/* 34 */     bellBody.addOrReplaceChild("bell_base", 
/* 35 */         CubeListBuilder.create()
/* 36 */         .texOffs(0, 13).addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F), 
/* 37 */         PartPose.offset(-8.0F, -12.0F, -8.0F));
/* 40 */     return LayerDefinition.create(mesh, 32, 32);
/*    */   }
/*    */   
/*    */   public void setupAnim(State state) {
/* 45 */     super.setupAnim(state);
/* 47 */     float xRot = 0.0F;
/* 48 */     float zRot = 0.0F;
/* 50 */     if (state.shakeDirection != null) {
/* 51 */       float baseRot = Mth.sin((state.ticks / 3.1415927F)) / (4.0F + state.ticks / 3.0F);
/* 52 */       switch (state.shakeDirection) {
/*    */         case NORTH:
/* 53 */           xRot = -baseRot;
/*    */           break;
/*    */         case SOUTH:
/* 54 */           xRot = baseRot;
/*    */           break;
/*    */         case EAST:
/* 55 */           zRot = -baseRot;
/*    */           break;
/*    */         case WEST:
/* 56 */           zRot = baseRot;
/*    */           break;
/*    */       } 
/*    */     } 
/* 60 */     this.bellBody.xRot = xRot;
/* 61 */     this.bellBody.zRot = zRot;
/*    */   }
/*    */   
/*    */   public static final class State extends Record {
/*    */     private final float ticks;
/*    */     
/*    */     private final Direction shakeDirection;
/*    */     
/*    */     public State(float ticks, Direction shakeDirection) {
/* 64 */       this.ticks = ticks;
/* 64 */       this.shakeDirection = shakeDirection;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/object/bell/BellModel$State;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/object/bell/BellModel$State;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/object/bell/BellModel$State;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float ticks() {
/* 64 */       return this.ticks;
/*    */     }
/*    */     
/*    */     public Direction shakeDirection() {
/* 64 */       return this.shakeDirection;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\bell\BellModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */