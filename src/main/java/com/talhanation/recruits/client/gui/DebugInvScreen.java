package com.talhanation.recruits.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.inventory.DebugInvContainer;
import com.talhanation.recruits.network.MessageAggroGui;
import com.talhanation.recruits.network.MessageDebugGui;
import de.maxhenkel.corelib.inventory.ScreenBase;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;

import static com.talhanation.recruits.CommandEvents.TEXT_PASSIVE;

@OnlyIn(Dist.CLIENT)
public class DebugInvScreen extends ScreenBase<DebugInvContainer> {

    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Main.MOD_ID,"textures/gui/debug_gui.png" );

    private static final int fontColor = 4210752;

    private final AbstractRecruitEntity recruit;
    private final Player player;
    private final Inventory playerInventory;

    private int group;
    private int follow;
    private int aggro;
    public DebugInvScreen(DebugInvContainer commandContainer, Inventory playerInventory, Component title) {
        super(RESOURCE_LOCATION, commandContainer, playerInventory, new TextComponent(""));
        imageWidth = 201;
        imageHeight = 170;
        this.recruit = commandContainer.getRecruit();
        this.player = playerInventory.player;
        this.playerInventory = playerInventory;
    }

    @Override
    protected void init() {
        super.init();

        int zeroLeftPos = leftPos + 180;
        int zeroTopPos = topPos + 10;

        int topPosGab = 5;


        xpButton(zeroLeftPos, zeroTopPos);
        lvlButton(zeroLeftPos, zeroTopPos);
        costButton(zeroLeftPos, zeroTopPos);
        hungerButton(zeroLeftPos, zeroTopPos);
        moralButton(zeroLeftPos, zeroTopPos);
        healthButton(zeroLeftPos, zeroTopPos);
    }

    private void xpButton(int zeroLeftPos, int zeroTopPos){
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 0, 23, 20, new TextComponent("+xp"), button -> {
                Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(0, recruit.getUUID()));
        }));
    }

    private void lvlButton(int zeroLeftPos, int zeroTopPos){
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 1, 23, 20, new TextComponent("+lvl"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(1, recruit.getUUID()));
        }));
    }

    private void costButton(int zeroLeftPos, int zeroTopPos){
        //increase cost
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 2, 23, 20, new TextComponent("+cost"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(2, recruit.getUUID()));
        }));
        //decrease cost
        addRenderableWidget(new Button(zeroLeftPos - 190, zeroTopPos + (20 + 5) * 2, 23, 20, new TextComponent("-cost"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(3, recruit.getUUID()));
        }));
    }

    private void hungerButton(int zeroLeftPos, int zeroTopPos){
        //increase hunger
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 3, 23, 20, new TextComponent("+hunger"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(4, recruit.getUUID()));
        }));

        //decrease hunger
        addRenderableWidget(new Button(zeroLeftPos - 190, zeroTopPos + (20 + 5) * 3, 23, 20, new TextComponent("-hunger"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(5, recruit.getUUID()));
        }));
    }

    private void moralButton(int zeroLeftPos, int zeroTopPos){
        //increase moral
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 4, 23, 20, new TextComponent("+moral"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(6, recruit.getUUID()));
        }));

        //decrease moral
        addRenderableWidget(new Button(zeroLeftPos - 190, zeroTopPos + (20 + 5) * 4, 23, 20, new TextComponent("-moral"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(7, recruit.getUUID()));
        }));
    }

    private void healthButton(int zeroLeftPos, int zeroTopPos){
        //increase health
        addRenderableWidget(new Button(zeroLeftPos - 210, zeroTopPos + (20 + 5) * 5, 23, 20, new TextComponent("+moral"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(8, recruit.getUUID()));
        }));

        //decrease health
        addRenderableWidget(new Button(zeroLeftPos - 190, zeroTopPos + (20 + 5) * 5, 23, 20, new TextComponent("-moral"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageDebugGui(9, recruit.getUUID()));
        }));
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        int health = Mth.ceil(recruit.getHealth());
        int maxHealth = Mth.ceil(recruit.getMaxHealth());
        int moral = Mth.ceil(recruit.getMoral());

        double A_damage = Mth.ceil(recruit.getAttackDamage());
        double speed = recruit.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) / 0.3;
        DecimalFormat decimalformat = new DecimalFormat("##.##");
        double armor = recruit.getArmorValue();
        int costs = recruit.getCost();


        int k = 79;//rechst links
        int l = 19;//höhe

        //Titles
        font.draw(matrixStack, recruit.getDisplayName().getVisualOrderText(), 8, 5, fontColor);
        font.draw(matrixStack, player.getInventory().getDisplayName().getVisualOrderText(), 8, this.imageHeight - 96 + 2, fontColor);

        //Info
        font.draw(matrixStack, "Hp:", k, l, fontColor);
        font.draw(matrixStack, "" + health, k + 25, l , fontColor);

        font.draw(matrixStack, "Lvl:", k , l  + 10, fontColor);
        font.draw(matrixStack, "" + recruit.getXpLevel(), k + 25 , l + 10, fontColor);

        font.draw(matrixStack, "Exp:", k, l + 20, fontColor);
        font.draw(matrixStack, "" + recruit.getXp(), k + 25, l + 20, fontColor);

        font.draw(matrixStack, "Kills:", k, l + 30, fontColor);
        font.draw(matrixStack, ""+ recruit.getKills(), k + 25, l + 30, fontColor);

        font.draw(matrixStack, "Moral:", k, l + 40, fontColor);
        font.draw(matrixStack, ""+ moral, k + 30, l + 40, fontColor);

        font.draw(matrixStack, "MaxHp:", k + 43, l, fontColor);
        font.draw(matrixStack, ""+ maxHealth, k + 77, l, fontColor);

        font.draw(matrixStack, "Attack:", k + 43, l + 10, fontColor);
        font.draw(matrixStack, ""+ A_damage, k + 77, l + 10, fontColor);

        font.draw(matrixStack, "Speed:", k +43, l + 20, fontColor);
        font.draw(matrixStack, ""+ decimalformat.format(speed), k + 77, l + 20, fontColor);

        font.draw(matrixStack, "Armor:", k + 43, l + 30, fontColor);
        font.draw(matrixStack, ""+ armor, k + 77, l + 30, fontColor);

        font.draw(matrixStack, "Cost:", k + 43, l + 40, fontColor);
        font.draw(matrixStack, ""+ costs, k + 77, l + 40, fontColor);

    }

}
