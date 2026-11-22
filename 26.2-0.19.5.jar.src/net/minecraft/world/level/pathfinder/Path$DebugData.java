/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ public final class DebugData extends Record {
/*     */   private final Node[] openSet;
/*     */   
/*     */   private final Node[] closedSet;
/*     */   
/*     */   private final Set<Target> targetNodes;
/*     */   
/*     */   public DebugData(Node[] openSet, Node[] closedSet, Set<Target> targetNodes) {
/* 213 */     this.openSet = openSet;
/* 213 */     this.closedSet = closedSet;
/* 213 */     this.targetNodes = targetNodes;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #213	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #213	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #213	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Node[] openSet() {
/* 213 */     return this.openSet;
/*     */   }
/*     */   
/*     */   public Node[] closedSet() {
/* 213 */     return this.closedSet;
/*     */   }
/*     */   
/*     */   public Set<Target> targetNodes() {
/* 213 */     return this.targetNodes;
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/* 220 */     output.writeCollection(this.targetNodes, (out, target) -> target.writeToStream(out));
/* 221 */     Path.writeNodeArray(output, this.openSet);
/* 222 */     Path.writeNodeArray(output, this.closedSet);
/*     */   }
/*     */   
/*     */   public static DebugData read(FriendlyByteBuf input) {
/* 226 */     HashSet<Target> targets = (HashSet<Target>)input.readCollection(HashSet::new, Target::createFromStream);
/* 227 */     Node[] openSet = Path.readNodeArray(input);
/* 228 */     Node[] closedSet = Path.readNodeArray(input);
/* 229 */     return new DebugData(openSet, closedSet, targets);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\pathfinder\Path$DebugData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */