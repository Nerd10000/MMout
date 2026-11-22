/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class Branch implements Octree.Node {
/*  55 */   private final Octree.Node[] nodes = new Octree.Node[8];
/*     */   
/*     */   private final BoundingBox boundingBox;
/*     */   
/*     */   private final int bbCenterX;
/*     */   
/*     */   private final int bbCenterY;
/*     */   
/*     */   private final int bbCenterZ;
/*     */   
/*     */   private final Octree.AxisSorting sorting;
/*     */   
/*     */   private final boolean cameraXDiffNegative;
/*     */   
/*     */   private final boolean cameraYDiffNegative;
/*     */   
/*     */   private final boolean cameraZDiffNegative;
/*     */   
/*     */   public Branch(BoundingBox boundingBox) {
/*  69 */     this.boundingBox = boundingBox;
/*  70 */     this.bbCenterX = this.boundingBox.minX() + this.boundingBox.getXSpan() / 2;
/*  71 */     this.bbCenterY = this.boundingBox.minY() + this.boundingBox.getYSpan() / 2;
/*  72 */     this.bbCenterZ = this.boundingBox.minZ() + this.boundingBox.getZSpan() / 2;
/*  74 */     int cameraXDiff = paramOctree.cameraSectionCenter.getX() - this.bbCenterX;
/*  75 */     int cameraYDiff = paramOctree.cameraSectionCenter.getY() - this.bbCenterY;
/*  76 */     int cameraZDiff = paramOctree.cameraSectionCenter.getZ() - this.bbCenterZ;
/*  78 */     this.sorting = Octree.AxisSorting.getAxisSorting(Math.abs(cameraXDiff), Math.abs(cameraYDiff), Math.abs(cameraZDiff));
/*  80 */     this.cameraXDiffNegative = (cameraXDiff < 0);
/*  81 */     this.cameraYDiffNegative = (cameraYDiff < 0);
/*  82 */     this.cameraZDiffNegative = (cameraZDiff < 0);
/*     */   }
/*     */   
/*     */   public boolean add(SectionRenderDispatcher.RenderSection section) {
/*  86 */     long sectionNode = section.getSectionNode();
/*  87 */     boolean sectionXDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode)) - this.bbCenterX < 0);
/*  88 */     boolean sectionYDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode)) - this.bbCenterY < 0);
/*  89 */     boolean sectionZDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode)) - this.bbCenterZ < 0);
/*  91 */     boolean xDiffsOppositeSides = (sectionXDiffNegative != this.cameraXDiffNegative);
/*  92 */     boolean yDiffsOppositeSides = (sectionYDiffNegative != this.cameraYDiffNegative);
/*  93 */     boolean zDiffsOppositeSides = (sectionZDiffNegative != this.cameraZDiffNegative);
/*  95 */     int nodeIndex = getNodeIndex(this.sorting, xDiffsOppositeSides, yDiffsOppositeSides, zDiffsOppositeSides);
/*  97 */     if (areChildrenLeaves()) {
/*  98 */       boolean alreadyExisted = (this.nodes[nodeIndex] != null);
/*  99 */       this.nodes[nodeIndex] = new Octree.Leaf(Octree.this, section);
/* 100 */       return !alreadyExisted;
/*     */     } 
/* 103 */     if (this.nodes[nodeIndex] != null) {
/* 104 */       Branch branch1 = (Branch)this.nodes[nodeIndex];
/* 105 */       return branch1.add(section);
/*     */     } 
/* 108 */     BoundingBox childBoundingBox = createChildBoundingBox(sectionXDiffNegative, sectionYDiffNegative, sectionZDiffNegative);
/* 109 */     Branch branch = new Branch(childBoundingBox);
/* 110 */     this.nodes[nodeIndex] = branch;
/* 111 */     return branch.add(section);
/*     */   }
/*     */   
/*     */   private static int getNodeIndex(Octree.AxisSorting sorting, boolean xDiffsOppositeSides, boolean yDiffsOppositeSides, boolean zDiffsOppositeSides) {
/* 115 */     int index = 0;
/* 116 */     if (xDiffsOppositeSides)
/* 117 */       index += sorting.xShift; 
/* 119 */     if (yDiffsOppositeSides)
/* 120 */       index += sorting.yShift; 
/* 122 */     if (zDiffsOppositeSides)
/* 123 */       index += sorting.zShift; 
/* 125 */     return index;
/*     */   }
/*     */   
/*     */   private boolean areChildrenLeaves() {
/* 129 */     return (this.boundingBox.getXSpan() == 32);
/*     */   }
/*     */   
/*     */   private BoundingBox createChildBoundingBox(boolean sectionXDiffNegative, boolean sectionYDiffNegative, boolean sectionZDiffNegative) {
/*     */     int minX;
/*     */     int maxX;
/*     */     int minY;
/*     */     int maxY;
/*     */     int minZ;
/*     */     int maxZ;
/* 140 */     if (sectionXDiffNegative) {
/* 141 */       minX = this.boundingBox.minX();
/* 142 */       maxX = this.bbCenterX - 1;
/*     */     } else {
/* 144 */       minX = this.bbCenterX;
/* 145 */       maxX = this.boundingBox.maxX();
/*     */     } 
/* 147 */     if (sectionYDiffNegative) {
/* 148 */       minY = this.boundingBox.minY();
/* 149 */       maxY = this.bbCenterY - 1;
/*     */     } else {
/* 151 */       minY = this.bbCenterY;
/* 152 */       maxY = this.boundingBox.maxY();
/*     */     } 
/* 154 */     if (sectionZDiffNegative) {
/* 155 */       minZ = this.boundingBox.minZ();
/* 156 */       maxZ = this.bbCenterZ - 1;
/*     */     } else {
/* 158 */       minZ = this.bbCenterZ;
/* 159 */       maxZ = this.boundingBox.maxZ();
/*     */     } 
/* 161 */     return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public void visitNodes(Octree.OctreeVisitor visitor, boolean skipFrustumCheck, Frustum frustum, int depth, int closeDistance, boolean isClose) {
/* 166 */     boolean isVisible = skipFrustumCheck;
/* 167 */     if (!skipFrustumCheck) {
/* 168 */       int checkResult = frustum.cubeInFrustum(this.boundingBox);
/* 169 */       skipFrustumCheck = (checkResult == -2);
/* 170 */       isVisible = (checkResult == -2 || checkResult == -1);
/*     */     } 
/* 172 */     if (isVisible) {
/* 173 */       isClose = (isClose && Octree.this.isClose(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), closeDistance));
/* 174 */       visitor.visit(this, skipFrustumCheck, depth, isClose);
/* 175 */       for (Octree.Node node : this.nodes) {
/* 176 */         if (node != null)
/* 177 */           node.visitNodes(visitor, skipFrustumCheck, frustum, depth + 1, closeDistance, isClose); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public SectionRenderDispatcher.RenderSection getSection() {
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   public AABB getAABB() {
/* 190 */     return new AABB(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ(), (this.boundingBox.maxX() + 1), (this.boundingBox.maxY() + 1), (this.boundingBox.maxZ() + 1));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Octree$Branch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */