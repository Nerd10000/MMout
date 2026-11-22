/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.math.Quadrant;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class SimpleModelState extends Record {
/*    */   private final Quadrant x;
/*    */   
/*    */   private final Quadrant y;
/*    */   
/*    */   private final Quadrant z;
/*    */   
/*    */   private final boolean uvLock;
/*    */   
/*    */   public static final MapCodec<SimpleModelState> MAP_CODEC;
/*    */   
/*    */   public SimpleModelState(Quadrant x, Quadrant y, Quadrant z, boolean uvLock) {
/* 64 */     this.x = x;
/* 64 */     this.y = y;
/* 64 */     this.z = z;
/* 64 */     this.uvLock = uvLock;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/Variant$SimpleModelState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Quadrant x() {
/* 64 */     return this.x;
/*    */   }
/*    */   
/*    */   public Quadrant y() {
/* 64 */     return this.y;
/*    */   }
/*    */   
/*    */   public Quadrant z() {
/* 64 */     return this.z;
/*    */   }
/*    */   
/*    */   public boolean uvLock() {
/* 64 */     return this.uvLock;
/*    */   }
/*    */   
/*    */   static {
/* 70 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Quadrant.CODEC.optionalFieldOf("x", Quadrant.R0).forGetter(SimpleModelState::x), (App)Quadrant.CODEC.optionalFieldOf("y", Quadrant.R0).forGetter(SimpleModelState::y), (App)Quadrant.CODEC.optionalFieldOf("z", Quadrant.R0).forGetter(SimpleModelState::z), (App)Codec.BOOL.optionalFieldOf("uvlock", Boolean.valueOf(false)).forGetter(SimpleModelState::uvLock)).apply((Applicative)i, SimpleModelState::new));
/*    */   }
/*    */   
/* 77 */   public static final SimpleModelState DEFAULT = new SimpleModelState(Quadrant.R0, Quadrant.R0, Quadrant.R0, false);
/*    */   
/*    */   public ModelState asModelState() {
/* 80 */     BlockModelRotation rotation = BlockModelRotation.get(Quadrant.fromXYZAngles(this.x, this.y, this.z));
/* 81 */     return this.uvLock ? rotation.withUvLock() : rotation;
/*    */   }
/*    */   
/*    */   public SimpleModelState withX(Quadrant x) {
/* 85 */     return new SimpleModelState(x, this.y, this.z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withY(Quadrant y) {
/* 89 */     return new SimpleModelState(this.x, y, this.z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withZ(Quadrant z) {
/* 93 */     return new SimpleModelState(this.x, this.y, z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withUvLock(boolean uvLock) {
/* 97 */     return new SimpleModelState(this.x, this.y, this.z, uvLock);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\Variant$SimpleModelState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */