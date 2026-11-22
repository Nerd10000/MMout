/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class Scope {
/*     */   private static final int NOT_FOUND = -1;
/*     */   
/*  18 */   private static final Object FRAME_START_MARKER = new Object() {
/*     */       public String toString() {
/*  21 */         return "frame";
/*     */       }
/*     */     };
/*     */   
/*     */   private static final int ENTRY_STRIDE = 2;
/*     */   
/*  27 */   private Object[] stack = new Object[128];
/*     */   
/*  28 */   private int topEntryKeyIndex = 0;
/*     */   
/*  29 */   private int topMarkerKeyIndex = 0;
/*     */   
/*     */   public Scope() {
/*  33 */     this.stack[0] = FRAME_START_MARKER;
/*  35 */     this.stack[1] = null;
/*     */   }
/*     */   
/*     */   private int valueIndex(Atom<?> atom) {
/*  39 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  40 */       Object key = this.stack[i];
/*  41 */       assert key instanceof Atom;
/*  42 */       if (key == atom)
/*  43 */         return i + 1; 
/*     */     } 
/*  46 */     return -1;
/*     */   }
/*     */   
/*     */   public int valueIndexForAny(Atom<?>... atoms) {
/*  50 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/*  51 */       Object key = this.stack[i];
/*  52 */       assert key instanceof Atom;
/*  53 */       for (Atom<?> atom : atoms) {
/*  54 */         if (atom == key)
/*  55 */           return i + 1; 
/*     */       } 
/*     */     } 
/*  59 */     return -1;
/*     */   }
/*     */   
/*     */   private void ensureCapacity(int additionalEntryCount) {
/*  63 */     int currentSize = this.stack.length;
/*  64 */     int currentLastValueIndex = this.topEntryKeyIndex + 1;
/*  65 */     int newLastValueIndex = currentLastValueIndex + additionalEntryCount * 2;
/*  67 */     if (newLastValueIndex >= currentSize) {
/*  68 */       int newSize = Util.growByHalf(currentSize, newLastValueIndex + 1);
/*  69 */       Object[] newStack = new Object[newSize];
/*  70 */       System.arraycopy(this.stack, 0, newStack, 0, currentSize);
/*  71 */       this.stack = newStack;
/*     */     } 
/*  73 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   private void setupNewFrame() {
/*  80 */     this.topEntryKeyIndex += 2;
/*  81 */     this.stack[this.topEntryKeyIndex] = FRAME_START_MARKER;
/*  82 */     this.stack[this.topEntryKeyIndex + 1] = Integer.valueOf(this.topMarkerKeyIndex);
/*  83 */     this.topMarkerKeyIndex = this.topEntryKeyIndex;
/*     */   }
/*     */   
/*     */   public void pushFrame() {
/*  87 */     ensureCapacity(1);
/*  88 */     setupNewFrame();
/*  89 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   private int getPreviousMarkerIndex(int markerKeyIndex) {
/*  93 */     return ((Integer)this.stack[markerKeyIndex + 1]).intValue();
/*     */   }
/*     */   
/*     */   public void popFrame() {
/*  97 */     assert this.topMarkerKeyIndex != 0;
/* 101 */     this.topEntryKeyIndex = this.topMarkerKeyIndex - 2;
/* 102 */     this.topMarkerKeyIndex = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 103 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public void splitFrame() {
/* 109 */     int currentFrameMarkerIndex = this.topMarkerKeyIndex;
/* 110 */     int nonMarkerEntriesInFrame = (this.topEntryKeyIndex - this.topMarkerKeyIndex) / 2;
/* 111 */     ensureCapacity(nonMarkerEntriesInFrame + 1);
/* 113 */     setupNewFrame();
/* 115 */     int sourceCursor = currentFrameMarkerIndex + 2;
/* 116 */     int targetCursor = this.topEntryKeyIndex;
/* 117 */     for (int i = 0; i < nonMarkerEntriesInFrame; i++) {
/* 118 */       targetCursor += 2;
/* 119 */       Object key = this.stack[sourceCursor];
/* 120 */       assert key != null;
/* 121 */       this.stack[targetCursor] = key;
/* 122 */       this.stack[targetCursor + 1] = null;
/* 123 */       sourceCursor += 2;
/*     */     } 
/* 125 */     this.topEntryKeyIndex = targetCursor;
/* 126 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public void clearFrameValues() {
/* 130 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 131 */       assert this.stack[i] instanceof Atom;
/* 132 */       this.stack[i + 1] = null;
/*     */     } 
/* 134 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public void mergeFrame() {
/* 140 */     int previousMarkerIndex = getPreviousMarkerIndex(this.topMarkerKeyIndex);
/* 141 */     int previousFrameCursor = previousMarkerIndex;
/* 142 */     int currentFrameCursor = this.topMarkerKeyIndex;
/* 148 */     while (currentFrameCursor < this.topEntryKeyIndex) {
/* 149 */       previousFrameCursor += 2;
/* 150 */       currentFrameCursor += 2;
/* 151 */       Object newKey = this.stack[currentFrameCursor];
/* 152 */       assert newKey instanceof Atom;
/* 153 */       Object newValue = this.stack[currentFrameCursor + 1];
/* 155 */       Object oldKey = this.stack[previousFrameCursor];
/* 156 */       if (oldKey != newKey) {
/* 158 */         this.stack[previousFrameCursor] = newKey;
/* 159 */         this.stack[previousFrameCursor + 1] = newValue;
/*     */         continue;
/*     */       } 
/* 160 */       if (newValue != null)
/* 161 */         this.stack[previousFrameCursor + 1] = newValue; 
/*     */     } 
/* 166 */     this.topEntryKeyIndex = previousFrameCursor;
/* 167 */     this.topMarkerKeyIndex = previousMarkerIndex;
/* 168 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public <T> void put(Atom<T> name, T value) {
/* 172 */     int valueIndex = valueIndex(name);
/* 173 */     if (valueIndex != -1) {
/* 174 */       this.stack[valueIndex] = value;
/*     */     } else {
/* 176 */       ensureCapacity(1);
/* 177 */       this.topEntryKeyIndex += 2;
/* 178 */       this.stack[this.topEntryKeyIndex] = name;
/* 179 */       this.stack[this.topEntryKeyIndex + 1] = value;
/*     */     } 
/* 181 */     assert validateStructure();
/*     */   }
/*     */   
/*     */   public <T> T get(Atom<T> name) {
/* 186 */     int valueIndex = valueIndex(name);
/* 187 */     return (valueIndex != -1) ? (T)this.stack[valueIndex] : null;
/*     */   }
/*     */   
/*     */   public <T> T getOrThrow(Atom<T> name) {
/* 192 */     int valueIndex = valueIndex(name);
/* 193 */     if (valueIndex == -1)
/* 194 */       throw new IllegalArgumentException("No value for atom " + String.valueOf(name)); 
/* 196 */     return (T)this.stack[valueIndex];
/*     */   }
/*     */   
/*     */   public <T> T getOrDefault(Atom<T> name, T fallback) {
/* 201 */     int valueIndex = valueIndex(name);
/* 202 */     return (valueIndex != -1) ? (T)this.stack[valueIndex] : fallback;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAny(Atom<? extends T>... names) {
/* 208 */     int valueIndex = valueIndexForAny((Atom<?>[])names);
/* 209 */     return (valueIndex != -1) ? (T)this.stack[valueIndex] : null;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final <T> T getAnyOrThrow(Atom<? extends T>... names) {
/* 215 */     int valueIndex = valueIndexForAny((Atom<?>[])names);
/* 216 */     if (valueIndex == -1)
/* 217 */       throw new IllegalArgumentException("No value for atoms " + Arrays.toString(names)); 
/* 219 */     return (T)this.stack[valueIndex];
/*     */   }
/*     */   
/*     */   public String toString() {
/* 224 */     StringBuilder result = new StringBuilder();
/* 225 */     boolean afterFrame = true;
/* 226 */     for (int i = 0; i <= this.topEntryKeyIndex; i += 2) {
/* 227 */       Object key = this.stack[i];
/* 228 */       Object value = this.stack[i + 1];
/* 229 */       if (key == FRAME_START_MARKER) {
/* 230 */         result.append('|');
/* 231 */         afterFrame = true;
/*     */       } else {
/* 233 */         if (!afterFrame)
/* 234 */           result.append(','); 
/* 236 */         afterFrame = false;
/* 237 */         result.append(key).append(':').append(value);
/*     */       } 
/*     */     } 
/* 240 */     return result.toString();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Map<Atom<?>, ?> lastFrame() {
/* 245 */     HashMap<Atom<?>, Object> result = new HashMap<>();
/* 248 */     for (int i = this.topEntryKeyIndex; i > this.topMarkerKeyIndex; i -= 2) {
/* 249 */       Object key = this.stack[i];
/* 250 */       Object value = this.stack[i + 1];
/* 251 */       result.put((Atom)key, value);
/*     */     } 
/* 253 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasOnlySingleFrame() {
/* 257 */     for (int i = this.topEntryKeyIndex; i > 0; i--) {
/* 258 */       if (this.stack[i] == FRAME_START_MARKER)
/* 259 */         return false; 
/*     */     } 
/* 263 */     if (this.stack[0] != FRAME_START_MARKER)
/* 264 */       throw new IllegalStateException("Corrupted stack"); 
/* 266 */     return true;
/*     */   }
/*     */   
/*     */   private boolean validateStructure() {
/* 270 */     assert this.topMarkerKeyIndex >= 0;
/* 271 */     assert this.topEntryKeyIndex >= this.topMarkerKeyIndex;
/* 273 */     for (int i = 0; i <= this.topEntryKeyIndex; i += 2) {
/* 274 */       Object key = this.stack[i];
/* 275 */       if (key != FRAME_START_MARKER && !(key instanceof Atom))
/* 276 */         return false; 
/*     */     } 
/* 280 */     int marker = this.topMarkerKeyIndex;
/* 281 */     while (marker != 0) {
/* 282 */       Object key = this.stack[marker];
/* 283 */       if (key != FRAME_START_MARKER)
/* 284 */         return false; 
/* 286 */       marker = getPreviousMarkerIndex(marker);
/*     */     } 
/* 289 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Scope.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */