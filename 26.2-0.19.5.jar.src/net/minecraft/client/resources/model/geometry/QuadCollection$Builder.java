/*     */ package net.minecraft.client.resources.model.geometry;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public class Builder {
/*  75 */   private final ImmutableList.Builder<BakedQuad> unculledFaces = ImmutableList.builder();
/*     */   
/*  76 */   private final Multimap<Direction, BakedQuad> culledFaces = (Multimap<Direction, BakedQuad>)ArrayListMultimap.create();
/*     */   
/*     */   public Builder addCulledFace(Direction direction, BakedQuad quad) {
/*  79 */     this.culledFaces.put(direction, quad);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addUnculledFace(BakedQuad quad) {
/*  84 */     this.unculledFaces.add(quad);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addAll(QuadCollection quadCollection) {
/*  89 */     this.culledFaces.putAll(Direction.UP, quadCollection.up);
/*  90 */     this.culledFaces.putAll(Direction.DOWN, quadCollection.down);
/*  91 */     this.culledFaces.putAll(Direction.NORTH, quadCollection.north);
/*  92 */     this.culledFaces.putAll(Direction.SOUTH, quadCollection.south);
/*  93 */     this.culledFaces.putAll(Direction.EAST, quadCollection.east);
/*  94 */     this.culledFaces.putAll(Direction.WEST, quadCollection.west);
/*  95 */     this.unculledFaces.addAll(quadCollection.unculled);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   private static QuadCollection createFromSublists(List<BakedQuad> all, int unculledCount, int northCount, int southCount, int eastCount, int westCount, int upCount, int downCount) {
/* 100 */     int index = 0;
/* 101 */     List<BakedQuad> unculled = all.subList(index, index += unculledCount);
/* 102 */     List<BakedQuad> north = all.subList(index, index += northCount);
/* 103 */     List<BakedQuad> south = all.subList(index, index += southCount);
/* 104 */     List<BakedQuad> east = all.subList(index, index += eastCount);
/* 105 */     List<BakedQuad> west = all.subList(index, index += westCount);
/* 106 */     List<BakedQuad> up = all.subList(index, index += upCount);
/* 107 */     List<BakedQuad> down = all.subList(index, index + downCount);
/* 108 */     return new QuadCollection(all, unculled, north, south, east, west, up, down);
/*     */   }
/*     */   
/*     */   public QuadCollection build() {
/* 112 */     ImmutableList<BakedQuad> unculledFaces = this.unculledFaces.build();
/* 114 */     if (this.culledFaces.isEmpty()) {
/* 115 */       if (unculledFaces.isEmpty())
/* 116 */         return QuadCollection.EMPTY; 
/* 120 */       return new QuadCollection((List<BakedQuad>)unculledFaces, (List<BakedQuad>)unculledFaces, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
/*     */     } 
/* 124 */     ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
/* 125 */     quads.addAll((Iterable)unculledFaces);
/* 127 */     Collection<BakedQuad> north = this.culledFaces.get(Direction.NORTH);
/* 128 */     quads.addAll(north);
/* 129 */     Collection<BakedQuad> south = this.culledFaces.get(Direction.SOUTH);
/* 130 */     quads.addAll(south);
/* 131 */     Collection<BakedQuad> east = this.culledFaces.get(Direction.EAST);
/* 132 */     quads.addAll(east);
/* 133 */     Collection<BakedQuad> west = this.culledFaces.get(Direction.WEST);
/* 134 */     quads.addAll(west);
/* 135 */     Collection<BakedQuad> up = this.culledFaces.get(Direction.UP);
/* 136 */     quads.addAll(up);
/* 137 */     Collection<BakedQuad> down = this.culledFaces.get(Direction.DOWN);
/* 138 */     quads.addAll(down);
/* 140 */     return createFromSublists((List<BakedQuad>)quads
/* 141 */         .build(), unculledFaces
/* 142 */         .size(), north
/* 143 */         .size(), south
/* 144 */         .size(), east
/* 145 */         .size(), west
/* 146 */         .size(), up
/* 147 */         .size(), down
/* 148 */         .size());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\geometry\QuadCollection$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */