/*     */ package net.minecraft.client.model.object.book;
/*     */ 
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BookModel extends Model<BookModel.State> {
/*     */   private static final String LEFT_PAGES = "left_pages";
/*     */   
/*     */   private static final String RIGHT_PAGES = "right_pages";
/*     */   
/*     */   private static final String FLIP_PAGE_1 = "flip_page1";
/*     */   
/*     */   private static final String FLIP_PAGE_2 = "flip_page2";
/*     */   
/*     */   private final ModelPart leftLid;
/*     */   
/*     */   private final ModelPart rightLid;
/*     */   
/*     */   private final ModelPart leftPages;
/*     */   
/*     */   private final ModelPart rightPages;
/*     */   
/*     */   private final ModelPart flipPage1;
/*     */   
/*     */   private final ModelPart flipPage2;
/*     */   
/*     */   public BookModel(ModelPart root) {
/*  28 */     super(root, RenderTypes::entitySolid);
/*  29 */     this.leftLid = root.getChild("left_lid");
/*  30 */     this.rightLid = root.getChild("right_lid");
/*  31 */     this.leftPages = root.getChild("left_pages");
/*  32 */     this.rightPages = root.getChild("right_pages");
/*  33 */     this.flipPage1 = root.getChild("flip_page1");
/*  34 */     this.flipPage2 = root.getChild("flip_page2");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBodyLayer() {
/*  38 */     MeshDefinition mesh = new MeshDefinition();
/*  39 */     PartDefinition root = mesh.getRoot();
/*  41 */     root.addOrReplaceChild("left_lid", 
/*  42 */         CubeListBuilder.create()
/*  43 */         .texOffs(0, 0).addBox(-6.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), 
/*  44 */         PartPose.offset(0.0F, 0.0F, -1.0F));
/*  46 */     root.addOrReplaceChild("right_lid", 
/*  47 */         CubeListBuilder.create()
/*  48 */         .texOffs(16, 0).addBox(0.0F, -5.0F, -0.005F, 6.0F, 10.0F, 0.005F), 
/*  49 */         PartPose.offset(0.0F, 0.0F, 1.0F));
/*  51 */     root.addOrReplaceChild("seam", 
/*  52 */         CubeListBuilder.create()
/*  53 */         .texOffs(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.005F), 
/*  54 */         PartPose.rotation(0.0F, 1.5707964F, 0.0F));
/*  56 */     root.addOrReplaceChild("left_pages", 
/*  57 */         CubeListBuilder.create()
/*  58 */         .texOffs(0, 10).addBox(0.0F, -4.0F, -0.99F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
/*  61 */     root.addOrReplaceChild("right_pages", 
/*  62 */         CubeListBuilder.create()
/*  63 */         .texOffs(12, 10).addBox(0.0F, -4.0F, -0.01F, 5.0F, 8.0F, 1.0F), PartPose.ZERO);
/*  67 */     CubeListBuilder page = CubeListBuilder.create().texOffs(24, 10).addBox(0.0F, -4.0F, 0.0F, 5.0F, 8.0F, 0.005F);
/*  68 */     root.addOrReplaceChild("flip_page1", page, PartPose.ZERO);
/*  69 */     root.addOrReplaceChild("flip_page2", page, PartPose.ZERO);
/*  71 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   public void setupAnim(State state) {
/*  76 */     super.setupAnim(state);
/*  77 */     float openness = state.openness;
/*  79 */     this.leftLid.yRot = 3.1415927F + openness;
/*  80 */     this.rightLid.yRot = -openness;
/*  81 */     this.leftPages.yRot = openness;
/*  82 */     this.rightPages.yRot = -openness;
/*  84 */     this.flipPage1.yRot = openness - openness * 2.0F * state.pageFlip1;
/*  85 */     this.flipPage2.yRot = openness - openness * 2.0F * state.pageFlip2;
/*  87 */     this.leftPages.x = Mth.sin(openness);
/*  88 */     this.rightPages.x = Mth.sin(openness);
/*  89 */     this.flipPage1.x = Mth.sin(openness);
/*  90 */     this.flipPage2.x = Mth.sin(openness);
/*     */   }
/*     */   
/*     */   public static final class State extends Record {
/*     */     private final float openness;
/*     */     
/*     */     private final float pageFlip1;
/*     */     
/*     */     private final float pageFlip2;
/*     */     
/*     */     public State(float openness, float pageFlip1, float pageFlip2) {
/*  96 */       this.openness = openness;
/*  96 */       this.pageFlip1 = pageFlip1;
/*  96 */       this.pageFlip2 = pageFlip2;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/object/book/BookModel$State;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/object/book/BookModel$State;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/object/book/BookModel$State;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/object/book/BookModel$State;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float openness() {
/*  96 */       return this.openness;
/*     */     }
/*     */     
/*     */     public float pageFlip1() {
/*  96 */       return this.pageFlip1;
/*     */     }
/*     */     
/*     */     public float pageFlip2() {
/*  96 */       return this.pageFlip2;
/*     */     }
/*     */     
/*     */     public static State forAnimation(float progress, float pageFlip1, float pageFlip2, float openness) {
/* 102 */       return new State((
/* 103 */           Mth.sin((progress * 0.02F)) * 0.1F + 1.25F) * openness, pageFlip1, pageFlip2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\book\BookModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */