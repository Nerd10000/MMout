/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ 
/*     */ class null implements ValueInput {
/*     */   public <T> Optional<T> read(String name, Codec<T> codec) {
/*  55 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public <T> Optional<T> read(MapCodec<T> codec) {
/*  60 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<ValueInput> child(String name) {
/*  65 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public ValueInput childOrEmpty(String name) {
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public Optional<ValueInput.ValueInputList> childrenList(String name) {
/*  75 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public ValueInput.ValueInputList childrenListOrEmpty(String name) {
/*  80 */     return ValueInputContextHelper.this.emptyChildList;
/*     */   }
/*     */   
/*     */   public <T> Optional<ValueInput.TypedInputList<T>> list(String name, Codec<T> codec) {
/*  85 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public <T> ValueInput.TypedInputList<T> listOrEmpty(String name, Codec<T> codec) {
/*  90 */     return ValueInputContextHelper.this.emptyTypedList();
/*     */   }
/*     */   
/*     */   public boolean getBooleanOr(String name, boolean defaultValue) {
/*  95 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public byte getByteOr(String name, byte defaultValue) {
/* 100 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public int getShortOr(String name, short defaultValue) {
/* 105 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Integer> getInt(String name) {
/* 110 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public int getIntOr(String name, int defaultValue) {
/* 115 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public long getLongOr(String name, long defaultValue) {
/* 120 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<Long> getLong(String name) {
/* 125 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public float getFloatOr(String name, float defaultValue) {
/* 130 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public double getDoubleOr(String name, double defaultValue) {
/* 135 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<String> getString(String name) {
/* 140 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public String getStringOr(String name, String defaultValue) {
/* 145 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider lookup() {
/* 150 */     return ValueInputContextHelper.this.lookup;
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(String name) {
/* 155 */     return (Optional)Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\ValueInputContextHelper$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */