/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class Stitcher<T extends Stitcher.Entry> {
/*     */   private static final Comparator<Holder<?>> HOLDER_COMPARATOR;
/*     */   
/*     */   private final int mipLevel;
/*     */   
/*     */   static {
/*  13 */     HOLDER_COMPARATOR = Comparator.comparing(h -> Integer.valueOf(-h.height)).thenComparing(h -> Integer.valueOf(-h.width)).thenComparing(h -> h.entry.name());
/*     */   }
/*     */   
/*  16 */   private final List<Holder<T>> texturesToBeStitched = new ArrayList<>();
/*     */   
/*  17 */   private final List<Region<T>> storage = new ArrayList<>();
/*     */   
/*     */   private int storageX;
/*     */   
/*     */   private int storageY;
/*     */   
/*     */   private final int maxWidth;
/*     */   
/*     */   private final int maxHeight;
/*     */   
/*     */   private final int padding;
/*     */   
/*     */   public Stitcher(int maxWidth, int maxHeight, int mipLevel, int anisotropyBit) {
/*  26 */     this.mipLevel = mipLevel;
/*  27 */     this.maxWidth = maxWidth;
/*  28 */     this.maxHeight = maxHeight;
/*  29 */     this.padding = 1 << mipLevel << Mth.clamp(anisotropyBit - 1, 0, 4);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  33 */     return this.storageX;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  37 */     return this.storageY;
/*     */   }
/*     */   
/*     */   public void registerSprite(T entry) {
/*  44 */     Holder<T> holder = new Holder<>(entry, smallestFittingMinTexel(entry.width() + this.padding * 2, this.mipLevel), smallestFittingMinTexel(entry.height() + this.padding * 2, this.mipLevel));
/*  46 */     this.texturesToBeStitched.add(holder);
/*     */   }
/*     */   
/*     */   public void stitch() {
/*  50 */     List<Holder<T>> holders = new ArrayList<>(this.texturesToBeStitched);
/*  51 */     holders.sort(HOLDER_COMPARATOR);
/*  53 */     for (Holder<T> holder : holders) {
/*  54 */       if (!addToStorage(holder))
/*  55 */         throw new StitcherException(holder.entry, (Collection)holders.stream().map(h -> h.entry).collect(ImmutableList.toImmutableList())); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void gatherSprites(SpriteLoader<T> loader) {
/*  65 */     for (Region<T> topRegion : this.storage)
/*  66 */       topRegion.walk(loader, this.padding); 
/*     */   }
/*     */   
/*     */   private static int smallestFittingMinTexel(int input, int maxMipLevel) {
/*  71 */     return (input >> maxMipLevel) + (((input & (1 << maxMipLevel) - 1) == 0) ? 0 : 1) << maxMipLevel;
/*     */   }
/*     */   
/*     */   private boolean addToStorage(Holder<T> holder) {
/*  75 */     for (Region<T> region : this.storage) {
/*  76 */       if (region.add(holder))
/*  77 */         return true; 
/*     */     } 
/*  81 */     return expand(holder);
/*     */   }
/*     */   
/*     */   private boolean expand(Holder<T> holder) {
/*     */     boolean growOnX;
/*     */     Region<T> slot;
/*  91 */     int xCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageX);
/*  92 */     int yCurrentSize = Mth.smallestEncompassingPowerOfTwo(this.storageY);
/*  93 */     int xNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageX + holder.width);
/*  94 */     int yNewSize = Mth.smallestEncompassingPowerOfTwo(this.storageY + holder.height);
/*  96 */     boolean xCanGrow = (xNewSize <= this.maxWidth);
/*  97 */     boolean yCanGrow = (yNewSize <= this.maxHeight);
/*  99 */     if (!xCanGrow && !yCanGrow)
/* 100 */       return false; 
/* 103 */     boolean xWillGrow = (xCanGrow && xCurrentSize != xNewSize);
/* 104 */     boolean yWillGrow = (yCanGrow && yCurrentSize != yNewSize);
/* 106 */     if (xWillGrow ^ yWillGrow) {
/* 107 */       growOnX = xWillGrow;
/*     */     } else {
/* 110 */       growOnX = (xCanGrow && xCurrentSize <= yCurrentSize);
/*     */     } 
/* 114 */     if (growOnX) {
/* 116 */       if (this.storageY == 0)
/* 117 */         this.storageY = yNewSize; 
/* 120 */       slot = new Region<>(this.storageX, 0, xNewSize - this.storageX, this.storageY);
/* 121 */       this.storageX = xNewSize;
/*     */     } else {
/* 124 */       slot = new Region<>(0, this.storageY, this.storageX, yNewSize - this.storageY);
/* 125 */       this.storageY = yNewSize;
/*     */     } 
/* 128 */     slot.add(holder);
/* 129 */     this.storage.add(slot);
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   private static final class Holder<T extends Entry> extends Record {
/*     */     private final T entry;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private Holder(T entry, int width, int height) {
/* 134 */       this.entry = entry;
/* 134 */       this.width = width;
/* 134 */       this.height = height;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #134	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #134	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/Stitcher$Holder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #134	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/Stitcher$Holder<TT;>;
/*     */     }
/*     */     
/*     */     public T entry() {
/* 134 */       return this.entry;
/*     */     }
/*     */     
/*     */     public int width() {
/* 134 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/* 134 */       return this.height;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Entry {
/*     */     int width();
/*     */     
/*     */     int height();
/*     */     
/*     */     Identifier name();
/*     */   }
/*     */   
/*     */   public static class Region<T extends Entry> {
/*     */     private final int originX;
/*     */     
/*     */     private final int originY;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private List<Region<T>> subSlots;
/*     */     
/*     */     private Stitcher.Holder<T> holder;
/*     */     
/*     */     public Region(int originX, int originY, int width, int height) {
/* 147 */       this.originX = originX;
/* 148 */       this.originY = originY;
/* 149 */       this.width = width;
/* 150 */       this.height = height;
/*     */     }
/*     */     
/*     */     public int getX() {
/* 154 */       return this.originX;
/*     */     }
/*     */     
/*     */     public int getY() {
/* 158 */       return this.originY;
/*     */     }
/*     */     
/*     */     public boolean add(Stitcher.Holder<T> holder) {
/* 163 */       if (this.holder != null)
/* 164 */         return false; 
/* 167 */       int textureWidth = holder.width;
/* 168 */       int textureHeight = holder.height;
/* 171 */       if (textureWidth > this.width || textureHeight > this.height)
/* 172 */         return false; 
/* 176 */       if (textureWidth == this.width && textureHeight == this.height) {
/* 178 */         this.holder = holder;
/* 179 */         return true;
/*     */       } 
/* 183 */       if (this.subSlots == null) {
/* 184 */         this.subSlots = new ArrayList<>(1);
/* 187 */         this.subSlots.add(new Region(this.originX, this.originY, textureWidth, textureHeight));
/* 189 */         int spareWidth = this.width - textureWidth;
/* 190 */         int spareHeight = this.height - textureHeight;
/* 192 */         if (spareHeight > 0 && spareWidth > 0) {
/* 207 */           int right = Math.max(this.height, spareWidth);
/* 208 */           int bottom = Math.max(this.width, spareHeight);
/* 209 */           if (right >= bottom) {
/* 210 */             this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 211 */             this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, this.height));
/*     */           } else {
/* 213 */             this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/* 214 */             this.subSlots.add(new Region(this.originX, this.originY + textureHeight, this.width, spareHeight));
/*     */           } 
/* 216 */         } else if (spareWidth == 0) {
/* 226 */           this.subSlots.add(new Region(this.originX, this.originY + textureHeight, textureWidth, spareHeight));
/* 227 */         } else if (spareHeight == 0) {
/* 238 */           this.subSlots.add(new Region(this.originX + textureWidth, this.originY, spareWidth, textureHeight));
/*     */         } 
/*     */       } 
/* 242 */       for (Region<T> subSlot : this.subSlots) {
/* 243 */         if (subSlot.add(holder))
/* 244 */           return true; 
/*     */       } 
/* 248 */       return false;
/*     */     }
/*     */     
/*     */     public void walk(Stitcher.SpriteLoader<T> output, int padding) {
/* 252 */       if (this.holder != null) {
/* 253 */         output.load(this.holder.entry, getX(), getY(), padding);
/* 254 */       } else if (this.subSlots != null) {
/* 255 */         for (Region<T> subSlot : this.subSlots)
/* 256 */           subSlot.walk(output, padding); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public String toString() {
/* 263 */       return "Slot{originX=" + this.originX + ", originY=" + this.originY + ", width=" + this.width + ", height=" + this.height + ", texture=" + String.valueOf(this.holder) + ", subSlots=" + String.valueOf(this.subSlots) + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface SpriteLoader<T extends Entry> {
/*     */     void load(T param1T, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\Stitcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */