/*     */ package net.minecraft.client.model.object.chest;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ChestModel extends Model<Float> {
/*     */   private static final String BOTTOM = "bottom";
/*     */   
/*     */   private static final String LID = "lid";
/*     */   
/*     */   private static final String LOCK = "lock";
/*     */   
/*     */   private final ModelPart lid;
/*     */   
/*     */   private final ModelPart lock;
/*     */   
/*     */   public ChestModel(ModelPart root) {
/*  26 */     super(root, RenderTypes::entityCutoutCull);
/*  27 */     this.lid = root.getChild("lid");
/*  28 */     this.lock = root.getChild("lock");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSingleBodyLayer() {
/*  32 */     MeshDefinition mesh = new MeshDefinition();
/*  33 */     PartDefinition root = mesh.getRoot();
/*  35 */     root.addOrReplaceChild("bottom", 
/*  36 */         CubeListBuilder.create()
/*  37 */         .texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
/*  40 */     root.addOrReplaceChild("lid", 
/*  41 */         CubeListBuilder.create()
/*  42 */         .texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), 
/*  43 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*  45 */     root.addOrReplaceChild("lock", 
/*  46 */         CubeListBuilder.create()
/*  47 */         .texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), 
/*  48 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*  51 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createDoubleBodyRightLayer() {
/*  55 */     MeshDefinition mesh = new MeshDefinition();
/*  56 */     PartDefinition root = mesh.getRoot();
/*  58 */     Set<Direction> visibleFaces = Util.allOfEnumExcept((Enum)Direction.EAST);
/*  60 */     root.addOrReplaceChild("bottom", 
/*  61 */         CubeListBuilder.create()
/*  62 */         .texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, visibleFaces), PartPose.ZERO);
/*  65 */     root.addOrReplaceChild("lid", 
/*  66 */         CubeListBuilder.create()
/*  67 */         .texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, visibleFaces), 
/*  68 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*  70 */     root.addOrReplaceChild("lock", 
/*  71 */         CubeListBuilder.create()
/*  72 */         .texOffs(0, 0).addBox(15.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F, visibleFaces), 
/*  73 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*  75 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createDoubleBodyLeftLayer() {
/*  79 */     MeshDefinition mesh = new MeshDefinition();
/*  80 */     PartDefinition root = mesh.getRoot();
/*  82 */     Set<Direction> visibleFaces = Util.allOfEnumExcept((Enum)Direction.WEST);
/*  84 */     root.addOrReplaceChild("bottom", 
/*  85 */         CubeListBuilder.create()
/*  86 */         .texOffs(0, 19).addBox(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F, visibleFaces), PartPose.ZERO);
/*  89 */     root.addOrReplaceChild("lid", 
/*  90 */         CubeListBuilder.create()
/*  91 */         .texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, visibleFaces), 
/*  92 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/*  94 */     root.addOrReplaceChild("lock", 
/*  95 */         CubeListBuilder.create()
/*  96 */         .texOffs(0, 0).addBox(0.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F, visibleFaces), 
/*  97 */         PartPose.offset(0.0F, 9.0F, 1.0F));
/* 100 */     return LayerDefinition.create(mesh, 64, 64);
/*     */   }
/*     */   
/*     */   public void setupAnim(Float open) {
/* 105 */     super.setupAnim(open);
/* 106 */     this.lid.xRot = -(open.floatValue() * 1.5707964F);
/* 107 */     this.lock.xRot = this.lid.xRot;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\chest\ChestModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */