/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Path {
/*     */   public static final StreamCodec<FriendlyByteBuf, Path> STREAM_CODEC;
/*     */   
/*     */   private final List<Node> nodes;
/*     */   
/*     */   private DebugData debugData;
/*     */   
/*     */   private int nextNodeIndex;
/*     */   
/*     */   private final BlockPos target;
/*     */   
/*     */   private final float distToTarget;
/*     */   
/*     */   private final boolean reached;
/*     */   
/*     */   static {
/*  22 */     STREAM_CODEC = StreamCodec.of((output, value) -> value.writeToStream(output), Path::createFromStream);
/*     */   }
/*     */   
/*     */   public Path(List<Node> nodes, BlockPos target, boolean reached) {
/*  34 */     this.nodes = nodes;
/*  35 */     this.target = target;
/*  37 */     this.distToTarget = nodes.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target);
/*  39 */     this.reached = reached;
/*     */   }
/*     */   
/*     */   public void advance() {
/*  43 */     this.nextNodeIndex++;
/*     */   }
/*     */   
/*     */   public boolean notStarted() {
/*  47 */     return (this.nextNodeIndex <= 0);
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  51 */     return (this.nextNodeIndex >= this.nodes.size());
/*     */   }
/*     */   
/*     */   public Node getEndNode() {
/*  55 */     if (!this.nodes.isEmpty())
/*  56 */       return this.nodes.get(this.nodes.size() - 1); 
/*  58 */     return null;
/*     */   }
/*     */   
/*     */   public Node getNode(int i) {
/*  62 */     return this.nodes.get(i);
/*     */   }
/*     */   
/*     */   public void truncateNodes(int index) {
/*  66 */     if (this.nodes.size() > index)
/*  67 */       this.nodes.subList(index, this.nodes.size()).clear(); 
/*     */   }
/*     */   
/*     */   public void replaceNode(int index, Node replaceWith) {
/*  72 */     this.nodes.set(index, replaceWith);
/*     */   }
/*     */   
/*     */   public int getNodeCount() {
/*  76 */     return this.nodes.size();
/*     */   }
/*     */   
/*     */   public int getNextNodeIndex() {
/*  80 */     return this.nextNodeIndex;
/*     */   }
/*     */   
/*     */   public void setNextNodeIndex(int nextNodeIndex) {
/*  84 */     this.nextNodeIndex = nextNodeIndex;
/*     */   }
/*     */   
/*     */   public Vec3 getEntityPosAtNode(Entity entity, int index) {
/*  88 */     Node node = this.nodes.get(index);
/*  89 */     double x = node.x + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  90 */     double y = node.y;
/*  91 */     double z = node.z + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  92 */     return new Vec3(x, y, z);
/*     */   }
/*     */   
/*     */   public BlockPos getNodePos(int index) {
/*  96 */     return ((Node)this.nodes.get(index)).asBlockPos();
/*     */   }
/*     */   
/*     */   public Vec3 getNextEntityPos(Entity entity) {
/* 101 */     return getEntityPosAtNode(entity, this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   public BlockPos getNextNodePos() {
/* 105 */     return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos();
/*     */   }
/*     */   
/*     */   public Node getNextNode() {
/* 109 */     return this.nodes.get(this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   public Node getPreviousNode() {
/* 113 */     return (this.nextNodeIndex > 0) ? this.nodes.get(this.nextNodeIndex - 1) : null;
/*     */   }
/*     */   
/*     */   public boolean sameAs(Path path) {
/* 117 */     return (path != null && this.nodes.equals(path.nodes));
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*     */     Path path;
/* 122 */     if (obj instanceof Path) {
/* 122 */       path = (Path)obj;
/*     */     } else {
/* 123 */       return false;
/*     */     } 
/* 125 */     return (this.nextNodeIndex == path.nextNodeIndex && this.debugData == path.debugData && this.reached == path.reached && this.target
/*     */       
/* 128 */       .equals(path.target) && this.nodes
/* 129 */       .equals(path.nodes));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 134 */     return this.nextNodeIndex + this.nodes.hashCode() * 31;
/*     */   }
/*     */   
/*     */   public boolean canReach() {
/* 139 */     return this.reached;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   void setDebug(Node[] openSet, Node[] closedSet, Set<Target> targets) {
/* 145 */     this.debugData = new DebugData(openSet, closedSet, targets);
/*     */   }
/*     */   
/*     */   public DebugData debugData() {
/* 149 */     return this.debugData;
/*     */   }
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf buffer) {
/* 153 */     if (this.debugData == null || this.debugData.targetNodes.isEmpty())
/* 154 */       throw new IllegalStateException("Missing debug data"); 
/* 157 */     buffer.writeBoolean(this.reached);
/* 158 */     buffer.writeInt(this.nextNodeIndex);
/* 159 */     buffer.writeBlockPos(this.target);
/* 160 */     buffer.writeCollection(this.nodes, (out, node) -> node.writeToStream(out));
/* 161 */     this.debugData.write(buffer);
/*     */   }
/*     */   
/*     */   public static Path createFromStream(FriendlyByteBuf buffer) {
/* 165 */     boolean reached = buffer.readBoolean();
/* 166 */     int indexStream = buffer.readInt();
/* 167 */     BlockPos target = buffer.readBlockPos();
/* 168 */     List<Node> nodes = buffer.readList(Node::createFromStream);
/* 169 */     DebugData debugData = DebugData.read(buffer);
/* 171 */     Path path = new Path(nodes, target, reached);
/* 172 */     path.debugData = debugData;
/* 173 */     path.nextNodeIndex = indexStream;
/* 175 */     return path;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 180 */     return "Path(length=" + this.nodes.size() + ")";
/*     */   }
/*     */   
/*     */   public BlockPos getTarget() {
/* 184 */     return this.target;
/*     */   }
/*     */   
/*     */   public float getDistToTarget() {
/* 188 */     return this.distToTarget;
/*     */   }
/*     */   
/*     */   private static Node[] readNodeArray(FriendlyByteBuf input) {
/* 192 */     Node[] nodes = new Node[input.readVarInt()];
/* 193 */     for (int i = 0; i < nodes.length; i++)
/* 194 */       nodes[i] = Node.createFromStream(input); 
/* 196 */     return nodes;
/*     */   }
/*     */   
/*     */   private static void writeNodeArray(FriendlyByteBuf output, Node[] nodes) {
/* 200 */     output.writeVarInt(nodes.length);
/* 201 */     for (Node node : nodes)
/* 202 */       node.writeToStream(output); 
/*     */   }
/*     */   
/*     */   public Path copy() {
/* 207 */     Path result = new Path(this.nodes, this.target, this.reached);
/* 208 */     result.debugData = this.debugData;
/* 209 */     result.nextNodeIndex = this.nextNodeIndex;
/* 210 */     return result;
/*     */   }
/*     */   
/*     */   public static final class DebugData extends Record {
/*     */     private final Node[] openSet;
/*     */     
/*     */     private final Node[] closedSet;
/*     */     
/*     */     private final Set<Target> targetNodes;
/*     */     
/*     */     public DebugData(Node[] openSet, Node[] closedSet, Set<Target> targetNodes) {
/* 213 */       this.openSet = openSet;
/* 213 */       this.closedSet = closedSet;
/* 213 */       this.targetNodes = targetNodes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #213	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Node[] openSet() {
/* 213 */       return this.openSet;
/*     */     }
/*     */     
/*     */     public Node[] closedSet() {
/* 213 */       return this.closedSet;
/*     */     }
/*     */     
/*     */     public Set<Target> targetNodes() {
/* 213 */       return this.targetNodes;
/*     */     }
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 220 */       output.writeCollection(this.targetNodes, (out, target) -> target.writeToStream(out));
/* 221 */       Path.writeNodeArray(output, this.openSet);
/* 222 */       Path.writeNodeArray(output, this.closedSet);
/*     */     }
/*     */     
/*     */     public static DebugData read(FriendlyByteBuf input) {
/* 226 */       HashSet<Target> targets = (HashSet<Target>)input.readCollection(HashSet::new, Target::createFromStream);
/* 227 */       Node[] openSet = Path.readNodeArray(input);
/* 228 */       Node[] closedSet = Path.readNodeArray(input);
/* 229 */       return new DebugData(openSet, closedSet, targets);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\pathfinder\Path.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */