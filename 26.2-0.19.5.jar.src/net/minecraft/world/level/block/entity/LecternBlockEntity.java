/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.commands.CommandSource;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Clearable;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.LecternMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.LecternBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LecternBlockEntity extends BlockEntity implements Clearable, MenuProvider {
/*     */   public static final int DATA_PAGE = 0;
/*     */   
/*     */   public static final int NUM_DATA = 1;
/*     */   
/*     */   public static final int SLOT_BOOK = 0;
/*     */   
/*     */   public static final int NUM_SLOTS = 1;
/*     */   
/*  41 */   private final Container bookAccess = new Container() {
/*     */       public int getContainerSize() {
/*  44 */         return 1;
/*     */       }
/*     */       
/*     */       public boolean isEmpty() {
/*  49 */         return LecternBlockEntity.this.book.isEmpty();
/*     */       }
/*     */       
/*     */       public ItemStack getItem(int slot) {
/*  54 */         return (slot == 0) ? LecternBlockEntity.this.book : ItemStack.EMPTY;
/*     */       }
/*     */       
/*     */       public ItemStack removeItem(int slot, int count) {
/*  59 */         if (slot == 0) {
/*  60 */           ItemStack result = LecternBlockEntity.this.book.split(count);
/*  61 */           if (LecternBlockEntity.this.book.isEmpty())
/*  62 */             LecternBlockEntity.this.onBookItemRemove(); 
/*  64 */           return result;
/*     */         } 
/*  66 */         return ItemStack.EMPTY;
/*     */       }
/*     */       
/*     */       public ItemStack removeItemNoUpdate(int slot) {
/*  71 */         if (slot == 0) {
/*  72 */           ItemStack prev = LecternBlockEntity.this.book;
/*  73 */           LecternBlockEntity.this.book = ItemStack.EMPTY;
/*  74 */           LecternBlockEntity.this.onBookItemRemove();
/*  75 */           return prev;
/*     */         } 
/*  77 */         return ItemStack.EMPTY;
/*     */       }
/*     */       
/*     */       public void setItem(int slot, ItemStack itemStack) {}
/*     */       
/*     */       public int getMaxStackSize() {
/*  87 */         return 1;
/*     */       }
/*     */       
/*     */       public void setChanged() {
/*  92 */         LecternBlockEntity.this.setChanged();
/*     */       }
/*     */       
/*     */       public boolean stillValid(Player player) {
/*  97 */         return (Container.stillValidBlockEntity(LecternBlockEntity.this, player) && LecternBlockEntity.this.hasBook());
/*     */       }
/*     */       
/*     */       public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 102 */         return false;
/*     */       }
/*     */       
/*     */       public void clearContent() {}
/*     */     };
/*     */   
/* 110 */   private final ContainerData dataAccess = new ContainerData() {
/*     */       public int get(int dataId) {
/* 113 */         return (dataId == 0) ? LecternBlockEntity.this.page : 0;
/*     */       }
/*     */       
/*     */       public void set(int dataId, int value) {
/* 118 */         if (dataId == 0)
/* 119 */           LecternBlockEntity.this.setPage(value); 
/*     */       }
/*     */       
/*     */       public int getCount() {
/* 125 */         return 1;
/*     */       }
/*     */     };
/*     */   
/* 129 */   private ItemStack book = ItemStack.EMPTY;
/*     */   
/*     */   private int page;
/*     */   
/*     */   private int pageCount;
/*     */   
/*     */   public LecternBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 134 */     super(BlockEntityTypes.LECTERN, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public ItemStack getBook() {
/* 138 */     return this.book;
/*     */   }
/*     */   
/*     */   public boolean hasBook() {
/* 142 */     return (this.book.has(DataComponents.WRITABLE_BOOK_CONTENT) || this.book.has(DataComponents.WRITTEN_BOOK_CONTENT));
/*     */   }
/*     */   
/*     */   public void setBook(ItemStack book) {
/* 146 */     setBook(book, (Player)null);
/*     */   }
/*     */   
/*     */   private void onBookItemRemove() {
/* 150 */     this.page = 0;
/* 151 */     this.pageCount = 0;
/* 152 */     LecternBlock.resetBookState(null, getLevel(), getBlockPos(), getBlockState(), false);
/*     */   }
/*     */   
/*     */   public void setBook(ItemStack book, Player resolutionContext) {
/* 156 */     this.book = resolveBook(book, resolutionContext);
/* 157 */     this.page = 0;
/* 158 */     this.pageCount = getPageCount(this.book);
/* 159 */     setChanged();
/*     */   }
/*     */   
/*     */   private void setPage(int page) {
/* 163 */     int newPage = Mth.clamp(page, 0, this.pageCount - 1);
/* 164 */     if (newPage != this.page) {
/* 165 */       this.page = newPage;
/* 166 */       setChanged();
/* 167 */       LecternBlock.signalPageChange(getLevel(), getBlockPos(), getBlockState());
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getPage() {
/* 172 */     return this.page;
/*     */   }
/*     */   
/*     */   public int getRedstoneSignal() {
/* 176 */     float pageProgress = (this.pageCount > 1) ? (getPage() / (this.pageCount - 1.0F)) : 1.0F;
/* 177 */     return Mth.floor(pageProgress * 14.0F) + (hasBook() ? 1 : 0);
/*     */   }
/*     */   
/*     */   private ItemStack resolveBook(ItemStack book, Player player) {
/* 181 */     Level level = this.level;
/* 181 */     if (level instanceof ServerLevel) {
/* 181 */       ServerLevel serverLevel = (ServerLevel)level;
/* 182 */       ResolutionContext context = ResolutionContext.create(createCommandSourceStack(player, serverLevel));
/* 183 */       WrittenBookContent.resolveForItem(book, context, (HolderLookup.Provider)this.level.registryAccess());
/*     */     } 
/* 185 */     return book;
/*     */   }
/*     */   
/*     */   private CommandSourceStack createCommandSourceStack(Player player, ServerLevel level) {
/*     */     String textName;
/*     */     Component displayName;
/* 191 */     if (player == null) {
/* 192 */       textName = "Lectern";
/* 193 */       MutableComponent mutableComponent = Component.literal("Lectern");
/*     */     } else {
/* 195 */       textName = player.getPlainTextName();
/* 196 */       displayName = player.getDisplayName();
/*     */     } 
/* 198 */     Vec3 pos = Vec3.atCenterOf((Vec3i)this.worldPosition);
/* 199 */     return new CommandSourceStack(CommandSource.NULL, pos, Vec2.ZERO, level, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER, textName, displayName, level.getServer(), (Entity)player);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 204 */     super.loadAdditional(input);
/* 206 */     this
/*     */       
/* 208 */       .book = input.read("Book", ItemStack.CODEC).map(book -> resolveBook(book, (Player)null)).orElse(ItemStack.EMPTY);
/* 210 */     this.pageCount = getPageCount(this.book);
/* 211 */     this.page = Mth.clamp(input.getIntOr("Page", 0), 0, this.pageCount - 1);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 216 */     super.saveAdditional(output);
/* 218 */     if (!getBook().isEmpty()) {
/* 219 */       output.store("Book", ItemStack.CODEC, getBook());
/* 220 */       output.putInt("Page", this.page);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearContent() {
/* 226 */     setBook(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 231 */     if (((Boolean)state.getValue((Property)LecternBlock.HAS_BOOK)).booleanValue() && this.level != null) {
/* 232 */       Direction direction = (Direction)state.getValue((Property)LecternBlock.FACING);
/* 233 */       ItemStack book = getBook().copy();
/* 234 */       float xo = 0.25F * direction.getStepX();
/* 235 */       float zo = 0.25F * direction.getStepZ();
/* 236 */       ItemEntity entity = new ItemEntity(this.level, pos.getX() + 0.5D + xo, (pos.getY() + 1), pos.getZ() + 0.5D + zo, book);
/* 237 */       entity.setDefaultPickUpDelay();
/* 238 */       this.level.addFreshEntity((Entity)entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 244 */     return (AbstractContainerMenu)new LecternMenu(containerId, this.bookAccess, this.dataAccess);
/*     */   }
/*     */   
/*     */   public Component getDisplayName() {
/* 249 */     return (Component)Component.translatable("container.lectern");
/*     */   }
/*     */   
/*     */   private static int getPageCount(ItemStack book) {
/* 253 */     WrittenBookContent writtenContent = (WrittenBookContent)book.get(DataComponents.WRITTEN_BOOK_CONTENT);
/* 254 */     if (writtenContent != null)
/* 255 */       return writtenContent.pages().size(); 
/* 257 */     WritableBookContent writableContent = (WritableBookContent)book.get(DataComponents.WRITABLE_BOOK_CONTENT);
/* 258 */     if (writableContent != null)
/* 259 */       return writableContent.pages().size(); 
/* 261 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */