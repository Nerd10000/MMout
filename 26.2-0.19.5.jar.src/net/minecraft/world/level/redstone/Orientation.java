/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class Orientation {
/*  31 */   public static final StreamCodec<ByteBuf, Orientation> STREAM_CODEC = ByteBufCodecs.idMapper(Orientation::fromIndex, Orientation::getIndex);
/*     */   
/*     */   private static final Orientation[] ORIENTATIONS;
/*     */   
/*     */   private final Direction up;
/*     */   
/*     */   private final Direction front;
/*     */   
/*     */   private final Direction side;
/*     */   
/*     */   private final SideBias sideBias;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final List<Direction> neighbors;
/*     */   
/*     */   private final List<Direction> horizontalNeighbors;
/*     */   
/*     */   private final List<Direction> verticalNeighbors;
/*     */   
/*     */   static {
/*  33 */     ORIENTATIONS = (Orientation[])Util.make(() -> {
/*     */           Orientation[] orientations = new Orientation[48];
/*     */           generateContext(new Orientation(Direction.UP, Direction.NORTH, SideBias.LEFT), orientations);
/*     */           return orientations;
/*     */         });
/*     */   }
/*     */   
/*  54 */   private final Map<Direction, Orientation> withFront = new EnumMap<>(Direction.class);
/*     */   
/*  55 */   private final Map<Direction, Orientation> withUp = new EnumMap<>(Direction.class);
/*     */   
/*  56 */   private final Map<SideBias, Orientation> withSideBias = new EnumMap<>(SideBias.class);
/*     */   
/*     */   private Orientation(Direction up, Direction front, SideBias sideBias) {
/*  59 */     this.up = up;
/*  60 */     this.front = front;
/*  61 */     this.sideBias = sideBias;
/*  62 */     this.index = generateIndex(up, front, sideBias);
/*  64 */     Vec3i rightVector = front.getUnitVec3i().cross(up.getUnitVec3i());
/*  65 */     Direction side = Direction.getNearest(rightVector, null);
/*  66 */     Objects.requireNonNull(side);
/*  67 */     if (this.sideBias == SideBias.RIGHT) {
/*  68 */       this.side = side;
/*     */     } else {
/*  70 */       this.side = side.getOpposite();
/*     */     } 
/*  72 */     this.neighbors = List.of(this.front
/*  73 */         .getOpposite(), this.front, this.side, this.side
/*     */         
/*  76 */         .getOpposite(), this.up
/*  77 */         .getOpposite(), this.up);
/*  80 */     this.horizontalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() != this.up.getAxis())).toList();
/*  81 */     this.verticalNeighbors = this.neighbors.stream().filter(d -> (d.getAxis() == this.up.getAxis())).toList();
/*     */   }
/*     */   
/*     */   public static Orientation of(Direction up, Direction front, SideBias sideBias) {
/*  85 */     return ORIENTATIONS[generateIndex(up, front, sideBias)];
/*     */   }
/*     */   
/*     */   public Orientation withUp(Direction up) {
/*  93 */     return this.withUp.get(up);
/*     */   }
/*     */   
/*     */   public Orientation withFront(Direction front) {
/* 101 */     return this.withFront.get(front);
/*     */   }
/*     */   
/*     */   public Orientation withFrontPreserveUp(Direction front) {
/* 108 */     if (front.getAxis() == this.up.getAxis())
/* 109 */       return this; 
/* 111 */     return this.withFront.get(front);
/*     */   }
/*     */   
/*     */   public Orientation withFrontAdjustSideBias(Direction front) {
/* 117 */     Orientation withFront = withFront(front);
/* 118 */     if (this.front == withFront.side)
/* 119 */       return withFront.withMirror(); 
/* 121 */     return withFront;
/*     */   }
/*     */   
/*     */   public Orientation withSideBias(SideBias sideBias) {
/* 126 */     return this.withSideBias.get(sideBias);
/*     */   }
/*     */   
/*     */   public Orientation withMirror() {
/* 131 */     return withSideBias(this.sideBias.getOpposite());
/*     */   }
/*     */   
/*     */   public Direction getFront() {
/* 135 */     return this.front;
/*     */   }
/*     */   
/*     */   public Direction getUp() {
/* 139 */     return this.up;
/*     */   }
/*     */   
/*     */   public Direction getSide() {
/* 143 */     return this.side;
/*     */   }
/*     */   
/*     */   public SideBias getSideBias() {
/* 147 */     return this.sideBias;
/*     */   }
/*     */   
/*     */   public List<Direction> getDirections() {
/* 153 */     return this.neighbors;
/*     */   }
/*     */   
/*     */   public List<Direction> getHorizontalDirections() {
/* 160 */     return this.horizontalNeighbors;
/*     */   }
/*     */   
/*     */   public List<Direction> getVerticalDirections() {
/* 167 */     return this.verticalNeighbors;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 172 */     return "[up=" + String.valueOf(this.up) + ",front=" + String.valueOf(this.front) + ",sideBias=" + String.valueOf(this.sideBias) + "]";
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 176 */     return this.index;
/*     */   }
/*     */   
/*     */   public static Orientation fromIndex(int index) {
/* 180 */     return ORIENTATIONS[index];
/*     */   }
/*     */   
/*     */   public static Orientation random(RandomSource rand) {
/* 184 */     return (Orientation)Util.getRandom((Object[])ORIENTATIONS, rand);
/*     */   }
/*     */   
/*     */   private static Orientation generateContext(Orientation self, Orientation[] lookup) {
/* 189 */     if (lookup[self.getIndex()] != null)
/* 190 */       return lookup[self.getIndex()]; 
/* 192 */     lookup[self.getIndex()] = self;
/* 194 */     for (SideBias sideBias : SideBias.values())
/* 195 */       self.withSideBias.put(sideBias, generateContext(new Orientation(self.up, self.front, sideBias), lookup)); 
/* 198 */     for (Direction facing : Direction.values()) {
/* 199 */       Direction up = self.up;
/* 201 */       if (facing == self.up)
/* 202 */         up = self.front.getOpposite(); 
/* 204 */       if (facing == self.up.getOpposite())
/* 205 */         up = self.front; 
/* 207 */       self.withFront.put(facing, generateContext(new Orientation(up, facing, self.sideBias), lookup));
/*     */     } 
/* 210 */     for (Direction facing : Direction.values()) {
/* 211 */       Direction front = self.front;
/* 213 */       if (facing == self.front)
/* 214 */         front = self.up.getOpposite(); 
/* 216 */       if (facing == self.front.getOpposite())
/* 217 */         front = self.up; 
/* 219 */       self.withUp.put(facing, generateContext(new Orientation(facing, front, self.sideBias), lookup));
/*     */     } 
/* 221 */     return self;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static int generateIndex(Direction up, Direction front, SideBias sideBias) {
/*     */     int frontAxisKey;
/* 226 */     if (up.getAxis() == front.getAxis())
/* 227 */       throw new IllegalStateException("Up-vector and front-vector can not be on the same axis"); 
/* 232 */     if (up.getAxis() == Direction.Axis.Y) {
/* 234 */       frontAxisKey = (front.getAxis() == Direction.Axis.X) ? 1 : 0;
/*     */     } else {
/* 237 */       frontAxisKey = (front.getAxis() == Direction.Axis.Y) ? 1 : 0;
/*     */     } 
/* 239 */     int frontKey = frontAxisKey << 1 | front.getAxisDirection().ordinal();
/* 240 */     return ((up.ordinal() << 2) + frontKey << 1) + sideBias.ordinal();
/*     */   }
/*     */   
/*     */   public enum SideBias {
/* 244 */     LEFT("left"),
/* 245 */     RIGHT("right");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     SideBias(String name) {
/* 250 */       this.name = name;
/*     */     }
/*     */     
/*     */     public SideBias getOpposite() {
/* 254 */       return (this == LEFT) ? RIGHT : LEFT;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 259 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\Orientation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */