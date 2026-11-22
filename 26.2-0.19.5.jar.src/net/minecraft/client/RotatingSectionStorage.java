/*     */ package net.minecraft.client;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ 
/*     */ public class RotatingSectionStorage<T extends RotatingSectionStorage.Value> implements Iterable<T> {
/*     */   private final Node<T>[] nodes;
/*     */   
/*     */   private final int radius;
/*     */   
/*     */   private final int minY;
/*     */   
/*     */   private final int maxY;
/*     */   
/*     */   private final int sectionGridSizeY;
/*     */   
/*     */   private final int sectionGridSizeXZ;
/*     */   
/*  20 */   private SectionPos centerSectionPos = SectionPos.of(-2147483648, -2147483648, -2147483648);
/*     */   
/*     */   public RotatingSectionStorage(int radius, int minY, int maxY, ValueCreator<T> valueCreator) {
/*  23 */     this.radius = radius;
/*  24 */     this.minY = minY;
/*  25 */     this.maxY = maxY;
/*  26 */     this.sectionGridSizeY = maxY - minY + 1;
/*  27 */     this.sectionGridSizeXZ = radius * 2 + 1;
/*  28 */     int totalSections = this.sectionGridSizeXZ * this.sectionGridSizeXZ * this.sectionGridSizeY;
/*  29 */     this.nodes = (Node<T>[])new Node[totalSections];
/*  31 */     for (int x = 0; x < this.sectionGridSizeXZ; x++) {
/*  32 */       for (int y = 0; y < this.sectionGridSizeY; y++) {
/*  33 */         for (int z = 0; z < this.sectionGridSizeXZ; z++) {
/*  34 */           int index = getSectionIndex(x, y, z);
/*  35 */           long sectionNode = SectionPos.asLong(x, y + minY, z);
/*  36 */           this.nodes[index] = new Node<>(valueCreator.createValue(index, sectionNode));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean repositionCenter(SectionPos newCenterSectionPos) {
/*  43 */     if (newCenterSectionPos.equals(this.centerSectionPos))
/*  44 */       return false; 
/*  47 */     int lowestX = newCenterSectionPos.x() - this.radius;
/*  48 */     int lowestZ = newCenterSectionPos.z() - this.radius;
/*  49 */     for (int gridX = 0; gridX < this.sectionGridSizeXZ; gridX++) {
/*  50 */       int newSectionX = lowestX + Math.floorMod(gridX - lowestX, this.sectionGridSizeXZ);
/*  52 */       for (int gridZ = 0; gridZ < this.sectionGridSizeXZ; gridZ++) {
/*  53 */         int newSectionZ = lowestZ + Math.floorMod(gridZ - lowestZ, this.sectionGridSizeXZ);
/*  55 */         for (int gridY = 0; gridY < this.sectionGridSizeY; gridY++) {
/*  56 */           int newSectionY = this.minY + gridY;
/*  57 */           Value value = (Value)(this.nodes[getSectionIndex(gridX, gridY, gridZ)]).value;
/*  59 */           long sectionNode = value.getSectionNode();
/*  60 */           if (sectionNode != SectionPos.asLong(newSectionX, newSectionY, newSectionZ))
/*  61 */             value.setSectionNode(SectionPos.asLong(newSectionX, newSectionY, newSectionZ)); 
/*     */         } 
/*     */       } 
/*     */     } 
/*  66 */     this.centerSectionPos = newCenterSectionPos;
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   public int radius() {
/*  72 */     return this.radius;
/*     */   }
/*     */   
/*     */   public int minY() {
/*  76 */     return this.minY;
/*     */   }
/*     */   
/*     */   public int maxY() {
/*  80 */     return this.maxY;
/*     */   }
/*     */   
/*     */   public int height() {
/*  84 */     return this.sectionGridSizeY;
/*     */   }
/*     */   
/*     */   public SectionPos centerSectionPos() {
/*  88 */     return this.centerSectionPos;
/*     */   }
/*     */   
/*     */   public T getValueAt(BlockPos pos) {
/*  92 */     return getValue(SectionPos.asLong(pos));
/*     */   }
/*     */   
/*     */   public T getValue(long sectionNode) {
/*  96 */     int sectionX = SectionPos.x(sectionNode);
/*  97 */     int sectionY = SectionPos.y(sectionNode);
/*  98 */     int sectionZ = SectionPos.z(sectionNode);
/*  99 */     return getValue(sectionX, sectionY, sectionZ);
/*     */   }
/*     */   
/*     */   public T getValue(int sectionX, int sectionY, int sectionZ) {
/* 103 */     if (!containsSection(sectionX, sectionY, sectionZ))
/* 104 */       return null; 
/* 106 */     int y = sectionY - this.minY;
/* 107 */     int x = Math.floorMod(sectionX, this.sectionGridSizeXZ);
/* 108 */     int z = Math.floorMod(sectionZ, this.sectionGridSizeXZ);
/* 110 */     return (this.nodes[getSectionIndex(x, y, z)]).value;
/*     */   }
/*     */   
/*     */   private boolean containsSection(int sectionX, int sectionY, int sectionZ) {
/* 114 */     if (sectionY < this.minY || sectionY > this.maxY)
/* 115 */       return false; 
/* 117 */     if (sectionX < this.centerSectionPos.x() - this.radius || sectionX > this.centerSectionPos.x() + this.radius)
/* 118 */       return false; 
/* 120 */     if (sectionZ < this.centerSectionPos.z() - this.radius || sectionZ > this.centerSectionPos.z() + this.radius)
/* 121 */       return false; 
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   private int getSectionIndex(int x, int y, int z) {
/* 127 */     return (z * this.sectionGridSizeY + y) * this.sectionGridSizeXZ + x;
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 132 */     return new Iterator<T>() {
/*     */         private int i;
/*     */         
/*     */         public boolean hasNext() {
/* 137 */           return (this.i < RotatingSectionStorage.this.nodes.length - 1);
/*     */         }
/*     */         
/*     */         public T next() {
/* 142 */           if (this.i >= RotatingSectionStorage.this.nodes.length)
/* 143 */             throw new NoSuchElementException(); 
/* 145 */           return (RotatingSectionStorage.this.nodes[this.i++]).value;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<? super T> action) {
/* 152 */     for (Node<T> node : this.nodes)
/* 153 */       action.accept(node.value); 
/*     */   }
/*     */   
/*     */   public Spliterator<T> spliterator() {
/* 159 */     return Spliterators.spliterator(iterator(), this.nodes.length, 0);
/*     */   }
/*     */   
/*     */   public int size() {
/* 163 */     return this.nodes.length;
/*     */   }
/*     */   
/*     */   public static interface Value {
/*     */     void setSectionNode(long param1Long);
/*     */     
/*     */     long getSectionNode();
/*     */   }
/*     */   
/*     */   public static interface ValueCreator<T extends Value> {
/*     */     T createValue(int param1Int, long param1Long);
/*     */   }
/*     */   
/*     */   private static final class Node<T> extends Record {
/*     */     private final T value;
/*     */     
/*     */     private Node(T value) {
/* 166 */       this.value = value;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/RotatingSectionStorage$Node;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/RotatingSectionStorage$Node;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/RotatingSectionStorage$Node;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/RotatingSectionStorage$Node<TT;>;
/*     */     }
/*     */     
/*     */     public T value() {
/* 166 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\RotatingSectionStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */