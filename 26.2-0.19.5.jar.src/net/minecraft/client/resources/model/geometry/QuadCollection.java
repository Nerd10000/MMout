/*     */ package net.minecraft.client.resources.model.geometry;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public class QuadCollection {
/*  13 */   public static final QuadCollection EMPTY = new QuadCollection(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
/*     */   
/*     */   private static final int FLAGS_NOT_COMPUTED = -1;
/*     */   
/*     */   private final List<BakedQuad> all;
/*     */   
/*     */   private final List<BakedQuad> unculled;
/*     */   
/*     */   private final List<BakedQuad> north;
/*     */   
/*     */   private final List<BakedQuad> south;
/*     */   
/*     */   private final List<BakedQuad> east;
/*     */   
/*     */   private final List<BakedQuad> west;
/*     */   
/*     */   private final List<BakedQuad> up;
/*     */   
/*     */   private final List<BakedQuad> down;
/*     */   
/*  25 */   private int materialFlags = -1;
/*     */   
/*     */   private QuadCollection(List<BakedQuad> all, List<BakedQuad> unculled, List<BakedQuad> north, List<BakedQuad> south, List<BakedQuad> east, List<BakedQuad> west, List<BakedQuad> up, List<BakedQuad> down) {
/*  28 */     this.all = all;
/*  29 */     this.unculled = unculled;
/*  30 */     this.north = north;
/*  31 */     this.south = south;
/*  32 */     this.east = east;
/*  33 */     this.west = west;
/*  34 */     this.up = up;
/*  35 */     this.down = down;
/*     */   }
/*     */   
/*     */   private static int computeMaterialFlags(List<BakedQuad> quads) {
/*  39 */     int flags = 0;
/*  40 */     for (BakedQuad quad : quads)
/*  41 */       flags |= quad.materialInfo().flags(); 
/*  43 */     return flags;
/*     */   }
/*     */   
/*     */   public List<BakedQuad> getQuads(Direction direction) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: astore_2
/*     */     //   2: iconst_0
/*     */     //   3: istore_3
/*     */     //   4: aload_2
/*     */     //   5: iload_3
/*     */     //   6: <illegal opcode> enumSwitch : (Lnet/minecraft/core/Direction;I)I
/*     */     //   11: tableswitch default -> 52, -1 -> 62, 0 -> 69, 1 -> 76, 2 -> 83, 3 -> 90, 4 -> 97, 5 -> 104
/*     */     //   52: new java/lang/MatchException
/*     */     //   55: dup
/*     */     //   56: aconst_null
/*     */     //   57: aconst_null
/*     */     //   58: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   61: athrow
/*     */     //   62: aload_0
/*     */     //   63: getfield unculled : Ljava/util/List;
/*     */     //   66: goto -> 108
/*     */     //   69: aload_0
/*     */     //   70: getfield north : Ljava/util/List;
/*     */     //   73: goto -> 108
/*     */     //   76: aload_0
/*     */     //   77: getfield south : Ljava/util/List;
/*     */     //   80: goto -> 108
/*     */     //   83: aload_0
/*     */     //   84: getfield east : Ljava/util/List;
/*     */     //   87: goto -> 108
/*     */     //   90: aload_0
/*     */     //   91: getfield west : Ljava/util/List;
/*     */     //   94: goto -> 108
/*     */     //   97: aload_0
/*     */     //   98: getfield up : Ljava/util/List;
/*     */     //   101: goto -> 108
/*     */     //   104: aload_0
/*     */     //   105: getfield down : Ljava/util/List;
/*     */     //   108: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #47	-> 0
/*     */     //   #48	-> 62
/*     */     //   #49	-> 69
/*     */     //   #50	-> 76
/*     */     //   #51	-> 83
/*     */     //   #52	-> 90
/*     */     //   #53	-> 97
/*     */     //   #54	-> 104
/*     */     //   #47	-> 108
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	109	0	this	Lnet/minecraft/client/resources/model/geometry/QuadCollection;
/*     */     //   0	109	1	direction	Lnet/minecraft/core/Direction;
/*     */   }
/*     */   
/*     */   public List<BakedQuad> getAll() {
/*  59 */     return this.all;
/*     */   }
/*     */   
/*     */   public int materialFlags() {
/*  63 */     if (this.materialFlags == -1)
/*  64 */       this.materialFlags = computeMaterialFlags(this.all); 
/*  67 */     return this.materialFlags;
/*     */   }
/*     */   
/*     */   public boolean hasMaterialFlag(int flag) {
/*  71 */     return ((materialFlags() & flag) != 0);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  75 */     private final ImmutableList.Builder<BakedQuad> unculledFaces = ImmutableList.builder();
/*     */     
/*  76 */     private final Multimap<Direction, BakedQuad> culledFaces = (Multimap<Direction, BakedQuad>)ArrayListMultimap.create();
/*     */     
/*     */     public Builder addCulledFace(Direction direction, BakedQuad quad) {
/*  79 */       this.culledFaces.put(direction, quad);
/*  80 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addUnculledFace(BakedQuad quad) {
/*  84 */       this.unculledFaces.add(quad);
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addAll(QuadCollection quadCollection) {
/*  89 */       this.culledFaces.putAll(Direction.UP, quadCollection.up);
/*  90 */       this.culledFaces.putAll(Direction.DOWN, quadCollection.down);
/*  91 */       this.culledFaces.putAll(Direction.NORTH, quadCollection.north);
/*  92 */       this.culledFaces.putAll(Direction.SOUTH, quadCollection.south);
/*  93 */       this.culledFaces.putAll(Direction.EAST, quadCollection.east);
/*  94 */       this.culledFaces.putAll(Direction.WEST, quadCollection.west);
/*  95 */       this.unculledFaces.addAll(quadCollection.unculled);
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     private static QuadCollection createFromSublists(List<BakedQuad> all, int unculledCount, int northCount, int southCount, int eastCount, int westCount, int upCount, int downCount) {
/* 100 */       int index = 0;
/* 101 */       List<BakedQuad> unculled = all.subList(index, index += unculledCount);
/* 102 */       List<BakedQuad> north = all.subList(index, index += northCount);
/* 103 */       List<BakedQuad> south = all.subList(index, index += southCount);
/* 104 */       List<BakedQuad> east = all.subList(index, index += eastCount);
/* 105 */       List<BakedQuad> west = all.subList(index, index += westCount);
/* 106 */       List<BakedQuad> up = all.subList(index, index += upCount);
/* 107 */       List<BakedQuad> down = all.subList(index, index + downCount);
/* 108 */       return new QuadCollection(all, unculled, north, south, east, west, up, down);
/*     */     }
/*     */     
/*     */     public QuadCollection build() {
/* 112 */       ImmutableList<BakedQuad> unculledFaces = this.unculledFaces.build();
/* 114 */       if (this.culledFaces.isEmpty()) {
/* 115 */         if (unculledFaces.isEmpty())
/* 116 */           return QuadCollection.EMPTY; 
/* 120 */         return new QuadCollection((List<BakedQuad>)unculledFaces, (List<BakedQuad>)unculledFaces, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
/*     */       } 
/* 124 */       ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
/* 125 */       quads.addAll((Iterable)unculledFaces);
/* 127 */       Collection<BakedQuad> north = this.culledFaces.get(Direction.NORTH);
/* 128 */       quads.addAll(north);
/* 129 */       Collection<BakedQuad> south = this.culledFaces.get(Direction.SOUTH);
/* 130 */       quads.addAll(south);
/* 131 */       Collection<BakedQuad> east = this.culledFaces.get(Direction.EAST);
/* 132 */       quads.addAll(east);
/* 133 */       Collection<BakedQuad> west = this.culledFaces.get(Direction.WEST);
/* 134 */       quads.addAll(west);
/* 135 */       Collection<BakedQuad> up = this.culledFaces.get(Direction.UP);
/* 136 */       quads.addAll(up);
/* 137 */       Collection<BakedQuad> down = this.culledFaces.get(Direction.DOWN);
/* 138 */       quads.addAll(down);
/* 140 */       return createFromSublists((List<BakedQuad>)quads
/* 141 */           .build(), unculledFaces
/* 142 */           .size(), north
/* 143 */           .size(), south
/* 144 */           .size(), east
/* 145 */           .size(), west
/* 146 */           .size(), up
/* 147 */           .size(), down
/* 148 */           .size());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\geometry\QuadCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */