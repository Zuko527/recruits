package com.talhanation.recruits.inventory;

import com.mojang.datafixers.util.Pair;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import de.maxhenkel.corelib.inventory.ContainerBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RecruitInventoryContainer extends ContainerBase {

    private final Container recruitInventory;
    private final AbstractRecruitEntity recruit;
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
            InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            InventoryMenu.EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public RecruitInventoryContainer(int id, AbstractRecruitEntity recruit, Inventory playerInventory) {
        super(Main.RECRUIT_CONTAINER_TYPE, id, playerInventory, recruit.getInventory());
        this.recruit = recruit;
        this.recruitInventory = recruit.getInventory();

        addRecruitInventorySlots();
        addRecruitHandSlots();
        addRecruitEquipmentSlots();
        addPlayerInventorySlots();
    }

    public AbstractRecruitEntity getRecruit() {
        return recruit;
    }

    @Override
    public int getInvOffset() {
        return 56;
    }

    //iv slots
    //9,10 = hand
    //11,12,13,14 = armor
    //0-8 = inv

    public void addRecruitInventorySlots() {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(recruitInventory, 0 + l + k * recruit.getInventoryColumns(), 2 * 18 + 82 + l * 18,  18 + k * 18));
            }
        }
    }

    public void addRecruitEquipmentSlots() {
        for (int k = 0; k < 4; ++k) {
            final EquipmentSlot equipmentslottype = SLOT_IDS[k];
            this.addSlot(new Slot(recruit.inventory, 11 + k, 8, 18 + k * 18) {
                public int getMaxStackSize() {
                    return 1;
                }

                public boolean mayPlace(ItemStack itemStack) {
                    return itemStack.canEquip(equipmentslottype, recruit)
                            || (itemStack.getItem() instanceof BannerItem && equipmentslottype.equals(EquipmentSlot.HEAD));
                }

                @Override
                public void set(ItemStack stack){
                    super.set(stack);
                    recruit.setItemSlot(equipmentslottype, stack);
                }

                @OnlyIn(Dist.CLIENT)
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslottype.getIndex()]);
                }
            });
        }

    }

    public void addRecruitHandSlots() {
        this.addSlot(new Slot(recruit.inventory, 9,26,90) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return  recruit.canHoldItem(itemStack);
            }

            @Override
            public void set(ItemStack stack){
                super.set(stack);

                recruit.setItemSlot(EquipmentSlot.MAINHAND, stack);
            }
        });

        this.addSlot(new Slot(recruit.inventory, 10,44,90) {
        @Override
        public boolean mayPlace(ItemStack stack){
            return stack.getItem() instanceof ShieldItem;
        }

        @Override
        public void set(ItemStack stack){
            super.set(stack);
            recruit.setItemSlot(EquipmentSlot.OFFHAND, stack);
        }

        @Override
        public int getSlotIndex(){
            return 10;
        }

        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon () {
            return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
        }
        });
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.recruitInventory.stillValid(playerIn) && this.recruit.isAlive() && this.recruit.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < this.getInventorySize()) {
                if (!this.moveItemStackTo(stack, this.getInventorySize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(11).mayPlace(itemstack) && !this.getSlot(11).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 11, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(12).mayPlace(itemstack) && !this.getSlot(12).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 12, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(13).mayPlace(itemstack) && !this.getSlot(13).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 13, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(14).mayPlace(itemstack) && !this.getSlot(14).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 14, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(15).mayPlace(itemstack) && !this.getSlot(15).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 15, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(9).mayPlace(itemstack) && !this.getSlot(9).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 9, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.getSlot(10).mayPlace(itemstack) && !this.getSlot(10).hasItem()) {
                if (!this.moveItemStackTo(itemstack, 10, this.getInventorySize(), false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(stack, 0, this.getInventorySize(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}