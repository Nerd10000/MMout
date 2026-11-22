/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class CardinalLighting extends Record {
/*    */   private final float down;
/*    */   
/*    */   private final float up;
/*    */   
/*    */   private final float north;
/*    */   
/*    */   private final float south;
/*    */   
/*    */   private final float west;
/*    */   
/*    */   private final float east;
/*    */   
/*    */   public CardinalLighting(float down, float up, float north, float south, float west, float east) {
/*  7 */     this.down = down;
/*  7 */     this.up = up;
/*  7 */     this.north = north;
/*  7 */     this.south = south;
/*  7 */     this.west = west;
/*  7 */     this.east = east;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/CardinalLighting;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/CardinalLighting;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/CardinalLighting;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/CardinalLighting;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/CardinalLighting;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/CardinalLighting;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float down() {
/*  7 */     return this.down;
/*    */   }
/*    */   
/*    */   public float up() {
/*  7 */     return this.up;
/*    */   }
/*    */   
/*    */   public float north() {
/*  7 */     return this.north;
/*    */   }
/*    */   
/*    */   public float south() {
/*  7 */     return this.south;
/*    */   }
/*    */   
/*    */   public float west() {
/*  7 */     return this.west;
/*    */   }
/*    */   
/*    */   public float east() {
/*  7 */     return this.east;
/*    */   }
/*    */   
/* 15 */   public static final CardinalLighting DEFAULT = new CardinalLighting(0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F);
/*    */   
/* 20 */   public static final CardinalLighting NETHER = new CardinalLighting(0.9F, 0.9F, 0.8F, 0.8F, 0.6F, 0.6F);
/*    */   
/*    */   public float byFace(Direction direction) {
/* 27 */     switch (direction) {
/*    */       default:
/* 27 */         throw new MatchException(null, null);
/*    */       case DOWN:
/*    */       
/*    */       case UP:
/*    */       
/*    */       case NORTH:
/*    */       
/*    */       case SOUTH:
/*    */       
/*    */       case WEST:
/*    */       
/*    */       case EAST:
/*    */         break;
/*    */     } 
/* 27 */     return 
/*    */       
/* 33 */       this.east;
/*    */   }
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 38 */     DEFAULT("default", CardinalLighting.DEFAULT),
/* 39 */     NETHER("nether", CardinalLighting.NETHER);
/*    */     
/* 42 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private final CardinalLighting lighting;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Type(String name, CardinalLighting lighting) {
/* 48 */       this.name = name;
/* 49 */       this.lighting = lighting;
/*    */     }
/*    */     
/*    */     public CardinalLighting get() {
/* 53 */       return this.lighting;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 58 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\CardinalLighting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */