package com.talhanation.recruits.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class AbstractInventoryEntity extends TameableEntity {

    private final Inventory inventory = new Inventory(9);

    public AbstractInventoryEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    ///////////////////////////////////TICK/////////////////////////////////////////

    public void aiStep() {
        super.aiStep();
    }

    public void tick() {
        super.tick();
    }

    ////////////////////////////////////DATA////////////////////////////////////

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("Inventory", this.inventory.createTag());
    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.inventory.fromTag(nbt.getList("Inventory", 30));

    }

    ////////////////////////////////////GET////////////////////////////////////

    public Inventory getInventory() {
        return this.inventory;
    }

    ////////////////////////////////////SET////////////////////////////////////

    public boolean setSlot(int slot, ItemStack itemStack) {
        if (super.setSlot(slot, itemStack)) {
            return true;
        } else {
            int i = slot - 300;
            if (i >= 0 && i < this.inventory.getContainerSize()) {
                this.inventory.setItem(i, itemStack);
                return true;
            } else {
                return false;
            }
        }
    }

    ////////////////////////////////////OTHER FUNCTIONS////////////////////////////////////

    public void die(DamageSource dmg) {
        super.die(dmg);
        for (int i = 0; i < this.inventory.getContainerSize(); i++)
            InventoryHelper.dropItemStack(this.level, getX(), getY(), getZ(), this.inventory.getItem(i));
    }

    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.wantsToPickUp(itemstack)) {
            Inventory inventory = this.getInventory();
            boolean flag = inventory.canAddItem(itemstack);
            if (!flag) {
                return;
            }

            this.onItemPickup(itemEntity);
            this.take(itemEntity, itemstack.getCount());
            ItemStack itemstack1 = inventory.addItem(itemstack);
            if (itemstack1.isEmpty()) {
                itemEntity.remove();
            } else {
                itemstack.setCount(itemstack1.getCount());
            }
        }

    }

    @Override
    public abstract boolean wantsToPickUp(ItemStack itemStack);

}