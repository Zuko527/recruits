package com.talhanation.recruits.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.talhanation.recruits.CommandEvents;
import com.talhanation.recruits.Main;
import com.talhanation.recruits.inventory.CommandContainer;
import com.talhanation.recruits.network.*;
import de.maxhenkel.corelib.inventory.ScreenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@OnlyIn(Dist.CLIENT)
public class CommandScreen extends ScreenBase<CommandContainer> {

    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Main.MOD_ID,"textures/gui/command_gui.png");
    private static final int fontColor = 16250871;
    private PlayerEntity player;
    private int group = 0;
    private int recCount = 0;

    public CommandScreen(CommandContainer commandContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(RESOURCE_LOCATION, commandContainer, playerInventory, title);
        imageWidth = 201;
        imageHeight = 170;
        player = playerInventory.player;
    }

    @Override
    public boolean keyReleased(int x, int y, int z) {
        super.keyReleased(x,y,z);
        this.onClose();
        return true;
    }

    @Override
    protected void init() {
        super.init();

        //WANDER
        addButton(new Button(leftPos - 70 - 40 + imageWidth / 2, topPos + 20 + 30, 81, 20, new StringTextComponent("Release!"), button -> {
            CommandEvents.sendFollowCommandInChat(0, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageFollow(player.getUUID(), 0, group));
        }));

        //FOLLOW
        addButton(new Button(leftPos - 40 + imageWidth / 2, topPos + 10, 81, 20, new StringTextComponent("Follow me!"), button -> {
            CommandEvents.sendFollowCommandInChat(1, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageFollow(player.getUUID(), 1, group));

        }));

        //HOLDPOS
        addButton(new Button(leftPos + 30 + imageWidth / 2, topPos + 20 + 30, 81, 20, new StringTextComponent("Hold your Pos.!"), button -> {
            CommandEvents.sendFollowCommandInChat(2, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageFollow(player.getUUID(), 2, group));
        }));

        //AGGRESSIVE
        addButton(new Button(leftPos - 40 - 70 + imageWidth / 2, topPos + 20 + 30 + 30, 81, 20, new StringTextComponent("Stay Aggressive!"), button -> {
            CommandEvents.sendAggroCommandInChat(1, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageAggro(player.getUUID(), 1, group));
        }));

        //RAID
        addButton(new Button(leftPos + 30 + imageWidth / 2, topPos + 20 + 30 + 30, 81, 20, new StringTextComponent("Raid!"), button -> {
            CommandEvents.sendAggroCommandInChat(2, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageAggro(player.getUUID(), 2, group));
        }));

        /*
        //ATTACK TARGET
        addButton(new Button(leftPos + 30 + imageWidth / 2, topPos + 150, 81, 20, new StringTextComponent("Attack!"), button -> {
            this.onAttackButton();
        }));
        */

        //NEUTRAL
        addButton(new Button(leftPos - 40 - 50 + imageWidth / 2, topPos + 120, 81, 20, new StringTextComponent("Stay Neutral!"), button -> {
            CommandEvents.sendAggroCommandInChat(0, player);
            Main.SIMPLE_CHANNEL.sendToServer(new MessageAggro(player.getUUID(), 0, group));
        }));

        //CLEAR TARGET
        addButton(new Button(leftPos + 10 + imageWidth / 2, topPos + 120, 81, 20, new StringTextComponent("Clear Targets!"), button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageClearTarget(player.getUUID(), group));
        }));

        //GROUP
        addButton(new Button(leftPos - 5 + imageWidth / 2, topPos - 40 + imageHeight / 2, 11, 20, new StringTextComponent("+"), button -> {
            if (this.group != 9) {
                this.group ++;
            }
        }));

        addButton(new Button(leftPos - 5 + imageWidth / 2, topPos + imageHeight / 2, 11, 20, new StringTextComponent("-"), button -> {
            if (this.group != 0) {
                this.group --;
            }
        }));
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        this.recCount = CommandEvents.getRecruitsInCommand();
        Main.SIMPLE_CHANNEL.sendToServer(new MessageRecruitsInCommand(player.getUUID()));

        int k = 78;//rechst links
        int l = 71;//höhe
        font.draw(matrixStack, "" +  handleGroupText(this.group), k , l, fontColor);
        //font.draw(matrixStack, "" +  handleRecruitCountText(recCount), k - 30 , 0, fontColor);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(matrixStack, partialTicks, mouseX, mouseY);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static String handleGroupText(int group){
        if (group == 0){
            return "Everyone";
        }
        else
            return ("Group " + group);
    }

    public static String handleRecruitCountText(int recCount){
        return ("Recruits in Command: " + recCount);
    }


    public void onAttackButton(){
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraft.player;

        RayTraceResult rayTraceResult = minecraft.hitResult;
        if (rayTraceResult != null) {
            if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) rayTraceResult;
                if (entityRayTraceResult.getEntity() instanceof LivingEntity && clientPlayerEntity != null) {
                    LivingEntity living = (LivingEntity) entityRayTraceResult.getEntity();
                    Main.SIMPLE_CHANNEL.sendToServer(new MessageAttackEntity(clientPlayerEntity.getUUID(), living.getUUID()));
                }
            }
        }
    }
}